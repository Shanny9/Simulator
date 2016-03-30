package com.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the tblCI_Department database table.
 * 
 */
@Embeddable
public class TblCI_DepartmentPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(insertable=false, updatable=false)
	private byte service_ID;

	@Column(name="devision_name", insertable=false, updatable=false)
	private String devisionName;

	@Column(name="department_name", insertable=false, updatable=false)
	private String departmentName;

	public TblCI_DepartmentPK() {
	}
	public byte getService_ID() {
		return this.service_ID;
	}
	public void setService_ID(byte service_ID) {
		this.service_ID = service_ID;
	}
	public String getDevisionName() {
		return this.devisionName;
	}
	public void setDevisionName(String devisionName) {
		this.devisionName = devisionName;
	}
	public String getDepartmentName() {
		return this.departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TblCI_DepartmentPK)) {
			return false;
		}
		TblCI_DepartmentPK castOther = (TblCI_DepartmentPK)other;
		return 
			(this.service_ID == castOther.service_ID)
			&& this.devisionName.equals(castOther.devisionName)
			&& this.departmentName.equals(castOther.departmentName);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) this.service_ID);
		hash = hash * prime + this.devisionName.hashCode();
		hash = hash * prime + this.departmentName.hashCode();
		
		return hash;
	}
}