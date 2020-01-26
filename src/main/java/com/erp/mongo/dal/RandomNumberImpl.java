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
	
	
	//---- Vendor RandomCade Getting ---
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
			return radomNumber;
		}catch(Exception e) {
			e.printStackTrace();
			return radomNumber;
		}finally {
			
		}	
	}
	
	@Override
	public boolean updateVendorRandamNumber(RandomNumber rn) {
		logger.info("current Vendor invoice number -->"+rn.getVendorinvoicenumber());		
		Query query = new Query();
	    query.addCriteria(Criteria.where("randomID").is(2));
		Update update = new Update();
		update.set("vendorinvoicenumber", rn.getVendorinvoicenumber()+1);			
		mongoTemplate.updateFirst(query, update, RandomNumber.class);//(query, RandamNumber.class);
		return true;//mongoTemplate.find(query, RandamNumber.class);//(RandamNumber.class);
	}
		
}
