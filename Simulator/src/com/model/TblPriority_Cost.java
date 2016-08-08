package com.model;

import java.io.Serializable;

/**
 * The persistent class for the tblPriority_Cost database table.
 * 
 */

public class TblPriority_Cost implements Serializable {
	private static final long serialVersionUID = 1L;

	private String pName;

	private double pCost;
	
	private boolean isActive;

	public boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	public TblPriority_Cost() {
	}

	public String getPName() {
		return this.pName;
	}

	public void setPName(String pName) {
		this.pName = pName;
	}

	public double getPCost() {
		return this.pCost;
	}

	public void setPCost(double pCost) {
		this.pCost = pCost;
	}

}