package com.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the tblCI_Division database table.
 * 
 */
@Embeddable
public class TblCI_DivisionPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(insertable=false, updatable=false)
	private byte service_ID;

	@Column(name="division_name", insertable=false, updatable=false)
	private String divisionName;

	public TblCI_DivisionPK() {
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
		if (!(other instanceof TblCI_DivisionPK)) {
			return false;
		}
		TblCI_DivisionPK castOther = (TblCI_DivisionPK)other;
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