package com.devops.krakenlabs.lanix.controllers;

import java.util.ArrayList;

/**
 * Created by Alan Giovani Cruz Méndez on 11/11/17 15:46.
 * cruzmendezalan@gmail.com
 * LOGICA DE NEGOCIO
 */

public class MiddlewareController {
    public static String TAG = MiddlewareController.class.getSimpleName();
    private static MiddlewareController middlewareController;
    //Singleton
    public static synchronized MiddlewareController getInstance(){
        if (middlewareController == null){
            middlewareController = new MiddlewareController();
        }
        return middlewareController;
    }

    public MiddlewareController() {
    }

    /**
     *
     * @param user
     * @param password
     * @return null  si cumple con las reglas
     */
    public static ArrayList<String> validateCredentials(String user, String password){
        // TODO: 11/11/17 agregar reglas de login
        return null;
    }
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }
}
