package com.devops.krakenlabs.lanix.controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

/**
 * Created by Alan Giovani Cruz Méndez on 05/02/18 15:53.
 * cruzmendezalan@gmail.com
 */

public class PersistenceController {
    public static final String TAG = PersistenceController.class.getSimpleName();
    private static PersistenceController persistenceController;
    private String vCatalog;
    private String cSession;
    private Context context;
    private SharedPreferences sharedPref;

    public static PersistenceController getInstance() {
        if (persistenceController == null){
            persistenceController = new PersistenceController();
        }
        return persistenceController;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getvCatalog() {
        return vCatalog;
    }

    public void setvCatalog(String vCatalog) {
        this.vCatalog = vCatalog;
    }

    public String getcSession() {
        return cSession;
    }

    public void setcSession(String cSession) {
        this.cSession = cSession;
    }

    private final String SESSION   = "SESSION"  ;
    private final String CATALOGOS = "CATALOGOS";
    private final String CATALOG_V = "CATALOG_V";
    private final String LANIX_KEY = "LANIX_KEY";
}
