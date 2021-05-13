package com.example.weatherapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.weatherapp.Common.Common;
import com.example.weatherapp.Model.WeatherResult;
import com.example.weatherapp.Model.WeatherTomorrowResult;
import com.example.weatherapp.Retrofit.IOpenWeatherMap;
import com.example.weatherapp.Retrofit.RetrofitClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TomorrowWeather {
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    CompositeDisposable compositeDisposable;
    IOpenWeatherMap mService;
    public ImageView img_weather;
    String description = "Sutra će biti ";
    String message = "";

    public TomorrowWeather() {
        buildLocationRequest();
        buildLocationCallBack();
    }

    public TomorrowWeather(LocationCallback locationCallback, LocationRequest locationRequest) {
        this.locationCallback = locationCallback;
        this.locationRequest = locationRequest;
    }


    private void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(8000);
        locationRequest.setFastestInterval(4000);
        locationRequest.setSmallestDisplacement(10.0f);

    }

    private void buildLocationCallBack() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                Common.current_location = locationResult.getLastLocation();
            }
        };
        tomorrowWeather();

    }
   /* public void tomorrowWeather() {
        compositeDisposable = new CompositeDisposable();
        Retrofit retrofit = RetrofitClient.getInstance();
        mService = retrofit.create(IOpenWeatherMap.class);
        getTomorrowWeatherInformation();



    }*/

    /*private void getTomorrowWeatherInformation() {
        compositeDisposable.add(mService.getTomorrowWeatherByLatLng(String.valueOf(Common.current_location.getLatitude()),
                String.valueOf(Common.current_location.getLongitude()),
                Common.exclude,
                Common.APP_ID,
                "metric",
                Common.lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherTomorrowResult>() {
                    @Override
                    public void accept(WeatherTomorrowResult weatherTomorrowResult) throws Exception {
                        description+=1;
                        description += new StringBuilder(String.valueOf(weatherTomorrowResult.daily.get(1).weather.get(0).getDescription())) + ". ";
                        description += "Temperatura između " + new StringBuilder(String.valueOf((int)(weatherTomorrowResult.daily.get(1).temp.getMax()))) + "°C i " + new StringBuilder(String.valueOf((int)(weatherTomorrowResult.daily.get(1).temp.getMin()))) + "°C." ;
                        Picasso.get().load(new StringBuilder("https://openweathermap.org/img/wn/").append(weatherTomorrowResult.daily.get(1).weather.get(0).getIcon()).append("@2x.png").toString()).into(img_weather);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        description+=2;
                        message="";
                    }
                })
        );



    }*/
    public void tomorrowWeather() {
        description+=1;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        description+=2;
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<List<WeatherTomorrowResult>>  call = jsonPlaceHolderApi.getTomorrowWeatherByLatLng(String.valueOf(Common.current_location.getLatitude()),
                String.valueOf(Common.current_location.getLongitude()),
                Common.exclude,
                Common.APP_ID,
                "metric",
                Common.lang);

        description+=3;
        call.enqueue(new Callback<List<WeatherTomorrowResult>>() {
            @Override
            public void onResponse(Call<List<WeatherTomorrowResult>> call, Response<List<WeatherTomorrowResult>> response) {
                description+=4;
                List<WeatherTomorrowResult> weatherTomorrowResults =response.body();

                for (WeatherTomorrowResult weatherTomorrowResult : weatherTomorrowResults) {
                    description += new StringBuilder(String.valueOf(weatherTomorrowResult.daily.get(1).weather.get(0).getDescription())) + ". ";
                    description += "Temperatura između " + new StringBuilder(String.valueOf((int)(weatherTomorrowResult.daily.get(1).temp.getMax()))) + "°C i " + new StringBuilder(String.valueOf((int)(weatherTomorrowResult.daily.get(1).temp.getMin()))) + "°C." ;
                }
            }

            public void onFailure(Call<List<WeatherTomorrowResult>> call, Throwable t) {
            }
        });


        description+=5;


    }


}
