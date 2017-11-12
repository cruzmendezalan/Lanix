package com.devops.krakenlabs.lanix.models.session;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;

/**
 * Created by Alan Giovani Cruz Méndez on 11/11/17 12:22.
 * cruzmendezalan@gmail.com
 */

public class RequestSession {
    public static String TAG = RequestSession.class.getSimpleName();
    private String Usuario;
    private String TokenDispositivo;
    private String Contrasenia;

    public RequestSession(String usuario, String tokenDispositivo, String contrasenia) {
        Usuario = usuario;
        TokenDispositivo = tokenDispositivo;
        Contrasenia = contrasenia;
    }

    public String getUsuario (){
        return Usuario;
    }

    public void setUsuario (String Usuario){
        this.Usuario = Usuario;
    }

    public String getTokenDispositivo (){
        return TokenDispositivo;
    }

    public void setTokenDispositivo (String TokenDispositivo){
        this.TokenDispositivo = TokenDispositivo;
    }

    public String getContrasenia (){
        return Contrasenia;
    }

    public void setContrasenia (String Contrasenia){
        this.Contrasenia = Contrasenia;
    }
    public JSONObject toJson(){
        JSONObject jsonObject = null;
        String jsonString = "";
        try {
            Gson gson = new Gson();
            jsonString = gson.toJson(this);
            jsonObject = new JSONObject(jsonString);
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.e(TAG, "toJson: "+jsonString );
        return jsonObject;
    }

    @Override
    public String toString(){
        return TAG+" [Usuario = "+Usuario+", TokenDispositivo = "+TokenDispositivo+", Contrasenia = "+Contrasenia+"]";
    }
}
