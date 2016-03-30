package com.model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Time;
import java.util.List;


/**
 * The persistent class for the tblChange database table.
 * 
 */
@Entity
@NamedQuery(name="TblChange.findAll", query="SELECT t FROM TblChange t")
public class TblChange implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private double change_ID;

	private Time avbTime;

	@Column(name="fixed_cost")
	private double fixedCost;

	private Time incidentFreq;

	private byte isActive;

	private byte isApplied;

	private Time rcmdTime;

	@Column(name="varient_income")
	private double varientIncome;

	//bi-directional many-to-one association to TblCI
	@ManyToOne
	@JoinColumn(name="CI_ID")
	private TblCI tblCi;

	//bi-directional many-to-many association to TblChange
	@ManyToMany
	@JoinTable(
		name="tblAffected_Change"
		, joinColumns={
			@JoinColumn(name="affecting")
			}
		, inverseJoinColumns={
			@JoinColumn(name="affected")
			}
		)
	private List<TblChange> tblChanges1;

	//bi-directional many-to-many association to TblChange
	@ManyToMany(mappedBy="tblChanges1")
	private List<TblChange> tblChanges2;

	//bi-directional many-to-one association to TblIncident
	@ManyToOne
	@JoinColumn(name="affectedIncident")
	private TblIncident tblIncident;

	//bi-directional many-to-one association to TblResource_Change
	@OneToMany(mappedBy="tblChange")
	private List<TblResource_Change> tblResourceChanges;

	public TblChange() {
	}

	public double getChange_ID() {
		return this.change_ID;
	}

	public void setChange_ID(double change_ID) {
		this.change_ID = change_ID;
	}

	public Time getAvbTime() {
		return this.avbTime;
	}

	public void setAvbTime(Time avbTime) {
		this.avbTime = avbTime;
	}

	public double getFixedCost() {
		return this.fixedCost;
	}

	public void setFixedCost(double fixedCost) {
		this.fixedCost = fixedCost;
	}

	public Time getIncidentFreq() {
		return this.incidentFreq;
	}

	public void setIncidentFreq(Time incidentFreq) {
		this.incidentFreq = incidentFreq;
	}

	public byte getIsActive() {
		return this.isActive;
	}

	public void setIsActive(byte isActive) {
		this.isActive = isActive;
	}

	public byte getIsApplied() {
		return this.isApplied;
	}

	public void setIsApplied(byte isApplied) {
		this.isApplied = isApplied;
	}

	public Time getRcmdTime() {
		return this.rcmdTime;
	}

	public void setRcmdTime(Time rcmdTime) {
		this.rcmdTime = rcmdTime;
	}

	public double getVarientIncome() {
		return this.varientIncome;
	}

	public void setVarientIncome(double varientIncome) {
		this.varientIncome = varientIncome;
	}

	public TblCI getTblCi() {
		return this.tblCi;
	}

	public void setTblCi(TblCI tblCi) {
		this.tblCi = tblCi;
	}

	public List<TblChange> getTblChanges1() {
		return this.tblChanges1;
	}

	public void setTblChanges1(List<TblChange> tblChanges1) {
		this.tblChanges1 = tblChanges1;
	}

	public List<TblChange> getTblChanges2() {
		return this.tblChanges2;
	}

	public void setTblChanges2(List<TblChange> tblChanges2) {
		this.tblChanges2 = tblChanges2;
	}

	public TblIncident getTblIncident() {
		return this.tblIncident;
	}

	public void setTblIncident(TblIncident tblIncident) {
		this.tblIncident = tblIncident;
	}

	public List<TblResource_Change> getTblResourceChanges() {
		return this.tblResourceChanges;
	}

	public void setTblResourceChanges(List<TblResource_Change> tblResourceChanges) {
		this.tblResourceChanges = tblResourceChanges;
	}

	public TblResource_Change addTblResourceChange(TblResource_Change tblResourceChange) {
		getTblResourceChanges().add(tblResourceChange);
		tblResourceChange.setTblChange(this);

		return tblResourceChange;
	}

	public TblResource_Change removeTblResourceChange(TblResource_Change tblResourceChange) {
		getTblResourceChanges().remove(tblResourceChange);
		tblResourceChange.setTblChange(null);

		return tblResourceChange;
	}

}