package com.devops.krakenlabs.lanix.models.session;

/**
 * Created by Alan Giovani Cruz Méndez on 11/11/17 12:22.
 * cruzmendezalan@gmail.com
 */

public class RequestSession {
    public static String TAG = RequestSession.class.getSimpleName();
    private String IdentificadorSesion;
    private String Modelo;
    private String IMEI;
    private String VersionApp;
    private String IdentificadorDispositivo;
    private String NumeroDeSerie;
    private String Nombre;
    private String VersionSO;
    private String VersionCatalogos;

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

    @Override
    public String toString(){
        return TAG+" [IdentificadorSesion = "+IdentificadorSesion+", Modelo = "+Modelo+", IMEI = "+IMEI+", VersionApp = "+VersionApp+", IdentificadorDispositivo = "+IdentificadorDispositivo+", NumeroDeSerie = "+NumeroDeSerie+", Nombre = "+Nombre+", VersionSO = "+VersionSO+", VersionCatalogos = "+VersionCatalogos+"]";
    }
}
