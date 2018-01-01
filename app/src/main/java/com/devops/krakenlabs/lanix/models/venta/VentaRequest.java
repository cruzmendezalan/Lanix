package com.devops.krakenlabs.lanix.models.venta;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class VentaRequest{

	@SerializedName("NombresCliente")
	private String nombresCliente;

	@SerializedName("TelefonoParticular")
	private String telefonoParticular;

	@SerializedName("Productos")
	private List<ProductosItem> productos;

	@SerializedName("Fecha")
	private String fecha;

	@SerializedName("ApellidoPaternoCliente")
	private String apellidoPaternoCliente;

	@SerializedName("CorreoElectronico")
	private String correoElectronico;

	@SerializedName("ImagenTicket")
	private String imagenTicket;

	@SerializedName("IdentificadorLocal")
	private String identificadorLocal;

	@SerializedName("NumeroTicket")
	private String numeroTicket;

	@SerializedName("ApellidoMaternoCliente")
	private String apellidoMaternoCliente;

	@SerializedName("IdentificadorSesion")
	private String identificadorSesion;

	@SerializedName("TelefonoCelular")
	private String telefonoCelular;

	public void setNombresCliente(String nombresCliente){
		this.nombresCliente = nombresCliente;
	}

	public String getNombresCliente(){
		return nombresCliente;
	}

	public void setTelefonoParticular(String telefonoParticular){
		this.telefonoParticular = telefonoParticular;
	}

	public String getTelefonoParticular(){
		return telefonoParticular;
	}

	public void setProductos(List<ProductosItem> productos){
		this.productos = productos;
	}

	public List<ProductosItem> getProductos(){
		return productos;
	}

	public void setFecha(String fecha){
		this.fecha = fecha;
	}

	public String getFecha(){
		return fecha;
	}

	public void setApellidoPaternoCliente(String apellidoPaternoCliente){
		this.apellidoPaternoCliente = apellidoPaternoCliente;
	}

	public String getApellidoPaternoCliente(){
		return apellidoPaternoCliente;
	}

	public void setCorreoElectronico(String correoElectronico){
		this.correoElectronico = correoElectronico;
	}

	public String getCorreoElectronico(){
		return correoElectronico;
	}

	public void setImagenTicket(String imagenTicket){
		this.imagenTicket = imagenTicket;
	}

	public String getImagenTicket(){
		return imagenTicket;
	}

	public void setIdentificadorLocal(String identificadorLocal){
		this.identificadorLocal = identificadorLocal;
	}

	public String getIdentificadorLocal(){
		return identificadorLocal;
	}

	public void setNumeroTicket(String numeroTicket){
		this.numeroTicket = numeroTicket;
	}

	public String getNumeroTicket(){
		return numeroTicket;
	}

	public void setApellidoMaternoCliente(String apellidoMaternoCliente){
		this.apellidoMaternoCliente = apellidoMaternoCliente;
	}

	public String getApellidoMaternoCliente(){
		return apellidoMaternoCliente;
	}

	public void setIdentificadorSesion(String identificadorSesion){
		this.identificadorSesion = identificadorSesion;
	}

	public String getIdentificadorSesion(){
		return identificadorSesion;
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
			"VentaRequest{" + 
			"nombresCliente = '" + nombresCliente + '\'' + 
			",telefonoParticular = '" + telefonoParticular + '\'' + 
			",productos = '" + productos + '\'' + 
			",fecha = '" + fecha + '\'' + 
			",apellidoPaternoCliente = '" + apellidoPaternoCliente + '\'' + 
			",correoElectronico = '" + correoElectronico + '\'' + 
			",imagenTicket = '" + imagenTicket + '\'' + 
			",identificadorLocal = '" + identificadorLocal + '\'' + 
			",numeroTicket = '" + numeroTicket + '\'' + 
			",apellidoMaternoCliente = '" + apellidoMaternoCliente + '\'' + 
			",identificadorSesion = '" + identificadorSesion + '\'' + 
			",telefonoCelular = '" + telefonoCelular + '\'' + 
			"}";
		}
}