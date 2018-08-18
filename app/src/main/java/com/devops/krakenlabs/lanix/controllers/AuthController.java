package com.devops.krakenlabs.lanix.controllers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.devops.krakenlabs.lanix.ControllerNotifier;
import com.devops.krakenlabs.lanix.base.LanixApplication;
import com.devops.krakenlabs.lanix.listeners.SessionNotifier;
import com.devops.krakenlabs.lanix.models.catalogos.CatalogRequest;
import com.devops.krakenlabs.lanix.models.device.Device;
import com.devops.krakenlabs.lanix.models.device.DeviceRequest;
import com.devops.krakenlabs.lanix.models.session.Error;
import com.devops.krakenlabs.lanix.models.session.SessionRequest;
import com.devops.krakenlabs.lanix.models.session.User;
import com.google.gson.Gson;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by Alan Giovani Cruz Méndez on 11/11/17 12:26. cruzmendezalan@gmail.com
 */

public class AuthController implements Response.ErrorListener, Response.Listener<JSONObject> {
  private static final String TAG = AuthController.class.getSimpleName();
  private static AuthController authController;
  private SessionNotifier sessionNotifier;
  private Context context;
  private User user;
  private Device device;
  private ControllerNotifier controllerNotifier;
  private int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 12221;
  private String KEY_CATALOG = "CATALOG_KEY";

  private Context mContext;
  private boolean isRefesh;
  private VolleyError error;
  private static int MY_PERMISSIONS_REQUEST_ACCESS_TELEPHONY_SERVICE;
  private com.devops.krakenlabs.lanix.models.session.Error errors;

  // Singleton
  public static synchronized AuthController getInstance(Context context) {
    if (authController == null) {
      authController = new AuthController(context);
    }
    return authController;
  }

  public AuthController(Context context) {
    this.context = context;
    isRefesh = false;
  }

  public SessionNotifier getSessionNotifier() {
    return sessionNotifier;
  }

  public void setSessionNotifier(SessionNotifier sessionNotifier) {
    this.sessionNotifier = sessionNotifier;
  }

