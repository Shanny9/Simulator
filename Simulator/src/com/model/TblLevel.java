package com.model;

import java.io.Serializable;

/**
 * The persistent class for the tblLevel database table.
 * 
 */

public class TblLevel implements Activable, Serializable {
	private static final long serialVersionUID = 1L;

	private String level;
	private boolean isActive;

	public TblLevel() {
	}

	public String getLevel() {
		return this.level;
	}

	public void setLevel(String level) {
		this.level = level;
	}
	
	public boolean isActive(){
		return this.isActive;
	}
	
	public void setActive(boolean isActive){
		this.isActive = isActive;
	}
}