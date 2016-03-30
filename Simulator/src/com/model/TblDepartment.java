package com.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the tblDepartment database table.
 * 
 */
@Entity
@NamedQuery(name="TblDepartment.findAll", query="SELECT t FROM TblDepartment t")
public class TblDepartment implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TblDepartmentPK id;

	private byte isActive;

	//bi-directional many-to-one association to TblCI_Department
	@OneToMany(mappedBy="tblDepartment")
	private List<TblCI_Department> tblCiDepartments;

	//bi-directional many-to-one association to TblDivision
	@ManyToOne
	@JoinColumn(name="devision_name")
	private TblDivision tblDivision;

	public TblDepartment() {
	}

	public TblDepartmentPK getId() {
		return this.id;
	}

	public void setId(TblDepartmentPK id) {
		this.id = id;
	}

	public byte getIsActive() {
		return this.isActive;
	}

	public void setIsActive(byte isActive) {
		this.isActive = isActive;
	}

	public List<TblCI_Department> getTblCiDepartments() {
		return this.tblCiDepartments;
	}

	public void setTblCiDepartments(List<TblCI_Department> tblCiDepartments) {
		this.tblCiDepartments = tblCiDepartments;
	}

	public TblCI_Department addTblCiDepartment(TblCI_Department tblCiDepartment) {
		getTblCiDepartments().add(tblCiDepartment);
		tblCiDepartment.setTblDepartment(this);

		return tblCiDepartment;
	}

	public TblCI_Department removeTblCiDepartment(TblCI_Department tblCiDepartment) {
		getTblCiDepartments().remove(tblCiDepartment);
		tblCiDepartment.setTblDepartment(null);

		return tblCiDepartment;
	}

	public TblDivision getTblDivision() {
		return this.tblDivision;
	}

	public void setTblDivision(TblDivision tblDivision) {
		this.tblDivision = tblDivision;
	}

}