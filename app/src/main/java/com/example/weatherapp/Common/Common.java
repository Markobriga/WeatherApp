package com.example.weatherapp.Common;

import android.location.Location;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Common {
    public static final String APP_ID = "edb0f53c276903c965219072751b7107";
    public static Location current_location = null;
    public static final String lang = "hr";

    public static String convertUnixToDateTime(long dt) {
        Date date= new Date(dt*1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd.MM HH:mm");
        String formatted = sdf.format(date);
        return formatted;
    }

    public static String convertUnixToHour(long dt) {
        Date date= new Date(dt*1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String formatted = sdf.format(date);
        return formatted;
    }

    public static String convertUnixToDate(long dt) {
        Date date= new Date(dt*1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("EEEEEEE");
        String formatted = sdf.format(date);
        return formatted;
    }
}
