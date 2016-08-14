package com.model;

import java.io.Serializable;
import java.util.List;


/**
 * The persistent class for the tblCI database table.
 * 
 */

public class TblCI implements Serializable {
	private static final long serialVersionUID = 1L;

	private byte ciId;

	private String CI_name;

	private boolean isActive;


	private TblSupplier tblSupplier1;

	private String supplierName1;
	
	private TblSupplier tblSupplier2;

	private String supplierName2;
	
	private List<TblResource_CI> tblResourceCis;


	private List<TblService> tblServices;

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

	public boolean getIsActive() {
		return this.isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

//	public TblSupplier getTblSupplier1() {
//		return this.tblSupplier1;
//	}

//	public void setTblSupplier1(TblSupplier tblSupplier1) {
//		this.tblSupplier1 = tblSupplier1;
//	}

//	public TblSupplier getTblSupplier2() {
//		return this.tblSupplier2;
//	}

//	public void setTblSupplier2(TblSupplier tblSupplier2) {
//		this.tblSupplier2 = tblSupplier2;
//	}

	public List<TblResource_CI> getTblResourceCis() {
		return this.tblResourceCis;
	}

	public void setTblResourceCis(List<TblResource_CI> tblResourceCis) {
		this.tblResourceCis = tblResourceCis;
	}

	public TblResource_CI addTblResourceCi(TblResource_CI tblResourceCi) {
		getTblResourceCis().add(tblResourceCi);
		tblResourceCi.setTblCi(this);

		return tblResourceCi;
	}

	public TblResource_CI removeTblResourceCi(TblResource_CI tblResourceCi) {
		getTblResourceCis().remove(tblResourceCi);
		tblResourceCi.setTblCi(null);

		return tblResourceCi;
	}

	public List<TblService> getTblServices() {
		return this.tblServices;
	}

	public void setTblServices(List<TblService> tblServices) {
		this.tblServices = tblServices;
	}

}