package ca.ucalgary.edu.ensf380;

import ca.ucalgary.edu.ensf380.api_test.WeatherInstance;
import ca.ucalgary.edu.ensf380.api_test.WeatherService;

public class TestWeather {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		WeatherInstance currentWeather = WeatherService.getCurrentWeather();
		System.out.println("Test");
		double temp = currentWeather.getTemperature();
		System.out.println(temp);
	}
}
