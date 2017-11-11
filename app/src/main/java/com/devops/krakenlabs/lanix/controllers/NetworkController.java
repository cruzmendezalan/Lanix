package com.devops.krakenlabs.lanix.controllers;

import android.content.Context;

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
    private Context context;
    private Properties properties;
    private InputStream inputStream = null;

    public NetworkController(Context context) {
        this.context = context;
        this.properties = initProperties();
    }


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
}
