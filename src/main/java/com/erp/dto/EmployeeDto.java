package com.erp.dto;

import java.io.Serializable;

public class EmployeeDto  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3962716785378801138L;
	
	String id 	;
	String employeecode 	;
	String date;
	String report;
	String type;
	
	
	
	
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
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getReport() {
		return report;
	}
	public void setReport(String report) {
		this.report = report;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	 
}
