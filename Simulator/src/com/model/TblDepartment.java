package com.model;

import java.io.Serializable;
import java.util.List;

/**
 * The persistent class for the tblDepartment database table.
 * 
 */

public class TblDepartment implements Activable, Serializable {
	private static final long serialVersionUID = 1L;


	private String divisionName;

	private String departmentName;
	
	private String shortName;

	private boolean isActive;

	private TblDivision tblDivision;

	private List<TblService_Department> tblServiceDepartments;

	public TblDepartment() {
	}


	public String getDivisionName() {
		return divisionName;

	}


	public void setDivisionName(String devisionName) {
		this.divisionName = devisionName;

	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	
	//
	// public TblDepartmentPK getId() {
	// return this.id;
	// }
	//
	// public void setId(TblDepartmentPK id) {
	// this.id = id;
	// }

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public boolean isActive() {
		return this.isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public TblDivision getTblDivision() {
		return this.tblDivision;
	}

	public void setTblDivision(TblDivision tblDivision) {
		this.tblDivision = tblDivision;
	}

	public List<TblService_Department> getTblServiceDepartments() {
		return this.tblServiceDepartments;
	}

	public void setTblServiceDepartments(
			List<TblService_Department> tblServiceDepartments) {
		this.tblServiceDepartments = tblServiceDepartments;
	}

	public TblService_Department addTblServiceDepartment(
			TblService_Department tblServiceDepartment) {
		getTblServiceDepartments().add(tblServiceDepartment);
		tblServiceDepartment.setTblDepartment(this);

		return tblServiceDepartment;
	}

	public TblService_Department removeTblServiceDepartment(
			TblService_Department tblServiceDepartment) {
		getTblServiceDepartments().remove(tblServiceDepartment);
		tblServiceDepartment.setTblDepartment(null);

		return tblServiceDepartment;
	}

}
