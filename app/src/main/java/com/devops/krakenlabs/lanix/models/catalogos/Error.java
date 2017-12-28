package com.devops.krakenlabs.lanix.models.catalogos;

import com.google.gson.annotations.SerializedName;


public class Error{

	@SerializedName("No")
	private int no;

	public void setNo(int no){
		this.no = no;
	}

	public int getNo(){
		return no;
	}

	@Override
 	public String toString(){
		return 
			"Error{" + 
			"no = '" + no + '\'' + 
			"}";
		}
}