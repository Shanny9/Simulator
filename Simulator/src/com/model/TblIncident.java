package com.model;

import java.io.Serializable;
import java.util.List;

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

	private List<TblChange> tblChanges;

	private List<TblEvent> tblEvents;

	private TblSolution tblSolution;
	
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

	public void setIncidentTime(int incidentTime) {
		this.incidentTime = incidentTime;
	}

	public boolean getIsActive() {
		return this.isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	public List<TblChange> getTblChanges() {
		return this.tblChanges;
	}

	public void setTblChanges(List<TblChange> tblChanges) {
		this.tblChanges = tblChanges;
	}

	public TblChange addTblChange(TblChange tblChange) {
		getTblChanges().add(tblChange);
		tblChange.setTblIncident(this);

		return tblChange;
	}

	public TblChange removeTblChange(TblChange tblChange) {
		getTblChanges().remove(tblChange);
		tblChange.setTblIncident(null);

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
		tblEvent.setTblIncident(this);

		return tblEvent;
	}

	public TblEvent removeTblEvent(TblEvent tblEvent) {
		getTblEvents().remove(tblEvent);
		tblEvent.setTblIncident(null);

		return tblEvent;
	}

	public TblSolution getTblSolution() {
		return this.tblSolution;
	}

	public void setTblSolution(TblSolution tblSolution) {
		this.tblSolution = tblSolution;
	}

}
