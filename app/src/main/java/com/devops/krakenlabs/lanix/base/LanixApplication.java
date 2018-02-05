package com.devops.krakenlabs.lanix.base;

import android.app.Application;

import com.devops.krakenlabs.lanix.controllers.AuthController;
import com.devops.krakenlabs.lanix.controllers.MiddlewareController;
import com.devops.krakenlabs.lanix.controllers.NetworkController;
import com.devops.krakenlabs.lanix.controllers.PersistenceController;

/**
 * Created by Alan Giovani Cruz Méndez on 11/11/17 13:24.
 * cruzmendezalan@gmail.com
 */

public class LanixApplication extends Application {
    private static String TAG = LanixApplication.class.getSimpleName();
    private static LanixApplication lanixApplication;

    //Controladores de aplicación
    private PersistenceController persistenceController;
    private AuthController authController;
    private MiddlewareController middlewareController;
    private NetworkController networkController;


    public static LanixApplication getInstance(){
        if (lanixApplication == null){
            lanixApplication = new LanixApplication();
        }
        return lanixApplication;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        lanixApplication     = this;
        persistenceController = PersistenceController.getInstance();
        persistenceController.setContext(this);
        middlewareController = MiddlewareController.getInstance(); //reglas de negocio, validaciones
        networkController    = NetworkController.getInstance(this); //Consumo de red
        authController       = AuthController.getInstance(this);//Usuario

    }

    public AuthController getAuthController() {
        return authController;
    }

    public MiddlewareController getMiddlewareController() {
        return middlewareController;
    }

    public NetworkController getNetworkController() {
        return networkController;
    }
}
