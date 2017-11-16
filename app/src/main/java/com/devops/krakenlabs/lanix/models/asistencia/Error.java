package com.devops.krakenlabs.lanix.models.asistencia;

/**
 * Created by Alan Giovani Cruz Méndez on 16/11/17 00:04.
 * cruzmendezalan@gmail.com
 */

public class Error {
    public static String TAG = Error.class.getSimpleName();
    private int No;

    private String Descripcion;

    public int getNo ()
    {
        return No;
    }

    public void setNo (int No)
    {
        this.No = No;
    }

    public String getDescripcion ()
    {
        return Descripcion;
    }

    public void setDescripcion (String Descripcion)
    {
        this.Descripcion = Descripcion;
    }

    @Override
    public String toString()
    {
        return TAG+" [No = "+No+", Descripcion = "+Descripcion+"]";
    }
}
