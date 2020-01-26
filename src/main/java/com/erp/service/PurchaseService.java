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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.erp.dto.Purchase;
import com.erp.mongo.dal.PurchaseDAL;
import com.erp.mongo.dal.RandomNumberDAL;
import com.erp.mongo.model.POInvoice;
import com.erp.mongo.model.POInvoiceDetails;
import com.erp.mongo.model.PurchaseOrder;
import com.erp.mongo.model.RandomNumber;
import com.erp.service.PurchaseService;

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


	
	
	private final PurchaseDAL purchasedal;
	private final RandomNumberDAL randomnumberdal;

	public PurchaseService(PurchaseDAL purchasedal,RandomNumberDAL randomnumberdal) {
		this.purchasedal = purchasedal;
		this.randomnumberdal = randomnumberdal;
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
			POInvoice poinvoice=null;
			POInvoiceDetails podetails=null;
			RandomNumber randomnumber=null;
			try {
				ObjectMapper mapper = new ObjectMapper();
				 System.out.println("Json -->"+purchsearray);		
				 
				 // Store into parent table to show in first data table view
				 randomnumber = randomnumberdal.getRandamNumber();	
				 System.out.println("PO Invoice random number-->"+randomnumber.getPoinvoicenumber());
				 System.out.println("PO Invoice random code-->"+randomnumber.getPoinvoicecode());
                 String invoice = randomnumber.getPoinvoicecode() + randomnumber.getPoinvoicenumber();
                 System.out.println("Invoice number -->"+invoice);
                
				 
				 //podetails.setStatus("Waiting");
                        /// one row add into main tale.
				 // end 
				 
				 JSONArray jsonArr = new JSONArray(purchsearray);
				    for (int i = 0; i < jsonArr.length(); i++) {
				    	System.out.println("Loop 1...."+i);
				    	if(jsonArr.optJSONArray(i)!=null) {  		
				    	JSONArray arr2 = jsonArr.optJSONArray(i);				    	
				    	for (int j = 0; j < arr2.length(); j++) {
				    		System.out.println("Loop 2...."+j);
				    		if(arr2.getJSONObject(j)!=null) {
				    			JSONObject jObject = arr2.getJSONObject(j);
								System.out.println(jObject.getString("productName"));
								System.out.println(jObject.getString("category"));
								podetails = new POInvoiceDetails();
								podetails.setInvoicenumber(invoice);// random table..
								podetails.setCategory(jObject.getString("category"));
								podetails.setItemname(jObject.getString("productName"));
								podetails.setDescription(jObject.getString("description"));
								podetails.setUnitprice(jObject.getString("unitPrice"));
								podetails.setQty(jObject.getString("quantity"));
								podetails.setSubtotal(jObject.getString("netAmount"));
								purchasedal.savePurchase(podetails);
				    		}
				    		else {
				    			System.out.println("Null....");
				    		}
				    		

				       }
			    }  
				    	else {
			    			System.out.println("Outer Null....");
			    		}
				    }
				     poinvoice=new POInvoice();
				     LocalDateTime localDateTime = LocalDateTime.now();
				     LocalTime localTime = localDateTime.toLocalTime();
				     poinvoice.setInvoicedate(localTime);
	                 poinvoice.setInvoicenumber(invoice);
	                 poinvoice.setStatus("Waiting");
	                 poinvoice.setVendorname("Alex Ubalton");
	                 poinvoice.setTotalqty(100);
	                 poinvoice.setTotalprice(1000000);
	                 purchasedal.savePOInvoice(poinvoice);
				    System.out.println("Service call start.....");
					purchase.setStatus("success");					
					return new ResponseEntity<Purchase>(purchase, HttpStatus.CREATED);

			  }
			
			catch(NullPointerException ne) {
				purchase=new Purchase();
			    System.out.println("Inside null pointer exception ....");
				purchase.setStatus("success");					
				 boolean status = randomnumberdal.updateRandamNumber(randomnumber);	

				return new ResponseEntity<Purchase>(purchase, HttpStatus.CREATED);

			   }
			catch(Exception e) {
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
	  
		//ArrayList<String> res = new ArrayList<String>();
		ArrayList<JSONArray> res= new ArrayList<JSONArray>(); 

	  @CrossOrigin(origins = "http://localhost:8080")	  
	  @GetMapping(value="/load",produces=MediaType.APPLICATION_JSON_VALUE) 
	  public  ResponseEntity<?> loadPurchase() {
	  logger.info("------------- Inside loadPurchase-----------------");
	  List<POInvoice> response= new ArrayList<POInvoice>();
	  List<Purchase> responseList= new ArrayList<Purchase>();
	  try {
	  logger.info("-----------Inside loadPurchase Called----------");
	  response=purchasedal.loadPurchase(response);	 
	  return new ResponseEntity<List<POInvoice>>(response, HttpStatus.CREATED);
	  
	  }catch(Exception e)
	  { logger.info("loadPurchase Exception ------------->"+e.getMessage());
	  e.printStackTrace(); }finally{
	  
	  } 
	  return new ResponseEntity<List<Purchase>>(responseList,HttpStatus.CREATED);
  
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
		
