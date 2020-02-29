package com.erp.mongo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Login {

	@Id
	private String id;
	private String invnumber;
	private String username;
	private String password;
	private String status;
	private String userOtp;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getInvnumber() {
		return invnumber;
	}
	public void setInvnumber(String invnumber) {
		this.invnumber = invnumber;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getUserOtp() {
		return userOtp;
	}
	public void setUserOtp(String userOtp) {
		this.userOtp = userOtp;
	}

	
	
	 
}
