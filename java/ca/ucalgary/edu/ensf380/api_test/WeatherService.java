package ca.ucalgary.edu.ensf380.api_test;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class WeatherService {
    public WeatherService() {
    }
	public static WeatherInstance getCurrentWeather() {
		WeatherInstance currentWeather = new WeatherInstance();
		String apiKey = "7919cef0282916061e569b2bbca43b3f";
        String cityCode = "5913490";

        OkHttpClient client = new OkHttpClient();

        String url = "https://api.openweathermap.org/data/2.5/weather?id=" + cityCode + "&appid=" + apiKey + "&units=metric";

        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                String html = response.body().string();
                
                String dateRegex = "\"dt\":(\\d+)";
                String temperatureRegex = "\"temp\":(\\d+\\.\\d+)";
                String weatherConditionRegex = "\"description\":\"([^\"]+)\"";
                String humidityRegex = "\"humidity\":(\\d+)";
                String windSpeedRegex = "\"speed\":(\\d+\\.\\d+)";

                Pattern datePattern = Pattern.compile(dateRegex);
                Pattern temperaturePattern = Pattern.compile(temperatureRegex);
                Pattern weatherConditionPattern = Pattern.compile(weatherConditionRegex);
                Pattern humidityPattern = Pattern.compile(humidityRegex);
                Pattern windSpeedPattern = Pattern.compile(windSpeedRegex);

                Matcher dateMatcher = datePattern.matcher(html);
                Matcher temperatureMatcher = temperaturePattern.matcher(html);
                Matcher weatherConditionMatcher = weatherConditionPattern.matcher(html);
                Matcher humidityMatcher = humidityPattern.matcher(html);
                Matcher windSpeedMatcher = windSpeedPattern.matcher(html);

                if (dateMatcher.find() && temperatureMatcher.find() && weatherConditionMatcher.find() &&
                        humidityMatcher.find() && windSpeedMatcher.find()) {
                	long timestamp = Long.parseLong(dateMatcher.group(1));
                    double temperature = Double.parseDouble(temperatureMatcher.group(1));
                    String weatherCondition = weatherConditionMatcher.group(1);
                    int humidity = Integer.parseInt(humidityMatcher.group(1));
                    double windSpeed = Double.parseDouble(windSpeedMatcher.group(1));
                    
                    windSpeed *= 3.6;
                    java.util.Date dateTime = new java.util.Date(timestamp * 1000);
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMMM d' 'yyyy, hh:mm a");
                    String formattedDateTime = sdf.format(dateTime);

                    
                    currentWeather = new WeatherInstance(formattedDateTime, temperature, weatherCondition, humidity, windSpeed);
                    
                } else {
                    System.out.println("Error: Unable to parse weather data from the HTML response.");
                }
            } else {
                System.out.println("Error: Unable to fetch weather data. Please check the city code and API key.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		return currentWeather;
	}
}

 