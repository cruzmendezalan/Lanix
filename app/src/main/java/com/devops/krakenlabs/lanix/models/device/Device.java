package com.devops.krakenlabs.lanix.models.device;

import com.google.gson.annotations.SerializedName;

public class Device{
	public static final String TAG = Device.class.getSimpleName();
	@SerializedName("SesionValida")
	private boolean sesionValida;

	@SerializedName("RutaDeDescargaApp")
	private String rutaDeDescargaApp;

	@SerializedName("TokenDispositivo")
	private String tokenDispositivo;

	@SerializedName("VersionApp")
	private String versionApp;

	@SerializedName("Error")
	private Error error;

	@SerializedName("ActualizarCatalogos")
	private boolean actualizarCatalogos;

	public void setSesionValida(boolean sesionValida){
		this.sesionValida = sesionValida;
	}

	public boolean isSesionValida(){
		return sesionValida;
	}

	public void setRutaDeDescargaApp(String rutaDeDescargaApp){
		this.rutaDeDescargaApp = rutaDeDescargaApp;
	}

	public String getRutaDeDescargaApp(){
		return rutaDeDescargaApp;
	}

	public void setTokenDispositivo(String tokenDispositivo){
		this.tokenDispositivo = tokenDispositivo;
	}

	public String getTokenDispositivo(){
		return tokenDispositivo;
	}

	public void setVersionApp(String versionApp){
		this.versionApp = versionApp;
	}

	public String getVersionApp(){
		return versionApp;
	}

	public void setError(Error error){
		this.error = error;
	}

	public Error getError(){
		return error;
	}

	public void setActualizarCatalogos(boolean actualizarCatalogos){
		this.actualizarCatalogos = actualizarCatalogos;
	}

	public boolean isActualizarCatalogos(){
		return actualizarCatalogos;
	}

	@Override
 	public String toString(){
		return 
			"Device{" + 
			" \nsesionValida = '" + sesionValida + '\'' +
			",\nrutaDeDescargaApp = '" + rutaDeDescargaApp + '\'' +
			",\ntokenDispositivo = '" + tokenDispositivo + '\'' +
			",\nversionApp = '" + versionApp + '\'' +
			",\nerror = '" + error + '\'' +
			",\nactualizarCatalogos = '" + actualizarCatalogos + '\'' +
			"}";
		}
}