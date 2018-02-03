package com.devops.krakenlabs.lanix.controllers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.devops.krakenlabs.lanix.LoginActivity;
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

    private static int MY_PERMISSIONS_REQUEST_ACCESS_TELEPHONY_SERVICE;

    //Singleton
    public static synchronized AuthController getInstance(Context context) {
        if (authController == null) {
            authController = new AuthController(context);
        }
        return authController;
    }

    public AuthController(Context context) {
        this.context = context;
    }

    public SessionNotifier getSessionNotifier() {
        return sessionNotifier;
    }

    public void setSessionNotifier(SessionNotifier sessionNotifier) {
        this.sessionNotifier = sessionNotifier;
    }

    public ArrayList<String> login(String username, String pasword) {
        Log.d(TAG, "login() called with: username = [" + username + "], pasword = [" + pasword + "]");
        try {
            LanixApplication lanixApplication = LanixApplication.getInstance();
            ArrayList<String> rulesViolated = lanixApplication.getMiddlewareController().validateCredentials(username, pasword);
            if (rulesViolated == null && device != null) {//
                //hacer login
                /**
                 * una vez que el usuario y la contraseña pasan por validaciones locales
                 * construimos el objeto que sera enviado al servicio
                 */
                RequestSession requestSession = new RequestSession(username, device.getTokenDispositivo(), pasword);
                NetworkController networkController = lanixApplication.getNetworkController();
                JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,
                        networkController.getServiceUrl(User.TAG),
                        requestSession.toJson(),
                        this,
                        this);
                jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                        0,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                Log.e(TAG, "login: "+networkController.getServiceUrl(User.TAG) );
                networkController.getQueue().add(jsObjRequest);
                return null;
            }
            return rulesViolated;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public void syncDevice() {
        try {
            TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                Log.e(TAG, "syncDevice: NO HAY PERMISOS" );
                if (context instanceof LoginActivity){
                    LoginActivity la = (LoginActivity) context;
                    la.mayRequestDevice();
                }else{
                    Log.e(TAG, "syncDevice: NO ES INSTANCIA DE LOGIN" );
                }
                return;
            }
            String deviceid = manager.getDeviceId();

            //Device Id is IMEI number

            Log.d("msg", "Device id " + deviceid);
            DeviceRequest deviceRequest = new DeviceRequest("",
                    "",
                    manager.getDeviceId(),
                    "0.9.4",
                    (user==null?"":user.getSesion().getIdentificador()),
                    manager.getDeviceId(),
                    "",
                    manager.getDeviceSoftwareVersion(),
                    ""
                    );
            Log.e(TAG, "syncDevice: "+deviceRequest.toJson().toString() );
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
            requestSyncDevice.setRetryPolicy(new DefaultRetryPolicy(5*DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, 0));
            requestSyncDevice.setRetryPolicy(new DefaultRetryPolicy(0, 0, 0));
            networkController.getQueue().add(requestSyncDevice);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e(TAG, "onErrorResponse() called with: error = [" + error + "]");
        if (sessionNotifier != null){
            sessionNotifier.sessionComplete();
        }
    }


    @Override //Respuesta de sesion
    public void onResponse(JSONObject response) {
        try{
            Log.e(TAG, "onResponse() called with: response = [" + response + "]");
            Gson gson = new Gson();
            user = gson.fromJson(response.toString(), User.class);
            if (user.getError().getNo() != 0){
                user = null;
            }
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

    public void setContext(Context context) {
        this.context = context;
    }
}
