package com.erp.dto;

import org.codehaus.jettison.json.JSONObject;

public class Purchase {
	
	
	/*
	 * Purchase(){
	 * 
	 * }
	 */
	  String productName; 
	  String category; 
	  String vendorName; 
	  String poDate; 
	  String quantity; 
	  String netAmount;	 
	  String unitPrice;
	  String status;
	  String description;
	  String[] purchasearray;
	
	 

	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	public String getPoDate() {
		return poDate;
	}
	public void setPoDate(String poDate) {
		this.poDate = poDate;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getNetAmount() {
		return netAmount;
	}
	public void setNetAmount(String netAmount) {
		this.netAmount = netAmount;
	}
	public String getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(String unitPrice) {
		this.unitPrice = unitPrice;
	}
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String[] getPurchasearray() {
		return purchasearray;
	}
	public void setPurchasearray(String[] purchasearray) {
		this.purchasearray = purchasearray;
	}
	
	
	
}
