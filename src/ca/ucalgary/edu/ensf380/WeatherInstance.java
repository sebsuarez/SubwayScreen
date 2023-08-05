package ca.ucalgary.edu.ensf380;

/**
 * Represents weather information at a specific instance.
 */
public class WeatherInstance {
    private String formattedDateTime;
    private double temperature;
    private String weatherCondition;
    private int humidity;
    private double windSpeed;

    /**
     * Constructs a WeatherInstance object with default values.
     */
    public WeatherInstance() {
        this.formattedDateTime = null;
        this.temperature = -273.15;
        this.weatherCondition = null;
        this.humidity = -1;
        this.windSpeed = -1.0;
    }

    /**
     * Returns the formatted date and time of the weather instance.
     *
     * @return The formatted date and time.
     */
    public String getFormattedDateTime() {
        return formattedDateTime;
    }

    /**
     * Returns the temperature at the weather instance.
     *
     * @return The temperature in Celsius.
     */
    public double getTemperature() {
        return temperature;
    }

    /**
     * Returns the weather condition at the weather instance.
     *
     * @return The weather condition.
     */
    public String getWeatherCondition() {
        return weatherCondition;
    }

    /**
     * Returns the humidity at the weather instance.
     *
     * @return The humidity level.
     */
    public int getHumidity() {
        return humidity;
    }

    /**
     * Returns the wind speed at the weather instance.
     *
     * @return The wind speed.
     */
    public double getWindSpeed() {
        return windSpeed;
    }

    /**
     * Constructs a WeatherInstance object with specified values.
     *
     * @param formattedDateTime The formatted date and time.
     * @param temperature       The temperature in Celsius.
     * @param weatherCondition The weather condition.
     * @param humidity          The humidity level.
     * @param windSpeed         The wind speed.
     */
    public WeatherInstance(String formattedDateTime, double temperature, String weatherCondition, int humidity,
            double windSpeed) {
        this.formattedDateTime = formattedDateTime;
        this.temperature = temperature;
        this.weatherCondition = weatherCondition;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
    }
}
