package com.devops.krakenlabs.lanix.models.venta;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

public class VentaRequest{
    private static final String TAG = VentaRequest.class.getSimpleName();

	private String nombresCliente;


	private String telefonoParticular;


	private List<ProductosItem> productos;


	private String fecha;


	private String apellidoPaternoCliente;


	private String correoElectronico;


	private String imagenTicket;


	private String identificadorLocal;


	private String numeroTicket;


	private String apellidoMaternoCliente;


	private String identificadorSesion;


	private String telefonoCelular;


	public VentaRequest(String nombresCliente, String telefonoParticular, List<ProductosItem> productos,
						String fecha, String apellidoPaternoCliente, String correoElectronico,
						String imagenTicket, String identificadorLocal, String numeroTicket,
						String apellidoMaternoCliente, String identificadorSesion, String telefonoCelular) {
		this.nombresCliente = nombresCliente;
		this.telefonoParticular = telefonoParticular;
		this.productos = productos;

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        c.setTime(new Date(fecha));
		this.fecha = df.format(c.getTime());
//		this.fecha = this.fecha.replace("/", "\\/");
        Log.e(TAG, "VentaRequest: "+this.fecha );
        this.apellidoPaternoCliente = apellidoPaternoCliente;
		this.correoElectronico = correoElectronico;
		this.imagenTicket = imagenTicket;
		this.identificadorLocal = identificadorLocal;
		this.numeroTicket = numeroTicket;
		this.apellidoMaternoCliente = apellidoMaternoCliente;
		this.identificadorSesion = identificadorSesion;
		this.telefonoCelular = telefonoCelular;
	}

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

    public JSONObject toJson(){
        JSONObject jsonObject = null;
        String jsonString = "";
        try {
            Gson gson  = new Gson();
            jsonString = gson.toJson(this);
            jsonObject = new JSONObject(jsonString);
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.e(TAG, "toJson: "+jsonString );
        return jsonObject;
    }
}