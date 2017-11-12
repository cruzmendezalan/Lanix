package com.devops.krakenlabs.lanix.models.device;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

/**
 * Created by Alan Giovani Cruz Méndez on 11/11/17 12:25.
 * cruzmendezalan@gmail.com
 */

public class DeviceRequest {
    public static String TAG = DeviceRequest.class.getSimpleName();
    private String IdentificadorSesion;
    private String Modelo;
    private String IMEI;
    private String VersionApp;
    private String IdentificadorDispositivo;
    private String NumeroDeSerie;
    private String Nombre;
    private String VersionSO;
    private String VersionCatalogos;

    public DeviceRequest(String identificadorSesion, String modelo, String IMEI, String versionApp, String identificadorDispositivo, String numeroDeSerie, String nombre, String versionSO, String versionCatalogos) {
        IdentificadorSesion = identificadorSesion;
        Modelo = modelo;
        this.IMEI = IMEI;
        VersionApp = versionApp;
        IdentificadorDispositivo = identificadorDispositivo;
        NumeroDeSerie = numeroDeSerie;
        Nombre = nombre;
        VersionSO = versionSO;
        VersionCatalogos = versionCatalogos;
    }

    public String getIdentificadorSesion (){
        return IdentificadorSesion;
    }

    public void setIdentificadorSesion (String IdentificadorSesion){
        this.IdentificadorSesion = IdentificadorSesion;
    }

    public String getModelo (){
        return Modelo;
    }

    public void setModelo (String Modelo){
        this.Modelo = Modelo;
    }

    public String getIMEI (){
        return IMEI;
    }

    public void setIMEI (String IMEI){
        this.IMEI = IMEI;
    }

    public String getVersionApp (){
        return VersionApp;
    }

    public void setVersionApp (String VersionApp){
        this.VersionApp = VersionApp;
    }

    public String getIdentificadorDispositivo (){
        return IdentificadorDispositivo;
    }

    public void setIdentificadorDispositivo (String IdentificadorDispositivo){
        this.IdentificadorDispositivo = IdentificadorDispositivo;
    }

    public String getNumeroDeSerie (){
        return NumeroDeSerie;
    }

    public void setNumeroDeSerie (String NumeroDeSerie){
        this.NumeroDeSerie = NumeroDeSerie;
    }

    public String getNombre (){
        return Nombre;
    }

    public void setNombre (String Nombre){
        this.Nombre = Nombre;
    }

    public String getVersionSO (){
        return VersionSO;
    }

    public void setVersionSO (String VersionSO){
        this.VersionSO = VersionSO;
    }

    public String getVersionCatalogos (){
        return VersionCatalogos;
    }

    public void setVersionCatalogos (String VersionCatalogos){
        this.VersionCatalogos = VersionCatalogos;
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
        Log.e(TAG, "toJson: "+jsonString );
        return jsonObject;
    }
    @Override
    public String toString(){
        return TAG+" [IdentificadorSesion = "+IdentificadorSesion+", Modelo = "+Modelo+", IMEI = "+IMEI+", VersionApp = "+VersionApp+", IdentificadorDispositivo = "+IdentificadorDispositivo+", NumeroDeSerie = "+NumeroDeSerie+", Nombre = "+Nombre+", VersionSO = "+VersionSO+", VersionCatalogos = "+VersionCatalogos+"]";
    }
}
