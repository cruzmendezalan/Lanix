package com.devops.krakenlabs.lanix.models.session;

/**
 * Created by Alan Giovani Cruz Méndez on 11/11/17 17:44.
 * cruzmendezalan@gmail.com
 */

public class Promotor {
    public static String TAG = Promotor.class.getSimpleName();
    private String TelefonoParticular;
    private String HorarioSalida;
    private String HorarioEntrada;
    private String ApellidoPaterno;
    private String Nombres;
    private String CorreoElectronico;
    private String ApellidoMaterno;
    private String TelefonoCelular;

    public String getTelefonoParticular (){
        return TelefonoParticular;
    }

    public void setTelefonoParticular (String TelefonoParticular){
        this.TelefonoParticular = TelefonoParticular;
    }

    public String getHorarioSalida (){
        return HorarioSalida;
    }

    public void setHorarioSalida (String HorarioSalida){
        this.HorarioSalida = HorarioSalida;
    }

    public String getHorarioEntrada (){
        return HorarioEntrada;
    }

    public void setHorarioEntrada (String HorarioEntrada){
        this.HorarioEntrada = HorarioEntrada;
    }

    public String getApellidoPaterno (){
        return ApellidoPaterno;
    }

    public void setApellidoPaterno (String ApellidoPaterno){
        this.ApellidoPaterno = ApellidoPaterno;
    }

    public String getNombres (){
        return Nombres;
    }

    public void setNombres (String Nombres){
        this.Nombres = Nombres;
    }

    public String getCorreoElectronico (){
        return CorreoElectronico;
    }

    public void setCorreoElectronico (String CorreoElectronico){
        this.CorreoElectronico = CorreoElectronico;
    }

    public String getApellidoMaterno (){
        return ApellidoMaterno;
    }

    public void setApellidoMaterno (String ApellidoMaterno){
        this.ApellidoMaterno = ApellidoMaterno;
    }

    public String getTelefonoCelular (){
        return TelefonoCelular;
    }

    public void setTelefonoCelular (String TelefonoCelular){
        this.TelefonoCelular = TelefonoCelular;
    }

    @Override
    public String toString(){
        return TAG+" [TelefonoParticular = "+TelefonoParticular+", HorarioSalida = "+HorarioSalida+", HorarioEntrada = "+HorarioEntrada+", ApellidoPaterno = "+ApellidoPaterno+", Nombres = "+Nombres+", CorreoElectronico = "+CorreoElectronico+", ApellidoMaterno = "+ApellidoMaterno+", TelefonoCelular = "+TelefonoCelular+"]";
    }
}
