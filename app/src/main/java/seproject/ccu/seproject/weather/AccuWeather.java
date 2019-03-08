package seproject.ccu.seproject.weather;

import java.util.List;
import seproject.ccu.seproject.weather.weatherContent.*;

public class AccuWeather {
    private String WeatherText;
    private String WeatherIcon;
    private Temperature Temperature;
    private int RelativeHumidity;

    public AccuWeather() {}

    public AccuWeather(String weatherText, String weatherIcon, Temperature temperature, int relativeHumidity) {
        WeatherText = weatherText;
        WeatherIcon = weatherIcon;
        this.Temperature = temperature;
        this.RelativeHumidity = relativeHumidity;
    }

    public String getWeatherText() {
        return WeatherText;
    }

    public void setWeatherText(String weatherText) {
        WeatherText = weatherText;
    }

    public String getWeatherIcon() {
        return WeatherIcon;
    }

    public void setWeatherIcon(String weatherIcon) {
        WeatherIcon = weatherIcon;
    }

    public Temperature getTemperature() {
        return Temperature;
    }

    public void setTemperature(Temperature temperature) {
        this.Temperature = temperature;
    }

    public int getRelativeHumidity() {
        return RelativeHumidity;
    }

    public void setRelativeHumidity(int relativeHumidity) {
        this.RelativeHumidity = relativeHumidity;
    }
}
