package com.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the tblSupplier database table.
 * 
 */
@Entity
@NamedQuery(name="TblSupplier.findAll", query="SELECT t FROM TblSupplier t")
public class TblSupplier implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="supplier_name")
	private String supplierName;

	private byte isActive;

	@Column(name="solution_cost")
	private double solutionCost;

	public TblSupplier() {
	}

	public String getSupplierName() {
		return this.supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public byte getIsActive() {
		return this.isActive;
	}

	public void setIsActive(byte isActive) {
		this.isActive = isActive;
	}

	public double getSolutionCost() {
		return this.solutionCost;
	}

	public void setSolutionCost(double solutionCost) {
		this.solutionCost = solutionCost;
	}

}