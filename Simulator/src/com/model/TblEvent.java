package com.model;

import java.io.Serializable;

/**
 * The persistent class for the tblEvent database table.
 * 
 */

public class TblEvent implements Serializable {
	private static final long serialVersionUID = 1L;

	private int eventId;

	private TblIncident tblIncident;

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