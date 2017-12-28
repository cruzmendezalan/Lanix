package com.devops.krakenlabs.lanix.models.catalogos;

import com.google.gson.annotations.SerializedName;

public class ModelosItem{

	@SerializedName("ModeloId")
	private int modeloId;

	@SerializedName("Modelo")
	private String modelo;

	public void setModeloId(int modeloId){
		this.modeloId = modeloId;
	}

	public int getModeloId(){
		return modeloId;
	}

	public void setModelo(String modelo){
		this.modelo = modelo;
	}

	public String getModelo(){
		return modelo;
	}

	@Override
 	public String toString(){
		return 
			"ModelosItem{" + 
			"modeloId = '" + modeloId + '\'' + 
			",modelo = '" + modelo + '\'' + 
			"}";
		}
}