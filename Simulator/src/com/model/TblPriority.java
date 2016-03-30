package com.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the tblPriority database table.
 * 
 */
@Entity
@NamedQuery(name="TblPriority.findAll", query="SELECT t FROM TblPriority t")
public class TblPriority implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="priority_number")
	private byte priorityNumber;

	private double cost;

	private byte isActive;

	@Column(name="priority_name")
	private String priorityName;

	//bi-directional many-to-one association to TblCI
	@OneToMany(mappedBy="tblPriority")
	private List<TblCI> tblCis;

	public TblPriority() {
	}

	public byte getPriorityNumber() {
		return this.priorityNumber;
	}

	public void setPriorityNumber(byte priorityNumber) {
		this.priorityNumber = priorityNumber;
	}

	public double getCost() {
		return this.cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public byte getIsActive() {
		return this.isActive;
	}

	public void setIsActive(byte isActive) {
		this.isActive = isActive;
	}

	public String getPriorityName() {
		return this.priorityName;
	}

	public void setPriorityName(String priorityName) {
		this.priorityName = priorityName;
	}

	public List<TblCI> getTblCis() {
		return this.tblCis;
	}

	public void setTblCis(List<TblCI> tblCis) {
		this.tblCis = tblCis;
	}

	public TblCI addTblCi(TblCI tblCi) {
		getTblCis().add(tblCi);
		tblCi.setTblPriority(this);

		return tblCi;
	}

	public TblCI removeTblCi(TblCI tblCi) {
		getTblCis().remove(tblCi);
		tblCi.setTblPriority(null);

		return tblCi;
	}

}