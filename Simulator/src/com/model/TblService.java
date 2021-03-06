package com.model;

import java.io.Serializable;


/**
 * The persistent class for the tblService database table.
 * 
 */

public class TblService implements Activable, Serializable {
	private static final long serialVersionUID = 1L;

	private byte serviceId;

	private double fixedCost;

	private double fixedIncome;

	private boolean isActive;

	private boolean isTechnical;

	private String serviceCode;

	private String serviceName;

	private String urgency;
	
	private int event_id;
	
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

	public boolean isActive() {
		return this.isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean getIsTechnical() {
		return this.isTechnical;
	}

	public void setIsTechnical(boolean isTechnical) {
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
	
	public int getEventId() {
		return this.event_id;
	}

	public void setEventId(int event_id) {
		this.event_id = event_id;
	}
}