package com.devops.krakenlabs.lanix.models.session;

/**
 * Created by Alan Giovani Cruz Méndez on 11/11/17 17:46.
 * cruzmendezalan@gmail.com
 */

public class Sesion {
    public static String TAG = String.class.getSimpleName();
    private String Valida;
    private String Identificador;

    public String getValida (){
        return Valida;
    }

    public void setValida (String Valida){
        this.Valida = Valida;
    }

    public String getIdentificador (){
        return Identificador;
    }

    public void setIdentificador (String Identificador){
        this.Identificador = Identificador;
    }

    @Override
    public String toString(){
        return TAG+" [Valida = "+Valida+", Identificador = "+Identificador+"]";
    }
}
