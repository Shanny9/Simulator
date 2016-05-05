package com.model;

import java.io.Serializable;


/**
 * The persistent class for the tblCMDB database table.
 * 
 */

public class TblCMDB implements Serializable {
	private static final long serialVersionUID = 1L;

	private TblCMDBPK id;

	public TblCMDB() {
	}

	public TblCMDBPK getId() {
		return this.id;
	}

	public void setId(TblCMDBPK id) {
		this.id = id;
	}

}