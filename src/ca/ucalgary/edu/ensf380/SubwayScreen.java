package ca.ucalgary.edu.ensf380;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SubwayScreen {
    private static List<NewsItem> news;
    private static WeatherInstance currentWeather;
    private static List<Train> trains;

    public static void main(String[] args) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> updateData(), 0, 15, TimeUnit.SECONDS);
    }

    private static void updateData() {
        // Update news
        try {
            news = NewsService.getNews("Canada");
        } catch (Exception e) {
            e.printStackTrace();
            news = null;
        }

        try {
            currentWeather = WeatherService.getCurrentWeather();
        } catch (Exception e) {
            e.printStackTrace();
            currentWeather = null;
        }

        SubwaySystem.getSubwaySystem();
        trains = SubwaySystem.getTrains();

        System.out.println("News:");
        if (news != null) {
            for (NewsItem newsItem : news) {
                System.out.println("Title: " + newsItem.getTitle());
                System.out.println("Source: " + newsItem.getSourceName());
                System.out.println();
            }
        } else {
            System.out.println("No news available.");
        }

        System.out.println("Current Weather:");
        if (currentWeather != null) {
            System.out.println("Temperature: " + currentWeather.getTemperature() + " °C");
            System.out.println("Date and Time: " + currentWeather.getFormattedDateTime());
        } else {
            System.out.println("Weather data unavailable");
        }

        System.out.println("Trains:");
        if (trains != null) {
            for (Train train : trains) {
                System.out.println("Train Number: " + train.getTrainNumber() + " is at: " + train.getCurrentStation().getStationName());
            }
        } else {
            System.out.println("No train data available.");
        }
    }
}

