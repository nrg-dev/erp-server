package com.erp.mongo.dal;

import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
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
import com.ggl.mongo.model.Customer;
import com.ggl.mongo.model.POInvoiceDetails;
import com.ggl.mongo.model.PurchaseOrder;
import com.ggl.mongo.model.RandomNumber;


@Repository
public class PurchaseImpl implements PurchaseDAL {
	
	public static final Logger logger = LoggerFactory.getLogger(PurchaseImpl.class);


	@Autowired
	private MongoTemplate mongoTemplate;

	/*
	 * @Autowired ErpBo investmentBo1;
	 */

	
	
	@Override
	public RandomNumber getRandamNumber() {
		RandomNumber radamNumber=null;
		try {
			logger.info("----------- Inside getRandamNumber-----------");
			Query query = new Query();
			logger.info("-----------  Before addCriteria-----------");
		    query.addCriteria(Criteria.where("type").is("invoice"));

			logger.info("-----------  After addCriteria-----------");
			radamNumber = mongoTemplate.findOne(query, RandomNumber.class);		
			logger.info("Invoice number ----------->"+radamNumber.getPoinvoicenumber());

			return radamNumber;//mongoTemplate.find(query, RandamNumber.class);//(RandamNumber.class);
		}catch(Exception e) {
			e.printStackTrace();
			return radamNumber;


		}finally {
			
		}
		
	}
	
	
	@Override
	public String updateRandamNumber(RandomNumber rn) {
		logger.info("currentqueueNumber"+rn.getPoinvoicenumber());	
		
		Query query = new Query();
	    query.addCriteria(Criteria.where("poinvoicenumber").is(1));
			Update update = new Update();
			update.set("poinvoicenumber", rn.getPoinvoicenumber()+1);			
		mongoTemplate.updateFirst(query, update, RandomNumber.class);//(query, RandamNumber.class);
		return "";//mongoTemplate.find(query, RandamNumber.class);//(RandamNumber.class);
	}
	
	

	// save
	
	@Override
	public POInvoiceDetails savePurchase(POInvoiceDetails purchaseorder) {
		//mongoTemplate.insert(customer);//(query, RandamNumber.class);
		System.out.println("Before save Invoice");
		mongoTemplate.save(purchaseorder);
		System.out.println("After save Invoice");
		//po.setStatus("success");
		return purchaseorder;
	}
	
	
	
	/*
	 * @Override public PurchaseOrder savePurchase(PurchaseOrder purchaseorder) {
	 * //mongoTemplate.insert(customer);//(query, RandamNumber.class);
	 * mongoTemplate.save(purchaseorder); //po.setStatus("success"); return
	 * purchaseorder; }
	 */
	
	
	public List<PurchaseOrder> loadPurchase(List<PurchaseOrder> list){
		//List<PurchaseOrder> 
		list =  mongoTemplate.findAll(PurchaseOrder.class);//.find(query, OwnTree.class); return
		return list;
			  
		} 
	
	/*
	 * // get
	 * 
	 * @Override public List<Customer> getCustomer(String primaryKey) {
	 * List<Customer> list; Query query = new Query();
	 * query.addCriteria(Criteria.where("userID").is(Integer.valueOf(primaryKey)));
	 * list = mongoTemplate.find(query, Customer.class); return list; //return
	 * mongoTemplate.find(query, Publictree.class); }
	 * 
	 * 
	 * // update
	 * 
	 * @Override public Customer updateCustomer(Customer customer) { Update update =
	 * new Update(); Query query = new Query();
	 * query.addCriteria(Criteria.where("id").is(customer.getId()));
	 * //update.set("userQueueStatus", "OUT"); //update.set("queueNumber", 0);
	 * //update.set("userstatus", "CLOSED"); mongoTemplate.updateFirst(query,
	 * update, Customer.class); return customer; }
	 * 
	 * public List<Customer> loadPurchase(List<PurchaseOrder> list){ list =
	 * mongoTemplate.findAll(PurchaseOrder.class);//.find(query, OwnTree.class); return
	 * list;
	 * 
	 * } public void removeCustomer(String customer) {
	 * mongoTemplate.remove(customer);
	 * 
	 * }
	 */
	
		
	
		
		
		
		
		

		
		
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
