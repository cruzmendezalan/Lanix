package com.devops.krakenlabs.lanix.models.device;

/**
 * Created by Alan Giovani Cruz Méndez on 11/11/17 12:25.
 * cruzmendezalan@gmail.com
 */

public class Device {
    public static String TAG = Device.class.getSimpleName();
    private String TokenDispositivo;
    private String VersionApp;
    private String SesionValida;
    private String ActualizarCatalogos;
    private String RutaDeDescargaApp;
    private Error Error;

    public String getTokenDispositivo (){
        return TokenDispositivo;
    }

    public void setTokenDispositivo (String TokenDispositivo){
        this.TokenDispositivo = TokenDispositivo;
    }

    public String getVersionApp (){
        return VersionApp;
    }

    public void setVersionApp (String VersionApp){
        this.VersionApp = VersionApp;
    }

    public String getSesionValida (){
        return SesionValida;
    }

    public void setSesionValida (String SesionValida){
        this.SesionValida = SesionValida;
    }

    public String getActualizarCatalogos (){
        return ActualizarCatalogos;
    }

    public void setActualizarCatalogos (String ActualizarCatalogos){
        this.ActualizarCatalogos = ActualizarCatalogos;
    }

    public String getRutaDeDescargaApp (){
        return RutaDeDescargaApp;
    }

    public void setRutaDeDescargaApp (String RutaDeDescargaApp){
        this.RutaDeDescargaApp = RutaDeDescargaApp;
    }

    public Error getError (){
        return Error;
    }

    public void setError (Error Error){
        this.Error = Error;
    }

    @Override
    public String toString(){
        return TAG+" [TokenDispositivo = "+TokenDispositivo+", VersionApp = "+VersionApp+", SesionValida = "+SesionValida+", ActualizarCatalogos = "+ActualizarCatalogos+", RutaDeDescargaApp = "+RutaDeDescargaApp+", Error = "+Error+"]";
    }
}
