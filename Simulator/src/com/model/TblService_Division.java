package com.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the tblService_Division database table.
 * 
 */

public class TblService_Division implements Serializable {
	private static final long serialVersionUID = 1L;

	private TblService_DivisionPK id;

	private byte isActive;

	//bi-directional many-to-one association to TblDivision
	private TblDivision tblDivision;

	//bi-directional many-to-one association to TblService
	private TblService tblService;

	public TblService_Division() {
	}

	public TblService_DivisionPK getId() {
		return this.id;
	}

	public void setId(TblService_DivisionPK id) {
		this.id = id;
	}

	public byte getIsActive() {
		return this.isActive;
	}

	public void setIsActive(byte isActive) {
		this.isActive = isActive;
	}

	public TblDivision getTblDivision() {
		return this.tblDivision;
	}

	public void setTblDivision(TblDivision tblDivision) {
		this.tblDivision = tblDivision;
	}

	public TblService getTblService() {
		return this.tblService;
	}

	public void setTblService(TblService tblService) {
		this.tblService = tblService;
	}

}