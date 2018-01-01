package com.devops.krakenlabs.lanix.models.venta;

import com.google.gson.annotations.SerializedName;


public class Error{

	@SerializedName("No")
	private long no;

	@SerializedName("Descripcion")
	private String descripcion;

	public void setNo(long no){
		this.no = no;
	}

	public long getNo(){
		return no;
	}

	public void setDescripcion(String descripcion){
		this.descripcion = descripcion;
	}

	public String getDescripcion(){
		return descripcion;
	}

	@Override
 	public String toString(){
		return 
			"Error{" + 
			"no = '" + no + '\'' + 
			",descripcion = '" + descripcion + '\'' + 
			"}";
		}
}