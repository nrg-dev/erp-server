package com.erp.service;

import org.springframework.beans.factory.annotation.Autowired;

//import org.springframework.beans.factory.annotation.Autowire;

//import javax.enterprise.inject.Produces;

 import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.erp.bo.ErpBo;
import com.erp.dto.Member;
import com.erp.dto.Purchase;
import com.erp.mongo.dal.CustomerDAL;
import com.erp.mongo.dal.PurchaseDAL;
import com.erp.service.PurchaseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ggl.mongo.model.Customer;
import com.ggl.mongo.model.PurchaseOrder;
import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;
import com.sun.xml.txw2.Document;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
@RestController
@RequestMapping(value="/purchase")
public class PurchaseService implements Filter{
	
	public static final Logger logger = LoggerFactory.getLogger(PurchaseService.class);
		
	
	@Autowired
	ErpBo erpBo;


	//private final RandamNumberRepository randamNumberRepository;


	List<String> publicfiles = new ArrayList<String>();
	List<String> privatefiles = new ArrayList<String>();
	List<String> ownfiles = new ArrayList<String>();
	List<String> minifiles = new ArrayList<String>();
	
	private final PurchaseDAL purchasedal;

	public PurchaseService(PurchaseDAL purchasedal) {
		this.purchasedal = purchasedal;
	}


	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

	    HttpServletResponse response = (HttpServletResponse) res;
	    response.setHeader("Access-Control-Allow-Origin", "*");
	    response.setHeader("Access-Control-Allow-Credentials", "true");
	    response.setHeader("Access-Control-Allow-Methods", "POST,PUT, GET, OPTIONS, DELETE");
	    response.setHeader("Access-Control-Max-Age", "3600");
	    response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, remember-me");
	    chain.doFilter(req, res);
	}

	@Override
	public void init(FilterConfig filterConfig) {
	}

	@Override
	public void destroy() {
	}
	
	
	
      // Save
		@CrossOrigin(origins = "http://localhost:8080")
		@RequestMapping(value="/save",method=RequestMethod.POST)
		public ResponseEntity<?>  savePurchase(@RequestBody String purchsearray) {
			System.out.println("--------save savePurchase-------------");
			Purchase purchase=null;
			try {	   
				 purchase=new Purchase();

				//for(Purchase p:purchsearray) {
					System.out.println(purchsearray);
				//}
					PurchaseOrder po = new PurchaseOrder();
					System.out.println("1");
					//JSON.parse(purchsearray); 
					JSONArray value = new JSONArray(purchsearray);
					po.setPurchaseorder(value);
					
					
					//po.setData(purchsearray);
					//po.setPo_order(purchsearray.toJson());
					System.out.println("2");

					//final Object myRestData  = JSON.parse(purchsearray.toString());

					//JSONObject rat = Rat.getJSONObject(purchsearray);
					//String newrat = rat.toString();

					//collection.insertOne(doc);


					//PurchaseOrder doc = PurchaseOrder.parse(purchsearray);
					//collection.insertOne(doc);

				    purchasedal.savePurchase(po);	
					System.out.println("3");

				//customer=  erpBo.saveCustomer(customer);	
					purchase.setStatus("success");
					
					//purchsearray="success";
				return new ResponseEntity<Purchase>(purchase, HttpStatus.CREATED);

			

		   }catch(Exception e) {
			   logger.info("Exception ------------->"+e.getMessage());
			   e.printStackTrace();
		   }
		   finally{
			   
		   }
		   return new ResponseEntity<Purchase>(purchase, HttpStatus.CREATED);
		}

	
	  // get
	 /* 
	  @CrossOrigin(origins = "http://localhost:8080")
	  
	  @RequestMapping(value="/get",method=RequestMethod.GET) public
	  ResponseEntity<?> geCustomer(String id) {
	  logger.info("------------- Inside getTempPublicTree-----------------");
	  List<Customer> responseList=null; try {
	  logger.info("-----------Inside getTempPublicTree Called----------");
	  responseList=customerdal.getCustomer(id);
	  
	  }catch(Exception e){ logger.info("Exception ------------->"+e.getMessage());
	  e.printStackTrace(); }finally{
	  
	  } return new ResponseEntity<List<Customer>>(responseList,
	  HttpStatus.CREATED);
	  
	  }*/
	  
	  
	  // update
	  
	/*
	 * @CrossOrigin(origins = "http://localhost:8080")
	 * 
	 * @RequestMapping(value="/update",method=RequestMethod.POST) public
	 * ResponseEntity<?> updateCustomer(@RequestBody Customer customer) { try {
	 * customer= customerdal.updateCustomer(customer); return new
	 * ResponseEntity<Customer>(customer, HttpStatus.CREATED);
	 * 
	 * }catch(Exception e) { logger.info("Exception ------------->"+e.getMessage());
	 * e.printStackTrace(); }finally{
	 * 
	 * } return new ResponseEntity<Customer>(customer, HttpStatus.CREATED); }
	 * 
	 */
	  
	  // load
	  
		ArrayList<String> res = new ArrayList<String>();
	  
	  @CrossOrigin(origins = "http://localhost:8080")	  
	  @GetMapping(value="/load",produces=MediaType.APPLICATION_JSON_VALUE) 
	  public  ResponseEntity<?> loadPurchase() {
	  logger.info("------------- Inside loadPurchase-----------------");
	  List<PurchaseOrder> responseList= new ArrayList<PurchaseOrder>(); 
	  try {
	  logger.info("-----------Inside loadPurchase Called----------");
	  responseList=purchasedal.loadPurchase(responseList);
	  for(PurchaseOrder po:responseList) {
		  System.out.println("product name-->"+po.getPurchaseorder());
		 // res.add(po.getPurchaseorder().toString());
	  }
	  System.out.println("list size--->"+responseList.size());
	  return new ResponseEntity<List<PurchaseOrder>>(responseList,
			  HttpStatus.CREATED);
	  }catch(Exception e){ logger.info("loadPurchase Exception ------------->"+e.getMessage());
	  e.printStackTrace(); }finally{
	  
	  } return new ResponseEntity<List<PurchaseOrder>>(responseList,
	  HttpStatus.CREATED);
	  
	  }
	  
	  // Remove
	  
	 /* 
	  @CrossOrigin(origins = "http://localhost:8080")
	  
	  @RequestMapping(value="/remove",method=RequestMethod.DELETE) public
	  ResponseEntity<?> removeCustomer(String id) {
	  
	  
	  try { 
	 // logger.info("-----------Before Calling  removeCustomer ----------");
	  customerdal.removeCustomer(id);
	  logger.info("-----------Successfully Called  removeCustomer ----------");
	  
	  }catch(Exception e){ 
	  
	  logger.info("Exception ------------->"+e.getMessage()); e.printStackTrace();
	  }finally{
	  
	  } return new ResponseEntity<String>("ok", HttpStatus.CREATED);
	  
	  }
	  
	 */

		
	
	
			
	
		
		
		
		
			
			
	
			
	
}
		
