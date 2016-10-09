package com.model;

import java.io.Serializable;

/**
 * The persistent class for the tblPriority database table.
 * 
 */

public class TblPriority implements Activable, Serializable {
	private static final long serialVersionUID = 1L;

	private TblPriorityPK id;
	private String priorityName;
	private String urgency;
	private String impact;
	private boolean isActive;
	
	public String getPriorityName() {
		return priorityName;
	}

	public void setPriorityName(String priorityName) {
		this.priorityName = priorityName;
	}
	
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

	@Override
	public boolean isActive() {
		return this.isActive;
	}

	@Override
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	private TblLevel tblLevel1;

	private TblLevel tblLevel2;

	private TblPriority_Cost tblPriorityCost;

	public TblPriority() {
	}

	public TblPriorityPK getId() {
		return this.id;
	}

	public void setId(TblPriorityPK id) {
		this.id = id;
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

	public TblPriority_Cost getTblPriorityCost() {
		return this.tblPriorityCost;
	}

	public void setTblPriorityCost(TblPriority_Cost tblPriorityCost) {
		this.tblPriorityCost = tblPriorityCost;
	}
}