package com.erp.mongo.dal;

import java.util.List;

import com.erp.dto.Purchase;
import com.erp.mongo.model.POInvoiceDetails;
import com.erp.mongo.model.POReturnDetails;
import com.erp.mongo.model.SOReturnDetails;

public interface StockDAL {

	List<POReturnDetails> loadPurchaseReturn(List<POReturnDetails> poList); 
	List<SOReturnDetails> loadSalesReturn(List<SOReturnDetails> poList); 



}