package ca.ucalgary.edu.ensf380;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Provides methods to fetch current weather data using the OpenWeatherMap API.
 */
public abstract class WeatherService {
    /**
     * Retrieves the current weather for the specified city using its city code.
     *
     * @param cityCode The city code for the desired city's weather.
     * @return A WeatherInstance object containing the current weather data.
     */
    public static WeatherInstance getCurrentWeather(String cityCode) {
        WeatherInstance currentWeather = new WeatherInstance();
        String apiKey = "7919cef0282916061e569b2bbca43b3f";

        try {
            String url = "https://api.openweathermap.org/data/2.5/weather?id=" + cityCode + "&appid=" + apiKey + "&units=metric";

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

                String html = response.toString();

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

 