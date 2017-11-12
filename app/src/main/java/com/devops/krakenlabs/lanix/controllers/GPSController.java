package com.devops.krakenlabs.lanix.controllers;

import android.content.Context;

/**
 * Created by Alan Giovani Cruz Méndez on 12/11/17 14:58.
 * cruzmendezalan@gmail.com
 */

public class GPSController {
    public static String TAG = GPSController.class.getSimpleName();
    private Context context;
    private static GPSController gpsController;

    //Singleton
    public static synchronized GPSController getInstance(Context context){
        if (gpsController == null){
            gpsController = new GPSController(context);
        }
        return gpsController;
    }

    public GPSController(Context context) {
        this.context = context;
    }
}
