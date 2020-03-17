package com.erp.mongo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class PettyCash {

	@Id
	private String id;
	private String description;
	private String addeddate;
	private String type;
	private String fromperson;
	private String toperson;
	private int totalamount;
	private String status;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAddeddate() {
		return addeddate;
	}

	public void setAddeddate(String addeddate) {
		this.addeddate = addeddate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFromperson() {
		return fromperson;
	}

	public void setFromperson(String fromperson) {
		this.fromperson = fromperson;
	}

	public String getToperson() {
		return toperson;
	}

	public void setToperson(String toperson) {
		this.toperson = toperson;
	}

	public int getTotalamount() {
		return totalamount;
	}

	public void setTotalamount(int totalamount) {
		this.totalamount = totalamount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	

}
