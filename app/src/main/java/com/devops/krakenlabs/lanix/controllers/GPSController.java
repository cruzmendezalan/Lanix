package com.devops.krakenlabs.lanix.controllers;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.devops.krakenlabs.lanix.HomeActivity;

/**
 * Created by Alan Giovani Cruz Méndez on 12/11/17 14:58.
 * cruzmendezalan@gmail.com
 */

public class GPSController implements ActivityCompat.OnRequestPermissionsResultCallback{
    public static String TAG = GPSController.class.getSimpleName();
    private Context context;
    private HomeActivity homeActivity;
    private static GPSController gpsController;
    private Boolean mLocationPermissionGranted;
    /**
     * Id to identify a gps permission request.
     */
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

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

    public void setHomeActivity(HomeActivity homeActivity) {
        this.homeActivity = homeActivity;
        getLocationPermission();
    }

    private void getLocationPermission() {
    /*
     * Request location permission, so that we can get the location of the
     * device. The result of the permission request is handled by a callback,
     * onRequestPermissionsResult.
     */
        if (ContextCompat.checkSelfPermission(context.getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(homeActivity,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult() called with: requestCode = [" + requestCode + "], permissions = [" + permissions + "], grantResults = [" + grantResults + "]");
    }
}
