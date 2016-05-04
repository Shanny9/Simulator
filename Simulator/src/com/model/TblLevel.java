package com.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the tblLevel database table.
 * 
 */
@Entity
@NamedQuery(name="TblLevel.findAll", query="SELECT t FROM TblLevel t")
public class TblLevel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String level;

	public TblLevel() {
	}

	public String getLevel() {
		return this.level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

}