package com.example.weatherapp.Retrofit;

import com.example.weatherapp.Model.WeatherForecastResult;
import com.example.weatherapp.Model.WeatherResult;
import com.example.weatherapp.Model.WeatherTomorrowResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IOpenWeatherMap {
    @GET("weather")
    Observable<WeatherResult> getWeatherByLatLng(@Query("lat") String lat,
                                                 @Query("lon") String lng,
                                                 @Query("appid") String appid,
                                                 @Query("units") String unit,
                                                 @Query("lang") String lang);

    @GET("forecast")
    Observable<WeatherForecastResult> getForecastWeatherByLatLng(@Query("lat") String lat,
                                                                 @Query("lon") String lng,
                                                                 @Query("appid") String appid,
                                                                 @Query("units") String unit,
                                                                 @Query("lang") String lang);
    @GET("onecall")
    Observable<WeatherTomorrowResult> getTomorrowWeatherByLatLng(@Query("lat") String lat,
                                                                 @Query("lon") String lng,
                                                                 @Query("exclude") String exclude,
                                                                 @Query("appid") String appid,
                                                                 @Query("units") String unit,
                                                                 @Query("lang") String lang);
}
