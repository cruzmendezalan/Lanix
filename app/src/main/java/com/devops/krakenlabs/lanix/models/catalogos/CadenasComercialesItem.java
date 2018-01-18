package com.devops.krakenlabs.lanix.models.catalogos;

import com.google.gson.annotations.SerializedName;

public class CadenasComercialesItem{

	@SerializedName("CadenaComercial")
	private String cadenaComercial;

	@SerializedName("CadenaComercialId")
	private int cadenaComercialId;

	public void setCadenaComercial(String cadenaComercial){
		this.cadenaComercial = cadenaComercial;
	}

	public String getCadenaComercial(){
		return cadenaComercial;
	}

	public void setCadenaComercialId(int cadenaComercialId){
		this.cadenaComercialId = cadenaComercialId;
	}

	public int getCadenaComercialId(){
		return cadenaComercialId;
	}

	@Override
 	public String toString(){
		return 
			"CadenasComercialesItem{" + 
			"cadenaComercial = '" + cadenaComercial + '\'' + 
			",cadenaComercialId = '" + cadenaComercialId + '\'' + 
			"}";
		}
}