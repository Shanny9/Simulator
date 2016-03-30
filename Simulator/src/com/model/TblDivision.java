package com.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the tblDivision database table.
 * 
 */
@Entity
@NamedQuery(name="TblDivision.findAll", query="SELECT t FROM TblDivision t")
public class TblDivision implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="division_name")
	private String divisionName;

	private byte isActive;

	//bi-directional many-to-one association to TblCI_Division
	@OneToMany(mappedBy="tblDivision")
	private List<TblCI_Division> tblCiDivisions;

	//bi-directional many-to-one association to TblDepartment
	@OneToMany(mappedBy="tblDivision")
	private List<TblDepartment> tblDepartments;

	public TblDivision() {
	}

	public String getDivisionName() {
		return this.divisionName;
	}

	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}

	public byte getIsActive() {
		return this.isActive;
	}

	public void setIsActive(byte isActive) {
		this.isActive = isActive;
	}

	public List<TblCI_Division> getTblCiDivisions() {
		return this.tblCiDivisions;
	}

	public void setTblCiDivisions(List<TblCI_Division> tblCiDivisions) {
		this.tblCiDivisions = tblCiDivisions;
	}

	public TblCI_Division addTblCiDivision(TblCI_Division tblCiDivision) {
		getTblCiDivisions().add(tblCiDivision);
		tblCiDivision.setTblDivision(this);

		return tblCiDivision;
	}

	public TblCI_Division removeTblCiDivision(TblCI_Division tblCiDivision) {
		getTblCiDivisions().remove(tblCiDivision);
		tblCiDivision.setTblDivision(null);

		return tblCiDivision;
	}

	public List<TblDepartment> getTblDepartments() {
		return this.tblDepartments;
	}

	public void setTblDepartments(List<TblDepartment> tblDepartments) {
		this.tblDepartments = tblDepartments;
	}

	public TblDepartment addTblDepartment(TblDepartment tblDepartment) {
		getTblDepartments().add(tblDepartment);
		tblDepartment.setTblDivision(this);

		return tblDepartment;
	}

	public TblDepartment removeTblDepartment(TblDepartment tblDepartment) {
		getTblDepartments().remove(tblDepartment);
		tblDepartment.setTblDivision(null);

		return tblDepartment;
	}

}