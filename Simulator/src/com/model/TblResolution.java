package com.model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Time;


/**
 * The persistent class for the tblResolution database table.
 * 
 */
@Entity
@NamedQuery(name="TblResolution.findAll", query="SELECT t FROM TblResolution t")
public class TblResolution implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TblResolutionPK id;

	private byte isPurchasedA;

	private byte isPurchasedB;

	private byte isResolvedA;

	private byte isResolvedB;

	private Time resolution_timeA;

	private Time resolution_timeB;

	//bi-directional many-to-one association to TblIncident
	@ManyToOne
	@JoinColumn(name="incident_ID")
	private TblIncident tblIncident;

	public TblResolution() {
	}

	public TblResolutionPK getId() {
		return this.id;
	}

	public void setId(TblResolutionPK id) {
		this.id = id;
	}

	public byte getIsPurchasedA() {
		return this.isPurchasedA;
	}

	public void setIsPurchasedA(byte isPurchasedA) {
		this.isPurchasedA = isPurchasedA;
	}

	public byte getIsPurchasedB() {
		return this.isPurchasedB;
	}

	public void setIsPurchasedB(byte isPurchasedB) {
		this.isPurchasedB = isPurchasedB;
	}

	public byte getIsResolvedA() {
		return this.isResolvedA;
	}

	public void setIsResolvedA(byte isResolvedA) {
		this.isResolvedA = isResolvedA;
	}

	public byte getIsResolvedB() {
		return this.isResolvedB;
	}

	public void setIsResolvedB(byte isResolvedB) {
		this.isResolvedB = isResolvedB;
	}

	public Time getResolution_timeA() {
		return this.resolution_timeA;
	}

	public void setResolution_timeA(Time resolution_timeA) {
		this.resolution_timeA = resolution_timeA;
	}

	public Time getResolution_timeB() {
		return this.resolution_timeB;
	}

	public void setResolution_timeB(Time resolution_timeB) {
		this.resolution_timeB = resolution_timeB;
	}

	public TblIncident getTblIncident() {
		return this.tblIncident;
	}

	public void setTblIncident(TblIncident tblIncident) {
		this.tblIncident = tblIncident;
	}

}