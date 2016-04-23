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

	//bi-directional many-to-one association to TblDivision
	@ManyToOne
	@JoinColumn(name="devision_name")
	private TblDivision tblDivision;

	//bi-directional many-to-one association to TblService_Department
	@OneToMany(mappedBy="tblDepartment")
	private List<TblService_Department> tblServiceDepartments;

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

	public TblDivision getTblDivision() {
		return this.tblDivision;
	}

	public void setTblDivision(TblDivision tblDivision) {
		this.tblDivision = tblDivision;
	}

	public List<TblService_Department> getTblServiceDepartments() {
		return this.tblServiceDepartments;
	}

	public void setTblServiceDepartments(List<TblService_Department> tblServiceDepartments) {
		this.tblServiceDepartments = tblServiceDepartments;
	}

	public TblService_Department addTblServiceDepartment(TblService_Department tblServiceDepartment) {
		getTblServiceDepartments().add(tblServiceDepartment);
		tblServiceDepartment.setTblDepartment(this);

		return tblServiceDepartment;
	}

	public TblService_Department removeTblServiceDepartment(TblService_Department tblServiceDepartment) {
		getTblServiceDepartments().remove(tblServiceDepartment);
		tblServiceDepartment.setTblDepartment(null);

		return tblServiceDepartment;
	}

}