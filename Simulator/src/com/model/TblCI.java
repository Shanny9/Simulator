package com.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


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

	//bi-directional many-to-one association to TblSupplier
	@ManyToOne
	@JoinColumn(name="supplier_level2")
	private TblSupplier tblSupplier1;

	//bi-directional many-to-one association to TblSupplier
	@ManyToOne
	@JoinColumn(name="supplier_level3")
	private TblSupplier tblSupplier2;

	//bi-directional many-to-one association to TblResource_CI
	@OneToMany(mappedBy="tblCi")
	private List<TblResource_CI> tblResourceCis;

	//bi-directional many-to-many association to TblService
	@ManyToMany(mappedBy="tblCis")
	private List<TblService> tblServices;

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

	public TblSupplier getTblSupplier1() {
		return this.tblSupplier1;
	}

	public void setTblSupplier1(TblSupplier tblSupplier1) {
		this.tblSupplier1 = tblSupplier1;
	}

	public TblSupplier getTblSupplier2() {
		return this.tblSupplier2;
	}

	public void setTblSupplier2(TblSupplier tblSupplier2) {
		this.tblSupplier2 = tblSupplier2;
	}

	public List<TblResource_CI> getTblResourceCis() {
		return this.tblResourceCis;
	}

	public void setTblResourceCis(List<TblResource_CI> tblResourceCis) {
		this.tblResourceCis = tblResourceCis;
	}

	public TblResource_CI addTblResourceCi(TblResource_CI tblResourceCi) {
		getTblResourceCis().add(tblResourceCi);
		tblResourceCi.setTblCi(this);

		return tblResourceCi;
	}

	public TblResource_CI removeTblResourceCi(TblResource_CI tblResourceCi) {
		getTblResourceCis().remove(tblResourceCi);
		tblResourceCi.setTblCi(null);

		return tblResourceCi;
	}

	public List<TblService> getTblServices() {
		return this.tblServices;
	}

	public void setTblServices(List<TblService> tblServices) {
		this.tblServices = tblServices;
	}

}