package com.devops.krakenlabs.lanix.models;

import com.devops.krakenlabs.lanix.controllers.LanixRequest;
import com.google.gson.Gson;

import org.json.JSONObject;

/**
 * Created by Alan Giovani Cruz Méndez on 15/11/17 01:21.
 * cruzmendezalan@gmail.com
 */

public class EventEntradaRequest implements LanixRequest{
    public static final String TAG = EventEntradaRequest.class.getSimpleName();
    private String Latitud;
    private String IdentificadorSesion;
    private String TipoAsistenciaId;
    private String IdentificadorLocal;
    private String Longitud;
    private String FechaHora;

    public EventEntradaRequest(String latitud, String longitud, String identificadorSesion, String tipoAsistenciaId, String fechaHora) {
        Latitud = latitud;
        IdentificadorSesion = identificadorSesion;
        TipoAsistenciaId = tipoAsistenciaId;
        Longitud = longitud;
        FechaHora = fechaHora;
    }

    public String getLatitud (){
        return Latitud;
    }

    public void setLatitud (String Latitud){
        this.Latitud = Latitud;
    }

    public String getIdentificadorSesion (){
        return IdentificadorSesion;
    }

    public void setIdentificadorSesion (String IdentificadorSesion){
        this.IdentificadorSesion = IdentificadorSesion;
    }

    public String getTipoAsistenciaId (){
        return TipoAsistenciaId;
    }

    public void setTipoAsistenciaId (String TipoAsistenciaId){
        this.TipoAsistenciaId = TipoAsistenciaId;
    }

    public String getIdentificadorLocal (){
        return IdentificadorLocal;
    }

    public void setIdentificadorLocal (String IdentificadorLocal){
        this.IdentificadorLocal = IdentificadorLocal;
    }

    public String getLongitud (){
        return Longitud;
    }

    public void setLongitud (String Longitud){
        this.Longitud = Longitud;
    }

    public String getFechaHora (){
        return FechaHora;
    }

    public void setFechaHora (String FechaHora){
        this.FechaHora = FechaHora;
    }

    @Override
    public String toString() {
        return "EventEntradaRequest{" +
                " \n Latitud='" + Latitud + '\'' +
                ",\n IdentificadorSesion='" + IdentificadorSesion + '\'' +
                ",\n TipoAsistenciaId='" + TipoAsistenciaId + '\'' +
                ",\n IdentificadorLocal='" + IdentificadorLocal + '\'' +
                ",\n Longitud='" + Longitud + '\'' +
                ",\n FechaHora='" + FechaHora + '\'' +
                '}';
    }

    @Override
    public JSONObject toJson(){
        JSONObject json = null;
        try{
            Gson g = new Gson();
            String t = g.toJson(this);
            return new JSONObject(t);
        }catch (Exception e){
            e.printStackTrace();
        }
        return json;
    }
}
