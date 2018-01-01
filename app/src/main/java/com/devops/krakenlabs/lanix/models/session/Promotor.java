package com.devops.krakenlabs.lanix.models.session;

import com.google.gson.annotations.SerializedName;


public class Promotor{

	@SerializedName("ApellidoMaterno")
	private String apellidoMaterno;

	@SerializedName("HorarioEntrada")
	private String horarioEntrada;

	@SerializedName("Nombres")
	private String nombres;

	@SerializedName("ApellidoPaterno")
	private String apellidoPaterno;

	@SerializedName("Modulo")
	private String modulo;

	@SerializedName("CorreoElectronico")
	private String correoElectronico;

	@SerializedName("Region")
	private String region;

	@SerializedName("HorarioSalida")
	private String horarioSalida;

	@SerializedName("Carrier")
	private String carrier;

	@SerializedName("Ciudad")
	private String ciudad;

	@SerializedName("TelefonoCelular")
	private String telefonoCelular;

	public void setApellidoMaterno(String apellidoMaterno){
		this.apellidoMaterno = apellidoMaterno;
	}

	public String getApellidoMaterno(){
		return apellidoMaterno;
	}

	public void setHorarioEntrada(String horarioEntrada){
		this.horarioEntrada = horarioEntrada;
	}

	public String getHorarioEntrada(){
		return horarioEntrada;
	}

	public void setNombres(String nombres){
		this.nombres = nombres;
	}

	public String getNombres(){
		return nombres;
	}

	public void setApellidoPaterno(String apellidoPaterno){
		this.apellidoPaterno = apellidoPaterno;
	}

	public String getApellidoPaterno(){
		return apellidoPaterno;
	}

	public void setModulo(String modulo){
		this.modulo = modulo;
	}

	public String getModulo(){
		return modulo;
	}

	public void setCorreoElectronico(String correoElectronico){
		this.correoElectronico = correoElectronico;
	}

	public String getCorreoElectronico(){
		return correoElectronico;
	}

	public void setRegion(String region){
		this.region = region;
	}

	public String getRegion(){
		return region;
	}

	public void setHorarioSalida(String horarioSalida){
		this.horarioSalida = horarioSalida;
	}

	public String getHorarioSalida(){
		return horarioSalida;
	}

	public void setCarrier(String carrier){
		this.carrier = carrier;
	}

	public String getCarrier(){
		return carrier;
	}

	public void setCiudad(String ciudad){
		this.ciudad = ciudad;
	}

	public String getCiudad(){
		return ciudad;
	}

	public void setTelefonoCelular(String telefonoCelular){
		this.telefonoCelular = telefonoCelular;
	}

	public String getTelefonoCelular(){
		return telefonoCelular;
	}

	@Override
 	public String toString(){
		return 
			"Promotor{" + 
			"apellidoMaterno = '" + apellidoMaterno + '\'' + 
			",horarioEntrada = '" + horarioEntrada + '\'' + 
			",nombres = '" + nombres + '\'' + 
			",apellidoPaterno = '" + apellidoPaterno + '\'' + 
			",modulo = '" + modulo + '\'' + 
			",correoElectronico = '" + correoElectronico + '\'' + 
			",region = '" + region + '\'' + 
			",horarioSalida = '" + horarioSalida + '\'' + 
			",carrier = '" + carrier + '\'' + 
			",ciudad = '" + ciudad + '\'' + 
			",telefonoCelular = '" + telefonoCelular + '\'' + 
			"}";
		}
}