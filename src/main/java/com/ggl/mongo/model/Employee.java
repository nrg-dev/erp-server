package com.ggl.mongo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Employee {

	@Id
	private String id;
	private String name;			
	private String rank;		
	private String phonenumber; 	
	private String address;			
	private String email;			
	private String dob 	;		
	private String contractnumber ;	
	private String npwp 	;		
	private String bpjs 	;		
	private String monthlysalary; 	
	private String workHour 	;	
	private String annualLeave 	;
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
	public String getRank() {
		return rank;
	}
	public void setRank(String rank) {
		this.rank = rank;
	}
	public String getPhonenumber() {
		return phonenumber;
	}
	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public String getContractnumber() {
		return contractnumber;
	}
	public void setContractnumber(String contractnumber) {
		this.contractnumber = contractnumber;
	}
	public String getNpwp() {
		return npwp;
	}
	public void setNpwp(String npwp) {
		this.npwp = npwp;
	}
	public String getBpjs() {
		return bpjs;
	}
	public void setBpjs(String bpjs) {
		this.bpjs = bpjs;
	}
	public String getMonthlysalary() {
		return monthlysalary;
	}
	public void setMonthlysalary(String monthlysalary) {
		this.monthlysalary = monthlysalary;
	}
	public String getWorkHour() {
		return workHour;
	}
	public void setWorkHour(String workHour) {
		this.workHour = workHour;
	}
	public String getAnnualLeave() {
		return annualLeave;
	}
	public void setAnnualLeave(String annualLeave) {
		this.annualLeave = annualLeave;
	}

	
}
