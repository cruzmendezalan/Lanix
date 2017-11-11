package com.devops.krakenlabs.lanix.controllers;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.devops.krakenlabs.lanix.base.LanixApplication;

/**
 * Created by Alan Giovani Cruz Méndez on 11/11/17 12:26.
 * cruzmendezalan@gmail.com
 */

public class AuthController {
    private static final String TAG = AuthController.class.getSimpleName();
    private Context context;
    // Instantiate the RequestQueue.
//    RequestQueue queue = Volley.newRequestQueue();

    public AuthController(Context context) {
        this.context = context;
    }
}
