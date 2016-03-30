package com.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the tblResource_Change database table.
 * 
 */
@Embeddable
public class TblResource_ChangePK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(insertable=false, updatable=false)
	private String resourceName;

	@Column(insertable=false, updatable=false)
	private double changeID;

	public TblResource_ChangePK() {
	}
	public String getResourceName() {
		return this.resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public double getChangeID() {
		return this.changeID;
	}
	public void setChangeID(double changeID) {
		this.changeID = changeID;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TblResource_ChangePK)) {
			return false;
		}
		TblResource_ChangePK castOther = (TblResource_ChangePK)other;
		return 
			this.resourceName.equals(castOther.resourceName)
			&& (this.changeID == castOther.changeID);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.resourceName.hashCode();
		hash = hash * prime + ((int) (java.lang.Double.doubleToLongBits(this.changeID) ^ (java.lang.Double.doubleToLongBits(this.changeID) >>> 32)));
		
		return hash;
	}
}