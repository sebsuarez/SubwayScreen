package ca.ucalgary.edu.ensf380;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Provides methods to fetch and parse news data from an external API.
 */
public abstract class NewsService {
    private static final String API_KEY = "b7358c163a22492e9d29f85397807fde";

    /**
     * Retrieves a list of news items based on the provided keyword.
     *
     * @param keyword The keyword to search for news.
     * @return A list of news items.
     * @throws IOException If there's an error while fetching news data.
     */
    public static List<NewsItem> getNews(String keyword) throws IOException {
        List<NewsItem> newsList = new ArrayList<>();

        try {
            String url = "https://newsapi.org/v2/top-headlines?country=ca&q=" + keyword + "&apiKey=" + API_KEY;

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                String jsonResponse = response.toString();
                newsList = parseJsonResponse(jsonResponse);
            } else {
                throw new IOException("Failed to fetch news data. HTTP error code: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
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


