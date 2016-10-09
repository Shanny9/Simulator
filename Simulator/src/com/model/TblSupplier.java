package com.model;

import java.io.Serializable;


/**
 * The persistent class for the tblSupplier database table.
 * 
 */

public class TblSupplier implements Activable, Serializable {
	private static final long serialVersionUID = 1L;

	private String supplierName;

	private String currency;

	private boolean isActive;

	private double solutionCost;

	public TblSupplier() {
	}

	public String getSupplierName() {
		return this.supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getCurrency() {
		return this.currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public boolean isActive() {
		return this.isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public double getSolutionCost() {
		return this.solutionCost;
	}

	public void setSolutionCost(double solutionCost) {
		this.solutionCost = solutionCost;
	}

//	public List<TblCI> getTblCis1() {
//		return this.tblCis1;
//	}
//
//	public void setTblCis1(List<TblCI> tblCis1) {
//		this.tblCis1 = tblCis1;
//	}
//
//	public TblCI addTblCis1(TblCI tblCis1) {
//		getTblCis1().add(tblCis1);
//		tblCis1.setTblSupplier1(this);
//
//		return tblCis1;
//	}
//
//	public TblCI removeTblCis1(TblCI tblCis1) {
//		getTblCis1().remove(tblCis1);
//		tblCis1.setTblSupplier1(null);
//
//		return tblCis1;
//	}
//
//	public List<TblCI> getTblCis2() {
//		return this.tblCis2;
//	}
//
//	public void setTblCis2(List<TblCI> tblCis2) {
//		this.tblCis2 = tblCis2;
//	}
//
//	public TblCI addTblCis2(TblCI tblCis2) {
//		getTblCis2().add(tblCis2);
//		tblCis2.setTblSupplier2(this);
//
//		return tblCis2;
//	}
//
//	public TblCI removeTblCis2(TblCI tblCis2) {
//		getTblCis2().remove(tblCis2);
//		tblCis2.setTblSupplier2(null);
//
//		return tblCis2;
//	}

}