package com.cj186.booktracker.network;

import android.util.Log;

import com.cj186.booktracker.model.Book;
import com.cj186.booktracker.model.Status;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class APIHandler {
    public static Book getBookFromISBN(String ISBN) {
        String parentUrl = "https://openlibrary.org";

        try {
            JSONObject response = new JSONObject(getAPIResponseFromURL( parentUrl + "/isbn/" + ISBN + ".json"));
            String publishYear = response.getString("publish_date");
            JSONArray workIDObject = response.getJSONArray("works");
            JSONObject works = workIDObject.getJSONObject(0);
            String workID = works.getString("key");

            response = new JSONObject(getAPIResponseFromURL(parentUrl + workID + ".json"));
            String title = response.getString("title");
            String cover = response.getJSONArray("covers").get(0).toString();

            JSONArray authorBlock = response.getJSONArray("authors");
            JSONObject authorPluralObject = authorBlock.getJSONObject(0);
            JSONObject authorObject = authorPluralObject.getJSONObject("author");
            Log.w("block", authorObject.toString());
            String authorID = authorObject.getString("key");

            JSONObject authorResponse = new JSONObject(getAPIResponseFromURL( parentUrl + authorID + ".json"));
            String author = authorResponse.getString("name");

            String description = "";
            if(response.toString().contains("description"))
                description = response.getString("description");
            Status status = Status.PLANNING_TO_READ;
            byte[] imageBytes = getImageFromUrl("https://covers.openlibrary.org/b/id/" + cover + "-L.jpg");

            return new Book(imageBytes, title, author, description, status, publishYear, ISBN, false);
        }
        catch (Exception e){
            Log.e("API Error", "Unable to get JSON response.");
            e.printStackTrace();
            return null;
        }
    }

    private static byte[] getImageFromUrl(String imageUrl){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();

            InputStream input = connection.getInputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = input.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            input.close();
            return outputStream.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getAPIResponseFromURL(String urlString){
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        StringBuilder jsonResult = new StringBuilder();

        try {
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                jsonResult.append(line);
            }

            return jsonResult.toString();
        }
        catch (MalformedURLException e){
            Log.e("API Error", urlString + " is not a valid URL.");
            return null;
        }
        catch (IOException e) {
            Log.e("API Error", "Unable to connect to " + urlString);
            return null;
        }
        finally {
            if(urlConnection != null)
                urlConnection.disconnect();
            try{
                if (reader != null)
                    reader.close();
            }
            catch (IOException e){
                Log.e("API Error", "Unable to close buffered reader.");
                throw new RuntimeException(e);
            }

        }
    }
}
