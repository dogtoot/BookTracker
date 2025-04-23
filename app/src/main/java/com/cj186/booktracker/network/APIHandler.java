package com.cj186.booktracker.network;

import android.util.Log;

import com.cj186.booktracker.model.Book;
import com.cj186.booktracker.model.Status;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class APIHandler {
    private static final String[] TRUSTED_PUBLISHERS = {
            // General and Fiction Publishers
            "Penguin Random House",
            "HarperCollins",
            "Macmillan",
            "Simon & Schuster",
            "Hachette Book Group",
            "Bloomsbury",
            "Farrar, Straus, and Giroux",

            // Science Fiction & Fantasy Publishers
            "Tor Books",
            "Del Rey Books",
            "Gollancz",
            "Baen Books",
            "Orbit",
            "Viking Press",

            // Non-Fiction Publishers
            "W.W. Norton & Company",
            "Oxford University Press",
            "Princeton University Press",
            "Harvard University Press",
            "Routledge",

            // Children's Book Publishers
            "Scholastic",
            "Candlewick Press",
            "Little, Brown Books for Young Readers",
            "Penguin Young Readers",

            // Academic and Specialized Publishers
            "Cambridge University Press",
            "Springer",
            "MIT Press",
            "Routledge",

            // Independent Publishers
            "Graywolf Press",
            "Coffee House Press",
            "The New Press",
            "Akashic Books"
    };

    public static Book getBookFromISBN(String ISBN) {
        String parentUrl = "https://openlibrary.org";

        try {
            JSONObject responseJson;
            try{
                responseJson = new JSONObject(getAPIResponseFromURL( parentUrl + "/isbn/" + ISBN + ".json"));
            }
            catch (Exception e){
                return null;
            }
            String publishYear = responseJson.getString("publish_date");
            JSONArray workIDObject = responseJson.getJSONArray("works");
            JSONObject works = workIDObject.getJSONObject(0);
            String workID = works.getString("key");

            responseJson = new JSONObject(getAPIResponseFromURL(parentUrl + workID + "/editions.json?limit=100"));
            JSONArray editions = responseJson.getJSONArray("entries");

            ArrayList<JSONObject> editionObjects = new ArrayList<>();

            // This code killed one of my two ram sticks :)
            for (int i = 0; i < editions.length(); i++) {
                JSONObject edition = editions.getJSONObject(i);
                JSONArray languages = edition.optJSONArray("languages");

                try{
                    edition.getString("description");
                    edition.getJSONArray("covers").get(0);

                    JSONArray publishers = edition.getJSONArray("publishers");
                    edition.getString("publish_date");

                    if (languages != null) {
                        // Check if one of the languages is English
                        for (int j = 0; j < languages.length(); j++) {
                            JSONObject language = languages.getJSONObject(j);
                            String languageKey = language.optString("key");
                            if(languageKey.equals("/languages/eng")){
                                for(int k = 0; k < publishers.length(); k++){
                                    String publisher = publishers.getString(k);
                                    if(isTrustedPublished(publisher)){
                                        //editionObjects.add(edition);
                                    }
                                }
                            }
                        }
                    }
                }
                catch(Exception ignored){}
            }

            JSONObject fallBackEdition = new JSONObject(getAPIResponseFromURL(parentUrl + workID + ".json"));
            if(editionObjects.isEmpty())
                editionObjects.add(fallBackEdition);

            editionObjects.sort((o1, o2) -> {
                try {
                    return o1.getString("publish_date").compareTo(o2.getString("publish_date"));
                }
                catch (Exception ignored) {}
                return 0;
            });

            String title = editionObjects.get(0).getString("title");
            String cover = editionObjects.get(0).getJSONArray("covers").get(0).toString();

            JSONArray authorBlock = fallBackEdition.getJSONArray("authors");
            JSONObject authorPluralObject = authorBlock.getJSONObject(0);
            JSONObject authorObject = authorPluralObject.getJSONObject("author");
            String authorID = authorObject.getString("key");

            JSONObject authorResponse = new JSONObject(getAPIResponseFromURL( parentUrl + authorID + ".json"));
            String author = authorResponse.getString("name");

            String description = "";
            if(editionObjects.get(0).toString().contains("description")){
                try{
                    JSONObject descriptionArray = editionObjects.get(0).getJSONObject("description");
                    description = descriptionArray.getString("value");
                }
                catch (Exception ignored){
                    description = editionObjects.get(0).getString("description");
                }
            }
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

    public static boolean isTrustedPublished(String inputPublisher){
        String normalizedInput = normalizeString(inputPublisher);
        for(String trustedPublisher : TRUSTED_PUBLISHERS){
            String normalizedTrusted = normalizeString(inputPublisher);
            if(normalizedInput.contains(normalizedTrusted) || normalizedTrusted.contains(normalizedInput))
                return true;
        }
        return false;
    }

    private static String normalizeString(String in){
        return in.toLowerCase().replace("[^a-z0-9]", "");
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
            }

        }
    }
}
