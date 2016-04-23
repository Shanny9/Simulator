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

	//bi-directional many-to-one association to TblDepartment
	@OneToMany(mappedBy="tblDivision")
	private List<TblDepartment> tblDepartments;

	//bi-directional many-to-one association to TblService_Division
	@OneToMany(mappedBy="tblDivision")
	private List<TblService_Division> tblServiceDivisions;

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

	public List<TblService_Division> getTblServiceDivisions() {
		return this.tblServiceDivisions;
	}

	public void setTblServiceDivisions(List<TblService_Division> tblServiceDivisions) {
		this.tblServiceDivisions = tblServiceDivisions;
	}

	public TblService_Division addTblServiceDivision(TblService_Division tblServiceDivision) {
		getTblServiceDivisions().add(tblServiceDivision);
		tblServiceDivision.setTblDivision(this);

		return tblServiceDivision;
	}

	public TblService_Division removeTblServiceDivision(TblService_Division tblServiceDivision) {
		getTblServiceDivisions().remove(tblServiceDivision);
		tblServiceDivision.setTblDivision(null);

		return tblServiceDivision;
	}

}