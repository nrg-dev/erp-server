package com.erp.mongo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class StockReturn {

	@Id
	private String id;
	String stockreturncode;
	String productname;
	String category;
	String quantity;
	String stockdate;
	String status;
	String currentstatus;
	String vendorname;
	String returncategory;
	String addeddate;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStockreturncode() {
		return stockreturncode;
	}

	public void setStockreturncode(String stockreturncode) {
		this.stockreturncode = stockreturncode;
	}

	public String getProductname() {
		return productname;
	}

	public void setProductname(String productname) {
		this.productname = productname;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getStockdate() {
		return stockdate;
	}

	public void setStockdate(String stockdate) {
		this.stockdate = stockdate;
	}

	public String getCurrentstatus() {
		return currentstatus;
	}

	public void setCurrentstatus(String currentstatus) {
		this.currentstatus = currentstatus;
	}

	public String getVendorname() {
		return vendorname;
	}

	public void setVendorname(String vendorname) {
		this.vendorname = vendorname;
	}

	public String getReturncategory() {
		return returncategory;
	}

	public void setReturncategory(String returncategory) {
		this.returncategory = returncategory;
	}

	public String getAddeddate() {
		return addeddate;
	}

	public void setAddeddate(String addeddate) {
		this.addeddate = addeddate;
	}

	

}