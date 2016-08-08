package com.model;

import java.io.Serializable;

/**
 * The primary key class for the tblService_Division database table.
 * 
 */

public class TblService_DepartmentPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	private byte service_ID;

	private String departmentName;
	
	private String divisionName;

	public String getDivisionName() {
		return divisionName;
	}
	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}
	public TblService_DepartmentPK() {
	}
	public byte getService_ID() {
		return this.service_ID;
	}
	public void setService_ID(byte service_ID) {
		this.service_ID = service_ID;
	}
	public String getDepartmentName() {
		return this.departmentName;
	}
	public void setDepartmentName(String divisionName) {
		this.departmentName = divisionName;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TblService_DepartmentPK)) {
			return false;
		}
		TblService_DepartmentPK castOther = (TblService_DepartmentPK)other;
		return 
			(this.service_ID == castOther.service_ID)
			&& this.departmentName.equals(castOther.departmentName);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) this.service_ID);
		hash = hash * prime + this.departmentName.hashCode();
		
		return hash;
	}
}