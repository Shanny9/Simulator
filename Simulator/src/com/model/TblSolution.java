package com.model;

import java.io.Serializable;


/**
 * The persistent class for the tblSolution database table.
 * 
 */

public class TblSolution implements Activable, Serializable {
	private static final long serialVersionUID = 1L;

	private int solutionId;
	private int solutionMarom;
	private int solutionRakia;
	private boolean isActive;

	public TblSolution() {
	}

	public int getSolutionId() {
		return this.solutionId;
	}

	public void setSolutionId(int solutionId) {
		this.solutionId = solutionId;
	}

	public int getSolutionMarom() {
		return this.solutionMarom;
	}

	public void setSolutionMarom(int solutionMarom) {
		this.solutionMarom = solutionMarom;
	}

	public int getSolutionRakia() {
		return this.solutionRakia;
	}

	public void setSolutionRakia(int solutionRakia) {
		this.solutionRakia = solutionRakia;
	}

	@Override
	public boolean isActive() {
		return this.isActive;
	}

	@Override
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
}