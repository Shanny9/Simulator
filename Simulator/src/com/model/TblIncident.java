package com.model;

import java.io.Serializable;

import utils.SimulationTime;

/**
 * The persistent class for the tblIncident database table.
 * 
 */

public class TblIncident implements Serializable {
	private static final long serialVersionUID = 1L;

	private TblIncidentPK pk = new TblIncidentPK();
	private byte ci_id;
	private int time;
	private boolean isActive;
	
	public TblIncident() {
	}
	
	public int getIncidentTime() {
		return this.pk.getTime();
	}
	
	public SimulationTime getSimulationTime() {
		return this.pk.getSimulationTime();
	}

	public void setIncidentTime(SimulationTime time) {
		this.pk.setTime(time);
		this.time = time.getRunTime();
	}
	
	public void setIncidentTime (int time) {
		this.pk.setTime(time);
		this.time = time;
	}

	public byte getCiId() {
		return pk.getCiId();
	}

	public void setCiId(byte ciId) {
		this.pk.setCiId(ciId);
		this.ci_id = ciId;
	}

	public boolean getIsActive() {
		return this.isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}
}
