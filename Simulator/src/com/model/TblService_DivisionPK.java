package com.model;

import java.io.Serializable;

/**
 * The primary key class for the tblService_Division database table.
 * 
 */

public class TblService_DivisionPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	private byte service_ID;

	private String divisionName;

	public TblService_DivisionPK() {
	}
	public byte getService_ID() {
		return this.service_ID;
	}
	public void setService_ID(byte service_ID) {
		this.service_ID = service_ID;
	}
	public String getDivisionName() {
		return this.divisionName;
	}
	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TblService_DivisionPK)) {
			return false;
		}
		TblService_DivisionPK castOther = (TblService_DivisionPK)other;
		return 
			(this.service_ID == castOther.service_ID)
			&& this.divisionName.equals(castOther.divisionName);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) this.service_ID);
		hash = hash * prime + this.divisionName.hashCode();
		
		return hash;
	}
}