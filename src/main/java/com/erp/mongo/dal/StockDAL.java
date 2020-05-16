package com.erp.mongo.dal;

import java.util.List;

import com.erp.mongo.model.Item;
import com.erp.mongo.model.POInvoiceDetails;
import com.erp.mongo.model.POReturnDetails;
import com.erp.mongo.model.SOReturnDetails;
import com.erp.mongo.model.Stock;
import com.erp.mongo.model.StockDamage;
import com.erp.mongo.model.StockInDetails;
import com.erp.mongo.model.StockReturn;


public interface StockDAL {

	public List<POReturnDetails> loadPurchaseReturn(List<POReturnDetails> poList);

	public List<SOReturnDetails> loadSalesReturn(List<SOReturnDetails> poList);
	
	public StockDamage saveStockDamage(StockDamage stockdamage);
	public List<StockDamage> loadStockDamage(List<StockDamage> damagelist);
	public StockDamage updateDamage(StockDamage damage);
	
	public StockReturn saveStockReturn(StockReturn stockreturn);

	public List<POInvoiceDetails> loadInvoice(List<POInvoiceDetails> polist, String paymentOption);

	public StockInDetails saveStockIn(StockInDetails stockIndetails);

	public Stock saveStock(Stock stock); 

	public POInvoiceDetails loadStockInTotal(StockInDetails stockIndetails);

	public POInvoiceDetails updatePurchase(POInvoiceDetails podetails);

	public Stock updateStock(Stock stockIn, String id);

	public List<Stock> loadStock(List<Stock> stocklist, String status); 

	public Stock loadStockInvoice(String stockInCategory, int i); 

	public POInvoiceDetails updateFullPurchase(StockInDetails stockIndetails, POInvoiceDetails podetails);

	public Stock saveStockOut(Stock stock);

}