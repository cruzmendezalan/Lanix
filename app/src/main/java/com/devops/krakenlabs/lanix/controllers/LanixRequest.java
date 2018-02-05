package com.devops.krakenlabs.lanix.controllers;

import org.json.JSONObject;

/**
 * Created by Alan Giovani Cruz Méndez on 05/02/18 12:21.
 * cruzmendezalan@gmail.com
 */

public interface LanixRequest {
    /**
     * Este metodo es utilziado por el controlador de red para enviar el contenido de la clase
     * serializado a la red
     */
    public JSONObject toJson();//metodo necesario
}
