package com.model;

import java.io.Serializable;

/**
 * The persistent class for the tblEvent database table.
 * 
 */

public class TblEvent implements Serializable {
	private static final long serialVersionUID = 1L;

	private int eventId;
	
	private byte incidentId;
	
	private byte serviceId;
	
	private byte session;
	
	private byte round;

	private boolean isActive;

	public TblEvent() {
	}

	
	
	
	public byte getSession() {
		return session;
	}




	public void setSession(byte session) {
		this.session = session;
	}




	public byte getRound() {
		return round;
	}




	public void setRound(byte round) {
		this.round = round;
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