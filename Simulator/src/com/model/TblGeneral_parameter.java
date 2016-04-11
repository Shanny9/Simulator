package com.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the tblGeneral_parameters database table.
 * 
 */
@Entity
@Table(name="tblGeneral_parameters")
@NamedQuery(name="TblGeneral_parameter.findAll", query="SELECT t FROM TblGeneral_parameter t")
public class TblGeneral_parameter implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int pk;

	@Column(name="initial_capital")
	private double initialCapital;

	@Column(name="num_of_rounds")
	private byte numOfRounds;

	@Column(name="pause_time")
	private int pauseTime;

	@Column(name="run_time")
	private int runTime;

	@Column(name="sessions_per_round")
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

	public int getTotalTime() {
		// TODO Auto-generated method stub
		return (runTime+pauseTime)*sessionsPerRound*numOfRounds;
	}

}