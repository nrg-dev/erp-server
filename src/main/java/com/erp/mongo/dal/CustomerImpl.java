package com.erp.mongo.dal;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.core.query.Update;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;



//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.util.stream.Stream;

import com.erp.bo.ErpBo;
import com.erp.dto.Member;
import com.erp.model.UserDetail;
import com.erp.mongo.model.Customer;
import com.erp.mongo.model.Vendor;
import com.erp.util.Email;


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
		System.out.println("Save Customer");
		//mongoTemplate.insert(customer);//(query, RandamNumber.class);
		mongoTemplate.save(customer);
		customer.setStatus("success");
		return customer;
	}
	// get
	@Override
	public List<Customer> getCustomer(String primaryKey) {
		List<Customer> list;
		Query query = new Query();
		query.addCriteria(Criteria.where("userID").is(Integer.valueOf(primaryKey)));
		list = mongoTemplate.find(query, Customer.class);
		return list;
		//return mongoTemplate.find(query, Publictree.class);
	}
	
	
	// update
	@Override
	public Customer updateCustomer(Customer customer) {
		Update update = new Update();
		Query query = new Query();
		query.addCriteria(Criteria.where("custcode").is(customer.getCustcode()));
		update.set("customerName", customer.getCustomerName());
		update.set("phoneNumber", customer.getPhoneNumber());
		update.set("mobileNumber", customer.getMobileNumber());
		update.set("country", customer.getCountry());
		update.set("email", customer.getEmail());
		update.set("city", customer.getCity());
		update.set("address", customer.getAddress());
		mongoTemplate.updateFirst(query, update, Customer.class);
		return customer;
	}
	
	//Load
	public List<Customer> loadCustomer(List<Customer> list){
		list = mongoTemplate.findAll(Customer.class);//.find(query, OwnTree.class);
		return list;
		
	}
	
	//Remove
	public Customer removeCustomer(String custcode) {
		Customer response=null;
		Query query= new Query();
		query.addCriteria(Criteria.where("custcode").is(custcode));
		mongoTemplate.remove(query,Customer.class);
		return response;
	
	}
			
	
		
	
		
		
		
		
		

		
		
		// Server
		private String privatefiles="/home/ec2-user/GGL/PrivatePayment/";
		private String publicfiles="/home/ec2-user/GGL/PublicPayment/";
		private String ownfiles="/home/ec2-user/GGL/OwnPayment/";
		private String minifiles="/home/ec2-user/GGL/MiniPayment/";

		// Local
		/*private String privatefiles="E:\\temp\\PrivatePayment\\";
		private String publicfiles="E:\\temp\\PublicPayment\\";
		private String ownfiles="E:\\temp\\OwnPayment\\";*/

		
		private final Path publicrootLocation = Paths.get(publicfiles);
		private final Path privateRootLocation = Paths.get(privatefiles);
		private final Path ownRootLocation = Paths.get(ownfiles);
		private final Path minirootLocation = Paths.get(minifiles);

		
}
