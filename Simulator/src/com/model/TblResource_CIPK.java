package com.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the tblResource_CI database table.
 * 
 */
@Embeddable
public class TblResource_CIPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(insertable=false, updatable=false)
	private String resourceName;

	@Column(name="CI_ID", insertable=false, updatable=false)
	private byte ciId;

	public TblResource_CIPK() {
	}
	public String getResourceName() {
		return this.resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public byte getCiId() {
		return this.ciId;
	}
	public void setCiId(byte ciId) {
		this.ciId = ciId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TblResource_CIPK)) {
			return false;
		}
		TblResource_CIPK castOther = (TblResource_CIPK)other;
		return 
			this.resourceName.equals(castOther.resourceName)
			&& (this.ciId == castOther.ciId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.resourceName.hashCode();
		hash = hash * prime + ((int) this.ciId);
		
		return hash;
	}
}