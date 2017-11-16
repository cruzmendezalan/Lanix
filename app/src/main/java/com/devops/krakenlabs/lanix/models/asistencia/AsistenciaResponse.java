package com.devops.krakenlabs.lanix.models.asistencia;

/**
 * Created by Alan Giovani Cruz Méndez on 16/11/17 00:03.
 * cruzmendezalan@gmail.com
 */

public class AsistenciaResponse{
    public static String TAG = AsistenciaResponse.class.getSimpleName();
    private Error Error;

    public Error getError ()
    {
        return Error;
    }

    public void setError (Error Error)
    {
        this.Error = Error;
    }

    @Override
    public String toString()
    {
        return TAG+" [Error = "+Error+"]";
    }
}
