package com.devops.krakenlabs.lanix.models;

import com.google.gson.Gson;

import org.json.JSONObject;

/**
 * Created by Alan Giovani Cruz Méndez on 15/11/17 01:21.
 * cruzmendezalan@gmail.com
 */

public class EventEntradaRequest {
    public static final String TAG = EventEntradaRequest.class.getSimpleName();
    private String Latitud;
    private String IdentificadorSesion;
    private String TipoAsistenciaId;
    private String IdentificadorLocal;
    private String Longitud;
    private String FechaHora;

    public EventEntradaRequest(String latitud, String identificadorSesion, String tipoAsistenciaId, String identificadorLocal, String longitud, String fechaHora) {
        Latitud = latitud;
        IdentificadorSesion = identificadorSesion;
        TipoAsistenciaId = tipoAsistenciaId;
        IdentificadorLocal = identificadorLocal;
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
    public String toString(){
        return TAG+" [Latitud = "+Latitud+", IdentificadorSesion = "+IdentificadorSesion+", TipoAsistenciaId = "+TipoAsistenciaId+", IdentificadorLocal = "+IdentificadorLocal+", Longitud = "+Longitud+", FechaHora = "+FechaHora+"]";
    }
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
