package com.erp.mongo.dal;




import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.core.query.Update;





//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;

import com.erp.mongo.model.RandomNumber;


@Repository
public class RandomNumberImpl implements RandomNumberDAL {
	
	public static final Logger logger = LoggerFactory.getLogger(PurchaseImpl.class);


	@Autowired
	private MongoTemplate mongoTemplate;

	/*
	 * @Autowired ErpBo investmentBo1;
	 */

	
	
	@Override
	public RandomNumber getRandamNumber() {
		RandomNumber radomNumber=null;
		try {
			logger.info("----------- Inside getRandamNumber-----------");
			Query query = new Query();
			logger.info("-----------  Before addCriteria-----------");
		    query.addCriteria(Criteria.where("randomID").is(1));
			logger.info("-----------  After addCriteria-----------");
			radomNumber = mongoTemplate.findOne(query, RandomNumber.class);
			logger.info("Invoice number ----------->"+radomNumber.getPoinvoicenumber());
			return radomNumber;//mongoTemplate.find(query, RandamNumber.class);//(RandamNumber.class);
		}catch(Exception e) {
			e.printStackTrace();
			return radomNumber;


		}finally {
			
		}
		
	}
	
	
	@Override
	public boolean updateRandamNumber(RandomNumber rn) {
		logger.info("current invoice number -->"+rn.getPoinvoicenumber());		
		Query query = new Query();
	    query.addCriteria(Criteria.where("randomID").is(1));
		Update update = new Update();
		update.set("poinvoicenumber", rn.getPoinvoicenumber()+1);			
		mongoTemplate.updateFirst(query, update, RandomNumber.class);//(query, RandamNumber.class);
		return true;//mongoTemplate.find(query, RandamNumber.class);//(RandamNumber.class);
	}
	
	
	//---- Vendor and customer RandomCade Getting ---
	@Override
	public RandomNumber getVendorRandamNumber() {
		RandomNumber radomNumber=null;
		try {
			logger.info("----------- Inside getVendorRandamNumber-----------");
			Query query = new Query();
			logger.info("-----------  Before addCriteria-----------");
		    query.addCriteria(Criteria.where("randomID").is(2));
			logger.info("-----------  After addCriteria-----------");
			radomNumber = mongoTemplate.findOne(query, RandomNumber.class);
			logger.info("Vendor Invoice number ----------->"+radomNumber.getVendorinvoicenumber());
			logger.info("Customer Invoice number ----------->"+radomNumber.getCustomerinvoicenumber());
			return radomNumber;
		}catch(Exception e) {
			e.printStackTrace();
			return radomNumber;
		}finally {
			
		}	
	}
	
	@Override
	public boolean updateVendorRandamNumber(RandomNumber rn,int num) {
		logger.info("current Vendor invoice number -->"+rn.getVendorinvoicenumber());	
		logger.info("Number for Vendor/Customer -->"+num);
		Query query = new Query();
	    query.addCriteria(Criteria.where("randomID").is(2));
		Update update = new Update();
		if(num == 1) {
			update.set("vendorinvoicenumber", rn.getVendorinvoicenumber()+1);			
		}else if(num == 2) {
			update.set("customerinvoicenumber", rn.getCustomerinvoicenumber()+1);			
		}
		mongoTemplate.updateFirst(query, update, RandomNumber.class);//(query, RandamNumber.class);
		return true;//mongoTemplate.find(query, RandamNumber.class);//(RandamNumber.class);
	}
	
	

	//---- Employee RandomCade Getting ---
	@Override
	public RandomNumber getEmployeeRandamNumber() {
		RandomNumber radomNumber=null;
		try {
			logger.info("----------- Inside getEmployeeRandamNumber-----------");
			Query query = new Query();
			logger.info("-----------  Before addCriteria-----------");
		    query.addCriteria(Criteria.where("randomID").is(4));
			logger.info("-----------  After addCriteria-----------");
			radomNumber = mongoTemplate.findOne(query, RandomNumber.class);
			logger.info("Employee Invoice number ----------->"+radomNumber.getEmployeeinvoicenumber());
			return radomNumber;
		}catch(Exception e) {
			e.printStackTrace();
			return radomNumber;
		}finally {
			
		}	
	}
	
	@Override
	public boolean updateEmployeeRandamNumber(RandomNumber rn) {
		logger.info("current Employee invoice number -->"+rn.getEmployeeinvoicenumber());	
		Query query = new Query();
	    query.addCriteria(Criteria.where("randomID").is(4));
		Update update = new Update();
		update.set("employeeinvoicenumber", rn.getEmployeeinvoicenumber()+1);			
		mongoTemplate.updateFirst(query, update, RandomNumber.class);//(query, RandamNumber.class);
		return true;//mongoTemplate.find(query, RandamNumber.class);//(RandamNumber.class);
	}
	
	
	//Category and product RandomNumber Getting
	@Override
	public RandomNumber getCategoryRandomNumber() {
		RandomNumber radomNumber=null;
		try {
			logger.info("----------- Inside getcategory and prod RandamNumber-----------");
			Query query = new Query();
			logger.info("-----------  Before addCriteria-----------");
		    query.addCriteria(Criteria.where("randomID").is(3));
			logger.info("-----------  After addCriteria-----------");
			radomNumber = mongoTemplate.findOne(query, RandomNumber.class);
			logger.info("category and prod Invoice number ----------->"+radomNumber.getCategoryinvoicenumber());
			logger.info("category and prod Invoice code ----------->"+radomNumber.getCategoryinvoicecode());
			return radomNumber;
		}catch(Exception e) {
			e.printStackTrace();
			return radomNumber;
		}finally {
			
		}	
	}
	
	@Override
	public boolean updateCategoryRandamNumber(RandomNumber rn,int num) {
		logger.info("current category and prod invoice number -->"+rn.getCategoryinvoicenumber());	
		logger.info("Number for category -->"+num);
		Query query = new Query();
	    query.addCriteria(Criteria.where("randomID").is(3));
		Update update = new Update();
		if(num == 1) {
			update.set("categoryinvoicenumber", rn.getCategoryinvoicenumber()+1);
		}else if(num == 2) {
			update.set("productinvoicenumber", rn.getProductinvoicenumber()+1);
		}
		mongoTemplate.updateFirst(query, update, RandomNumber.class);//(query, RandamNumber.class);
		return true;//mongoTemplate.find(query, RandamNumber.class);//(RandamNumber.class);
	}
}
