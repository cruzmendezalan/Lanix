package com.devops.krakenlabs.lanix.models.venta;

import com.google.gson.annotations.SerializedName;

public class ProductosItem{

	@SerializedName("NumeroAsignado")
	private String numeroAsignado;

	@SerializedName("ModeloId")
	private long modeloId;

	@SerializedName("ProductoId")
	private long productoId;

	@SerializedName("Modelo")
	private String modelo;

	@SerializedName("Color")
	private String color;

	@SerializedName("Iccid")
	private String iccid;

	@SerializedName("ProductoVentaId")
	private int productoVentaId;

	@SerializedName("Imei")
	private String imei;

    public ProductosItem(String numeroAsignado, long modeloId, long productoId, String modelo, String color, String iccid, int productoVentaId, String imei) {
        this.numeroAsignado = numeroAsignado;
        this.modeloId = modeloId;
        this.productoId = productoId;
        this.modelo = modelo;
        this.color = color;
        this.iccid = iccid;
        this.productoVentaId = productoVentaId;
        this.imei = imei;
    }

    public void setNumeroAsignado(String numeroAsignado){
		this.numeroAsignado = numeroAsignado;
	}

	public String getNumeroAsignado(){
		return numeroAsignado;
	}

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

	public void setIccid(String iccid){
		this.iccid = iccid;
	}

	public String getIccid(){
		return iccid;
	}

	public void setProductoVentaId(int productoVentaId){
		this.productoVentaId = productoVentaId;
	}

	public int getProductoVentaId(){
		return productoVentaId;
	}

	public void setImei(String imei){
		this.imei = imei;
	}

	public String getImei(){
		return imei;
	}

	@Override
 	public String toString(){
		return 
			"ProductosItem{" + 
			"numeroAsignado = '" + numeroAsignado + '\'' + 
			",modeloId = '" + modeloId + '\'' + 
			",productoId = '" + productoId + '\'' + 
			",modelo = '" + modelo + '\'' + 
			",color = '" + color + '\'' + 
			",iccid = '" + iccid + '\'' + 
			",productoVentaId = '" + productoVentaId + '\'' + 
			",imei = '" + imei + '\'' + 
			"}";
		}
}