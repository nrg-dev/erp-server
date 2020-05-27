package com.erp.mongo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class UserRole {

	@Id
	private String id;
	private String invnumber;
	private String username;
	private String password;
	private String userRole;
	private String menuItem;
	private String subMenuItem;
	private String departmentname;
	
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
	
	public String getUserRole() {
		return userRole;
	}
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
	public String getMenuItem() {
		return menuItem;
	}
	public void setMenuItem(String menuItem) {
		this.menuItem = menuItem;
	}
	public String getSubMenuItem() {
		return subMenuItem;
	}
	public void setSubMenuItem(String subMenuItem) {
		this.subMenuItem = subMenuItem;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDepartmentname() {
		return departmentname;
	}
	public void setDepartmentname(String departmentname) {
		this.departmentname = departmentname;
	}
		
	 
}
