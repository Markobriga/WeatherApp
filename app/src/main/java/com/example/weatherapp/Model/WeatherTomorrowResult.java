package com.example.weatherapp.Model;

import java.util.List;

public class WeatherTomorrowResult {
    public double lat;

    public List<Daily> getDaily() {
        return daily;
    }

    public void setDaily(List<Daily> daily) {
        this.daily = daily;
    }

    public double lon;
    public String timezone;
    public int timezone_offset;
    public List<Daily> daily;
}
