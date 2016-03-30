package com.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the tblCI_Division database table.
 * 
 */
@Entity
@NamedQuery(name="TblCI_Division.findAll", query="SELECT t FROM TblCI_Division t")
public class TblCI_Division implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TblCI_DivisionPK id;

	private byte isActive;

	//bi-directional many-to-one association to TblCI
	@ManyToOne
	@JoinColumn(name="service_ID")
	private TblCI tblCi;

	//bi-directional many-to-one association to TblDivision
	@ManyToOne
	@JoinColumn(name="division_name")
	private TblDivision tblDivision;

	public TblCI_Division() {
	}

	public TblCI_DivisionPK getId() {
		return this.id;
	}

	public void setId(TblCI_DivisionPK id) {
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

	public TblDivision getTblDivision() {
		return this.tblDivision;
	}

	public void setTblDivision(TblDivision tblDivision) {
		this.tblDivision = tblDivision;
	}

}