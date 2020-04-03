package com.erp.mongo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class AbsentList {

	@Id
	private String id;
	 String employeecode 	;
	 String checkinreason;
	 String checkintime;
	 String checkoutreason;
	 String checkouttime;
	 String absent;
	 String reason;
	 String date;
	 
	 public AbsentList(String employeecode,String checkinreason,String checkintime,
			 String checkoutreason,String checkouttime,String absent,String reason,String date){
		 this.employeecode=employeecode;
		 this.checkinreason=checkinreason;
		 this.checkintime=checkintime;
		 this.checkoutreason=checkoutreason;
		 this.checkouttime=checkouttime;
		 this.absent=absent;
		 this.reason=reason;
		 this.date=date;
	 }
	 
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEmployeecode() {
		return employeecode;
	}
	public void setEmployeecode(String employeecode) {
		this.employeecode = employeecode;
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
	public String getCheckinreason() {
		return checkinreason;
	}
	public void setCheckinreason(String checkinreason) {
		this.checkinreason = checkinreason;
	}
	public String getCheckoutreason() {
		return checkoutreason;
	}
	public void setCheckoutreason(String checkoutreason) {
		this.checkoutreason = checkoutreason;
	}

	
	

}
