package com.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the tblService_Department database table.
 * 
 */
@Entity
@NamedQuery(name="TblService_Department.findAll", query="SELECT t FROM TblService_Department t")
public class TblService_Department implements Serializable {
	private static final long serialVersionUID = 1L;

	private byte isActive;

	//bi-directional many-to-one association to TblDepartment
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="department_name", referencedColumnName="department_name"),
		@JoinColumn(name="devision_name", referencedColumnName="devision_name")
		})
	private TblDepartment tblDepartment;

	//bi-directional many-to-one association to TblService
	@ManyToOne
	@JoinColumn(name="service_ID")
	private TblService tblService;

	public TblService_Department() {
	}

	public byte getIsActive() {
		return this.isActive;
	}

	public void setIsActive(byte isActive) {
		this.isActive = isActive;
	}

	public TblDepartment getTblDepartment() {
		return this.tblDepartment;
	}

	public void setTblDepartment(TblDepartment tblDepartment) {
		this.tblDepartment = tblDepartment;
	}

	public TblService getTblService() {
		return this.tblService;
	}

	public void setTblService(TblService tblService) {
		this.tblService = tblService;
	}

}