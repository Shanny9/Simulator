package com.model;

import java.io.Serializable;

/**
 * The primary key class for the tblCMDB database table.
 * 
 */

public class TblCMDBPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;


	private byte ciId;


	private byte serviceId;

	public TblCMDBPK() {
	}
	public byte getCiId() {
		return this.ciId;
	}
	public void setCiId(byte ciId) {
		this.ciId = ciId;
	}
	public byte getServiceId() {
		return this.serviceId;
	}
	public void setServiceId(byte serviceId) {
		this.serviceId = serviceId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TblCMDBPK)) {
			return false;
		}
		TblCMDBPK castOther = (TblCMDBPK)other;
		return 
			(this.ciId == castOther.ciId)
			&& (this.serviceId == castOther.serviceId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) this.ciId);
		hash = hash * prime + ((int) this.serviceId);
		
		return hash;
	}
}