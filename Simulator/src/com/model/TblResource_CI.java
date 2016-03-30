package com.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the tblResource_CI database table.
 * 
 */
@Entity
@NamedQuery(name="TblResource_CI.findAll", query="SELECT t FROM TblResource_CI t")
public class TblResource_CI implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TblResource_CIPK id;

	private int quantity;

	//bi-directional many-to-one association to TblCI
	@ManyToOne
	@JoinColumn(name="CI_ID")
	private TblCI tblCi;

	//bi-directional many-to-one association to TblResource
	@ManyToOne
	@JoinColumn(name="resourceName")
	private TblResource tblResource;

	public TblResource_CI() {
	}

	public TblResource_CIPK getId() {
		return this.id;
	}

	public void setId(TblResource_CIPK id) {
		this.id = id;
	}

	public int getQuantity() {
		return this.quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public TblCI getTblCi() {
		return this.tblCi;
	}

	public void setTblCi(TblCI tblCi) {
		this.tblCi = tblCi;
	}

	public TblResource getTblResource() {
		return this.tblResource;
	}

	public void setTblResource(TblResource tblResource) {
		this.tblResource = tblResource;
	}

}