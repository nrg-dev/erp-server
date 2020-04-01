package com.erp.mongo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class RandomNumber {

	@Id
	private String id;

	
	private long number;
	private String code;
	private String description;
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}	

	public long getNumber() {
		return number;
	}

	public void setNumber(long number) {
		this.number = number;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
