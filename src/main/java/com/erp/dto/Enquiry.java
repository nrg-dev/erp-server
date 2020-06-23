package com.erp.dto;

import java.io.Serializable;

import org.springframework.data.annotation.Id;

public class Enquiry {
	/**
	 * 
	 */
	@Id
	private String id;
	String name; // return status
	String phonenumber;	
	String email_ID;
	String message;
    String country;
    String enquirytype;
    String status;
    String addeddate;
    
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhonenumber() {
		return phonenumber;
	}
	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}
	public String getEmail_ID() {
		return email_ID;
	}
	public void setEmail_ID(String email_ID) {
		this.email_ID = email_ID;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getEnquirytype() {
		return enquirytype;
	}
	public void setEnquirytype(String enquirytype) {
		this.enquirytype = enquirytype;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAddeddate() {
		return addeddate;
	}
	public void setAddeddate(String addeddate) {
		this.addeddate = addeddate;
	}
	
}
