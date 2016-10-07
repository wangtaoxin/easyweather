package com.weather.byhieg.easyweather.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import com.weather.byhieg.easyweather.Activity.MainActivity;
import com.weather.byhieg.easyweather.Bean.WeatherBean;
import com.weather.byhieg.easyweather.R;
import com.weather.byhieg.easyweather.Tools.HandleDaoData;
import com.weather.byhieg.easyweather.Tools.MyJson;
import com.weather.byhieg.easyweather.Tools.WeatherIcon;

/**
 * Created by byhieg on 16-10-7.
 * Mail byhieg@gmail.com
 */

public class NotificationService extends Service{

    public WeatherBean notificationWeather;
    public NotificationManager notificationManager;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        try {
            notificationWeather = HandleDaoData.getWeatherBean(HandleDaoData.getShowCity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new Notification();
        notification.icon = WeatherIcon.getWeatherColor(MyJson.getWeather(notificationWeather).getNow().getCond().getCode());
        notification.tickerText = "天气状况查看";
        notification.when = System.currentTimeMillis();
        notification.flags = Notification.FLAG_NO_CLEAR;// 不能够自动清除
        notification.contentIntent = pendingIntent;

        RemoteViews contentViews = new RemoteViews(getPackageName(), R.layout.layout_notification);
        contentViews.setTextViewText(R.id.city_text,MyJson.getWeather(notificationWeather).getBasic().getCity());
        contentViews.setTextViewText(R.id.weather_text,MyJson.getWeather(notificationWeather).getNow().getCond().getTxt());
        contentViews.setImageViewResource(R.id.weather_image,
                    WeatherIcon.getWeatherColor(MyJson.getWeather(notificationWeather).getNow().getCond().getCode()));
        contentViews.setTextViewText(R.id.temperature_text,MyJson.getWeather(notificationWeather).getNow().getTmp()+"℃");
        String exerciseStr = "运动情况:" + MyJson.getWeather(notificationWeather).getSuggestion().getSport().getBrf();
        contentViews.setTextViewText(R.id.exercise_text,exerciseStr);
        notification.contentView = contentViews;
        notificationManager.notify(1, notification);
        startForeground(1, notification);
        return super.onStartCommand(intent, flags, startId);
    }
}
