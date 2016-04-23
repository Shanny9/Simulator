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

}