package com.erp.mongo.model;

import java.time.LocalTime;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class POInvoice {

	@Id
	private String id;
	private String invoicedate;
	private String invoicenumber;			
	private String vendorname;		
	private int totalqty; 	
	private String deliveryprice;			
	private int totalprice;			
	private String status;		

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
	public String getVendorname() {
		return vendorname;
	}
	public void setVendorname(String vendorname) {
		this.vendorname = vendorname;
	}
	public int getTotalqty() {
		return totalqty;
	}
	public void setTotalqty(int totalqty) {
		this.totalqty = totalqty;
	}
	
	public String getDeliveryprice() {
		return deliveryprice;
	}
	public void setDeliveryprice(String deliveryprice) {
		this.deliveryprice = deliveryprice;
	}
	
	public int getTotalprice() {
		return totalprice;
	}
	public void setTotalprice(int totalprice) {
		this.totalprice = totalprice;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	

	
}
