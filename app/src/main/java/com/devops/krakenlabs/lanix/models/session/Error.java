package com.devops.krakenlabs.lanix.models.session;

/**
 * Created by Alan Giovani Cruz Méndez on 11/11/17 17:49.
 * cruzmendezalan@gmail.com
 */

public class Error {
    public static String TAG = Error.class.getSimpleName();
    private String No;

    public String getNo () {
        return No;
    }

    public void setNo (String No) {
        this.No = No;
    }

    @Override
    public String toString() {
        return TAG+" [No = "+No+"]";
    }
}
