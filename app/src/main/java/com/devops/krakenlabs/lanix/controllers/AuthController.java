package com.devops.krakenlabs.lanix.controllers;

import android.content.Context;

import com.devops.krakenlabs.lanix.base.LanixApplication;

import java.util.ArrayList;

/**
 * Created by Alan Giovani Cruz Méndez on 11/11/17 12:26.
 * cruzmendezalan@gmail.com
 */

public class AuthController {
    private static final String TAG = AuthController.class.getSimpleName();
    private static AuthController authController;
    private Context context;
    private String user;
    private String id;

    //Singleton
    public static synchronized AuthController getInstance(Context context){
        if (authController == null){
            authController = new AuthController(context);
        }
        return authController;
    }

    public AuthController(Context context) {
        this.context = context;
    }

    private ArrayList<String> login(String user, String pasword){
        try {
            LanixApplication lanixApplication = LanixApplication.getInstance();
            ArrayList<String> rulesViolated = lanixApplication.getMiddlewareController().validateCredentials(user,pasword);
            if (rulesViolated == null){//
                //hacer login
                NetworkController networkController = lanixApplication.getNetworkController();
                return null;
            }
            return rulesViolated;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
