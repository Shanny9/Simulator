package com.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the tblIncident database table.
 * 
 */
@Entity
@NamedQuery(name="TblIncident.findAll", query="SELECT t FROM TblIncident t")
public class TblIncident implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="incident_id")
	private byte incidentId;

	@Column(name="ci_id")
	private byte ciId;

	private int incidentTime;

	private byte isActive;

	//bi-directional many-to-one association to TblChange
	@OneToMany(mappedBy="tblIncident")
	private List<TblChange> tblChanges;

	//bi-directional many-to-one association to TblEvent
	@OneToMany(mappedBy="tblIncident")
	private List<TblEvent> tblEvents;

	//bi-directional many-to-one association to TblSolution
	@ManyToOne
	@JoinColumn(name="solution_id")
	private TblSolution tblSolution;

	public TblIncident() {
	}

	public byte getIncidentId() {
		return this.incidentId;
	}

	public void setIncidentId(byte incidentId) {
		this.incidentId = incidentId;
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

	public byte getIsActive() {
		return this.isActive;
	}

	public void setIsActive(byte isActive) {
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