package com.model;

import java.io.Serializable;
import java.util.List;


/**
 * The persistent class for the tblDivision database table.
 * 
 */

public class TblDivision implements Activable, Serializable {
	private static final long serialVersionUID = 1L;


	private String divisionName;

	private String shortName;
	
	private boolean isActive;

	private List<TblDepartment> tblDepartments;

//	private List<TblService_Division> tblServiceDivisions;

	public TblDivision() {
	}

	public String getDivisionName() {
		return this.divisionName;
	}

	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}

	
	
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

	public List<TblDepartment> getTblDepartments() {
		return this.tblDepartments;
	}

	public void setTblDepartments(List<TblDepartment> tblDepartments) {
		this.tblDepartments = tblDepartments;
	}

	public TblDepartment addTblDepartment(TblDepartment tblDepartment) {
		getTblDepartments().add(tblDepartment);
		tblDepartment.setTblDivision(this);

		return tblDepartment;
	}

	public TblDepartment removeTblDepartment(TblDepartment tblDepartment) {
		getTblDepartments().remove(tblDepartment);
		tblDepartment.setTblDivision(null);

		return tblDepartment;
	}

//	public List<TblService_Division> getTblServiceDivisions() {
//		return this.tblServiceDivisions;
//	}
//
//	public void setTblServiceDivisions(List<TblService_Division> tblServiceDivisions) {
//		this.tblServiceDivisions = tblServiceDivisions;
//	}
//
//	public TblService_Division addTblServiceDivision(TblService_Division tblServiceDivision) {
//		getTblServiceDivisions().add(tblServiceDivision);
//		tblServiceDivision.setTblDivision(this);
//
//		return tblServiceDivision;
//	}
//
//	public TblService_Division removeTblServiceDivision(TblService_Division tblServiceDivision) {
//		getTblServiceDivisions().remove(tblServiceDivision);
//		tblServiceDivision.setTblDivision(null);
//
//		return tblServiceDivision;
//	}

}