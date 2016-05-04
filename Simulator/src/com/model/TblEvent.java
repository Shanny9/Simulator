package com.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the tblEvent database table.
 * 
 */
@Entity
@NamedQuery(name="TblEvent.findAll", query="SELECT t FROM TblEvent t")
public class TblEvent implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="event_id")
	private int eventId;

	//bi-directional many-to-one association to TblIncident
	@ManyToOne
	@JoinColumn(name="incident_id")
	private TblIncident tblIncident;

	//bi-directional many-to-one association to TblService
	@ManyToOne
	@JoinColumn(name="service_id")
	private TblService tblService;

	public TblEvent() {
	}

	public int getEventId() {
		return this.eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public TblIncident getTblIncident() {
		return this.tblIncident;
	}

	public void setTblIncident(TblIncident tblIncident) {
		this.tblIncident = tblIncident;
	}

	public TblService getTblService() {
		return this.tblService;
	}

	public void setTblService(TblService tblService) {
		this.tblService = tblService;
	}

}