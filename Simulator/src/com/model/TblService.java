package com.model;

import java.io.Serializable;
import java.util.List;


/**
 * The persistent class for the tblService database table.
 * 
 */

public class TblService implements Serializable {
	private static final long serialVersionUID = 1L;

	private byte serviceId;

	private double fixedCost;

	private double fixedIncome;

	private byte isActive;

	private byte isTechnical;

	private String serviceCode;

	private String serviceName;

	private String supplierLevel2;

	private String supplierLevel3;

	private String urgency;
	
	public String getUrgency() {
		return urgency;
	}

	public void setUrgency(String urgency) {
		this.urgency = urgency;
	}

	public String getImpact() {
		return impact;
	}

	public void setImpact(String impact) {
		this.impact = impact;
	}

	private String impact;
	
	//bi-directional many-to-one association to TblChange
	private List<TblChange> tblChanges;

	//bi-directional many-to-one association to TblEvent
	private List<TblEvent> tblEvents;

	//bi-directional many-to-many association to TblCI
	private List<TblCI> tblCis;

	//bi-directional many-to-one association to TblLevel
	private TblLevel tblLevel1;

	//bi-directional many-to-one association to TblLevel
	private TblLevel tblLevel2;

	//bi-directional many-to-one association to TblService_Department
	private List<TblService_Department> tblServiceDepartments;

	//bi-directional many-to-one association to TblService_Division
	private List<TblService_Division> tblServiceDivisions;

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

	public List<TblChange> getTblChanges() {
		return this.tblChanges;
	}

	public void setTblChanges(List<TblChange> tblChanges) {
		this.tblChanges = tblChanges;
	}

	public TblChange addTblChange(TblChange tblChange) {
		getTblChanges().add(tblChange);
		tblChange.setTblService(this);

		return tblChange;
	}

	public TblChange removeTblChange(TblChange tblChange) {
		getTblChanges().remove(tblChange);
		tblChange.setTblService(null);

		return tblChange;
	}

	public List<TblEvent> getTblEvents() {
		return this.tblEvents;
	}

	public void setTblEvents(List<TblEvent> tblEvents) {
		this.tblEvents = tblEvents;
	}

	public TblEvent addTblEvent(TblEvent tblEvent) {
		getTblEvents().add(tblEvent);
		tblEvent.setTblService(this);

		return tblEvent;
	}

	public TblEvent removeTblEvent(TblEvent tblEvent) {
		getTblEvents().remove(tblEvent);
		tblEvent.setTblService(null);

		return tblEvent;
	}

	public List<TblCI> getTblCis() {
		return this.tblCis;
	}

	public void setTblCis(List<TblCI> tblCis) {
		this.tblCis = tblCis;
	}

	public TblLevel getTblLevel1() {
		return this.tblLevel1;
	}

	public void setTblLevel1(TblLevel tblLevel1) {
		this.tblLevel1 = tblLevel1;
	}

	public TblLevel getTblLevel2() {
		return this.tblLevel2;
	}

	public void setTblLevel2(TblLevel tblLevel2) {
		this.tblLevel2 = tblLevel2;
	}

	public List<TblService_Department> getTblServiceDepartments() {
		return this.tblServiceDepartments;
	}

	public void setTblServiceDepartments(List<TblService_Department> tblServiceDepartments) {
		this.tblServiceDepartments = tblServiceDepartments;
	}

	public TblService_Department addTblServiceDepartment(TblService_Department tblServiceDepartment) {
		getTblServiceDepartments().add(tblServiceDepartment);
		tblServiceDepartment.setTblService(this);

		return tblServiceDepartment;
	}

	public TblService_Department removeTblServiceDepartment(TblService_Department tblServiceDepartment) {
		getTblServiceDepartments().remove(tblServiceDepartment);
		tblServiceDepartment.setTblService(null);

		return tblServiceDepartment;
	}

	public List<TblService_Division> getTblServiceDivisions() {
		return this.tblServiceDivisions;
	}

	public void setTblServiceDivisions(List<TblService_Division> tblServiceDivisions) {
		this.tblServiceDivisions = tblServiceDivisions;
	}

	public TblService_Division addTblServiceDivision(TblService_Division tblServiceDivision) {
		getTblServiceDivisions().add(tblServiceDivision);
		tblServiceDivision.setTblService(this);

		return tblServiceDivision;
	}

	public TblService_Division removeTblServiceDivision(TblService_Division tblServiceDivision) {
		getTblServiceDivisions().remove(tblServiceDivision);
		tblServiceDivision.setTblService(null);

		return tblServiceDivision;
	}

}