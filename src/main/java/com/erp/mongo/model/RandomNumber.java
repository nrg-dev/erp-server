package com.erp.mongo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class RandomNumber {

	@Id
	private String id;
	private long poinvoicenumber;	
	private String poinvoicecode;
	private long salesinvoicenumber;
	private String salesinvoicecode;
	private long vendorinvoicenumber;
	private String vendorinvoicecode;
	private long customerinvoicenumber;
	private String customerinvoicecode;
	

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public long getPoinvoicenumber() {
		return poinvoicenumber;
	}
	public void setPoinvoicenumber(long poinvoicenumber) {
		this.poinvoicenumber = poinvoicenumber;
	}
	public String getPoinvoicecode() {
		return poinvoicecode;
	}
	public void setPoinvoicecode(String poinvoicecode) {
		this.poinvoicecode = poinvoicecode;
	}
	public long getSalesinvoicenumber() {
		return salesinvoicenumber;
	}
	public void setSalesinvoicenumber(long salesinvoicenumber) {
		this.salesinvoicenumber = salesinvoicenumber;
	}
	public String getSalesinvoicecode() {
		return salesinvoicecode;
	}
	public void setSalesinvoicecode(String salesinvoicecode) {
		this.salesinvoicecode = salesinvoicecode;
	}
	public long getVendorinvoicenumber() {
		return vendorinvoicenumber;
	}
	public void setVendorinvoicenumber(long vendorinvoicenumber) {
		this.vendorinvoicenumber = vendorinvoicenumber;
	}
	public String getVendorinvoicecode() {
		return vendorinvoicecode;
	}
	public void setVendorinvoicecode(String vendorinvoicecode) {
		this.vendorinvoicecode = vendorinvoicecode;
	}
	public long getCustomerinvoicenumber() {
		return customerinvoicenumber;
	}
	public void setCustomerinvoicenumber(long customerinvoicenumber) {
		this.customerinvoicenumber = customerinvoicenumber;
	}
	public String getCustomerinvoicecode() {
		return customerinvoicecode;
	}
	public void setCustomerinvoicecode(String customerinvoicecode) {
		this.customerinvoicecode = customerinvoicecode;
	}
	

	
}
