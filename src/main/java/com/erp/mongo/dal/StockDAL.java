package com.erp.mongo.dal;

import java.util.List;

import com.erp.mongo.model.POInvoice;
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

	public List<POInvoice> loadInvoice(List<POInvoice> polist);

	public StockInDetails saveStockIn(StockInDetails stockIndetails);

	public Stock saveStock(Stock stock);

	public StockInDetails loadStockInTotal(String itemName);
	public List<Stock> loadStockIn(List<Stock> stocklist); 

}