package com.erp.mongo.dal;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.erp.mongo.model.POInvoice;
import com.erp.mongo.model.POInvoiceDetails;
import com.erp.mongo.model.POReturnDetails;
import com.erp.mongo.model.SOReturnDetails;
import com.erp.mongo.model.Stock;
import com.erp.mongo.model.StockDamage;
import com.erp.mongo.model.StockInDetails;
import com.erp.mongo.model.StockReturn;
import com.erp.mongo.model.Vendor;

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
	public List<POInvoice> loadInvoice(List<POInvoice> polist) {
		polist = mongoTemplate.findAll(POInvoice.class);
		return polist;
	}
	
	// Save StockInDetails
	@Override
	public StockInDetails saveStockIn(StockInDetails stockIndetails) {
		System.out.println("Before save stockIn details");
		mongoTemplate.save(stockIndetails);
		System.out.println("After save stockIn details");
		return stockIndetails;
	}
	
	// Save Stock
	public Stock saveStock(Stock stock) {
		System.out.println("Before Save Stock");
		mongoTemplate.save(stock);
		System.out.println("After Save Stock");
		return stock;
	}
	
	@Override
	public StockInDetails loadStockInTotal(String itemName) {
		StockInDetails stockIn;
		Query query = new Query();
		query.addCriteria(Criteria.where("itemname").is(itemName));
		stockIn = mongoTemplate.findOne(query, StockInDetails.class);
		return stockIn;
	}
	
	// load
	public List<Stock> loadStockIn(List<Stock> stocklist) {
		stocklist = mongoTemplate.findAll(Stock.class);
		return stocklist;
	}


}
