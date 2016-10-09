package com.model;

import java.io.Serializable;


/**
 * The persistent class for the tblCI database table.
 * 
 */

public class TblCI implements Activable, Serializable {
	private static final long serialVersionUID = 1L;

	private byte ciId;

	private String CI_name;

	private boolean isActive;

	private String supplierName1;
	
	private String supplierName2;
	
	private int solutionId;

	public TblCI() {
	}
	
	public String getSupplierName1() {
		return supplierName1;
	}

	public void setSupplierName1(String supplierName1) {
		this.supplierName1 = supplierName1;
	}

	public String getSupplierName2() {
		return supplierName2;
	}

	public void setSupplierName2(String supplierName2) {
		this.supplierName2 = supplierName2;
	}

	public byte getCiId() {
		return this.ciId;
	}

	public void setCiId(byte ciId) {
		this.ciId = ciId;
	}

	public String getCI_name() {
		return this.CI_name;
	}

	public void setCI_name(String CI_name) {
		this.CI_name = CI_name;
	}

	public boolean isActive() {
		return this.isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	public int getSolutionId() {
		return solutionId;
	}

	public void setSolutionId(int solutionId) {
		this.solutionId = solutionId;
	}
}