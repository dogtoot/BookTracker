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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Collin J. Johnson
 * 5/6/2025
 * 2376 Mobile Applications Development
 *
 * This class pulls book data based upon isbn from Google Books/Open Library.
 */

public class APIHandler {
    // List of trusted publishers to prioritize from open library.
    private static final String[] TRUSTED_PUBLISHERS = {
            // General and Fiction Publishers
            "Penguin Random House",
            "HarperCollins",
            "Macmillan",
            "Simon & Schuster",
            "Hachette Book Group",
            "Bloomsbury",
            "Farrar, Straus, and Giroux",
            "Little, Brown and Company",
            "St. Martin's Press",
            "Knopf Doubleday Publishing Group",
            "W.W. Norton & Company",
            "Random House",
            "Alfred A. Knopf",
            "The New York Review of Books",

            // Science Fiction & Fantasy Publishers
            "Tor Books",
            "Del Rey Books",
            "Gollancz",
            "Baen Books",
            "Orbit",
            "Viking Press",
            "DAW Books",
            "Ace Books",
            "Subterranean Press",
            "The Gollancz Fantasy Masterworks",
            "Eos Books",

            // Non-Fiction Publishers
            "W.W. Norton & Company",
            "Oxford University Press",
            "Princeton University Press",
            "Harvard University Press",
            "Routledge",
            "Penguin Press",
            "Farrar, Straus, and Giroux (Non-Fiction)",
            "Basic Books",
            "The New Press",
            "MIT Press",
            "University of California Press",
            "McGraw-Hill Education",
            "Harper Business",

            // Children's Book Publishers
            "Scholastic",
            "Candlewick Press",
            "Little, Brown Books for Young Readers",
            "Penguin Young Readers",
            "Random House Children's Books",
            "HarperCollins Children's Books",
            "Hachette Book Group Children's Books",
            "Bloomsbury Children's Books",
            "Chronicle Books",
            "Blue Apple Books",
            "Tundra Books",
            "Atheneum Books for Young Readers",
            "Dial Books for Young Readers",
            "Puffin Books",

            // Academic and Specialized Publishers
            "Cambridge University Press",
            "Springer",
            "MIT Press",
            "Routledge",
            "John Wiley & Sons",
            "Palgrave Macmillan",
            "Oxford University Press",
            "Harvard University Press",
            "Princeton University Press",
            "Blackwell Publishing",
            "Elsevier",
            "SAGE Publications",
            "Taylor & Francis",
            "American Psychological Association",

            // Independent Publishers
            "Graywolf Press",
            "Coffee House Press",
            "The New Press",
            "Akashic Books",
            "Small Beer Press",
            "Haymarket Books",
            "Seven Stories Press",
            "The Feminist Press",
            "Soft Skull Press",
            "City Lights Publishers",
            "Or Books",

            // Manga Publishers
            "VIZ Media",
            "Kodansha USA",
            "Yen Press",
            "Seven Seas Entertainment",
            "Shonen Jump",
            "Dark Horse Manga",
            "TokyoPop",
            "Image Comics",
            "CLAMP",
            "J-Novel Club",
            "ShoPro (Shogakukan Production)",
            "Kodansha Comics",
            "Yomiuri Shimbun",
            "Futabasha",
            "Hakusensha",
            "Kodansha International",
            "Tokyopop (Manga)",
            "Denpa",
            "VIZ Signature",

            // Graphic Novel Publishers
            "Marvel Comics",
            "DC Comics",
            "Dark Horse Comics",
            "IDW Publishing",
            "Archie Comics",
            "VIZ Media",
            "BOOM! Studios",
            "Valiant Comics",
            "Image Comics",
            "First Second Books",
            "Fantagraphics Books",
            "Drawn & Quarterly",
            "Top Shelf Productions",
            "Papercutz",
            "Dynamite Entertainment",
            "Z2 Comics",
            "AfterShock Comics",
            "Action Lab Comics",
            "Rebellion Developments",

            // Horror Publishers
            "Dark Horse Comics",
            "Samhain Publishing",
            "JournalStone",
            "Grave Distractions Publications",
            "Valancourt Books",
            "Bloodshot Books",
            "Leisure Books",
            "Cemetery Dance Publications",
            "Necro Publications",
            "Pulp Hero Press",

            // Romance Publishers
            "Harlequin",
            "Silhouette Books",
            "Sourcebooks Casablanca",
            "Avon Romance",
            "Berkley Books",
            "Forever Romance",
            "St. Martin's Press",
            "Entangled Publishing",
            "Montlake Romance",
            "Kensington Publishing Corporation",
            "Tule Publishing",
            "Grand Central Publishing",

            // Historical Fiction Publishers
            "Penguin Classics",
            "Random House Trade Paperbacks",
            "Anchor Books",
            "Hachette Book Group",
            "Macmillan",
            "HarperCollins Publishers",
            "Scribner",
            "Houghton Mifflin Harcourt",
            "William Morrow",
            "The History Press",
            "Sourcebooks Landmark",

            // Poetry Publishers
            "Farrar, Straus, and Giroux",
            "Penguin Poets",
            "Knopf Poetry",
            "Copper Canyon Press",
            "Graywolf Press",
            "W.W. Norton & Company",
            "Bloodaxe Books",
            "Poetry Wales Press",
            "BOA Editions",
            "Verse Press",
            "Tupelo Press",
            "Milkweed Editions",

            // Thriller & Crime Publishers
            "Bantam Books",
            "Penguin Books",
            "Simon & Schuster",
            "HarperCollins",
            "St. Martin's Press",
            "Mulholland Books",
            "Atlantic Monthly Press",
            "Kensington Books",
            "Soho Press",
            "Serpent's Tail",
            "Hodder & Stoughton",
            "Canongate Books",
            "Raven Books",
            "Severn House Publishers",
            "Orion Publishing Group"
    };


