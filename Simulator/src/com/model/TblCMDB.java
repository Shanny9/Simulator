package com.model;

import java.io.Serializable;

/**
 * The persistent class for the tblCMDB database table.
 * 
 */

public class TblCMDB implements Activable, Serializable {
	private static final long serialVersionUID = 1L;
	private byte ciId;
	private byte serviceId;
	private boolean isActive;

	public TblCMDB() {
	}

	public byte getCiId() {
		return ciId;
	}

	public void setCiId(byte ciId) {
		this.ciId = ciId;
	}

	public byte getServiceId() {
		return serviceId;
	}

	public void setServiceId(byte serviceId) {
		this.serviceId = serviceId;
	}

	public boolean isActive() {
		return this.isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

}