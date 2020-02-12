package com.erp.mongo.dal;

import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.core.query.Update;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;



//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.util.stream.Stream;

import com.erp.bo.ErpBo;
import com.erp.dto.Member;
import com.erp.model.UserDetail;
import com.erp.mongo.model.Category;
import com.erp.mongo.model.Customer;
import com.erp.mongo.model.Item;
import com.erp.mongo.model.POInvoice;
import com.erp.mongo.model.POInvoiceDetails;
import com.erp.mongo.model.POReturnDetails;
import com.erp.mongo.model.PurchaseOrder;
import com.erp.mongo.model.RandomNumber;
import com.erp.mongo.model.Vendor;
import com.erp.util.Email;


@Repository
public class PurchaseImpl implements PurchaseDAL {
	
	public static final Logger logger = LoggerFactory.getLogger(PurchaseImpl.class);


	@Autowired
	private MongoTemplate mongoTemplate;

	/*
	 * @Autowired ErpBo investmentBo1;
	 */

	
	// Save PO Invoice 
	public POInvoice savePOInvoice(POInvoice poinvoice) {
		System.out.println("Before save Invoice");
		mongoTemplate.save(poinvoice);
		System.out.println("After save Invoice");
		return poinvoice;
	
	}

	// Save PO Invoice details	
	@Override
	public POInvoiceDetails savePurchase(POInvoiceDetails purchaseorder) {
		System.out.println("Before save PO Invoice details");
		mongoTemplate.save(purchaseorder);
		System.out.println("After save Invoice details");
		return purchaseorder;
	}
	
	
	
	/*
	 * @Override public PurchaseOrder savePurchase(PurchaseOrder purchaseorder) {
	 * //mongoTemplate.insert(customer);//(query, RandamNumber.class);
	 * mongoTemplate.save(purchaseorder); //po.setStatus("success"); return
	 * purchaseorder; }
	 */
	public List<Vendor> loadVendorList(List<Vendor> list){
		list =  mongoTemplate.findAll(Vendor.class);//.find(query, OwnTree.class); return
		return list;
			  
	} 
	
	public List<POInvoice> loadPurchase(List<POInvoice> list){
		//List<PurchaseOrder> 
		list =  mongoTemplate.findAll(POInvoice.class);//.find(query, OwnTree.class); return
		return list;
			  
	} 
	
	// get Purchase on Impl
	@Override
	public List<POInvoiceDetails> getPurchase(String invoiceNumber) {
		List<POInvoiceDetails> podetaillist;
		Query query = new Query();
		query.addCriteria(Criteria.where("invoicenumber").is(invoiceNumber));
		podetaillist = mongoTemplate.find(query, POInvoiceDetails.class);
		return podetaillist;
	}
	
	@Override
	public Vendor getVendorDetails(String vendorCode) {
		Vendor vendor;
		Query query = new Query();
		query.addCriteria(Criteria.where("vendorcode").is(vendorCode));
		vendor = mongoTemplate.findOne(query, Vendor.class);
		return vendor;
	}
	
	// revmoe 
	@Override
	public String removePurchase(String invoiceNumber) {
		String response= "failure";
		Query query= new Query();
		query.addCriteria(Criteria.where("invoicenumber").is(invoiceNumber));
		mongoTemplate.remove(query,POInvoiceDetails.class);
		mongoTemplate.remove(query,POInvoice.class);
		response= "Success";
		return response;
	}
	
	// revmoe 
	@Override
	public String removePartId(String id,String invoiceNumber, int temp) {
		String response= "failure";
		Query query= new Query();
		Query query2= new Query();
		query.addCriteria(
		    new Criteria().andOperator(
	    		Criteria.where("id").is(id),
	    		Criteria.where("invoicenumber").is(invoiceNumber)
		    )
		);
		if(temp == 1) {
			mongoTemplate.remove(query,POInvoiceDetails.class);
			query2.addCriteria(Criteria.where("invoicenumber").is(invoiceNumber));
			mongoTemplate.remove(query2,POInvoice.class);
		}else if(temp == 2) {
			mongoTemplate.remove(query,POInvoiceDetails.class);
		}
		response= "Success";
		return response;
	}
	
	@Override
	public List<Item> loadItem(String categoryCode) {
		List<Item> list;
		Query query = new Query();
		query.addCriteria(Criteria.where("categorycode").is(categoryCode));
		list = mongoTemplate.find(query, Item.class);
		return list;
	}
	
	@Override
	public Item getUnitPrice(String productCode,String categoryCode) {
		Item item;
		Query query = new Query();
		query.addCriteria(Criteria.where("prodcode").is(productCode));
		/*query.addCriteria(
		    new Criteria().andOperator(
	    		Criteria.where("categorycode").is(categoryCode),
	    		Criteria.where("prodcode").is(productCode)
		    )
		);*/
		item = mongoTemplate.findOne(query, Item.class);
		return item;
	}
	
	// update PoDetails
	@Override
	public POInvoiceDetails updatePurchase(POInvoiceDetails purchase) {
		Update update = new Update();
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(purchase.getId()));
		
		update.set("invoicenumber", purchase.getInvoicenumber());
		update.set("category", purchase.getCategory());
		update.set("itemname", purchase.getItemname());
		update.set("qty", purchase.getQty());
		update.set("description", purchase.getDescription());
		update.set("unitprice", purchase.getUnitprice());
		update.set("subtotal", purchase.getSubtotal());
		update.set("poDate", purchase.getPoDate());
		update.set("lastUpdate", purchase.getLastUpdate());
		mongoTemplate.updateFirst(query, update, POInvoiceDetails.class);
		return purchase;
	}
	
	@Override
	public POInvoice loadPOInvoice(String invoicenumber) {
		POInvoice poinvoice;
		Query query = new Query();
		query.addCriteria(Criteria.where("invoicenumber").is(invoicenumber));
		poinvoice = mongoTemplate.findOne(query, POInvoice.class);
		return poinvoice;
	}
	
	// update POInvoice
	@Override
	public POInvoice updatePOInvoice(POInvoice purchase) {
		Update update = new Update();
		Query query = new Query();
		query.addCriteria(Criteria.where("invoicenumber").is(purchase.getInvoicenumber()));
		
		update.set("invoicedate", purchase.getInvoicedate());
		update.set("invoicenumber", purchase.getInvoicenumber());
		update.set("vendorname", purchase.getVendorname());
		update.set("deliveryprice", purchase.getDeliveryprice());
		update.set("totalqty", purchase.getTotalqty());
		update.set("totalprice", purchase.getTotalprice());
		update.set("totalitem", purchase.getTotalitem());
		update.set("status", purchase.getStatus());

		mongoTemplate.updateFirst(query, update, POInvoice.class);
		return purchase;
	}

	// Save PO Return details	
	@Override
	public POReturnDetails insertReturn(POReturnDetails purchasereturn) {
		System.out.println("Before save PO Return details");
		mongoTemplate.save(purchasereturn);
		System.out.println("After save PO Return details");
		return purchasereturn;
	}
}
