package com.model;

import java.io.Serializable;
import java.util.List;


/**
 * The persistent class for the tblResource database table.
 * 
 */
public class TblResource implements Serializable {
	private static final long serialVersionUID = 1L;

	private String resourceName;

	private double pricePerUnit;

	//bi-directional many-to-one association to TblResource_CI
	private List<TblResource_CI> tblResourceCis;

	//bi-directional many-to-one association to TblResource_Change
	private List<TblResource_Change> tblResourceChanges;

	public TblResource() {
	}

	public String getResourceName() {
		return this.resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public double getPricePerUnit() {
		return this.pricePerUnit;
	}

	public void setPricePerUnit(double pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}

	public List<TblResource_CI> getTblResourceCis() {
		return this.tblResourceCis;
	}

	public void setTblResourceCis(List<TblResource_CI> tblResourceCis) {
		this.tblResourceCis = tblResourceCis;
	}

	public TblResource_CI addTblResourceCi(TblResource_CI tblResourceCi) {
		getTblResourceCis().add(tblResourceCi);
		tblResourceCi.setTblResource(this);

		return tblResourceCi;
	}

	public TblResource_CI removeTblResourceCi(TblResource_CI tblResourceCi) {
		getTblResourceCis().remove(tblResourceCi);
		tblResourceCi.setTblResource(null);

		return tblResourceCi;
	}

	public List<TblResource_Change> getTblResourceChanges() {
		return this.tblResourceChanges;
	}

	public void setTblResourceChanges(List<TblResource_Change> tblResourceChanges) {
		this.tblResourceChanges = tblResourceChanges;
	}

	public TblResource_Change addTblResourceChange(TblResource_Change tblResourceChange) {
		getTblResourceChanges().add(tblResourceChange);
		tblResourceChange.setTblResource(this);

		return tblResourceChange;
	}

	public TblResource_Change removeTblResourceChange(TblResource_Change tblResourceChange) {
		getTblResourceChanges().remove(tblResourceChange);
		tblResourceChange.setTblResource(null);

		return tblResourceChange;
	}

}