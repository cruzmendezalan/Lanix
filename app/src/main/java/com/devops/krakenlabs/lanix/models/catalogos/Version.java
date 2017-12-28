package com.devops.krakenlabs.lanix.models.catalogos;


import com.google.gson.annotations.SerializedName;


public class Version{

	@SerializedName("Modelo")
	private Modelo modelo;

	public void setModelo(Modelo modelo){
		this.modelo = modelo;
	}

	public Modelo getModelo(){
		return modelo;
	}

	@Override
 	public String toString(){
		return 
			"Version{" + 
			"modelo = '" + modelo + '\'' + 
			"}";
		}
}