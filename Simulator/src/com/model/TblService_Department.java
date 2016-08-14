package com.model;

import java.io.Serializable;



/**
 * The persistent class for the tblService_Department database table.
 * 
 */
public class TblService_Department implements Serializable {
	private static final long serialVersionUID = 1L;

	private TblService_DepartmentPK id;
	
	private byte service_ID;

	private String departmentName;
	
	private String divisionName;

	private boolean isActive;

	//bi-directional many-to-one association to TblDepartment
	private TblDepartment tblDepartment;

	//bi-directional many-to-one association to TblService
	private TblService tblService;

	public TblService_Department() {
	}

	public boolean getIsActive() {
		return this.isActive;
	}

	public void setIsActive(boolean isActive) {
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
	
//	public TblService_DepartmentPK getId() {
//		return id;
//	}
//
//	public void setId(TblService_DepartmentPK id) {
//		this.id = id;
//	}

	public byte getService_ID() {
		return service_ID;
	}

	public void setService_ID(byte service_ID) {
		this.service_ID = service_ID;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getDivisionName() {
		return divisionName;
	}

	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}

	

}