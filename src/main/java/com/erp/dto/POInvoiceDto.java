package com.erp.dto;

public class POInvoiceDto {

	String createddate;
	String[] ordernumbers;
	long subtotal;
	int deliverycharge;
	long totalprice;
	long[] qty;
	String[] vendorcode;
	String[] productname;
	String paymenttype;

	public String getCreateddate() {
		return createddate;
	}

	public void setCreateddate(String createddate) {
		this.createddate = createddate;
	}

	public String[] getOrdernumbers() {
		return ordernumbers;
	}

	public void setOrdernumbers(String[] ordernumbers) {
		this.ordernumbers = ordernumbers;
	}

	public int getDeliverycharge() {
		return deliverycharge;
	}

	public void setDeliverycharge(int deliverycharge) {
		this.deliverycharge = deliverycharge;
	}

	public long getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(long subtotal) {
		this.subtotal = subtotal;
	}

	public long getTotalprice() {
		return totalprice;
	}

	public void setTotalprice(long totalprice) {
		this.totalprice = totalprice;
	}

	public long[] getQty() {
		return qty;
	}

	public void setQty(long[] qty) {
		this.qty = qty;
	}

	public String[] getVendorcode() {
		return vendorcode;
	}

	public void setVendorcode(String[] vendorcode) {
		this.vendorcode = vendorcode;
	}

	public String[] getProductname() {
		return productname;
	}

	public void setProductname(String[] productname) {
		this.productname = productname;
	}

	public String getPaymenttype() {
		return paymenttype;
	}

	public void setPaymenttype(String paymenttype) {
		this.paymenttype = paymenttype;
	}

	

}
