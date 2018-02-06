package com.devops.krakenlabs.lanix.models.historico;


import com.google.gson.annotations.SerializedName;


public class VentasItem{

	@SerializedName("VentaId")
	private long ventaId;

	@SerializedName("ModeloId")
	private long modeloId;

	@SerializedName("ProductoId")
	private long productoId;

	@SerializedName("Producto")
	private String producto;


	@SerializedName("Modelo")
	private String modelo;

	@SerializedName("Color")
	private String color;

	@SerializedName("FechaVenta")
	private String fechaVenta;

	public void setVentaId(long ventaId){
		this.ventaId = ventaId;
	}

	public long getVentaId(){
		return ventaId;
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

	public void setFechaVenta(String fechaVenta){
		this.fechaVenta = fechaVenta;
	}

	public String getFechaVenta(){
		return fechaVenta;
	}

	public String getProducto() {
		return producto;
	}

	public void setProducto(String producto) {
		this.producto = producto;
	}

	@Override
 	public String toString(){
		return 
			"VentasItem{" + 
			"ventaId = '" + ventaId + '\'' + 
			",modeloId = '" + modeloId + '\'' + 
			",productoId = '" + productoId + '\'' + 
			",modelo = '" + modelo + '\'' + 
			",color = '" + color + '\'' + 
			",fechaVenta = '" + fechaVenta + '\'' + 
			"}";
		}
}