package com.erp.mongo.dal;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.erp.bo.ErpBo;
import com.erp.mongo.model.Customer;
import com.erp.mongo.model.Employee;
import com.erp.mongo.model.PettyCash;

@Repository
public class CustomerImpl implements CustomerDAL {

	public static final Logger logger = LoggerFactory.getLogger(CustomerImpl.class);

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	ErpBo investmentBo1;

	// save

	@Override
	public Customer saveCustomer(Customer customer) {
		logger.info("saveCustomer");
		mongoTemplate.save(customer);
		customer.setStatus("success");
		return customer;
	}

	// get
	@Override
	public List<Customer> getCustomer(String custcode) {
		List<Customer> list;
		Query query = new Query();
		query.addCriteria(Criteria.where("custcode").is(custcode));
		list = mongoTemplate.find(query, Customer.class);
		return list;
		// return mongoTemplate.find(query, Publictree.class);
	}

	// update
	@Override
	public Customer updateCustomer(Customer customer) {
		logger.debug("Customer Code -->"+customer.getCustcode());
		logger.debug("Customer Name -->"+customer.getCustomerName());
		logger.debug("Customer Phone -->"+customer.getPhoneNumber());
		logger.debug("Customer Country -->"+customer.getCountry());
		logger.debug("Customer City -->"+customer.getCity());
		logger.debug("Customer Address -->"+customer.getAddress());
		logger.debug("Customer Email -->"+customer.getEmail());
		logger.debug("Customer Base64 -->"+customer.getCustomerbase64());
		Update update = new Update();
		Query query = new Query();
		query.addCriteria(Criteria.where("custcode").is(customer.getCustcode()));
		update.set("custcode", customer.getCustcode());
		update.set("customerName", customer.getCustomerName());
		update.set("phoneNumber", customer.getPhoneNumber());
		update.set("mobileNumber", customer.getMobileNumber());
		update.set("country", customer.getCountry());
		update.set("email", customer.getEmail());
		update.set("city", customer.getCity());
		update.set("address", customer.getAddress());
		update.set("customerbase64", customer.getCustomerbase64());
		mongoTemplate.updateFirst(query, update, Customer.class);
		return customer;
	}

	// Load
	public List<Customer> loadCustomer(List<Customer> list) {
		 Query query = new Query();
		 query.with(new Sort(new Order(Direction.DESC, "custcode")));
  		 list = mongoTemplate.find(query,Customer.class);
		 //list = mongoTemplate.findAll(Customer.class);// .find(query, OwnTree.class);
		 return list;

	}

	// Remove
	public Customer removeCustomer(String custcode) {
		Customer response = null;
		Query query = new Query();
		query.addCriteria(Criteria.where("custcode").is(custcode));
		mongoTemplate.remove(query, Customer.class);
		return response;
	}

	

	

}
