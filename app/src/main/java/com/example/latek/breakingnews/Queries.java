package com.example.latek.breakingnews;

import android.text.TextUtils;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

public final class Queries {
    public static String authorName;
    private Queries() {
    }
    public static List<NewsKeys> fetchNewsData(String requestUrl) {
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
        }
        List<NewsKeys> news = extractFeatureFromJson(jsonResponse);
        return news;
    }
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
        }
        return url;
    }
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpsURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
            }
        } catch (IOException e) {
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
    private static List<NewsKeys> extractFeatureFromJson(String newsJSON) {
        List<NewsKeys> newsList = new ArrayList<>();
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }
        try {
            JSONObject baseJsonResponse = new JSONObject(newsJSON);
            JSONObject responseJSONObject = baseJsonResponse.getJSONObject("response");
            JSONArray newsArray = responseJSONObject.getJSONArray("results");
            for (int i = 0; i < newsArray.length(); i++) {
                JSONObject currentNews = newsArray.getJSONObject(i);
                String title = currentNews.getString("webTitle");
                String sectionName = currentNews.getString("sectionName");
                String date = currentNews.getString("webPublicationDate");
                // the other problem I faced is here, I get the date as string but cannot convert to normal date Simpledateandtime
                String url = currentNews.getString("webUrl");
                JSONArray tagsArray = currentNews.getJSONArray("tags");
                //I did my best read a lot of stuff to use getString(R.string.missing) but could not manage to achieve
                //I also tried it in different java files (adapter and Newskey).It seems JSON and string as string does not work together
                authorName = "Missing Author";
                if (tagsArray.length() == 1) {
                    JSONObject contributorTag = (JSONObject) tagsArray.get(0);
                    authorName = contributorTag.getString("webTitle");
                }
                NewsKeys news = new NewsKeys(title, sectionName, date, url, authorName);
                newsList.add(news);
            }
            return newsList;
        } catch (JSONException e) {
        }
        List<NewsKeys> news = new ArrayList<>();
        return news;
    }
}

