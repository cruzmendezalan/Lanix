package com.devops.krakenlabs.lanix.models.catalogos;

import android.util.Log;

import com.devops.krakenlabs.lanix.controllers.LanixRequest;
import com.google.gson.Gson;

import org.json.JSONObject;

/**
 * Created by Alan Giovani Cruz Méndez on 28/12/17 14:08.
 * cruzmendezalan@gmail.com
 */

public class CatalogRequest implements LanixRequest{
    private static final String TAG = CatalogRequest.class.getSimpleName();

    @Override
    public JSONObject toJson(){
        JSONObject jsonObject = null;
        String jsonString = "";
        try {
            Gson gson  = new Gson();
            jsonString = gson.toJson(this);
            jsonObject = new JSONObject(jsonString);
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.e(TAG, "toJson: "+jsonString );
        return jsonObject;
    }
}
