package ca.ucalgary.edu.ensf380.api_test;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class NewsService {
    private static final String API_KEY = "b7358c163a22492e9d29f85397807fde";

    public static List<NewsItem> getNews(String keyword) throws IOException {
        List<NewsItem> newsList = new ArrayList<>();
        OkHttpClient httpClient = new OkHttpClient();

        String url = "https://newsapi.org/v2/top-headlines?country=ca&q=" + keyword + "&apiKey=" + API_KEY;

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try {
            Response response = httpClient.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("Failed to fetch news data. HTTP error code: " + response.code());
            }

            String jsonResponse = response.body().string();
            newsList = parseJsonResponse(jsonResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newsList;
    }

    private static List<NewsItem> parseJsonResponse(String jsonResponse) {
        List<NewsItem> newsItems = new ArrayList<>();

        Pattern titlePattern = Pattern.compile("\"title\"\\s*:\\s*\"([^\"]+)\"");
        Pattern sourceNamePattern = Pattern.compile("\"name\"\\s*:\\s*\"([^\"]+)\"");

        Matcher titleMatcher = titlePattern.matcher(jsonResponse);
        Matcher sourceNameMatcher = sourceNamePattern.matcher(jsonResponse);

        while (titleMatcher.find() && sourceNameMatcher.find()) {
            String title = titleMatcher.group(1);
            String sourceName = sourceNameMatcher.group(1);
            newsItems.add(new NewsItem(title, sourceName));
        }

        return newsItems;
    }
}


