package com.devops.krakenlabs.lanix.models.session;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Alan Giovani Cruz Méndez on 11/11/17 17:49. cruzmendezalan@gmail.com
 */

public class Error {
  public static String TAG = Error.class.getSimpleName();
  private int No;

  @SerializedName("Descripcion")
  private String descripcion;

  public int getNo() {
    return No;
  }

  public void setNo(int No) {
    this.No = No;
  }

  @Override
  public String toString() {
    return "Error{" + "No=" + No + ", descripcion='" + descripcion + '\'' + '}';
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }
}
