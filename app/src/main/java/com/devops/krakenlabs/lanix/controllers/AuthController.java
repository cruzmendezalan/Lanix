package com.devops.krakenlabs.lanix.controllers;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.devops.krakenlabs.lanix.base.LanixApplication;
import com.devops.krakenlabs.lanix.listeners.SessionNotifier;
import com.devops.krakenlabs.lanix.models.device.Device;
import com.devops.krakenlabs.lanix.models.device.DeviceRequest;
import com.devops.krakenlabs.lanix.models.session.RequestSession;
import com.devops.krakenlabs.lanix.models.session.User;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Alan Giovani Cruz Méndez on 11/11/17 12:26.
 * cruzmendezalan@gmail.com
 */

public class AuthController implements Response.ErrorListener, Response.Listener<JSONObject> {
    private static final String TAG = AuthController.class.getSimpleName();
    private static AuthController authController;
    private SessionNotifier sessionNotifier;
    private Context context;
    private User user;
    private Device device;

    //Singleton
    public static synchronized AuthController getInstance(Context context){
        if (authController == null){
            authController = new AuthController(context);
        }
        return authController;
    }

    public AuthController(Context context) {
        this.context = context;
        syncDevice();
    }

    public SessionNotifier getSessionNotifier() {
        return sessionNotifier;
    }

    public void setSessionNotifier(SessionNotifier sessionNotifier) {
        this.sessionNotifier = sessionNotifier;
    }

    public ArrayList<String> login(String username, String pasword){
        Log.d(TAG, "login() called with: username = [" + username + "], pasword = [" + pasword + "]");
        try {
            LanixApplication lanixApplication = LanixApplication.getInstance();
            ArrayList<String> rulesViolated = lanixApplication.getMiddlewareController().validateCredentials(username,pasword);
            if (rulesViolated == null && device != null){//
                //hacer login
                /**
                 * una vez que el usuario y la contraseña pasan por validaciones locales
                 * construimos el objeto que sera enviado al servicio
                 */
                RequestSession requestSession = new RequestSession(username,device.getTokenDispositivo(), pasword);
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

    public void syncDevice(){
        try {
            DeviceRequest deviceRequest = new DeviceRequest("",
                    "SL-A50 mini",
                    "352437061386981",
                    "1.0.0",
                    "db388a3951fecd2b",
                    "0123456789ABCDEF",
                    "SL-A50 mini",
                    "4.4.2",
                    "1978-12-25"
                    );
            LanixApplication lanixApplication   = LanixApplication.getInstance();
            NetworkController networkController = lanixApplication.getNetworkController();
            JsonObjectRequest requestSyncDevice = new JsonObjectRequest(Request.Method.POST,
                    networkController.getServiceUrl(Device.TAG),
                    deviceRequest.toJson(),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Gson gson = new Gson();
                            device    = gson.fromJson(response.toString(),Device.class);
                        }
                    },
                    this);
            networkController.getQueue().add(requestSyncDevice);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.d(TAG, "onErrorResponse() called with: error = [" + error + "]");
    }


    @Override //Respuesta de sesion
    public void onResponse(JSONObject response) {
        try{
            Log.d(TAG, "onResponse() called with: response = [" + response + "]");
            Gson gson = new Gson();
            user = gson.fromJson(response.toString(), User.class);
            if (sessionNotifier != null){
                sessionNotifier.sessionComplete();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public User getUser() {
        return user;
    }
}
