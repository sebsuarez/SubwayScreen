package ca.ucalgary.edu.ensf380.api_test;

public class WeatherInstance{
	private String formattedDateTime;
	private double temperature;
	private String weatherCondition;
	private int humidity;
	private double windSpeed;
	public WeatherInstance() {
		this.formattedDateTime = null;
		this.temperature = -273.15;
		this.weatherCondition = null;
		this.humidity = -1;
		this.windSpeed = -1.0;
	}
	public String getFormattedDateTime() {
		return formattedDateTime;
	}
	public double getTemperature() {
		return temperature;
	}
	public String getWeatherCondition() {
		return weatherCondition;
	}
	public int getHumidity() {
		return humidity;
	}
	public double getWindSpeed() {
		return windSpeed;
	}
	public WeatherInstance(String formattedDateTime, double temperature, String weatherCondition, int humidity,
			double windSpeed) {
		this.formattedDateTime = formattedDateTime;
		this.temperature = temperature;
		this.weatherCondition = weatherCondition;
		this.humidity = humidity;
		this.windSpeed = windSpeed;
	}
	

}
