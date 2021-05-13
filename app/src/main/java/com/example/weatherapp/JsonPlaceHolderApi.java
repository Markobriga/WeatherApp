package com.example.weatherapp;

import com.example.weatherapp.Model.WeatherTomorrowResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JsonPlaceHolderApi {
    @GET("onecall")
    Call<List<WeatherTomorrowResult>> getTomorrowWeatherByLatLng(@Query("lat") String lat,
                                                                 @Query("lon") String lng,
                                                                 @Query("exclude") String exclude,
                                                                 @Query("appid") String appid,
                                                                 @Query("units") String unit,
                                                                 @Query("lang") String lang);
}
