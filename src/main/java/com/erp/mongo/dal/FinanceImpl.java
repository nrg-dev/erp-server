package com.erp.mongo.dal;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.erp.mongo.model.Customer;
import com.erp.mongo.model.PettyCash;
import com.erp.mongo.model.Vendor;

@Repository
public class FinanceImpl implements FinanceDAL {

	public static final Logger logger = LoggerFactory.getLogger(FinanceImpl.class);

	@Autowired
	private MongoTemplate mongoTemplate;

	// load customer and vendor name & code
	public ArrayList<String> loadCustomerVendorName() {
		ArrayList<String> list = new ArrayList<String>();
		List<Customer> customerlist = mongoTemplate.findAll(Customer.class);
		List<Vendor> vendorlist = mongoTemplate.findAll(Vendor.class);
		for (Customer customer : customerlist) {
			logger.debug("Customer name-->" + customer.getCustomerName());
			logger.debug("Customer code-->" + customer.getCustcode());
			list.add(customer.getCustomerName() + "-" + customer.getCustcode());
		}
		for (Vendor vendor : vendorlist) {
			logger.debug("Vendor name-->" + vendor.getVendorName());
			logger.debug("Vendor code-->" + vendor.getVendorcode());
			list.add(vendor.getVendorName() + "-" + vendor.getVendorcode());
		}
		return list;
	}

	// save
	public PettyCash save(PettyCash finance) {
		mongoTemplate.save(finance);
		return finance;
	}

	// load petty cash list
	public List<PettyCash> load() {
		List<PettyCash> pettycashlist = mongoTemplate.findAll(PettyCash.class);
		return pettycashlist;
	}

	public PettyCash updatePettyCash(PettyCash pettycash) {
		Update update = new Update();
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(pettycash.getId()));
		update.set("description", pettycash.getDescription());
		update.set("addedDate", pettycash.getAddedDate());
		update.set("type", pettycash.getType());
		update.set("toPerson", pettycash.getToPerson());
		update.set("totalAmount", pettycash.getTotalAmount());
		update.set("currency", pettycash.getCurrency());
		update.set("status", "Active");
		mongoTemplate.updateFirst(query, update, PettyCash.class);
		return pettycash;
	}

	public PettyCash removePettyCash(String id) {
		PettyCash response = null;
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(id));
		mongoTemplate.remove(query, PettyCash.class);
		return response;
	}

}
