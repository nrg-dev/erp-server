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
	
	private long employeeinvoicenumber;
	private String employeeinvoicecode;

	private long vendorinvoicenumber;
	private String vendorinvoicecode;

	private long customerinvoicenumber;
	private String customerinvoicecode;

	private long categoryinvoicenumber;
	private String categoryinvoicecode;

	private long productinvoicenumber;
	private String productinvoicecode;
	
	private long discountinvoicenumber;
	private String discountinvoicecode;
	
	private long poreturninvoicenumber;
	private String poreturninvoicecode;
	
	private long soreturninvoicenumber;
	private String soreturninvoicecode;	

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

	public long getCategoryinvoicenumber() {
		return categoryinvoicenumber;
	}

	public void setCategoryinvoicenumber(long categoryinvoicenumber) {
		this.categoryinvoicenumber = categoryinvoicenumber;
	}

	public String getCategoryinvoicecode() {
		return categoryinvoicecode;
	}

	public void setCategoryinvoicecode(String categoryinvoicecode) {
		this.categoryinvoicecode = categoryinvoicecode;
	}

	public long getProductinvoicenumber() {
		return productinvoicenumber;
	}

	public void setProductinvoicenumber(long productinvoicenumber) {
		this.productinvoicenumber = productinvoicenumber;
	}

	public String getProductinvoicecode() {
		return productinvoicecode;
	}

	public void setProductinvoicecode(String productinvoicecode) {
		this.productinvoicecode = productinvoicecode;
	}

	public long getEmployeeinvoicenumber() {
		return employeeinvoicenumber;
	}

	public void setEmployeeinvoicenumber(long employeeinvoicenumber) {
		this.employeeinvoicenumber = employeeinvoicenumber;
	}

	public String getEmployeeinvoicecode() {
		return employeeinvoicecode;
	}

	public void setEmployeeinvoicecode(String employeeinvoicecode) {
		this.employeeinvoicecode = employeeinvoicecode;
	}

	public long getDiscountinvoicenumber() {
		return discountinvoicenumber;
	}

	public void setDiscountinvoicenumber(long discountinvoicenumber) {
		this.discountinvoicenumber = discountinvoicenumber;
	}

	public String getDiscountinvoicecode() {
		return discountinvoicecode;
	}

	public void setDiscountinvoicecode(String discountinvoicecode) {
		this.discountinvoicecode = discountinvoicecode;
	}

	public long getPoreturninvoicenumber() {
		return poreturninvoicenumber;
	}

	public void setPoreturninvoicenumber(long poreturninvoicenumber) {
		this.poreturninvoicenumber = poreturninvoicenumber;
	}

	public String getPoreturninvoicecode() {
		return poreturninvoicecode;
	}

	public void setPoreturninvoicecode(String poreturninvoicecode) {
		this.poreturninvoicecode = poreturninvoicecode;
	}

	public long getSoreturninvoicenumber() {
		return soreturninvoicenumber;
	}

	public void setSoreturninvoicenumber(long soreturninvoicenumber) {
		this.soreturninvoicenumber = soreturninvoicenumber;
	}

	public String getSoreturninvoicecode() {
		return soreturninvoicecode;
	}

	public void setSoreturninvoicecode(String soreturninvoicecode) {
		this.soreturninvoicecode = soreturninvoicecode;
	}
	
	
	

}
