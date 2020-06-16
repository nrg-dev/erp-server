package com.erp.mongo.dal;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.erp.mongo.model.POInvoiceDetails;
import com.erp.mongo.model.POReturnDetails;
import com.erp.mongo.model.SOInvoice;
import com.erp.mongo.model.SOReturnDetails;
import com.erp.mongo.model.Stock;
import com.erp.mongo.model.StockDamage;
import com.erp.mongo.model.StockInDetails;
import com.erp.mongo.model.StockReturn;

@Repository
public class StockImpl implements StockDAL {

	public static final Logger logger = LoggerFactory.getLogger(StockImpl.class);

	@Autowired
	private MongoTemplate mongoTemplate;

	// loadPurchae Return
	public List<POReturnDetails> loadPurchaseReturn(List<POReturnDetails> polist) {
		polist = mongoTemplate.findAll(POReturnDetails.class);
		return polist;
	}

	// loadSales Return
	public List<SOReturnDetails> loadSalesReturn(List<SOReturnDetails> solist) {
		solist = mongoTemplate.findAll(SOReturnDetails.class);
		return solist;
	}

	// save
	public StockDamage saveStockDamage(StockDamage damage) {
		mongoTemplate.save(damage);
		damage.setStatus("success");
		return damage;
	}

	// load
	public List<StockDamage> loadStockDamage(List<StockDamage> damagelist) {
		damagelist = mongoTemplate.findAll(StockDamage.class);
		return damagelist;
	}

	// update
	@Override
	public StockDamage updateDamage(StockDamage damage) {
		Update update = new Update();
		Query query = new Query();
		query.addCriteria(Criteria.where("stockDamageCode").is(damage.getStockDamageCode()));
		update.set("productName", damage.getProductName());
		update.set("category", damage.getCategory());
		update.set("quantity", damage.getQuantity());
		update.set("stockDate", damage.getStockDate());
		update.set("currentStatus", damage.getCurrentStatus());
		mongoTemplate.updateFirst(query, update, StockDamage.class);
		damage.setStatus("success");
		return damage;
	}
	
	//save
	public StockReturn saveStockReturn(StockReturn stReturn) {
		mongoTemplate.save(stReturn);
		stReturn.setStatus("success");
		return stReturn;
	}
	
	// load
	public List<POInvoiceDetails> loadInvoice(List<POInvoiceDetails> polist, String paymentOption) {
		Query query = new Query();
		logger.info("PaymentOption --->"+paymentOption);
		if(paymentOption.equalsIgnoreCase("FullStockIn")) {
			query.addCriteria( new Criteria().orOperator(
					Criteria.where("paymentStatus").is(""),
					Criteria.where("paymentStatus").is("Not Paid") ));
		}else if(paymentOption.equalsIgnoreCase("PartialStockIn")) {
			query.addCriteria( new Criteria().orOperator(
					Criteria.where("paymentStatus").is("Not Paid"),Criteria.where("paymentStatus").is(""),
					Criteria.where("paymentStatus").is("PartialStockIn") ) );
		}
		polist = mongoTemplate.find(query,POInvoiceDetails.class);
		return polist;
	}
	
	// Save StockInDetails
	@Override
	public StockInDetails saveStockIn(StockInDetails stockIndetails) {
		logger.info("Before save stockIn details");
		mongoTemplate.save(stockIndetails);
		logger.info("After save stockIn details");
		return stockIndetails;
	}
	
	// Save Stock
	public Stock saveStock(Stock stock) {
		logger.info("Before Save Stock");
		mongoTemplate.save(stock);
		stock.setStatus("success");
		logger.info("After Save Stock");
		return stock;
	}
	
	@Override
	public POInvoiceDetails loadStockInTotal(StockInDetails stockIndetails) {
		POInvoiceDetails stockIn;
		Query query = new Query();
		query.addCriteria( new Criteria().andOperator(
				Criteria.where("invoicenumber").is(stockIndetails.getInvoicenumber()),
				Criteria.where("itemname").is(stockIndetails.getItemname()) ) );
		stockIn = mongoTemplate.findOne(query, POInvoiceDetails.class);
		return stockIn;
	}
	
	// load
	public List<Stock> loadStock(List<Stock> stocklist, String status) {
		Query query = new Query();
		if(status.equalsIgnoreCase("StockIn")) {
			query.addCriteria(Criteria.where("status").is("StockIn"));
		}else if(status.equalsIgnoreCase("StockOut")) {
			query.addCriteria(Criteria.where("status").is("StockOut"));
		}else if(status.equalsIgnoreCase("Ready for Sales")) {
			query.addCriteria( new Criteria().andOperator(
					Criteria.where("status").is("Ready for Sales"),
					Criteria.where("recentStock").ne(0) ) );
		}
		stocklist = mongoTemplate.find(query, Stock.class);
		return stocklist;
	}
	
