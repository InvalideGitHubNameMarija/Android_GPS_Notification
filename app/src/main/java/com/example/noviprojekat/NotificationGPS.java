package com.example.noviprojekat;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Color;

public class NotificationGPS extends Application {


    public static final String CHANNELID_1 = "channel_1";
    public static final String CHANNELID_2 = "channel_2";

    @Override
    public void onCreate() {
        super.onCreate();

        createChannels();
    }


    public void createChannels() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(CHANNELID_1, "channel1", NotificationManager.IMPORTANCE_HIGH);
            channel1.enableLights(true);
            channel1.setLightColor(Color.RED);
            channel1.enableVibration(true);
            channel1.setDescription("Ovo je kanal za neuspesne radnje.");

            NotificationChannel channel2 = new NotificationChannel(CHANNELID_2, "channel2", NotificationManager.IMPORTANCE_HIGH);
            channel2.enableLights(true);
            channel2.setLightColor(Color.BLUE);
            channel2.enableVibration(true);
            channel2.setDescription("Ovo je kanal za uspesne radnje.");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
            manager.createNotificationChannel(channel2);
        }
    }
}