    public static Book getBookFromISBN(String isbn){
        // Errors are likely, as some books do not contain required meta-data.
        // This is especially an issue on OpenLibrary, where books may not have a cover.
        try{
            // Attempt to get a book from Google Books.
            return getBookFromGoogleBooksUsingISBN(isbn);
        }
        catch (Exception ignored){
            Log.w("API Failed", "Unable to connect/utilize Google Books API.");
        }

        try{
            // Attempt to get the book from OpenLibrary,
            // we use this as a fallback as it is considerably slower.
            return getBookFromOpenLibraryUsingISBN(isbn, "eng");
        }
        catch (Exception ignored){
            Log.w("API Failed", "Unable to connect/utilize Open Library API.");
        }

        return null;
    }

    private static Book getBookFromGoogleBooksUsingISBN(String ISBN) throws JSONException{
        // Version of Google Books.
        String version = "v1";
        // Create our parent url.
        String parentUrl = "https://www.googleapis.com/books/" + version + "/";
        // Get the response JSON from GoogleBooks using getAPIResponseFromUrl.
        JSONObject responseJson;
        responseJson = new JSONObject(getAPIResponseFromURL(parentUrl + "volumes?q=isbn:" + ISBN));

        // Get the array of books from the returned JSON.
        JSONArray bookArray = responseJson.getJSONArray("items");

        // Get the first book.
        JSONObject bookObject;
        bookObject = bookArray.getJSONObject(0).getJSONObject("volumeInfo");

        // Attempt to get the highest resolution image.
        String imageUrl = bookObject.getJSONObject("imageLinks").optString("extraLarge",
                bookObject.getJSONObject("imageLinks").optString("large",
                bookObject.getJSONObject("imageLinks").optString("medium",
                bookObject.getJSONObject("imageLinks").optString("thumbnail"))));

        if(imageUrl.isEmpty())
            throw new JSONException("No Image Found.");

        // Android sometimes throws a fit if you try to make a request to a non secured link,
        // so we replace http with https.
        imageUrl = imageUrl.replace("http", "https");

        // Get the image's bytes from getImageFromUrl()
        byte[] imageBytes = getImageFromUrl(imageUrl);
        // Get the books title.
        String title = bookObject.getString("title");

        // Because books returned from GoogleBooks occasionally have multiple authors,
        // we loop through all the authors returned.
        JSONArray authorArray = bookObject.getJSONArray("authors");
        StringBuilder author = new StringBuilder();
        if(authorArray.length() > 1){
            for(int i = 0; i < authorArray.length(); i++){
                if(authorArray.get(i) instanceof String){
                    author.append(authorArray.get(i)).append(", ");
                }
            }
        }
        else{
            // If there is just one author, we get the first author in the authorArray.
            author.append(authorArray.getString(0));
        }

        // Get the description using an optional string.
        String description = bookObject.optString("description");
        // Set the default publish year, "N/A"
        String publishYear = "N/A";

        // Attempt to format the date into a Date object.
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        try {
            Date date = format.parse(bookObject.getString("publishedDate"));
            // Format the date into publishYear so we only have the year.
            publishYear = String.format("%tY", date);
        }
        catch (Exception ignored) {}
        // Return the book.
        return new Book(imageBytes, title, author.toString(), description, Status.PLANNING_TO_READ, publishYear, ISBN, false);
    }

