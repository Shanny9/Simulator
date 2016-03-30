package com.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the tblCourse database table.
 * 
 */
@Entity
@NamedQuery(name="TblCourse.findAll", query="SELECT t FROM TblCourse t")
public class TblCourse implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="course_name")
	private String courseName;

	@Temporal(TemporalType.DATE)
	private Date date;

	private byte isActive;

	private byte lastRoundDone;

	public TblCourse() {
	}

	public String getCourseName() {
		return this.courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public byte getIsActive() {
		return this.isActive;
	}

	public void setIsActive(byte isActive) {
		this.isActive = isActive;
	}

	public byte getLastRoundDone() {
		return this.lastRoundDone;
	}

	public void setLastRoundDone(byte lastRoundDone) {
		this.lastRoundDone = lastRoundDone;
	}

}