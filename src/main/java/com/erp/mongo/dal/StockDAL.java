package com.erp.mongo.dal;

import java.util.List;

import com.erp.mongo.model.POReturnDetails;
import com.erp.mongo.model.SOReturnDetails;
import com.erp.mongo.model.StockDamage;

public interface StockDAL {

	public List<POReturnDetails> loadPurchaseReturn(List<POReturnDetails> poList);

	public List<SOReturnDetails> loadSalesReturn(List<SOReturnDetails> poList);

	public StockDamage saveStockDamage(StockDamage stockdamage);

	public List<StockDamage> loadStockDamage(List<StockDamage> damagelist);

	public StockDamage updateDamage(StockDamage damage);

}