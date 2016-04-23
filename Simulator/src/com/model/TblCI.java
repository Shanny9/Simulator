package com.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the tblCI database table.
 * 
 */
@Entity
@NamedQuery(name="TblCI.findAll", query="SELECT t FROM TblCI t")
public class TblCI implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="CI_ID")
	private byte ciId;

	private String CI_name;

	private byte isActive;

	@Column(name="supplier_level2")
	private String supplierLevel2;

	@Column(name="supplier_level3")
	private String supplierLevel3;

	public TblCI() {
	}

	public byte getCiId() {
		return this.ciId;
	}

	public void setCiId(byte ciId) {
		this.ciId = ciId;
	}

	public String getCI_name() {
		return this.CI_name;
	}

	public void setCI_name(String CI_name) {
		this.CI_name = CI_name;
	}

	public byte getIsActive() {
		return this.isActive;
	}

	public void setIsActive(byte isActive) {
		this.isActive = isActive;
	}

	public String getSupplierLevel2() {
		return this.supplierLevel2;
	}

	public void setSupplierLevel2(String supplierLevel2) {
		this.supplierLevel2 = supplierLevel2;
	}

	public String getSupplierLevel3() {
		return this.supplierLevel3;
	}

	public void setSupplierLevel3(String supplierLevel3) {
		this.supplierLevel3 = supplierLevel3;
	}

}