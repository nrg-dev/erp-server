package com.erp.mongo.dal;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.erp.bo.ErpBo;
import com.erp.mongo.model.Customer;
import com.erp.mongo.model.Stock;
import com.erp.mongo.model.Vendor;

@Repository
public class VendorImpl implements VendorDAL {

	public static final Logger logger = LoggerFactory.getLogger(VendorImpl.class);

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	ErpBo investmentBo1;

	// save

	@Override
	public Vendor saveVendor(Vendor vendor) {
		logger.info("Save Vendor");
		mongoTemplate.save(vendor);
		vendor.setStatus("success");
		return vendor;
	}

	// Load
	public List<Vendor> loadVendor(List<Vendor> list) {
		 Query query = new Query();
		 query.with(new Sort(new Order(Direction.DESC, "vendorcode")));
		 query.addCriteria( new Criteria().orOperator(
					Criteria.where("status").is(""),Criteria.where("status").is(null),
					Criteria.where("status").is("Active") ));
  		 list = mongoTemplate.find(query,Vendor.class);
		 //list = mongoTemplate.findAll(Vendor.class);// .find(query, OwnTree.class);
		 return list;

	}

	// get
	@Override
	public List<Vendor> getVendor(String vendorcode) {
		List<Vendor> list;
		Query query = new Query();
		//query.addCriteria(Criteria.where("userID").is(Integer.valueOf(primaryKey)));
		query.addCriteria(Criteria.where("vendorcode").is(vendorcode));
		list = mongoTemplate.find(query, Vendor.class);
		return list;
		// return mongoTemplate.find(query, Publictree.class);
	}

	// update
	@Override
	public Vendor updateVendor(Vendor vendor) {
		Update update = new Update();
		Query query = new Query();
		query.addCriteria(Criteria.where("vendorcode").is(vendor.getVendorcode()));
		update.set("vendorName", vendor.getVendorName());
		update.set("phoneNumber", vendor.getPhoneNumber());
		update.set("country", vendor.getCountry());
		update.set("email", vendor.getEmail());
		update.set("city", vendor.getCity());
		update.set("address", vendor.getAddress());
		mongoTemplate.findAndModify(query, update,
				new FindAndModifyOptions().returnNew(true), Vendor.class);
		//mongoTemplate.updateFirst(query, update, Vendor.class);
		return vendor;
	}

	// revmoe
	public Vendor removeVendor(String vendorcode) {
		Vendor response = null;
		Query query = new Query();
		Update update = new Update();
		query.addCriteria(Criteria.where("vendorcode").is(vendorcode));
		update.set("status", "InActive");
		mongoTemplate.findAndModify(query, update,
				new FindAndModifyOptions().returnNew(true), Vendor.class);
		/*
		 * query.addCriteria(Criteria.where("vendorcode").is(vendorcode));
		 * mongoTemplate.remove(query, Vendor.class);
		 */
		return response;
	}



}
