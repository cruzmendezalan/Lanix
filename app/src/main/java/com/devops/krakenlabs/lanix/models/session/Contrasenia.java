package com.devops.krakenlabs.lanix.models.session;

import com.google.gson.Gson;

import org.json.JSONObject;

/**
 * Created by Alan Giovani Cruz Méndez on 26/11/17 19:28.
 * cruzmendezalan@gmail.com
 */

public class Contrasenia {
    public static String TAG = com.devops.krakenlabs.lanix.models.session.Contrasenia.class.getSimpleName();
    private String ConfirmacionNuevaContrasenia;
    private String NuevaContrasenia;
    private String Contrasenia;
    private String OTP;

    public Contrasenia(String confirmacionNuevaContrasenia, String nuevaContrasenia, String contrasenia, String OTP) {
        ConfirmacionNuevaContrasenia = confirmacionNuevaContrasenia;
        NuevaContrasenia            = nuevaContrasenia;
        Contrasenia                 = contrasenia;
        this.OTP                    = OTP;
    }

    public String getConfirmacionNuevaContrasenia ()
    {
        return ConfirmacionNuevaContrasenia;
    }

    public void setConfirmacionNuevaContrasenia (String ConfirmacionNuevaContrasenia)
    {
        this.ConfirmacionNuevaContrasenia = ConfirmacionNuevaContrasenia;
    }

    public String getNuevaContrasenia ()
    {
        return NuevaContrasenia;
    }

    public void setNuevaContrasenia (String NuevaContrasenia)
    {
        this.NuevaContrasenia = NuevaContrasenia;
    }

    public String getContrasenia ()
    {
        return Contrasenia;
    }

    public void setContrasenia (String Contrasenia)
    {
        this.Contrasenia = Contrasenia;
    }

    public String getOTP ()
    {
        return OTP;
    }

    public void setOTP (String OTP)
    {
        this.OTP = OTP;
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

    @Override
    public String toString() {
        return "Contrasenia{" +
                "ConfirmacionNuevaContrasenia='" + ConfirmacionNuevaContrasenia + '\'' +
                ", NuevaContrasenia='" + NuevaContrasenia + '\'' +
                ", Contrasenia='" + Contrasenia + '\'' +
                ", OTP='" + OTP + '\'' +
                '}';
    }
}
