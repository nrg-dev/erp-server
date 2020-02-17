package com.erp.mongo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Discount {

	@Id
	private String id;
	String discountcode;
	String productname;
	String discountType;
	String discount;
	String qty ;
	String freegift;
	String others ;
	String fromdate_promotionperiod;
	String todate_promotionperiod;
	String status;
	String categorycode;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getDiscountType() {
		return discountType;
	}
	public void setDiscountType(String discountType) {
		this.discountType = discountType;
	}
	public String getDiscountcode() {
		return discountcode;
	}
	public void setDiscountcode(String discountcode) {
		this.discountcode = discountcode;
	}
	public String getProductname() {
		return productname;
	}
	public void setProductname(String productname) {
		this.productname = productname;
	}
	public String getDiscount() {
		return discount;
	}
	public void setDiscount(String discount) {
		this.discount = discount;
	}
	public String getQty() {
		return qty;
	}
	public void setQty(String qty) {
		this.qty = qty;
	}
	public String getFreegift() {
		return freegift;
	}
	public void setFreegift(String freegift) {
		this.freegift = freegift;
	}
	public String getOthers() {
		return others;
	}
	public void setOthers(String others) {
		this.others = others;
	}
	
	public String getFromdate_promotionperiod() {
		return fromdate_promotionperiod;
	}
	public void setFromdate_promotionperiod(String fromdate_promotionperiod) {
		this.fromdate_promotionperiod = fromdate_promotionperiod;
	}
	public String getTodate_promotionperiod() {
		return todate_promotionperiod;
	}
	public void setTodate_promotionperiod(String todate_promotionperiod) {
		this.todate_promotionperiod = todate_promotionperiod;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCategorycode() {
		return categorycode;
	}
	public void setCategorycode(String categorycode) {
		this.categorycode = categorycode;
	}

	
}
