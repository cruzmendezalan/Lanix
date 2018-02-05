package com.devops.krakenlabs.lanix.models.historico;

import com.devops.krakenlabs.lanix.controllers.LanixRequest;

import org.json.JSONObject;

public class VentasHistoriaRequest implements LanixRequest {

	@Override
 	public String toString(){
		return 
			"VentasHistoriaRequest{" + 
			"}";
		}

	/**
	 * Este metodo es utilziado por el controlador de red para enviar el contenido de la clase
	 * serializado a la red
	 */
	@Override
	public JSONObject toJson() {
		return new JSONObject();
	}
}