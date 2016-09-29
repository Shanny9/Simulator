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
}
