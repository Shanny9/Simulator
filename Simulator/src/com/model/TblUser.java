package com.model;

import java.io.Serializable;

/**
 * The persistent class for the tblGeneral_parameters database table.
 * 
 */

public class TblUser implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String password;
	
	private String username;
	
	private String type;


	public TblUser() {
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}

	

}