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
	
	private byte incidentId;

	private TblService tblService;
	
	private byte serviceId;

	private boolean isActive;

	public TblEvent() {
	}

	
	public byte getIncidentId() {
		return incidentId;
	}


	public void setIncidentId(byte incidentId) {
		this.incidentId = incidentId;
	}


	public byte getServiceId() {
		return serviceId;
	}


	public void setServiceId(byte serviceId) {
		this.serviceId = serviceId;
	}


	public int getEventId() {
		return this.eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

//	public TblIncident getTblIncident() {
//		return this.tblIncident;
//	}
//
//	public void setTblIncident(TblIncident tblIncident) {
//		this.tblIncident = tblIncident;
//	}
//
//	public TblService getTblService() {
//		return this.tblService;
//	}
//
//	public void setTblService(TblService tblService) {
//		this.tblService = tblService;
//	}

	public boolean getIsActive() {
		return this.isActive;
	}
	
	public void setIsActive(boolean isActive){
		this.isActive = isActive;
	}

}