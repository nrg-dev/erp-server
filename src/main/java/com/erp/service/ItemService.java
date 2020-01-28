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
import com.erp.mongo.dal.ItemDAL;
import com.erp.mongo.dal.RandomNumberDAL;
import com.erp.mongo.model.Item;
import com.erp.mongo.model.RandomNumber;
import com.erp.service.ItemService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
@RestController
@RequestMapping(value="/item")
public class ItemService implements Filter{
	
	public static final Logger logger = LoggerFactory.getLogger(ItemService.class);
		
	
	@Autowired
	ErpBo erpBo;


	private final ItemDAL itemdal;
	private final RandomNumberDAL randomnumberdal;

	public ItemService(ItemDAL itemdal,RandomNumberDAL randomnumberdal) {
		this.itemdal = itemdal;
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
		@RequestMapping(value="/save",method=RequestMethod.POST) // you need to pass vendor info or vendor id and po date
		public ResponseEntity<?>  saveItem(@RequestBody Item item) {
			System.out.println("--------save savePurchase-------------");
			RandomNumber randomnumber=null;
			try {
				 
				 // Store into parent table to show in first data table view
				 randomnumber = randomnumberdal.getRandamNumber();	
				 System.out.println("Item random number-->"+randomnumber.getPoinvoicenumber());
				 System.out.println("Item random code-->"+randomnumber.getPoinvoicecode());
	             String invoice = randomnumber.getPoinvoicecode() + randomnumber.getPoinvoicenumber();
	             System.out.println("Item number -->"+invoice);
				 boolean status = randomnumberdal.updateRandamNumber(randomnumber);	
	
				return new ResponseEntity<Item>(item, HttpStatus.CREATED);
	
			  }
			
			catch(Exception e) {
			   logger.info("Exception ------------->"+e.getMessage());
			   e.printStackTrace();
		   }
			
		   finally{
			   
		   }
		   return new ResponseEntity<Item>(item, HttpStatus.CREATED);
		}

	  // load
	  @CrossOrigin(origins = "http://localhost:8080")	  
	  @GetMapping(value="/load",produces=MediaType.APPLICATION_JSON_VALUE) 
	  public  ResponseEntity<?> loadItem() {
	  logger.info("------------- Inside loadPurchase-----------------");
	  List<Item> itemlist= new ArrayList<Item>();
	  try {
	  logger.info("-----------Inside loadPurchase Called----------");
	  itemlist=itemdal.loadItem(itemlist);	 
	  return new ResponseEntity<List<Item>>(itemlist, HttpStatus.CREATED);
	  
	  }catch(Exception e)
	  { logger.info("loadPurchase Exception ------------->"+e.getMessage());
	  e.printStackTrace(); }finally{
	  
	  } 
	  return new ResponseEntity<List<Item>>(itemlist,HttpStatus.CREATED);
  
	  }
		  
	  // get	  
	  @CrossOrigin(origins = "http://localhost:8080")	  
	  @RequestMapping(value="/get",method=RequestMethod.GET) 
	  public  ResponseEntity<?> geItem(String id) {
	  logger.info("------------- Inside getTempPublicTree-----------------");
	  Item item=null;
	  try {
	  logger.info("-----------Inside getTempPublicTree Called----------");
	  item=itemdal.getItem(id);
	  
	  }catch(Exception e){ logger.info("Exception ------------->"+e.getMessage());
	  e.printStackTrace(); }finally{
	  
	  } return new ResponseEntity<Item>(item, HttpStatus.CREATED);
	  
	  }
	  
	  
	  // Update  	
	  @CrossOrigin(origins = "http://localhost:8080")	  
	  @RequestMapping(value="/update",method=RequestMethod.POST) public
	  ResponseEntity<?> updateCustomer(@RequestBody Item item) { try {
	  item = itemdal.updateItem(item); 
	  return new ResponseEntity<Item>(item, HttpStatus.CREATED);
	  
	  }catch(Exception e) 
	  { 
	  logger.info("Exception ------------->"+e.getMessage());
	  e.printStackTrace(); 
	  }finally{
	  
	  } return new ResponseEntity<Item>(item, HttpStatus.CREATED); }
	  
	  // Remove  	  
	  @CrossOrigin(origins = "http://localhost:8080")	  
	  @RequestMapping(value="/remove",method=RequestMethod.DELETE) public
	  ResponseEntity<?> removeItem(String id) {  	  
	  try { 
	  logger.info("-----------Before Calling  removeItem ----------");
	  itemdal.removeItem(id);
	  logger.info("-----------Successfully Called  removeItem ----------");	  
	  }catch(Exception e){ 
	  
	  logger.info("Exception ------------->"+e.getMessage()); e.printStackTrace();
	  }finally{
	  
	  } return new ResponseEntity<String>("ok", HttpStatus.CREATED);
	  
	  }
	  
	 

		
	
	
			
	
		
		
		
		
			
			
	
			
	
}
		
