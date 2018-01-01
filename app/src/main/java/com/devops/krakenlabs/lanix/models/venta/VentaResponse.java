package com.devops.krakenlabs.lanix.models.venta;


import com.google.gson.annotations.SerializedName;

public class VentaResponse{

	@SerializedName("VentaId")
	private long ventaId;

	@SerializedName("Error")
	private Error error;

	@SerializedName("IdentificadorLocal")
	private String identificadorLocal;

	public void setVentaId(long ventaId){
		this.ventaId = ventaId;
	}

	public long getVentaId(){
		return ventaId;
	}

	public void setError(Error error){
		this.error = error;
	}

	public Error getError(){
		return error;
	}

	public void setIdentificadorLocal(String identificadorLocal){
		this.identificadorLocal = identificadorLocal;
	}

	public String getIdentificadorLocal(){
		return identificadorLocal;
	}

	@Override
 	public String toString(){
		return 
			"VentaResponse{" + 
			"ventaId = '" + ventaId + '\'' + 
			",error = '" + error + '\'' + 
			",identificadorLocal = '" + identificadorLocal + '\'' + 
			"}";
		}
}