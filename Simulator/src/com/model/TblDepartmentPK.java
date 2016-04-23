package com.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the tblDepartment database table.
 * 
 */
@Embeddable
public class TblDepartmentPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="devision_name", insertable=false, updatable=false)
	private String devisionName;

	@Column(name="department_name")
	private String departmentName;

	public TblDepartmentPK() {
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
		if (!(other instanceof TblDepartmentPK)) {
			return false;
		}
		TblDepartmentPK castOther = (TblDepartmentPK)other;
		return 
			this.devisionName.equals(castOther.devisionName)
			&& this.departmentName.equals(castOther.departmentName);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.devisionName.hashCode();
		hash = hash * prime + this.departmentName.hashCode();
		
		return hash;
	}
}