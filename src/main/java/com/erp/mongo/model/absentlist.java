package com.erp.mongo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class absentlist {

	@Id
	private String id;
	private String empcode;
	private String checkintime;
	private String checkouttime;
	private String absent;
	private String reason;
	private String date;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmpcode() {
		return empcode;
	}

	public void setEmpcode(String empcode) {
		this.empcode = empcode;
	}

	public String getCheckintime() {
		return checkintime;
	}

	public void setCheckintime(String checkintime) {
		this.checkintime = checkintime;
	}

	public String getCheckouttime() {
		return checkouttime;
	}

	public void setCheckouttime(String checkouttime) {
		this.checkouttime = checkouttime;
	}

	public String getAbsent() {
		return absent;
	}

	public void setAbsent(String absent) {
		this.absent = absent;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	
}