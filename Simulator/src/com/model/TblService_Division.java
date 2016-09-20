//package com.model;
//
//import java.io.Serializable;
//
//
///**
// * The persistent class for the tblService_Division database table.
// * 
// */
//
//public class TblService_Division implements Serializable {
//	private static final long serialVersionUID = 1L;
//
//	private byte service_ID;
//
//	private String divisionName;
//	
//	private boolean isActive;
//
//	//bi-directional many-to-one association to TblDivision
//	private TblDivision tblDivision;
//
//	//bi-directional many-to-one association to TblService
//	private TblService tblService;
//
//	public TblService_Division() {
//	}
//
//	
//	
//	public byte getService_ID() {
//		return service_ID;
//	}
//
//
//
//	public void setService_ID(byte service_ID) {
//		this.service_ID = service_ID;
//	}
//
//
//
//	public String getDivisionName() {
//		return divisionName;
//	}
//
//
//
//	public void setDivisionName(String divisionName) {
//		this.divisionName = divisionName;
//	}
//
//
//
////	public TblService_DivisionPK getId() {
////		return this.id;
////	}
////
////	public void setId(TblService_DivisionPK id) {
////		this.id = id;
////	}
//
//	public boolean getIsActive() {
//		return this.isActive;
//	}
//
//	public void setIsActive(boolean isActive) {
//		this.isActive = isActive;
//	}
//
//	public TblDivision getTblDivision() {
//		return this.tblDivision;
//	}
//
//	public void setTblDivision(TblDivision tblDivision) {
//		this.tblDivision = tblDivision;
//	}
//
//	public TblService getTblService() {
//		return this.tblService;
//	}
//
//	public void setTblService(TblService tblService) {
//		this.tblService = tblService;
//	}
//
//}