package com.model;

import utils.SimulationTime;

public class TblIncidentPK {
	private int time;
	private byte ciId;
	/**
	 * @return the time
	 */
	public int getTime() {
		return time;
	}
	
	public SimulationTime getSimulationTime(){
		return new SimulationTime(time);
	}
	/**
	 * @param time the time to set
	 */
	public void setTime(SimulationTime time) {
		this.time = time.getRunTime();
	}
	
	public void setTime(int time){
		this.time = time;
	}
	/**
	 * @return the ciId
	 */
	public byte getCiId() {
		return ciId;
	}
	/**
	 * @param ciId the ciId to set
	 */
	public void setCiId(byte ciId) {
		this.ciId = ciId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ciId;
		result = prime * result + time;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TblIncidentPK other = (TblIncidentPK) obj;
		if (ciId != other.ciId)
			return false;
		if (time != other.time)
			return false;
		return true;
	}
	
	
}
