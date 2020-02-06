package com.erp.mongo.dal;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;
import com.erp.mongo.model.Employee;
import com.erp.mongo.model.POReturnDetails;
import com.erp.mongo.model.SOReturnDetails;


@Repository
public class StockImpl implements StockDAL {
	
	public static final Logger logger = LoggerFactory.getLogger(StockImpl.class);


	@Autowired
	private MongoTemplate mongoTemplate;

	//loadPurchae Return
	public List<POReturnDetails> loadPurchaseReturn(List<POReturnDetails> polist){
		polist = mongoTemplate.findAll(POReturnDetails.class);
		return polist;
	}
	
	
	//loadSales Return
	public List<SOReturnDetails> loadSalesReturn(List<SOReturnDetails> solist){
		solist = mongoTemplate.findAll(SOReturnDetails.class);
		return solist;
	}
	
	
}
