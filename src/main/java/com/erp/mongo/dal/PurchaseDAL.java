package com.erp.mongo.dal;

import java.util.List;

import com.erp.dto.Member;
import com.ggl.mongo.model.Customer;
import com.ggl.mongo.model.POInvoice;
import com.ggl.mongo.model.POInvoiceDetails;
import com.ggl.mongo.model.PurchaseOrder;
import com.ggl.mongo.model.RandomNumber;

import java.nio.file.Path;
//import java.util.List;
import java.util.stream.Stream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
public interface PurchaseDAL {

	//public PurchaseOrder savePurchase(PurchaseOrder purchaseorder);
	public RandomNumber getRandamNumber();
	public String updateRandamNumber(RandomNumber rn);
	

	public POInvoiceDetails savePurchase(POInvoiceDetails purchaseorder);
	public List<PurchaseOrder> loadPurchase(List<PurchaseOrder> list);
	/*
	 * public List<Customer> getPurchase(String id); public Customer
	 * updatePurchase(PurchaseOrder customer); public String[] loadPurchase(String[]
	 * list); public void removePurchase(String id);
	 */
	
}