package com.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the tblAffected_CI database table.
 * 
 */
@Embeddable
public class TblAffected_CIPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(insertable=false, updatable=false)
	private byte incident_ID;

	@Column(insertable=false, updatable=false)
	private byte service_ID;

	public TblAffected_CIPK() {
	}
	public byte getIncident_ID() {
		return this.incident_ID;
	}
	public void setIncident_ID(byte incident_ID) {
		this.incident_ID = incident_ID;
	}
	public byte getService_ID() {
		return this.service_ID;
	}
	public void setService_ID(byte service_ID) {
		this.service_ID = service_ID;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TblAffected_CIPK)) {
			return false;
		}
		TblAffected_CIPK castOther = (TblAffected_CIPK)other;
		return 
			(this.incident_ID == castOther.incident_ID)
			&& (this.service_ID == castOther.service_ID);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) this.incident_ID);
		hash = hash * prime + ((int) this.service_ID);
		
		return hash;
	}
}