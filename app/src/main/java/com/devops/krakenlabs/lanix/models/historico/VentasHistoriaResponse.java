package com.devops.krakenlabs.lanix.models.historico;

import java.util.List;
import com.google.gson.annotations.SerializedName;


public class VentasHistoriaResponse{

	@SerializedName("Ventas")
	private List<VentasItem> ventas;

	@SerializedName("Error")
	private Error error;

	public void setVentas(List<VentasItem> ventas){
		this.ventas = ventas;
	}

	public List<VentasItem> getVentas(){
		return ventas;
	}

	public void setError(Error error){
		this.error = error;
	}

	public Error getError(){
		return error;
	}

	@Override
 	public String toString(){
		return 
			"VentasHistoriaResponse{" + 
			"ventas = '" + ventas + '\'' + 
			",error = '" + error + '\'' + 
			"}";
		}
}