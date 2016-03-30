package com.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the tblAffected_CI database table.
 * 
 */
@Entity
@NamedQuery(name="TblAffected_CI.findAll", query="SELECT t FROM TblAffected_CI t")
public class TblAffected_CI implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TblAffected_CIPK id;

	private byte isActive;

	//bi-directional many-to-one association to TblCI
	@ManyToOne
	@JoinColumn(name="service_ID")
	private TblCI tblCi;

	//bi-directional many-to-one association to TblIncident
	@ManyToOne
	@JoinColumn(name="incident_ID")
	private TblIncident tblIncident;

	public TblAffected_CI() {
	}

	public TblAffected_CIPK getId() {
		return this.id;
	}

	public void setId(TblAffected_CIPK id) {
		this.id = id;
	}

	public byte getIsActive() {
		return this.isActive;
	}

	public void setIsActive(byte isActive) {
		this.isActive = isActive;
	}

	public TblCI getTblCi() {
		return this.tblCi;
	}

	public void setTblCi(TblCI tblCi) {
		this.tblCi = tblCi;
	}

	public TblIncident getTblIncident() {
		return this.tblIncident;
	}

	public void setTblIncident(TblIncident tblIncident) {
		this.tblIncident = tblIncident;
	}

}