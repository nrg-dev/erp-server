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
import com.erp.util.Email;
import com.erp.mongo.model.Customer;
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
		System.out.println("Save Vendor");
		//mongoTemplate.insert(customer);//(query, RandamNumber.class);
		mongoTemplate.save(vendor);
		vendor.setStatus("success");
		return vendor;
	}
	// get
	@Override
	public List<Vendor> getVendor(String primaryKey) {
		List<Vendor> list;
		Query query = new Query();
		query.addCriteria(Criteria.where("userID").is(Integer.valueOf(primaryKey)));
		list = mongoTemplate.find(query, Vendor.class);
		return list;
		//return mongoTemplate.find(query, Publictree.class);
	}
	
	
	// update
	@Override
	public Vendor updateVendor(Vendor vendor) {
		Update update = new Update();
		Query query = new Query();
		query.addCriteria(Criteria.where("vendorcode").is(vendor.getVendorcode()));
		update.set("vendorName", vendor.getVendorName());
		update.set("phoneNumber", vendor.getPhoneNumber());
		update.set("mobileNumber", vendor.getMobileNumber());
		update.set("country", vendor.getCountry());
		update.set("email", vendor.getEmail());
		update.set("city", vendor.getCity());
		update.set("address", vendor.getAddress());
		mongoTemplate.updateFirst(query, update, Vendor.class);
		return vendor;
	}
	
	// Load
	public List<Vendor> loadVendor(List<Vendor> list){
		list = mongoTemplate.findAll(Vendor.class);//.find(query, OwnTree.class);
		return list;
		
	}
	// revmoe 
	public Vendor removeVendor(String vendorcode) {
		Vendor response=null;
		Query query= new Query();
		query.addCriteria(Criteria.where("vendorcode").is(vendorcode));
		mongoTemplate.remove(query,Vendor.class);
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
