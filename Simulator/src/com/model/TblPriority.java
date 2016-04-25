package com.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the tblPriority database table.
 * 
 */
@Entity
@NamedQuery(name="TblPriority.findAll", query="SELECT t FROM TblPriority t")
public class TblPriority implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TblPriorityPK id;

	private String priorityName;

	//bi-directional many-to-one association to TblLevel
	@ManyToOne
	@JoinColumn(name="urgency")
	private TblLevel tblLevel1;

	//bi-directional many-to-one association to TblLevel
	@ManyToOne
	@JoinColumn(name="impact")
	private TblLevel tblLevel2;

	public TblPriority() {
	}

	public TblPriorityPK getId() {
		return this.id;
	}

	public void setId(TblPriorityPK id) {
		this.id = id;
	}

	public String getPriorityName() {
		return this.priorityName;
	}

	public void setPriorityName(String priorityName) {
		this.priorityName = priorityName;
	}

	public TblLevel getTblLevel1() {
		return this.tblLevel1;
	}

	public void setTblLevel1(TblLevel tblLevel1) {
		this.tblLevel1 = tblLevel1;
	}

	public TblLevel getTblLevel2() {
		return this.tblLevel2;
	}

	public void setTblLevel2(TblLevel tblLevel2) {
		this.tblLevel2 = tblLevel2;
	}

}