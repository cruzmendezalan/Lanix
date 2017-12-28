package com.devops.krakenlabs.lanix.models.catalogos;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Catalog{

	@SerializedName("Productos")
	private List<ProductosItem> productos;

	@SerializedName("Version")
	private Version version;

	@SerializedName("Modelos")
	private List<ModelosItem> modelos;

	@SerializedName("Error")
	private Error error;

	public void setProductos(List<ProductosItem> productos){
		this.productos = productos;
	}

	public List<ProductosItem> getProductos(){
		return productos;
	}

	public void setVersion(Version version){
		this.version = version;
	}

	public Version getVersion(){
		return version;
	}

	public void setModelos(List<ModelosItem> modelos){
		this.modelos = modelos;
	}

	public List<ModelosItem> getModelos(){
		return modelos;
	}

	public void setError(Error error){
		this.error = error;
	}

	public Error getError(){
		return error;
	}

	@Override
 	public String toString(){
		return 
			"Catalog{" + 
			"productos = '" + productos + '\'' + 
			",version = '" + version + '\'' + 
			",modelos = '" + modelos + '\'' + 
			",error = '" + error + '\'' + 
			"}";
		}
}