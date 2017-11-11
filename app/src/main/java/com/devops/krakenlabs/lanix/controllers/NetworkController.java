package com.devops.krakenlabs.lanix.controllers;

import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

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
            inputStream = new FileInputStream("services.properties");
            properties.load(inputStream);
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

    public RequestQueue getQueue() {
        return queue;
    }
}
