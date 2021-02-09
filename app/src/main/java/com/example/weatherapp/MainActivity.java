package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherapp.Adapter.WeatherForecastAdapter;
import com.example.weatherapp.Common.Common;
import com.example.weatherapp.Model.WeatherForecastResult;
import com.example.weatherapp.Model.WeatherResult;
import com.example.weatherapp.Retrofit.IOpenWeatherMap;
import com.example.weatherapp.Retrofit.RetrofitClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    SwipeRefreshLayout swiperefresh;

    ImageView img_weather;
    TextView txt_city_name, txt_humidity, txt_sunrise, txt_sunset, txt_pressure, txt_temperature, txt_date_time, txt_wind, txt_summary, txt_description;
    LinearLayout weather_panel;
    ProgressBar loading;
    String description;

    RecyclerView recycler_forecast;

    CompositeDisposable compositeDisposable, compositeDisposable1;
    IOpenWeatherMap mService, mService1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swiperefresh=(SwipeRefreshLayout)findViewById(R.id.swiperefresh);

        //Request permission
        Dexter.withActivity(this).withPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                if (multiplePermissionsReport.areAllPermissionsGranted()) {
                    buildLocationRequest();
                    buildLocationCallBack();

                    swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());
                            overridePendingTransition(0, 0);
                            swiperefresh.setRefreshing(false);
                        }
                    });

                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
                    fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                Toast.makeText(MainActivity.this,"Dozvola odbijena",Toast.LENGTH_LONG).show();
            }
        }).check();
    }


    private void buildLocationRequest() {
        locationRequest= new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10.0f);
    }


    private void buildLocationCallBack() {
        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                Common.current_location = locationResult.getLastLocation();

                todayWeather();
                forecastWeather();
            }
        };

    }


    private void todayWeather() {

        compositeDisposable = new CompositeDisposable();
        Retrofit retrofit = RetrofitClient.getInstance();
        mService = retrofit.create(IOpenWeatherMap.class);

        img_weather = (ImageView)findViewById(R.id.img_weather);
        txt_city_name = (TextView)findViewById(R.id.txt_city_name);
        txt_date_time = (TextView)findViewById(R.id.txt_date_time);
        txt_humidity = (TextView)findViewById(R.id.txt_humidity);
        txt_sunrise = (TextView)findViewById(R.id.txt_sunrise);
        txt_sunset = (TextView)findViewById(R.id.txt_sunset);
        txt_pressure = (TextView)findViewById(R.id.txt_pressure);
        txt_temperature = (TextView)findViewById(R.id.txt_temperature);
        txt_wind = (TextView)findViewById(R.id.txt_wind);
        txt_summary = (TextView)findViewById(R.id.txt_summary);
        txt_description = (TextView)findViewById(R.id.txt_description);

        weather_panel = (LinearLayout)findViewById(R.id.weather_panel);
        loading = (ProgressBar)findViewById(R.id.loading);

        getWeatherInformation();
    }

    private void getWeatherInformation() {
        compositeDisposable.add(mService.getWeatherByLatLng(String.valueOf(Common.current_location.getLatitude()),
                String.valueOf(Common.current_location.getLongitude()),
                Common.APP_ID,
                "metric",
                Common.lang)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<WeatherResult>() {
            @Override
            public void accept(WeatherResult weatherResult) throws Exception {
                Picasso.get().load(new StringBuilder("https://openweathermap.org/img/wn/").append(weatherResult.getWeather().get(0).getIcon()).append("@2x.png").toString()).into(img_weather);

                txt_city_name.setText(weatherResult.getName());
                txt_temperature.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getTemp())).append("°C").toString());
                txt_date_time.setText(Common.convertUnixToDateTime(weatherResult.getDt()));
                txt_pressure.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getPressure())).append(" hpa").toString());
                txt_humidity.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getHumidity())).append("%").toString());
                txt_sunrise.setText(Common.convertUnixToHour(weatherResult.getSys().getSunrise()));
                txt_sunset.setText(Common.convertUnixToHour(weatherResult.getSys().getSunset()));
                txt_wind.setText(new StringBuilder(String.valueOf(weatherResult.getWind().getSpeed())).append((" km/h")));
                txt_summary.setText("Čini se kao " + new StringBuilder(String.valueOf((int)weatherResult.getMain().getFeels_like())).append("°C").toString());

                description = new StringBuilder(String.valueOf(weatherResult.getWeather().get(0).getDescription())).toString();
                description = description.substring(0, 1).toUpperCase() + description.substring(1);
                txt_description.setText(description);

                weather_panel.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Toast.makeText(MainActivity.this,""+throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }));
    }

    private void forecastWeather() {
        compositeDisposable1 = new CompositeDisposable();
        Retrofit retrofit = RetrofitClient.getInstance();
        mService1 = retrofit.create(IOpenWeatherMap.class);

        recycler_forecast=(RecyclerView)findViewById(R.id.recycler_forecast);
        recycler_forecast.setHasFixedSize(true);
        recycler_forecast.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));

        getForecastWeatherInformation();
    }

    private void getForecastWeatherInformation() {
        compositeDisposable.add(mService.getForecastWeatherByLatLng(String.valueOf(Common.current_location.getLatitude()),
                String.valueOf(Common.current_location.getLongitude()),
                Common.APP_ID,
                "metric",
                Common.lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherForecastResult>() {
                    @Override
                    public void accept(WeatherForecastResult weatherForecastResult) throws Exception {
                        displayForecastWeather(weatherForecastResult);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(MainActivity.this,""+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
        );


    }

    private void displayForecastWeather(WeatherForecastResult weatherForecastResult) {
        WeatherForecastAdapter adapter = new WeatherForecastAdapter(MainActivity.this, weatherForecastResult);
        recycler_forecast.setAdapter(adapter);
    }

}