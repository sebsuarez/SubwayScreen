package ca.ucalgary.edu.ensf380;

import static org.junit.Assert.*;
import org.junit.Test;

public class WeatherServiceTest {
	
    @Test
    public void testGetCurrentWeather() {
        WeatherInstance testGetCurrentWeather = WeatherService.getCurrentWeather("5913490");
        boolean isWeatherCorrect = true;

        // Check if any required weather data is missing or invalid
        if (testGetCurrentWeather.getFormattedDateTime() == null) {
            isWeatherCorrect = false;
        }
        if (testGetCurrentWeather.getTemperature() == -273.15) {
            isWeatherCorrect = false;
        }
        if (testGetCurrentWeather.getWeatherCondition() == null) {
            isWeatherCorrect = false;
        }
        if (testGetCurrentWeather.getHumidity() == -1) {
            isWeatherCorrect = false;
        }
        if (testGetCurrentWeather.getWindSpeed() == -1.0) {
            isWeatherCorrect = false;
        }

        assertTrue("Weather data is incorrect", isWeatherCorrect);
    }

}
