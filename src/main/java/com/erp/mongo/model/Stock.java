package com.erp.mongo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Stock {

	@Id
	private String id;
	private String invoicedate;
	private String invoicenumber;
	private String stockCategory;
	private String itemname;
	private String itemcode;
	private String category;
	private String categorycode;
	private long recentStock;
	private long addedqty;
	private String status;
	private String stockOutCategory;
	private String unit;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getInvoicedate() {
		return invoicedate;
	}

	public void setInvoicedate(String invoicedate) {
		this.invoicedate = invoicedate;
	}

	public String getInvoicenumber() {
		return invoicenumber;
	}

	public void setInvoicenumber(String invoicenumber) {
		this.invoicenumber = invoicenumber;
	}

	public String getStockCategory() {
		return stockCategory;
	}

	public void setStockCategory(String stockCategory) {
		this.stockCategory = stockCategory;
	}

	public String getItemname() {
		return itemname;
	}

	public void setItemname(String itemname) {
		this.itemname = itemname;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStockOutCategory() {
		return stockOutCategory;
	}

	public void setStockOutCategory(String stockOutCategory) {
		this.stockOutCategory = stockOutCategory;
	}

	public String getItemcode() {
		return itemcode;
	}

	public void setItemcode(String itemcode) {
		this.itemcode = itemcode;
	}

	public String getCategorycode() {
		return categorycode;
	}

	public void setCategorycode(String categorycode) {
		this.categorycode = categorycode;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public long getRecentStock() {
		return recentStock;
	}

	public void setRecentStock(long recentStock) {
		this.recentStock = recentStock;
	}

	public long getAddedqty() {
		return addedqty;
	}

	public void setAddedqty(long addedqty) {
		this.addedqty = addedqty;
	}

 
	 
}
