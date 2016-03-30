package com.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the tblResolution database table.
 * 
 */
@Embeddable
public class TblResolutionPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(insertable=false, updatable=false)
	private byte incident_ID;

	private String course;

	public TblResolutionPK() {
	}
	public byte getIncident_ID() {
		return this.incident_ID;
	}
	public void setIncident_ID(byte incident_ID) {
		this.incident_ID = incident_ID;
	}
	public String getCourse() {
		return this.course;
	}
	public void setCourse(String course) {
		this.course = course;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TblResolutionPK)) {
			return false;
		}
		TblResolutionPK castOther = (TblResolutionPK)other;
		return 
			(this.incident_ID == castOther.incident_ID)
			&& this.course.equals(castOther.course);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) this.incident_ID);
		hash = hash * prime + this.course.hashCode();
		
		return hash;
	}
}