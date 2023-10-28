package org.sda.model;

import javax.persistence.*;
import java.util.Date;
@Entity
@Table(name="weather_data")
public class WeatherData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Date date;
    private double temperature;
    private double humidity;
    private  double pressure;
    private double windspeed;
    private double windDirection;

    public WeatherData() {
    }

    public WeatherData(Long id, Date date, double temperature, double humidity, double pressure, double windspeed, double windDirection) {
        this.id = id;
        this.date = date;
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        this.windspeed = windspeed;
        this.windDirection = windDirection;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getWindspeed() {
        return windspeed;
    }

    public void setWindspeed(double windspeed) {
        this.windspeed = windspeed;
    }

    public double getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(double windDirection) {
        this.windDirection = windDirection;
    }

    @Override
    public String toString() {// to string
        return "WeatherData{" +
                "id=" + id +
                ", date=" + date +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                ", pressure=" + pressure +
                ", windspeed=" + windspeed +
                ", windDirection=" + windDirection +
                '}';
    }
}
