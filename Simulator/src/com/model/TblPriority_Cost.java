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