package com.devops.krakenlabs.lanix.controllers;

import android.content.Context;
import android.util.Log;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.devops.krakenlabs.lanix.base.LanixApplication;
import com.devops.krakenlabs.lanix.models.catalogos.CatalogRequest;
import com.devops.krakenlabs.lanix.models.session.User;

import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;


import javax.net.ssl.TrustManager;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;
import java.util.ResourceBundle;
/**
 * Created by Alan Giovani Cruz Méndez on 11/11/17 13:31.
 * cruzmendezalan@gmail.com
 */

public class NetworkController {
    private static final String TAG = NetworkController.class.getSimpleName();
    private static NetworkController networkController;
    private Context context;
    private Properties properties;
    private InputStream inputStream = null;
    // Instantiate the cache
    private Cache cache;
    // Set up the network to use HttpURLConnection as the HTTP client.
    private Network network;
    // Instantiate the RequestQueue with the cache and network.
    private RequestQueue queue;

    //Singleton
    public static synchronized NetworkController getInstance(Context context){
        if (networkController == null){
            networkController = new NetworkController(context);
        }
        return networkController;
    }


    public NetworkController(Context context) {
        this.context    = context;
        this.properties = initProperties();
        cache           = new DiskBasedCache(context.getCacheDir(), 1024 * 1024 * 5); // 5MB cap
        network         = new BasicNetwork(new HurlStack());
        queue           = new RequestQueue(cache, network);
        // Start the queue
        queue.start();
    }


    /**
     * inicializamos el archivo de propiedades, que es donde se almacenan
     * las urls de los servicios
     * @return
     */
    private Properties initProperties(){
        properties = new Properties();
        try {
            properties.load(context.getAssets().open("services.properties"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return properties;
    }

    private RequestQueue getQueue() {
        return queue;
    }

    public String getServiceUrl(String service) {
        Log.d(TAG, "getServiceUrl() called with: service = [" + service + "]");
        String url = properties.getProperty("ProductionUrl"); //Lógica para url de desarrollo y producción aqui
        return url + properties.getProperty(service);
    }

    public void requestData(LanixRequest objectToSend, int method,
                            Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener ){
        Log.d(TAG, "requestData() called with: objectToSend = [" + objectToSend + "],\n  method = [" + method + "],\n responseListener = [" + responseListener + "],\n errorListener = [" + errorListener + "]");
//        HttpsTrustManager.allowAllSSL();
        String t = "";
        if(objectToSend instanceof CatalogRequest){
            t = "/"+LanixApplication.getInstance().getAuthController().getUser().getSesion().getIdentificador();
        }


        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        } };
        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("SSL");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };



        JsonObjectRequest jsObjRequest = new JsonObjectRequest(method,
                getServiceUrl(objectToSend.getClass().getSimpleName())+t,
                objectToSend.toJson(),
                responseListener,
                errorListener);
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        networkController.getQueue().add(jsObjRequest);
    }

}
