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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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
            "Image Comics",  // Also publishes graphic novels
            "CLAMP",
            "J-Novel Club",
            "ShoPro (Shogakukan Production)",
            "Kodansha Comics",
            "Yomiuri Shimbun (for manga)",
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
            "VIZ Media (also for manga)",
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
            "Rebellion Developments (2000 AD)",

            // Horror Publishers
            "Dark Horse Comics",
            "Tor Books (Horror)",
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
            "St. Martin's Press (Romance)",
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
            "Macmillan (Historical Fiction)",
            "HarperCollins Publishers (Historical Fiction)",
            "Scribner",
            "Houghton Mifflin Harcourt",
            "William Morrow",
            "The History Press",
            "Sourcebooks Landmark",

            // Poetry Publishers
            "Farrar, Straus, and Giroux (Poetry)",
            "Penguin Poets",
            "Knopf Poetry",
            "Copper Canyon Press",
            "Graywolf Press (Poetry)",
            "W.W. Norton & Company (Poetry)",
            "Bloodaxe Books",
            "Poetry Wales Press",
            "BOA Editions",
            "Verse Press",
            "Tupelo Press",
            "Milkweed Editions",

            // Thriller & Crime Publishers
            "Bantam Books",
            "Penguin Books (Thriller & Crime)",
            "Simon & Schuster (Thriller)",
            "HarperCollins (Crime Fiction)",
            "St. Martin's Press (Thriller)",
            "Mulholland Books",
            "Atlantic Monthly Press",
            "Kensington Books (Crime)",
            "Soho Press (Crime Fiction)",
            "Serpent's Tail",
            "Hodder & Stoughton (Crime)",
            "Canongate Books (Thriller)",
            "Raven Books",
            "Severn House Publishers",
            "Orion Publishing Group"
    };


    public static Book getBookFromISBN(String isbn){
        try{
            return getBookFromGoogleBooksUsingISBN(isbn);
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        try{
            return getBookFromOpenLibraryUsingISBN(isbn);
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        return null;
    }

    private static Book getBookFromGoogleBooksUsingISBN(String ISBN) throws JSONException{
        String version = "v1";
        String parentUrl = "https://www.googleapis.com/books/" + version + "/";
        JSONObject responseJson;
        responseJson = new JSONObject(getAPIResponseFromURL(parentUrl + "volumes?q=isbn:" + ISBN));

        JSONArray bookArray = responseJson.getJSONArray("items");
        JSONObject bookObject = bookArray.getJSONObject(0).getJSONObject("volumeInfo");

        //String imageUrl = bookObject.getJSONObject("imageLinks").getString("thumbnail").replace("http", "https");
        String imageUrl = bookObject.getJSONObject("imageLinks").optString("extraLarge",
                bookObject.getJSONObject("imageLinks").optString("large",
                bookObject.getJSONObject("imageLinks").optString("medium",
                bookObject.getJSONObject("imageLinks").optString("thumbnail"))));

        imageUrl = imageUrl.replace("http", "https");

        byte[] imageBytes = getImageFromUrl(imageUrl);
        String title = bookObject.getString("title");
        JSONArray authorArray = bookObject.getJSONArray("authors");
        StringBuilder author = new StringBuilder();
        if(authorArray.length() > 1){
            for(int i = 0; i < authorArray.length(); i++){
                if(authorArray.get(i) instanceof String){
                    author.append(authorArray.get(i)).append("\n");
                }
            }
        }
        else{
            author.append(authorArray.getString(0));
        }
        String description = bookObject.optString("description");
        String publishYear = "N/A";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        try {
            Date date = format.parse(bookObject.getString("publishedDate"));
            publishYear = String.format("%tY", date);
        }
        catch (Exception ignored) {}
        return new Book(imageBytes, title, author.toString(), description, Status.PLANNING_TO_READ, publishYear, ISBN, false);
    }

    private static Book getBookFromOpenLibraryUsingISBN(String ISBN) throws JSONException {
        String parentUrl = "https://openlibrary.org";

        JSONObject responseJson;
        responseJson = new JSONObject(getAPIResponseFromURL( parentUrl + "/isbn/" + ISBN + ".json"));
        String publishYear = "";
        try{
            publishYear = responseJson.getString("publish_date");
        }
        catch (Exception ignored){}

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
                                    editionObjects.add(edition);
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
            try{
                return o1.getString("publish_date").compareTo(o2.getString("publish_date"));
            }
            catch (JSONException ignored) {}
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
