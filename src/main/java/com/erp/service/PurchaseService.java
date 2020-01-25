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
import com.fasterxml.jackson.core.type.TypeReference;
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
import com.erp.dto.Member;
import com.erp.dto.Purchase;
import com.erp.mongo.dal.CustomerDAL;
import com.erp.mongo.dal.PurchaseDAL;
import com.erp.service.PurchaseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ggl.mongo.model.Customer;
import com.ggl.mongo.model.POInvoiceDetails;
import com.ggl.mongo.model.PurchaseOrder;
import com.ggl.mongo.model.RandomNumber;
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
			JSONArray jsonArray = null;
			PurchaseOrder po=null;
			POInvoiceDetails podetails=null;
			ObjectMapper mapper = new ObjectMapper();
			try {	
				 System.out.println("Json -->"+purchsearray);
				 	/*purchase=new Purchase();
				 	po = new PurchaseOrder();
					System.out.println("Json array format-->"+purchsearray);
					//JSON.parse(purchsearray); 
					jsonArray = new JSONArray(purchsearray);
					po.setPurchaseorder(jsonArray);
				    purchasedal.savePurchase(po);	
					System.out.println("3");
					*/
				 
				 // Store into parent table to show in first data table view
				 RandomNumber randomnumber = purchasedal.getRandamNumber();	
				 System.out.println("Current random number-->"+randomnumber.getPoinvoicenumber());
				 //podetails.setStatus("Waiting");
                        /// one row add into main tale.
				 // end 
				 
				 JSONArray jsonArr = new JSONArray(purchsearray);
				    List<Purchase> dataList = new ArrayList<Purchase>();
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
								podetails.setInvoicenumber("INV001");// random table..
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
				    	
				      //  JSONObject jsonObj = jsonArr.getJSONObject(i);				       
					   // System.out.println(jsonObj.getString("category"));
						//System.out.println(jsonObj.getString("productName"));
						//System.out.println(item.getQuantity());
						//System.out.println(item.getUnitPrice());
						//System.out.println(item.getNetAmount());
						
				    }  
				    	else {
			    			System.out.println("Outer Null....");
			    		}
				    }
				    
				    //purchasedal.savePurchase(podetails);
				    System.out.println("Service call start.....");
					purchase.setStatus("success");					
					return new ResponseEntity<Purchase>(purchase, HttpStatus.CREATED);

			  }
			
			catch(NullPointerException ne) {
				purchase=new Purchase();
			    System.out.println("Inside null pointer exception ....");
				purchase.setStatus("success");					

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
	  List<PurchaseOrder> response= new ArrayList<PurchaseOrder>();
	  List<Purchase> responseList= new ArrayList<Purchase>();
	 // List<JSONArray> res= new ArrayList<JSONArray>(); 
	  Purchase purchase;
	  
	  try {
	  logger.info("-----------Inside loadPurchase Called----------");
	  response=purchasedal.loadPurchase(response);
	  int lenth = response.size();
	  System.out.println("List size-->"+lenth);
	  int i=0;
			/*
			 * for(PurchaseOrder po:response) {
			 * 
			 * }
			 */
      ObjectMapper mapper = new ObjectMapper();

	  for(PurchaseOrder po:response) {
		 
		  System.out.println("lenth-->"+po.getPurchaseorder().length());
		  System.out.println("value-->"+po.getPurchaseorder().get(i));
		   purchase = new Purchase();
			  String json= String.valueOf(po.getPurchaseorder().get(i));

           List<Purchase> jsonlist = Arrays.asList(mapper.readValue(json, Purchase[].class));

		 // JSONArray jsonArr = new JSONArray(po.getPurchaseorder().get(i));
		  System.out.println("\nJSON array to List of objects");
		  for(Purchase item : jsonlist){
			  System.out.println("For Loop");
				System.out.println(item.getNetAmount());
	            purchase.setNetAmount(item.getNetAmount());

			}
		  
		  responseList.add(purchase);
				/*
				 * ppl2.forEach(name -> { System.out.println(name); });
				 */
		 
		  
		  //ppl2.stream().forEach(x -> 
          
        //  System.out.println(x.getNetAmount()));
          
		   // for (int i = 0; i < jsonArr.length(); i++) {
		     //   JSONObject jsonObj = jsonArr.getJSONObject(i);
		       
		      //  purchase.setQuantity(jsonObj.getString("quantity"));
//		        data.setTitle(jsonObj.getString("title"));
//		        data.setDescription(jsonObj.getString("description"));
//		        data.setUserId(jsonObj.getString("user_id"));
		        responseList.add(purchase);
		 //   }
		    
				/*
				 * for(int j=0;j<=po.getPurchaseorder().length();i++) { purchase = new
				 * Purchase(); System.out.println("test..");
				 * purchase.setNetAmount(po.getPurchaseorder().get(i).toString());
				 * responseList.add(purchase); }
				 */
	 // System.out.println(po.getPurchaseorder());
		 // res.add(po.getPurchaseorder());
		  i++;
	  }
	  
			/*
			 * for(int j=0;j<response.length();i++) { System.out.println("test.."); }
			 */
	//  System.out.println("list size--->"+res.size());
	  return new ResponseEntity<List<Purchase>>(responseList, HttpStatus.CREATED);
	 // return ResponseEntity.ok(res);
	  
	  }catch(Exception e){ logger.info("loadPurchase Exception ------------->"+e.getMessage());
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
		
