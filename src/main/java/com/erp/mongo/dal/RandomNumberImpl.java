package com.erp.mongo.dal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

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
	public RandomNumber getRandamNumber(int i) {
		logger.info("getRandamNumber");
		RandomNumber radomNumber = null;
		Query query = null;
		try {
			query = new Query();
			query.addCriteria(Criteria.where("randomID").is(i));
			logger.info("Before Select Random");
			radomNumber = mongoTemplate.findOne(query, RandomNumber.class);
			logger.info("After Select Random");
			logger.debug("Random Number-->"+radomNumber);
			return radomNumber;
		} catch (Exception e) {
			logger.error("Exception-->"+e.getMessage());
			return radomNumber;
		} finally {

		}

	}

	@Override
	public boolean updateRandamNumber(RandomNumber rn,int id) {
		logger.info("updateRandamNumber");
		logger.info("Code | Number -->" + rn.getCode() + "|" +rn.getNumber());
		Query query = new Query();
		query.addCriteria(Criteria.where("randomID").is(id));
		Update update = new Update();
		update.set("number", rn.getNumber() + 1);
		mongoTemplate.updateFirst(query, update, RandomNumber.class);
		return true;
	}

	@Override
	public boolean updateSalesRandamNumber(RandomNumber rn) {
		logger.info("updateSalesRandamNumber");
		logger.debug("Code | Number -->" + rn.getCode() + "|" +rn.getNumber());
		Query query = new Query();
		query.addCriteria(Criteria.where("randomID").is(7));
		Update update = new Update();
		update.set("number", rn.getNumber() + 1);
		mongoTemplate.updateFirst(query, update, RandomNumber.class);
		return true;
	}

	// ---- Vendor and customer RandomCade Getting ---
	@Override
	public RandomNumber getVendorRandamNumber() {
		logger.info("getVendorRandamNumber");
		RandomNumber radomNumber = null;
		try {
			logger.info("Inside getVendorRandamNumber-----------");
			Query query = new Query();
			logger.info("-----------  Before addCriteria-----------");
			query.addCriteria(Criteria.where("randomID").is(3));
			logger.info("-----------  After addCriteria-----------");
			radomNumber = mongoTemplate.findOne(query, RandomNumber.class);
			logger.debug("Code-->" + radomNumber.getCode());
			logger.debug("Number-->" + radomNumber.getNumber());
			return radomNumber;
		} catch (Exception e) {
			e.printStackTrace();
			return radomNumber;
		} finally {

		}
	}
	
	// ---- Vendor and customer RandomCade Getting ---
		@Override
		public RandomNumber getCustomerRandamNumber() {
			logger.info("getCustomerRandamNumber");
			RandomNumber radomNumber = null;
			try {
				logger.info("----------- Inside getCustomerRandamNumber-----------");
				Query query = new Query();
				logger.info("-----------  Before addCriteria-----------");
				query.addCriteria(Criteria.where("randomID").is(2));
				logger.info("-----------  After addCriteria-----------");
				radomNumber = mongoTemplate.findOne(query, RandomNumber.class);
				logger.debug("Code-->" + radomNumber.getCode());
				logger.debug("Number-->" + radomNumber.getNumber());
				return radomNumber;
			} catch (Exception e) {
				e.printStackTrace();
				return radomNumber;
			} finally {

			}
		}

	@Override
	public boolean updateVendorRandamNumber(RandomNumber rn, int num) {
		logger.info("updateVendorRandamNumber");
		logger.debug("Code | Number -->" + rn.getCode() + "|" +rn.getNumber());
		Query query = new Query();
		Update update = new Update();
		if (num == 1) {
			query.addCriteria(Criteria.where("randomID").is(3));
			update.set("number", rn.getNumber() + 1);
		} else if (num == 2) {
			query.addCriteria(Criteria.where("randomID").is(2));
			update.set("number", rn.getNumber() + 1);
		}
		mongoTemplate.updateFirst(query, update, RandomNumber.class);// (query, RandamNumber.class);
		return true;// mongoTemplate.find(query, RandamNumber.class);//(RandamNumber.class);
	}

	// ---- Employee RandomCode Getting ---
	@Override
	public RandomNumber getEmployeeRandamNumber() {
		logger.info("getEmployeeRandamNumber");
		RandomNumber radomNumber = null;
		try {
			logger.info("----------- Inside getEmployeeRandamNumber-----------");
			Query query = new Query();
			logger.info("-----------  Before addCriteria-----------");
			query.addCriteria(Criteria.where("randomID").is(1));
			logger.info("-----------  After addCriteria-----------");
			radomNumber = mongoTemplate.findOne(query, RandomNumber.class);
			logger.debug("Number-->" + radomNumber.getNumber());
			return radomNumber;
		} catch (Exception e) {
			e.printStackTrace();
			return radomNumber;
		} finally {

		}
	}

	@Override
	public boolean updateEmployeeRandamNumber(RandomNumber rn) {
		logger.info("updateEmployeeRandamNumber");
		logger.info("Code | Number -->" + rn.getCode() + "|" +rn.getNumber());
		Query query = new Query();
		query.addCriteria(Criteria.where("randomID").is(1));
		Update update = new Update();
		update.set("number", rn.getNumber() + 1);
		mongoTemplate.updateFirst(query, update, RandomNumber.class);// (query, RandamNumber.class);
		return true;// mongoTemplate.find(query, RandamNumber.class);//(RandamNumber.class);
	}

	// Category and product RandomNumber Getting
	@Override
	public RandomNumber getCategoryRandomNumber(int i) {
		logger.info("getCategoryRandomNumber");
		RandomNumber radomNumber = null;
		try {
			logger.info("----------- Inside getcategory and prod RandamNumber-----------");
			Query query = new Query();
			logger.info("-----------  Before addCriteria-----------");
			if(i == 1) {
				// Category
				query.addCriteria(Criteria.where("randomID").is(5));
			}else if(i == 2) {
				// Product
				query.addCriteria(Criteria.where("randomID").is(4));
			}
			logger.info("-----------  After addCriteria-----------");
			radomNumber = mongoTemplate.findOne(query, RandomNumber.class);
			logger.info("Code-->" + radomNumber.getCode());
			logger.info("Number-->" + radomNumber.getNumber());
			return radomNumber;
		} catch (Exception e) {
			e.printStackTrace();
			return radomNumber;
		} finally {

		}
	}

	@Override
	public boolean updateCategoryRandamNumber(RandomNumber rn, int num) {
		logger.info("updateCategoryRandamNumber");
		logger.info("Code | Number -->" + rn.getCode() + "|" +rn.getNumber());
		logger.debug("Number for category -->" + num);
		Query query = new Query();
		Update update = new Update();
		if (num == 1) {
			query.addCriteria(Criteria.where("randomID").is(5));
			update.set("number", rn.getNumber() + 1);
		} else if (num == 2) {
			query.addCriteria(Criteria.where("randomID").is(4));
			update.set("number", rn.getNumber() + 1);
		}
		mongoTemplate.updateFirst(query, update, RandomNumber.class);// (query, RandamNumber.class);
		return true;// mongoTemplate.find(query, RandamNumber.class);//(RandamNumber.class);
	}

	// Discount RandomNumber Getting
	@Override
	public RandomNumber getdiscountRandamNumber() {
		logger.info("getdiscountRandamNumber");
		RandomNumber radomNumber = null;
		try {
			logger.info("----------- Inside discount RandamNumber-----------");
			Query query = new Query();
			logger.info("-----------  Before addCriteria-----------");
			query.addCriteria(Criteria.where("randomID").is(15));
			logger.info("-----------  After addCriteria-----------");
			radomNumber = mongoTemplate.findOne(query, RandomNumber.class);
			logger.debug("Code-->" + radomNumber.getCode());
			logger.debug("Number-->" + radomNumber.getNumber());
			return radomNumber;
		} catch (Exception e) {
			e.printStackTrace();
			return radomNumber;
		} finally {

		}
	}

	@Override
	public boolean updatediscountRandamNumber(RandomNumber rn) {
		logger.info("updatediscountRandamNumber");
		logger.debug("Code | Number -->" + rn.getCode() + "|" +rn.getNumber());
		Query query = new Query();
		query.addCriteria(Criteria.where("randomID").is(15));
		Update update = new Update();
		update.set("number", rn.getNumber() + 1);
		mongoTemplate.updateFirst(query, update, RandomNumber.class);// (query, RandamNumber.class);
		return true;// mongoTemplate.find(query, RandamNumber.class);//(RandamNumber.class);
	}

	@Override
	public RandomNumber getReturnRandamNumber(int i) {
		logger.info("getReturnRandamNumber");
		RandomNumber radomNumber = null;
		try {
			logger.info("----------- Inside getReturnRandamNumber-----------");
			Query query = new Query();
			logger.info("---------  Before addCriteria ---------");
			if(i==1) {
				query.addCriteria(Criteria.where("randomID").is(8));

			}else {
				query.addCriteria(Criteria.where("randomID").is(9));

			}
			logger.info("-----------  After addCriteria -----------");
			radomNumber = mongoTemplate.findOne(query, RandomNumber.class);
			logger.info("Code-->" + radomNumber.getCode());
			logger.info("Number-->" + radomNumber.getNumber());
			return radomNumber;
		} catch (Exception e) {
			e.printStackTrace();
			return radomNumber;
		} finally {

		}
	}

	@Override
	public boolean updatePOReturnRandamNumber(RandomNumber rn) {
		logger.info("updatePOReturnRandamNumber");
		logger.info("Code | Number -->" + rn.getCode() + "|" +rn.getNumber());
		Query query = new Query();
		query.addCriteria(Criteria.where("randomID").is(8));
		Update update = new Update();
		update.set("number", rn.getNumber() + 1);
		mongoTemplate.updateFirst(query, update, RandomNumber.class);
		return true;
	}

	@Override
	public boolean updateSalesReturnRandamNumber(RandomNumber rn) {
		logger.info("updateSalesReturnRandamNumber");
		logger.info("Code | Number -->" + rn.getCode() + "|" +rn.getNumber());
		Query query = new Query();
		query.addCriteria(Criteria.where("randomID").is(9));
		Update update = new Update();
		update.set("number", rn.getNumber() + 1);
		mongoTemplate.updateFirst(query, update, RandomNumber.class);
		return true;
	}

	// Category and product RandomNumber Getting
	@Override
	public RandomNumber getStockDamageRandomNumber() {
		logger.info("getStockDamageRandomNumber");
		RandomNumber radomNumber = null;
		try {
			logger.info("----------- Inside getStockDamageRandomNumber -----------");
			Query query = new Query();
			logger.info("-----------  Before addCriteria-----------");
			query.addCriteria(Criteria.where("randomID").is(7));
			logger.info("-----------  After addCriteria-----------");
			radomNumber = mongoTemplate.findOne(query, RandomNumber.class);
			logger.info("Code-->" + radomNumber.getCode());
			logger.info("Number-->" + radomNumber.getNumber());
			return radomNumber;
		} catch (Exception e) {
			e.printStackTrace();
			return radomNumber;
		} finally {

		}
	}

	@Override
	public boolean updateStockDamRandamNumber(RandomNumber rn, int temp) {
		logger.info("updateStockDamRandamNumber");
		logger.info("Code | Number -->" + rn.getCode() + "|" +rn.getNumber());
		Query query = new Query();
		Update update = new Update();
		if(temp == 1) {
			query.addCriteria(Criteria.where("randomID").is(7));
			//logger.info("current invoice number -->"+rn.getStockreturninvoicenumber());	
			//update.set("stockreturninvoicenumber", rn.getNumber()+1);			
		}else if(temp == 2) {
			query.addCriteria(Criteria.where("randomID").is(14));
			logger.info("Number-->"+rn.getNumber());		
			update.set("number", rn.getNumber()+1);			
		}
		mongoTemplate.updateFirst(query, update, RandomNumber.class);
		return true;
	}
	
	
	@Override
	public RandomNumber getStockRandamNumber() {
		logger.info("getStockRandamNumber");
		RandomNumber radomNumber = null;
		try {
			logger.info("----------- Inside getStockRandamNumber -----------");
			Query query = new Query();
			query.addCriteria(Criteria.where("randomID").is(8));
			logger.info("-----------  After addCriteria-----------");
			radomNumber = mongoTemplate.findOne(query, RandomNumber.class);
			logger.info("Code-->" + radomNumber.getCode());
			logger.info("Number-->" + radomNumber.getNumber());
			return radomNumber;
		} catch (Exception e) {
			e.printStackTrace();
			return radomNumber;
		} finally {

		}
	}
	
	@Override
	public boolean updateStockRandamNumber(RandomNumber rn, int temp) {
		logger.info("updateStockRandamNumber");
		logger.debug("Number-->" + rn.getNumber());
		Query query = new Query();
		Update update = new Update();
		if(temp == 1) {
			query.addCriteria(Criteria.where("randomID").is(8));
			logger.debug("current invoice number -->"+rn.getNumber());	
			update.set("number", rn.getNumber()+1);			
		}else if(temp == 2) {
			query.addCriteria(Criteria.where("randomID").is(8));
			logger.debug("current invoice number -->"+rn.getNumber());		
			update.set("number", rn.getNumber()+1);			
		}
		mongoTemplate.updateFirst(query, update, RandomNumber.class);
		return true;
	}

}
