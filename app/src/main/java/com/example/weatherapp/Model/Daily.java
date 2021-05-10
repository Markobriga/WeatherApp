package com.example.weatherapp.Model;

import java.util.List;

public class Daily {
    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    public Temp getTemp() {
        return temp;
    }

    public void setTemp(Temp temp) {
        this.temp = temp;
    }

    public int dt;
    public int sunrise;
    public int sunset;
    public Temp temp;
    public FeelsLike feels_like;
    public int pressure;
    public int humidity;
    public double dew_point;
    public double wind_speed;
    public int wind_deg;
    public List<Weather> weather;
    public int clouds;
    public double pop;
    public double rain;
    public double uvi;
}
