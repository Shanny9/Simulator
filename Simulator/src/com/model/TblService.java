package com.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the tblService database table.
 * 
 */
@Entity
@NamedQuery(name="TblService.findAll", query="SELECT t FROM TblService t")
public class TblService implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="service_id")
	private byte serviceId;

	@Column(name="fixed_cost")
	private double fixedCost;

	@Column(name="fixed_income")
	private double fixedIncome;

	private String impact;

	private byte isActive;

	private byte isTechnical;

	@Column(name="service_code")
	private String serviceCode;

	@Column(name="service_name")
	private String serviceName;

	@Column(name="supplier_level2")
	private String supplierLevel2;

	@Column(name="supplier_level3")
	private String supplierLevel3;

	private String urgency;

	public TblService() {
	}

	public byte getServiceId() {
		return this.serviceId;
	}

	public void setServiceId(byte serviceId) {
		this.serviceId = serviceId;
	}

	public double getFixedCost() {
		return this.fixedCost;
	}

	public void setFixedCost(double fixedCost) {
		this.fixedCost = fixedCost;
	}

	public double getFixedIncome() {
		return this.fixedIncome;
	}

	public void setFixedIncome(double fixedIncome) {
		this.fixedIncome = fixedIncome;
	}

	public String getImpact() {
		return this.impact;
	}

	public void setImpact(String impact) {
		this.impact = impact;
	}

	public byte getIsActive() {
		return this.isActive;
	}

	public void setIsActive(byte isActive) {
		this.isActive = isActive;
	}

	public byte getIsTechnical() {
		return this.isTechnical;
	}

	public void setIsTechnical(byte isTechnical) {
		this.isTechnical = isTechnical;
	}

	public String getServiceCode() {
		return this.serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getServiceName() {
		return this.serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
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

	public String getUrgency() {
		return this.urgency;
	}

	public void setUrgency(String urgency) {
		this.urgency = urgency;
	}

}