package com.ggl.mongo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class RandomNumber {

	@Id
	private String id;
	private String poinvoicenumber;			
	private String salesinvoicenumber;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPoinvoicenumber() {
		return poinvoicenumber;
	}
	public void setPoinvoicenumber(String poinvoicenumber) {
		this.poinvoicenumber = poinvoicenumber;
	}
	public String getSalesinvoicenumber() {
		return salesinvoicenumber;
	}
	public void setSalesinvoicenumber(String salesinvoicenumber) {
		this.salesinvoicenumber = salesinvoicenumber;
	}		
	

	
}
