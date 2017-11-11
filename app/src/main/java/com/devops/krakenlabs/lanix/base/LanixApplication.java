package com.devops.krakenlabs.lanix.base;

import android.app.Application;
import android.content.Context;
import android.util.Log;

/**
 * Created by Alan Giovani Cruz Méndez on 11/11/17 13:24.
 * cruzmendezalan@gmail.com
 */

public class LanixApplication extends Application {
    private static String TAG = LanixApplication.class.getSimpleName();
    private static LanixApplication lanixApplication;

    public Context getInstance(){
        if (lanixApplication == null){
            lanixApplication = new LanixApplication();
        }
        return lanixApplication;
    }


    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate() called");
        super.onCreate();
        lanixApplication = this;
    }
}
