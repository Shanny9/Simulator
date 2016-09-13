package com.model;

import java.io.Serializable;

/**
 * The primary key class for the tblPriority database table.
 * 
 */

public class TblPriorityPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	private String urgency;

	private String impact;

	public TblPriorityPK() {
	}
	public String getUrgency() {
		return this.urgency;
	}
	public void setUrgency(String urgency) {
		this.urgency = urgency;
	}
	public String getImpact() {
		return this.impact;
	}
	public void setImpact(String impact) {
		this.impact = impact;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TblPriorityPK)) {
			return false;
		}
		TblPriorityPK castOther = (TblPriorityPK)other;
		return 
			this.urgency.equals(castOther.urgency)
			&& this.impact.equals(castOther.impact);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.urgency.hashCode();
		hash = hash * prime + this.impact.hashCode();
		
		return hash;
	}
}