    private static Book getBookFromOpenLibraryUsingISBN(String ISBN, String lang) throws JSONException {
        // Set the parent url.
        String parentUrl = "https://openlibrary.org";

        // Get the response JSON.
        JSONObject responseJson;
        responseJson = new JSONObject(getAPIResponseFromURL( parentUrl + "/isbn/" + ISBN + ".json"));
        // OpenLibrary has more variation in the date format, so we just attempt to pull the date.
        String publishYear;
        publishYear = responseJson.optString("publish_date");

        // OpenLibrary splits books into works, editions, books, and authors. Here we get the workID.
        // We have to get the WorkID first in order to get the author, as well as the book.
        JSONArray workIDObject = responseJson.getJSONArray("works");
        JSONObject works = workIDObject.getJSONObject(0);
        String workID = works.getString("key");

        // Here we populate an ArrayList of JSONObjects,
        // which is returned from getOpenLibraryEditions,
        // we get these editions using the workID and the desired language.
        ArrayList<JSONObject> editionObjects = getOpenLibraryEditions(workID, parentUrl, lang);

        if(editionObjects.isEmpty())
            // If getOpenLibraryEditions did not return any editions, we pull the book directly.
            editionObjects.add(new JSONObject(getAPIResponseFromURL(parentUrl + workID + ".json")));

        // Because editionObjects is pre-sorted, we pull the first index.
        JSONObject book = editionObjects.get(0);

        // Get the book title and cover url.
        String title = book.getString("title");
        String cover = book.getJSONArray("covers").get(0).toString();

        // Get the author.
        JSONArray authorBlock = book.getJSONArray("authors");
        JSONObject authorParentObject = authorBlock.getJSONObject(0);
        JSONObject authorObject = authorParentObject.getJSONObject("author");
        String authorID = authorObject.getString("key");

        // Get the response from the authorID.
        JSONObject authorResponse = new JSONObject(getAPIResponseFromURL( parentUrl + authorID + ".json"));
        // Get the author's name.
        String author = authorResponse.getString("name");

        // Get the book's description,
        String description = "";
        if(book.toString().contains("description")){
            try{
                JSONObject descriptionArray = book.getJSONObject("description");
                description = descriptionArray.getString("value");
            }
            catch (Exception ignored){
                description = book.getString("description");
            }
        }

        // Get the image's bytes.
        byte[] imageBytes = getImageFromUrl("https://covers.openlibrary.org/b/id/" + cover + "-L.jpg");

        return new Book(imageBytes, title, author, description, Status.PLANNING_TO_READ, publishYear, ISBN, false);
    }

