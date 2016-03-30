package com.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the tblResource_Change database table.
 * 
 */
@Entity
@NamedQuery(name="TblResource_Change.findAll", query="SELECT t FROM TblResource_Change t")
public class TblResource_Change implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TblResource_ChangePK id;

	private int quantity;

	//bi-directional many-to-one association to TblChange
	@ManyToOne
	@JoinColumn(name="changeID")
	private TblChange tblChange;

	//bi-directional many-to-one association to TblResource
	@ManyToOne
	@JoinColumn(name="resourceName")
	private TblResource tblResource;

	public TblResource_Change() {
	}

	public TblResource_ChangePK getId() {
		return this.id;
	}

	public void setId(TblResource_ChangePK id) {
		this.id = id;
	}

	public int getQuantity() {
		return this.quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public TblChange getTblChange() {
		return this.tblChange;
	}

	public void setTblChange(TblChange tblChange) {
		this.tblChange = tblChange;
	}

	public TblResource getTblResource() {
		return this.tblResource;
	}

	public void setTblResource(TblResource tblResource) {
		this.tblResource = tblResource;
	}

}