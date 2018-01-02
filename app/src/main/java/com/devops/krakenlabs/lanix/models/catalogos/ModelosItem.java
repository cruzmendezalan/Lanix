package com.devops.krakenlabs.lanix.models.catalogos;

import com.google.gson.annotations.SerializedName;

public class ModelosItem{
	private Boolean isSelected = false;//siempre deseleccionado

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

	public Boolean getSelected() {
		return isSelected;
	}

	public void setSelected(Boolean selected) {
		isSelected = selected;
	}
}