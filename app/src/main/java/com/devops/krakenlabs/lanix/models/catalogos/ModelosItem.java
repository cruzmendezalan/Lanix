package com.devops.krakenlabs.lanix.models.catalogos;


import com.google.gson.annotations.SerializedName;

public class ModelosItem{

	@SerializedName("ModeloId")
	private long modeloId;

	@SerializedName("Modelo")
	private String modelo;

	public void setModeloId(long modeloId){
		this.modeloId = modeloId;
	}

	public long getModeloId(){
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