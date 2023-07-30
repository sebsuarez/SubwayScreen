package ca.ucalgary.edu.ensf380.api_test;

import java.io.IOException;
import java.util.List;

public class MainClass {

	public static void main(String[] args) throws IOException {
	    WeatherInstance currentWeather = WeatherService.getCurrentWeather();
	    double temp = currentWeather.getTemperature();
	    System.out.println(temp);
	    List<NewsItem> news = NewsService.getNews("Calgary");

	    if (news.isEmpty()) {
	        System.out.println("No news available.");
	    } else {
	        for (NewsItem newsItem : news) {
	            System.out.println("Title: " + newsItem.getTitle());
	            System.out.println("Source: " + newsItem.getSourceName());
	            System.out.println();
	        }
	    }
	}

}