	//--- Get ID Based on Invoice
	public Stock loadStockInvoice(String StockInCategory, int i) {
		Stock res=null;
		try {
			Query query = new Query();
			if(i == 1) {
				query.addCriteria(Criteria.where("stockCategory").is(StockInCategory));
			}else if(i == 2) {
				logger.info("Item Code --->"+StockInCategory);
				query.addCriteria( new Criteria().andOperator(
						Criteria.where("itemcode").is(StockInCategory),
						Criteria.where("status").is("Ready for Sales") ) );
			}
			
			res = mongoTemplate.findOne(query, Stock.class);
			if(res != null) {
				logger.info("------ StockId Match ------");
			}else {
				logger.info("------ StockId Not Match---------------");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	// update FullPoDetails
	public POInvoiceDetails updateFullPurchase(StockInDetails stockIndetails,POInvoiceDetails podetails) {
		Update update = new Update();
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(stockIndetails.getId()));
		
		update.set("invoicenumber", stockIndetails.getInvoicenumber());
		update.set("category", stockIndetails.getCategory());
		update.set("itemname", stockIndetails.getItemname());
		update.set("qty", stockIndetails.getQty());
		update.set("description", stockIndetails.getDescription());
		update.set("unitprice", stockIndetails.getUnitprice());
		update.set("subtotal", stockIndetails.getSubtotal());
		update.set("poDate", stockIndetails.getPoDate());
		update.set("lastUpdate", podetails.getLastUpdate());
		update.set("paymentStatus", podetails.getPaymentStatus());
		update.set("remainingAmount", podetails.getRemainingQty());
		mongoTemplate.updateFirst(query, update, POInvoiceDetails.class);

		return podetails;
	}

	//----- Update PartialPoDetails---
	@Override
	public POInvoiceDetails updatePurchase(POInvoiceDetails podetails) {
		Update update = new Update();
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(podetails.getId()));
		
		update.set("invoicenumber", podetails.getInvoicenumber());
		update.set("category", podetails.getCategory());
		update.set("itemname", podetails.getItemname());
		update.set("qty", podetails.getQty());
		update.set("description", podetails.getDescription());
		update.set("unitprice", podetails.getUnitprice());
		update.set("subtotal", podetails.getSubtotal());
		update.set("poDate", podetails.getPoDate());
		update.set("lastUpdate", podetails.getLastUpdate());
		update.set("paymentStatus", podetails.getPaymentStatus());
		update.set("remainingAmount", podetails.getRemainingQty());
		mongoTemplate.updateFirst(query, update, POInvoiceDetails.class);

		return podetails;
	}
	
	// update PoDetails
	@Override
	public Stock updateStock(Stock stock, String id) {
		Update update = new Update();
		Query query = new Query();
		if(id == "all") {
			query.addCriteria( new Criteria().andOperator(
					Criteria.where("itemcode").is(stock.getItemcode()),
					Criteria.where("status").is("Ready for Sales") ) );
			update.set("invoicedate", stock.getInvoicedate());
			update.set("invoicenumber", "NONE");
			update.set("itemname", stock.getItemname());
			update.set("itemcode", stock.getItemcode());
			update.set("category", stock.getCategory());
			update.set("categorycode", stock.getCategorycode());
			update.set("recentStock", stock.getAddedqty());
			update.set("unit", stock.getUnit());
			update.set("status", "Ready for Sales");
			mongoTemplate.updateFirst(query, update, Stock.class);
		}else if(id == "update") {
			query.addCriteria(Criteria.where("id").is(stock.getId()));
			update.set("recentStock", stock.getRecentStock());
			mongoTemplate.findAndModify(query, update,
					new FindAndModifyOptions().returnNew(true), Stock.class);
		}else {
			query.addCriteria(Criteria.where("id").is(id));
			update.set("invoicedate", stock.getInvoicedate());
			update.set("stockInCategory", stock.getStockCategory());
			update.set("itemname", stock.getItemname());
			update.set("category", stock.getCategory());
			update.set("recentStock", stock.getRecentStock());
			update.set("addedqty", stock.getAddedqty());
			update.set("status", stock.getStatus());
			mongoTemplate.updateFirst(query, update, Stock.class);
		}
		return stock;
	}
	
	@Override
	public Stock saveStockOut(Stock stock) {
		mongoTemplate.save(stock);
		stock.setStatus("success");
		return stock;
	}

}
