package com.model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Time;
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
	private byte incident_ID;

	private byte isActive;

	@Column(name="priority_")
	private byte priority;

	@Column(name="time_")
	private Time time;

	//bi-directional many-to-one association to TblChange
	@OneToMany(mappedBy="tblIncident")
	private List<TblChange> tblChanges;

	//bi-directional many-to-one association to TblCI
	@ManyToOne
	@JoinColumn(name="root_CI_ID")
	private TblCI tblCi;

	//bi-directional many-to-one association to TblResolution
	@OneToMany(mappedBy="tblIncident")
	private List<TblResolution> tblResolutions;

	public TblIncident() {
	}

	public byte getIncident_ID() {
		return this.incident_ID;
	}

	public void setIncident_ID(byte incident_ID) {
		this.incident_ID = incident_ID;
	}

	public byte getIsActive() {
		return this.isActive;
	}

	public void setIsActive(byte isActive) {
		this.isActive = isActive;
	}

	public byte getPriority() {
		return this.priority;
	}

	public void setPriority(byte priority) {
		this.priority = priority;
	}

	public Time getTime() {
		return this.time;
	}

	public void setTime(Time time) {
		this.time = time;
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

	public TblCI getTblCi() {
		return this.tblCi;
	}

	public void setTblCi(TblCI tblCi) {
		this.tblCi = tblCi;
	}

	public List<TblResolution> getTblResolutions() {
		return this.tblResolutions;
	}

	public void setTblResolutions(List<TblResolution> tblResolutions) {
		this.tblResolutions = tblResolutions;
	}

	public TblResolution addTblResolution(TblResolution tblResolution) {
		getTblResolutions().add(tblResolution);
		tblResolution.setTblIncident(this);

		return tblResolution;
	}

	public TblResolution removeTblResolution(TblResolution tblResolution) {
		getTblResolutions().remove(tblResolution);
		tblResolution.setTblIncident(null);

		return tblResolution;
	}

}