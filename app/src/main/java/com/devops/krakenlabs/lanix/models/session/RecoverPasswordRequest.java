package com.devops.krakenlabs.lanix.models.session;

import com.devops.krakenlabs.lanix.controllers.LanixRequest;
import com.google.gson.Gson;
import org.json.JSONObject;

/**
 * Created by Alan Giovani Cruz Méndez on 26/11/17 15:43.
 * cruzmendezalan@gmail.com
 */

public class RecoverPasswordRequest implements LanixRequest{
    public static String TAG = RecoverPasswordRequest.class.getSimpleName();
    private String CorreoElectronico;

    public RecoverPasswordRequest(String email) {
        this.CorreoElectronico = email;
    }

    @Override
    public String toString() {
        return "RecoverPasswordRequest{" +
                "CorreoElectronico='" + CorreoElectronico + '\'' +
                '}';
    }

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
        return jsonObject;
    }
}
