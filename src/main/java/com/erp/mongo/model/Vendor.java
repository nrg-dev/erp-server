package com.erp.mongo.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Vendor {

	  @Id
	  private String id;
	  String vendorcode 	;
      String vendorname 	;
      String phonenumber 	;
      String mobilenumber 	;
      String address 		;
      String country 		;
      String city 			;
      String email 			;
      String lastedit 		;
      String status 		;
      private String addeddate;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getVendorcode() {
		return vendorcode;
	}
	public void setVendorcode(String vendorcode) {
		this.vendorcode = vendorcode;
	}
	public String getVendorname() {
		return vendorname;
	}
	public void setVendorname(String vendorname) {
		this.vendorname = vendorname;
	}
	public String getPhonenumber() {
		return phonenumber;
	}
	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}
	public String getMobilenumber() {
		return mobilenumber;
	}
	public void setMobilenumber(String mobilenumber) {
		this.mobilenumber = mobilenumber;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getLastedit() {
		return lastedit;
	}
	public void setLastedit(String lastedit) {
		this.lastedit = lastedit;
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
