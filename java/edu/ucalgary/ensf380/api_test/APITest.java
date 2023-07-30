package edu.ucalgary.ensf380.api_test;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class APITest {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please provide the city code as a command-line argument.");
            return;
        }

        String apiKey = "7919cef0282916061e569b2bbca43b3f";
        String cityCode = args[0];

        OkHttpClient client = new OkHttpClient();

        String url = "https://api.openweathermap.org/data/2.5/weather?id=" + cityCode + "&appid=" + apiKey + "&units=metric";

        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                String html = response.body().string();
                
                String temperatureRegex = "\"temp\":(\\d+\\.\\d+)";
                String weatherConditionRegex = "\"description\":\"([^\"]+)\"";
                String humidityRegex = "\"humidity\":(\\d+)";
                String windSpeedRegex = "\"speed\":(\\d+\\.\\d+)";

                Pattern temperaturePattern = Pattern.compile(temperatureRegex);
                Pattern weatherConditionPattern = Pattern.compile(weatherConditionRegex);
                Pattern humidityPattern = Pattern.compile(humidityRegex);
                Pattern windSpeedPattern = Pattern.compile(windSpeedRegex);

                Matcher temperatureMatcher = temperaturePattern.matcher(html);
                Matcher weatherConditionMatcher = weatherConditionPattern.matcher(html);
                Matcher humidityMatcher = humidityPattern.matcher(html);
                Matcher windSpeedMatcher = windSpeedPattern.matcher(html);

                if (temperatureMatcher.find() && weatherConditionMatcher.find() &&
                        humidityMatcher.find() && windSpeedMatcher.find()) {
                    double temperature = Double.parseDouble(temperatureMatcher.group(1));
                    String weatherCondition = weatherConditionMatcher.group(1);
                    int humidity = Integer.parseInt(humidityMatcher.group(1));
                    double windSpeed = Double.parseDouble(windSpeedMatcher.group(1));
                    
                    windSpeed *= 3.6;

                    System.out.println("Current Temperature: " + temperature + " °C");
                    System.out.println("Weather Condition: " + weatherCondition);
                    System.out.println("Humidity: " + humidity + "%");
                    System.out.println("Wind Speed: " + windSpeed + " km/h");
                } else {
                    System.out.println("Error: Unable to parse weather data from the HTML response.");
                }
            } else {
                System.out.println("Error: Unable to fetch weather data. Please check the city code and API key.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
