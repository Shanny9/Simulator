package com.model;

import java.io.Serializable;
import java.util.List;


/**
 * The persistent class for the tblSolution database table.
 * 
 */

public class TblSolution implements Serializable {
	private static final long serialVersionUID = 1L;

	private int solutionId;

	private int solutionMarom;

	private int solutionRakia;
	
	private boolean isActive;

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

//	private List<TblIncident> tblIncidents;

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

//	public List<TblIncident> getTblIncidents() {
//		return this.tblIncidents;
//	}
//
//	public void setTblIncidents(List<TblIncident> tblIncidents) {
//		this.tblIncidents = tblIncidents;
//	}
//
//	public TblIncident addTblIncident(TblIncident tblIncident) {
//		getTblIncidents().add(tblIncident);
//		tblIncident.setTblSolution(this);
//
//		return tblIncident;
//	}
//
//	public TblIncident removeTblIncident(TblIncident tblIncident) {
//		getTblIncidents().remove(tblIncident);
//		tblIncident.setTblSolution(null);
//
//		return tblIncident;
//	}

}