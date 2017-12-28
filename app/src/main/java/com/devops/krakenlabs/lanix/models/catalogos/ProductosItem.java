package com.devops.krakenlabs.lanix.models.catalogos;


import com.google.gson.annotations.SerializedName;


public class ProductosItem{

	@SerializedName("ModeloId")
	private long modeloId;

	@SerializedName("ProductoId")
	private long productoId;

	@SerializedName("Modelo")
	private String modelo;

	@SerializedName("Color")
	private String color;

	public void setModeloId(long modeloId){
		this.modeloId = modeloId;
	}

	public long getModeloId(){
		return modeloId;
	}

	public void setProductoId(long productoId){
		this.productoId = productoId;
	}

	public long getProductoId(){
		return productoId;
	}

	public void setModelo(String modelo){
		this.modelo = modelo;
	}

	public String getModelo(){
		return modelo;
	}

	public void setColor(String color){
		this.color = color;
	}

	public String getColor(){
		return color;
	}

	@Override
 	public String toString(){
		return 
			"ProductosItem{" + 
			"modeloId = '" + modeloId + '\'' + 
			",productoId = '" + productoId + '\'' + 
			",modelo = '" + modelo + '\'' + 
			",color = '" + color + '\'' + 
			"}";
		}
}