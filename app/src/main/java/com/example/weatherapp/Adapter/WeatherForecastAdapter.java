package com.example.weatherapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.Common.Common;
import com.example.weatherapp.Model.WeatherForecastResult;
import com.example.weatherapp.R;
import com.squareup.picasso.Picasso;

public class WeatherForecastAdapter extends RecyclerView.Adapter<WeatherForecastAdapter.MyViewHolder> {

    Context context;
    WeatherForecastResult weatherForecastResult;

    public WeatherForecastAdapter(Context context, WeatherForecastResult weatherForecastResult) {
        this.context = context;
        this.weatherForecastResult = weatherForecastResult;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_weather_forecast, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.get().load(new StringBuilder("https://openweathermap.org/img/wn/").append(weatherForecastResult.list.get(position).weather.get(0).getIcon()).append("@2x.png").toString()).into(holder.img_forecast_weather);
        holder.txt_date.setText(new StringBuilder(Common.convertUnixToDate(weatherForecastResult.list.get(position).dt)));
        holder.txt_time.setText(new StringBuilder(Common.convertUnixToHour(weatherForecastResult.list.get(position).dt)));
        holder.txt_forecast_temperature.setText(new StringBuilder(String.valueOf(weatherForecastResult.list.get(position).main.getTemp())).append("°"));
        holder.txt_cloudiness.setText(new StringBuilder(String.valueOf(weatherForecastResult.list.get(position).clouds.getAll())).append("%"));

    }

    @Override
    public int getItemCount() {
        return weatherForecastResult.list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txt_date, txt_time, txt_cloudiness, txt_forecast_temperature;
        ImageView img_forecast_weather;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            img_forecast_weather=(ImageView)itemView.findViewById(R.id.img_forecast_weather);
            txt_date=(TextView)itemView.findViewById(R.id.txt_date);
            txt_time=(TextView)itemView.findViewById(R.id.txt_time);
            txt_cloudiness=(TextView)itemView.findViewById(R.id.txt_cloudiness);
            txt_forecast_temperature=(TextView)itemView.findViewById(R.id.txt_forecast_temperature);

        }
    }
}
