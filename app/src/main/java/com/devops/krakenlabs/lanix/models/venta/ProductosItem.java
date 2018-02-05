package com.devops.krakenlabs.lanix.models.venta;


import com.google.gson.annotations.SerializedName;

public class ProductosItem{

	@SerializedName("NumeroAsignado")
	private String numeroAsignado;

	@SerializedName("ProductoId")
	private int productoId;

	@SerializedName("Iccid")
	private String iccid;

	@SerializedName("Imei")
	private String imei;

	private String modeloName;
	public ProductosItem(int productoId, String imei, String modeloName) {
		this.numeroAsignado = "";
		this.productoId 	= productoId;
//		this.iccid = iccid;
		this.imei = imei;
		this.modeloName = modeloName;
	}

	public void setNumeroAsignado(String numeroAsignado){
		this.numeroAsignado = numeroAsignado;
	}

	public String getNumeroAsignado(){
		return numeroAsignado;
	}

	public void setProductoId(int productoId){
		this.productoId = productoId;
	}

	public int getProductoId(){
		return productoId;
	}

	public void setIccid(String iccid){
		this.iccid = iccid;
	}

	public String getIccid(){
		return iccid;
	}

	public void setImei(String imei){
		this.imei = imei;
	}

	public String getImei(){
		return imei;
	}

	public String getModeloName() {
		return modeloName;
	}

	public void setModeloName(String modeloName) {
		this.modeloName = modeloName;
	}

	@Override
	public String toString() {
		return "ProductosItem{" +
				" \n numeroAsignado='" + numeroAsignado + '\'' +
				",\n productoId=" + productoId +
				",\n iccid='" + iccid + '\'' +
				",\n imei='" + imei + '\'' +
				",\n modeloName='" + modeloName + '\'' +
				'}';
	}
}