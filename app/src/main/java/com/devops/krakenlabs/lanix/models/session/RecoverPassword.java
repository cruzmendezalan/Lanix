package com.devops.krakenlabs.lanix.models.session;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;

/**
 * Created by Alan Giovani Cruz Méndez on 26/11/17 15:43.
 * cruzmendezalan@gmail.com
 */

public class RecoverPassword {
    public static String TAG = RecoverPassword.class.getSimpleName();
    private String CorreoElectronico;

    public RecoverPassword(String email) {
        this.CorreoElectronico = email;
    }

    @Override
    public String toString() {
        return "RecoverPassword{" +
                "CorreoElectronico='" + CorreoElectronico + '\'' +
                '}';
    }

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
        return jsonObject;
    }
}
