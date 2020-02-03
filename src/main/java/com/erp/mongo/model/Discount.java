package com.erp.mongo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Discount {

	@Id
	private String id;
	String discountcode;
	String productname;
	String discount;
	String qty ;
	String freegift;
	String others ;
	String promotionperiod;
	String status;
	String categorycode;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getPromotionperiod() {
		return promotionperiod;
	}
	public void setPromotionperiod(String promotionperiod) {
		this.promotionperiod = promotionperiod;
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
