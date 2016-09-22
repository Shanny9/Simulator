package com.model;

import java.io.Serializable;

import utils.SimulationTime;

/**
 * The persistent class for the tblIncident database table.
 * 
 */

public class TblIncident implements Serializable {
	private static final long serialVersionUID = 1L;

	private byte incidentId;

	private byte ciId;

	private int incidentTime;

	private boolean isActive;

//	private List<TblChange> tblChanges;
//
//	private List<TblEvent> tblEvents;
//
//	private TblSolution tblSolution;
	
	private int solutionId;

	public TblIncident() {
	}

	
	
	public int getSolutionId() {
		return solutionId;
	}



	public void setSolutionId(int solutionId) {
		this.solutionId = solutionId;
	}



	public byte getIncidentId() {
		return this.incidentId;
	}

	public void setIncidentId(byte i) {
		this.incidentId = i;
	}

	public byte getCiId() {
		return this.ciId;
	}

	public void setCiId(byte ciId) {
		this.ciId = ciId;
	}

	public int getIncidentTime() {
		return this.incidentTime;
	}
	
	public SimulationTime getSimulationTime() {
		return new SimulationTime(incidentTime);
	}

	public void setIncidentTime(int time) {
		this.incidentTime = time;
	}

	public boolean getIsActive() {
		return this.isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}
}
