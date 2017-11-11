package com.devops.krakenlabs.lanix.controllers;

import android.content.Context;

import com.devops.krakenlabs.lanix.base.LanixApplication;
import com.devops.krakenlabs.lanix.models.session.RequestSession;
import com.devops.krakenlabs.lanix.models.session.User;

import java.util.ArrayList;

/**
 * Created by Alan Giovani Cruz Méndez on 11/11/17 12:26.
 * cruzmendezalan@gmail.com
 */

public class AuthController {
    private static final String TAG = AuthController.class.getSimpleName();
    private static AuthController authController;
    private Context context;
    private User user;

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
                /**
                 * una vez que el usuario y la contraseña pasan por validaciones locales
                 * construimos el objeto que sera enviado al servicio
                 */
                RequestSession requestSession = new RequestSession();
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
