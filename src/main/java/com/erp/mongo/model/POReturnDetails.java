package com.erp.mongo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class POReturnDetails {

	@Id
	private String id;
	private String invoicenumber;
	private String returnStatus;
	private String itemname;
	private String qty;
	private String vendorname;
	private String category;
	private String itemStatus;
	private String createddate;
	private String status;
	private int invid;
	private String invoicedqty;
	private String invoiceddate;
	private long price;
	private String pocode;
	private String paymentstatus;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getInvoicenumber() {
		return invoicenumber;
	}

	public void setInvoicenumber(String invoicenumber) {
		this.invoicenumber = invoicenumber;
	}

	public String getItemname() {
		return itemname;
	}

	public void setItemname(String itemname) {
		this.itemname = itemname;
	}

	
	public String getVendorname() {
		return vendorname;
	}

	public void setVendorname(String vendorname) {
		this.vendorname = vendorname;
	}

	public String getQty() {
		return qty;
	}

	public void setQty(String qty) {
		this.qty = qty;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getItemStatus() {
		return itemStatus;
	}

	public void setItemStatus(String itemStatus) {
		this.itemStatus = itemStatus;
	}

	public String getReturnStatus() {
		return returnStatus;
	}

	public void setReturnStatus(String returnStatus) {
		this.returnStatus = returnStatus;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getInvid() {
		return invid;
	}

	public void setInvid(int invid) {
		this.invid = invid;
	}

	public String getInvoicedqty() {
		return invoicedqty;
	}

	public void setInvoicedqty(String invoicedqty) {
		this.invoicedqty = invoicedqty;
	}

	public String getCreateddate() {
		return createddate;
	}

	public void setCreateddate(String createddate) {
		this.createddate = createddate;
	}

	public String getInvoiceddate() {
		return invoiceddate;
	}

	public void setInvoiceddate(String invoiceddate) {
		this.invoiceddate = invoiceddate;
	}

	public long getPrice() {
		return price;
	}

	public void setPrice(long price) {
		this.price = price;
	}

	public String getPocode() {
		return pocode;
	}

	public void setPocode(String pocode) {
		this.pocode = pocode;
	}

	public String getPaymentstatus() {
		return paymentstatus;
	}

	public void setPaymentstatus(String paymentstatus) {
		this.paymentstatus = paymentstatus;
	}

	
}
