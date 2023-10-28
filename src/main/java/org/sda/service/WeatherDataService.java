package org.sda.service;

import org.sda.model.Location;
import org.sda.model.WeatherData;

import java.util.List;
import java.util.UUID;

public interface WeatherDataService {
    WeatherData getWeatherDataById(UUID id);

    List<WeatherData> getWeatherDataForLocation(Location location);
    public void addWeatherData(WeatherData weatherData);

    public void  updateWheatherData(WeatherData weatherData);
    public void deleteWheatherData(WeatherData weatherData);
}
