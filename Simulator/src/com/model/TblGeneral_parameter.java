package com.model;

import java.io.Serializable;

/**
 * The persistent class for the tblGeneral_parameters database table.
 * 
 */

public class TblGeneral_parameter implements Serializable {
	private static final long serialVersionUID = 1L;

	private int pk;

	private double initialCapital;

	private byte numOfRounds;

	private int pauseTime;

	private int runTime;

	private byte sessionsPerRound;

	public TblGeneral_parameter() {
	}

	public int getPk() {
		return this.pk;
	}

	public void setPk(int pk) {
		this.pk = pk;
	}

	public double getInitialCapital() {
		return this.initialCapital;
	}

	public void setInitialCapital(double initialCapital) {
		this.initialCapital = initialCapital;
	}

	public byte getNumOfRounds() {
		return this.numOfRounds;
	}

	public void setNumOfRounds(byte numOfRounds) {
		this.numOfRounds = numOfRounds;
	}

	public int getPauseTime() {
		return this.pauseTime;
	}

	public void setPauseTime(int pauseTime) {
		this.pauseTime = pauseTime;
	}

	public int getRunTime() {
		return this.runTime;
	}

	public void setRunTime(int runTime) {
		this.runTime = runTime;
	}

	public byte getSessionsPerRound() {
		return this.sessionsPerRound;
	}

	public void setSessionsPerRound(byte sessionsPerRound) {
		this.sessionsPerRound = sessionsPerRound;
	}

}