  public ArrayList<String> login(String username, String pasword) {
    ArrayList<String> rulesViolated = null;
    try {
      LanixApplication lanixApplication = LanixApplication.getInstance();
      rulesViolated =
          lanixApplication.getMiddlewareController().validateCredentials(username, pasword);
      if (rulesViolated == null && device != null) {//
        /**
         * una vez que el usuario y la contraseña pasan por validaciones locales construimos el
         * objeto que sera enviado al servicio
         */
        SessionRequest sessionRequest =
            new SessionRequest(username, device.getTokenDispositivo(), pasword);
        NetworkController networkController = lanixApplication.getNetworkController();
        networkController.requestData(sessionRequest, Request.Method.POST, this, this);
        return null;
      } else {
        Log.w(TAG, "login: DEVICE NULL");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    onErrorResponse(null);
    return rulesViolated;
  }


  public void syncDevice() {
    Log.d(TAG, "syncDevice() called");
    try {
      Log.d("msg", "Device id " + getDeviceIMEI());
      TelephonyManager tm = getDeviceIMEI();

      if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

        Log.w(TAG, "syncDevice: SIN PERMISOS.....");
        return;
      }
      DeviceRequest deviceRequest =
          new DeviceRequest(
              (user == null ? "" : user.getSesion().getIdentificador()),
              Build.MODEL,
              tm.getDeviceId(),
              ""
                  + context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName,
              "", tm.getSimSerialNumber(), tm.getDeviceSoftwareVersion(), ""
                  + Build.VERSION.SDK_INT, "");
      deviceRequest.setNombre(tm.getDeviceId());
      // Log.e(TAG, "syncDevice: "+deviceRequest.toString() );
      LanixApplication lanixApplication = LanixApplication.getInstance();
      NetworkController networkController = lanixApplication.getNetworkController();
      LanixApplication.getInstance().getNetworkController()
          .requestData(deviceRequest, Request.Method.POST, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
              Log.d(TAG, "onResponse() called with: response = [" + response + "]");
              Gson gson = new Gson();
              device = gson.fromJson(response.toString(), Device.class);
              if (controllerNotifier != null) {
                controllerNotifier.tokenDeviceComplete();
              }
            }
          }, this);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onErrorResponse(VolleyError error) {
    Log.e(TAG, "onErrorResponse() called with: error = [" + error + "]");
    this.error = error;
    if (sessionNotifier != null) {
      sessionNotifier.sessionComplete();
    }
  }

  public VolleyError getError() {
    return error;
  }

  @Override
  // Respuesta de sesion
  public void onResponse(JSONObject response) {
    try {
      Log.e(TAG, "onResponse() called with: response = [" + response + "]");
      storeProfile(response.toString());
      user = getProfile();
      Log.w(TAG, "onResponse: " + user.getError());
      errors = user.getError();
      if (user.getError().getNo() != 0) {
        user = null;
      }
      Log.e(TAG, "onResponse: " + isRefesh);
      if (sessionNotifier != null && !isRefesh) {
        sessionNotifier.sessionComplete();
      }
      requestCatalogs();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public User getUser() {
    return getProfile();
  }

  public void setContext(Context context) {
    this.context = context;
  }

  public ControllerNotifier getControllerNotifier() {
    return controllerNotifier;
  }

  public void setControllerNotifier(ControllerNotifier controllerNotifier) {
    this.controllerNotifier = controllerNotifier;
  }

  public Device getDevice() {
    return device;
  }

  /**
   * Returns the unique identifier for the device
   *
   * @return unique identifier for the device
   */
  public TelephonyManager getDeviceIMEI() {
    try {
      String deviceUniqueIdentifier = null;
      TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
      if (null == deviceUniqueIdentifier || 0 == deviceUniqueIdentifier.length()) {
        deviceUniqueIdentifier =
            Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
      }
      // Log.d(TAG, "getDeviceIMEI() called "+tm.getDeviceSoftwareVersion());
      // Log.d(TAG, "getDeviceIMEI() called "+tm.getSimSerialNumber());
      // Log.d(TAG, "getDeviceIMEI() called "+tm.getDeviceId());
      return tm;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  private void requestCatalogs() {
    CatalogRequest catalogRequest = new CatalogRequest();
    LanixApplication.getInstance().getNetworkController()
        .requestData(catalogRequest, Request.Method.GET, new Response.Listener<JSONObject>() {
          @Override
          public void onResponse(JSONObject response) {
            Log.e(TAG, "onResponse() called with: response = [" + response + "]");
            storeCatalogs(response.toString());
          }
        }, this);
  }

  private void storeCatalogs(String response) {
    SharedPreferences sharedPref = ((Activity) mContext).getPreferences(Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPref.edit();
    editor.putString(KEY_CATALOG, response);
    editor.commit();
  }

  public String getCatalog() {
    SharedPreferences sharedPref = ((Activity) mContext).getPreferences(Context.MODE_PRIVATE);
    return sharedPref.getString(KEY_CATALOG, "");
  }

  private void storeProfile(String response) {
    try {
      if (null != response) {
        Gson gson = new Gson();
        user = gson.fromJson(response, User.class);
      } else {
        SharedPreferences sharedPref = ((Activity) mContext).getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(User.TAG, response);
        editor.commit();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private User getProfile() {
    if (null != user) {
      return user;
    } else {
      SharedPreferences sharedPref = ((Activity) mContext).getPreferences(Context.MODE_PRIVATE);
      String t = sharedPref.getString(User.TAG, "");
      if (!"".equals(t)) {
        Gson gson = new Gson();
        user = gson.fromJson(t, User.class);
        if (user.getError() != null) {
          errors = user.getError();
        }
      }
    }
    return user;
  }


  public void setmContext(Context mContext) {
    this.mContext = mContext;
  }

  public void setRefesh(boolean refesh) {
    isRefesh = refesh;
  }

  public Error getErrors() {
    return errors;
  }
}
