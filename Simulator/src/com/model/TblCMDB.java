package com.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the tblCMDB database table.
 * 
 */
@Entity
@NamedQuery(name="TblCMDB.findAll", query="SELECT t FROM TblCMDB t")
public class TblCMDB implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
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