package com.devops.krakenlabs.lanix.controllers;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.devops.krakenlabs.lanix.base.LanixApplication;
import com.devops.krakenlabs.lanix.models.session.RequestSession;
import com.devops.krakenlabs.lanix.models.session.User;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Alan Giovani Cruz Méndez on 11/11/17 12:26.
 * cruzmendezalan@gmail.com
 */

public class AuthController implements Response.ErrorListener, Response.Listener<JSONObject> {
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

    public ArrayList<String> login(String username, String pasword){
        try {
            LanixApplication lanixApplication = LanixApplication.getInstance();
            ArrayList<String> rulesViolated = lanixApplication.getMiddlewareController().validateCredentials(username,pasword);
            if (rulesViolated == null){//
                //hacer login
                /**
                 * una vez que el usuario y la contraseña pasan por validaciones locales
                 * construimos el objeto que sera enviado al servicio
                 */
                RequestSession requestSession = new RequestSession(username,"1CFD2E8C-78AE-472F-95E9-9F7210D3386E", pasword);
                NetworkController networkController = lanixApplication.getNetworkController();
                JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,
                                                                        networkController.getServiceUrl(User.TAG),
                                                                        requestSession.toJson(),
                                                                        this,
                                                                        this);
                networkController.getQueue().add(jsObjRequest);
                return null;
            }
            return rulesViolated;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        Log.d(TAG, "onErrorResponse() called with: error = [" + error + "]");
    }


    @Override
    public void onResponse(JSONObject response) {
        Log.d(TAG, "onResponse() called with: response = [" + response + "]");
    }
}
