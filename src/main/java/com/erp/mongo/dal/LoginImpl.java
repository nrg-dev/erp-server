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

import com.erp.bo.ErpBo;
import com.erp.dto.User;
import com.erp.model.UserLogin;
import com.erp.mongo.model.Login;
import com.erp.mongo.model.POInvoiceDetails;
import com.erp.mongo.model.StockDamage;
import com.erp.mongo.model.Vendor;

@Repository
public class LoginImpl implements LoginDAL {

	public static final Logger logger = LoggerFactory.getLogger(LoginImpl.class);

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public List<Login> userLogin(User user, List<Login> result) {
		Query query=null;
		logger.info("-------- Inside UserLogin Method Calling in Impl ---------");
		try {
			if(user.getId()==1) {
				query = new Query();
				query.addCriteria( new Criteria().andOperator(
						Criteria.where("username").is(user.getUsername()),
						Criteria.where("status").is("Active") ) );
				result = mongoTemplate.find(query, Login.class);
			}
			if(user.getId()==2){
				query.addCriteria( new Criteria().orOperator(
						Criteria.where("username").is(user.getUsername()),Criteria.where("status").is("Active"),
						Criteria.where("password").is(user.getPassword()) ) );
				
				result = mongoTemplate.find(query, Login.class);
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			
		}
		return result;
	}

	

	



}