    private static ArrayList<JSONObject> getOpenLibraryEditions(String workID, String parentUrl, String bookLang) throws JSONException{
        // Get the editions using getAPIResponseFromUrl.
        JSONObject responseJson = new JSONObject(getAPIResponseFromURL(parentUrl + workID + "/editions.json?limit=100"));
        JSONArray editions = responseJson.getJSONArray("entries");

        ArrayList<JSONObject> editionObjects = new ArrayList<>();
        ArrayList<JSONObject> tempEditionObjects = new ArrayList<>();

        // This code killed one of my two ram sticks :)
        for (int i = 0; i < editions.length(); i++) {
            JSONObject edition = editions.getJSONObject(i);
            JSONArray languages = edition.optJSONArray("languages");

            try{
                // Attempt to get the description as well as the cover.
                edition.getString("description");
                edition.getJSONArray("covers").get(0);

                // Get the book's publishers and publish date.
                JSONArray publishers = edition.getJSONArray("publishers");
                edition.getString("publish_date");

                if (languages != null) {
                    // If the book has a defined language.
                    // Check if one of the languages is our desired language.
                    for (int j = 0; j < languages.length(); j++) {
                        // Loop through the languages, and get the language.
                        JSONObject language = languages.getJSONObject(j);
                        // Get language's key.
                        String languageKey = language.optString("key");
                        if(languageKey.equals("/languages/" + bookLang)){
                            // If the book is in our desired language we add it to a temporary list.
                            tempEditionObjects.add(edition);
                            for(int k = 0; k < publishers.length(); k++){
                                // Loop over our trusted publishers.
                                String publisher = publishers.getString(k);
                                if(isTrustedPublisher(publisher)){
                                    // If the book is from a trusted publisher, we add it to
                                    // editionObjects and remove it from tempEditionObjects.
                                    editionObjects.add(edition);
                                    tempEditionObjects.remove(edition);
                                    break;
                                }
                            }
                        }
                    }
                    if(editionObjects.isEmpty())
                        // If we don't have editions from a trusted publisher,
                        // we make edition objects equal to tempEditionObjects.
                        editionObjects = tempEditionObjects;
                }
                // Attempt to sort the editions by newest.
                editionObjects.sort((o1, o2) -> {
                    try{
                        return o1.getString("publish_date").compareTo(o2.getString("publish_date"));
                    }
                    catch (JSONException ignored) {}
                    return 0;
                });
            }
            catch(Exception ignored){}
        }

        // Return our editions.
        return editionObjects;
    }

    private static boolean isTrustedPublisher(String inputPublisher){
        // Normalize the input publisher.
        String normalizedInput = normalizeString(inputPublisher);
        for(String trustedPublisher : TRUSTED_PUBLISHERS){
            // For each trusted publisher, normalize the publisher.
            String normalizedTrusted = normalizeString(trustedPublisher);
            if(normalizedInput.contains(normalizedTrusted) || normalizedTrusted.contains(normalizedInput))
                // Check if the two normalized publishers contain each other, if they do, return true.
                return true;
        }
        // If the publisher isn't trusted, return false.
        return false;
    }

    private static String normalizeString(String in){
        // Normalize "in".
        return in.toLowerCase().replace("[^a-z0-9]", "");
    }


    private static byte[] getImageFromUrl(String imageUrl){
        // Open a ByteArrayOutputStream.
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()){
            // Get the url from imageURL.
            URL url = new URL(imageUrl);
            // Open a connection to the image's URL.
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // connection.setDoInput(true);
            // Connect to the URL.
            connection.connect();

            // Get the input from the connection.
            InputStream input = connection.getInputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = input.read(buffer)) != -1) {
                // Write the bytes to the outputStream.
                outputStream.write(buffer, 0, bytesRead);
            }

            // Close the input and return the byte array in outputStream.
            input.close();
            return outputStream.toByteArray();
        }
        catch (Exception e) {
            return null;
        }
    }

    private static String getAPIResponseFromURL(String urlString){
        // Set our HttpURLConnection and BufferedReader to null.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        // Create StringBuilder for the JSON response.
        StringBuilder jsonResult = new StringBuilder();

        try {
            // Create our URL.
            URL url = new URL(urlString);
            // Open the connection to url, and set the headers, then connect.
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.connect();

            // Read the inputStream as a BufferedReader.
            InputStream inputStream = urlConnection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));

            // Read the JSON data.
            String line;
            while ((line = reader.readLine()) != null) {
                jsonResult.append(line);
            }

            // Return the JSON result.
            return jsonResult.toString();
        }
        catch (MalformedURLException e){
            // Return null if the URL is invalid.
            Log.e("API Error", urlString + " is not a valid URL.");
            return null;
        }
        catch (IOException e) {
            // Return null if anything throws an IOException.
            Log.e("API Error", "Unable to connect to " + urlString);
            return null;
        }
        finally {
            // Close urlConnection.
            if(urlConnection != null)
                urlConnection.disconnect();
            try{
                // Close reader.
                if (reader != null)
                    reader.close();
            }
            catch (IOException e){
                Log.e("API Error", "Unable to close buffered reader.");
            }
        }
    }
}
