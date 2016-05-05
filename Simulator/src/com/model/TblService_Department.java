package com.model;

import java.io.Serializable;



/**
 * The persistent class for the tblService_Department database table.
 * 
 */
public class TblService_Department implements Serializable {
	private static final long serialVersionUID = 1L;

	private byte isActive;

	//bi-directional many-to-one association to TblDepartment
	private TblDepartment tblDepartment;

	//bi-directional many-to-one association to TblService
	private TblService tblService;

	public TblService_Department() {
	}

	public byte getIsActive() {
		return this.isActive;
	}

	public void setIsActive(byte isActive) {
		this.isActive = isActive;
	}

	public TblDepartment getTblDepartment() {
		return this.tblDepartment;
	}

	public void setTblDepartment(TblDepartment tblDepartment) {
		this.tblDepartment = tblDepartment;
	}

	public TblService getTblService() {
		return this.tblService;
	}

	public void setTblService(TblService tblService) {
		this.tblService = tblService;
	}

}