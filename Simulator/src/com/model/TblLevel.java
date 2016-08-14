package com.model;

import java.io.Serializable;

/**
 * The persistent class for the tblLevel database table.
 * 
 */

public class TblLevel implements Serializable {
	private static final long serialVersionUID = 1L;

	private String level;

//	private List<TblPriority> tblPriorities1;
//
//	private List<TblPriority> tblPriorities2;
//
//	private List<TblService> tblServices1;
//
//	private List<TblService> tblServices2;

	public TblLevel() {
	}

	public String getLevel() {
		return this.level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

//	public List<TblPriority> getTblPriorities1() {
//		return this.tblPriorities1;
//	}
//
//	public void setTblPriorities1(List<TblPriority> tblPriorities1) {
//		this.tblPriorities1 = tblPriorities1;
//	}
//
//	public TblPriority addTblPriorities1(TblPriority tblPriorities1) {
//		getTblPriorities1().add(tblPriorities1);
//		tblPriorities1.setTblLevel1(this);
//
//		return tblPriorities1;
//	}
//
//	public TblPriority removeTblPriorities1(TblPriority tblPriorities1) {
//		getTblPriorities1().remove(tblPriorities1);
//		tblPriorities1.setTblLevel1(null);
//
//		return tblPriorities1;
//	}
//
//	public List<TblPriority> getTblPriorities2() {
//		return this.tblPriorities2;
//	}
//
//	public void setTblPriorities2(List<TblPriority> tblPriorities2) {
//		this.tblPriorities2 = tblPriorities2;
//	}
//
//	public TblPriority addTblPriorities2(TblPriority tblPriorities2) {
//		getTblPriorities2().add(tblPriorities2);
//		tblPriorities2.setTblLevel2(this);
//
//		return tblPriorities2;
//	}
//
//	public TblPriority removeTblPriorities2(TblPriority tblPriorities2) {
//		getTblPriorities2().remove(tblPriorities2);
//		tblPriorities2.setTblLevel2(null);
//
//		return tblPriorities2;
//	}
//
//	public List<TblService> getTblServices1() {
//		return this.tblServices1;
//	}
//
//	public void setTblServices1(List<TblService> tblServices1) {
//		this.tblServices1 = tblServices1;
//	}
//
//	public TblService addTblServices1(TblService tblServices1) {
//		getTblServices1().add(tblServices1);
//		tblServices1.setTblLevel1(this);
//
//		return tblServices1;
//	}
//
//	public TblService removeTblServices1(TblService tblServices1) {
//		getTblServices1().remove(tblServices1);
//		tblServices1.setTblLevel1(null);
//
//		return tblServices1;
//	}
//
//	public List<TblService> getTblServices2() {
//		return this.tblServices2;
//	}
//
//	public void setTblServices2(List<TblService> tblServices2) {
//		this.tblServices2 = tblServices2;
//	}
//
//	public TblService addTblServices2(TblService tblServices2) {
//		getTblServices2().add(tblServices2);
//		tblServices2.setTblLevel2(this);
//
//		return tblServices2;
//	}
//
//	public TblService removeTblServices2(TblService tblServices2) {
//		getTblServices2().remove(tblServices2);
//		tblServices2.setTblLevel2(null);
//
//		return tblServices2;
//	}

}