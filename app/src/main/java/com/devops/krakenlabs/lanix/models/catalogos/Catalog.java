package com.devops.krakenlabs.lanix.models.catalogos;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Catalog{

	@SerializedName("CadenasComerciales")
	private List<CadenasComercialesItem> cadenasComerciales;

	@SerializedName("Productos")
	private List<ProductosItem> productos;

	@SerializedName("Version")
	private String version;

	@SerializedName("Modelos")
	private List<ModelosItem> modelos;

	@SerializedName("Error")
	private Error error;

	public void setCadenasComerciales(List<CadenasComercialesItem> cadenasComerciales){
		this.cadenasComerciales = cadenasComerciales;
	}

	public List<CadenasComercialesItem> getCadenasComerciales(){
		return cadenasComerciales;
	}

	public void setProductos(List<ProductosItem> productos){
		this.productos = productos;
	}

	public List<ProductosItem> getProductos(){
		return productos;
	}

	public void setVersion(String version){
		this.version = version;
	}

	public String getVersion(){
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
			"cadenasComerciales = '" + cadenasComerciales + '\'' + 
			",productos = '" + productos + '\'' + 
			",version = '" + version + '\'' + 
			",modelos = '" + modelos + '\'' + 
			",error = '" + error + '\'' + 
			"}";
		}
}