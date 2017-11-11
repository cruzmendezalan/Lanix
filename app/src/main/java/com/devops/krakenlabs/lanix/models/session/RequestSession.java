package com.devops.krakenlabs.lanix.models.session;

/**
 * Created by Alan Giovani Cruz Méndez on 11/11/17 12:22.
 * cruzmendezalan@gmail.com
 */

public class RequestSession {
    public static String TAG = RequestSession.class.getSimpleName();
    private String Usuario;
    private String TokenDispositivo;
    private String Contrasenia;

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

    @Override
    public String toString(){
        return TAG+" [Usuario = "+Usuario+", TokenDispositivo = "+TokenDispositivo+", Contrasenia = "+Contrasenia+"]";
    }
}
