package com.devops.krakenlabs.lanix.models.session;

/**
 * Created by Alan Giovani Cruz Méndez on 11/11/17 12:22.
 * cruzmendezalan@gmail.com
 */

public class User {
    public static String TAG = User.class.getSimpleName();
    private Promotor Promotor;
    private Error Error;
    private Sesion Sesion;

    public Promotor getPromotor ()
    {
        return Promotor;
    }

    public void setPromotor (Promotor Promotor)
    {
        this.Promotor = Promotor;
    }

    public Error getError ()
    {
        return Error;
    }

    public void setError (Error Error)
    {
        this.Error = Error;
    }

    public Sesion getSesion ()
    {
        return Sesion;
    }

    public void setSesion (Sesion Sesion)
    {
        this.Sesion = Sesion;
    }

    @Override
    public String toString()
    {
        return TAG+" [Promotor = "+Promotor+", Error = "+Error+", Sesion = "+Sesion+"]";
    }
}
