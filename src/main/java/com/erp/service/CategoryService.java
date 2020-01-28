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
import com.erp.mongo.dal.CategoryDAL;
import com.erp.mongo.dal.PurchaseDAL;
import com.erp.mongo.dal.RandomNumberDAL;
import com.erp.mongo.model.Category;
import com.erp.mongo.model.POInvoice;
import com.erp.mongo.model.POInvoiceDetails;
import com.erp.mongo.model.RandomNumber;
import com.erp.service.CategoryService;
import com.erp.util.Custom;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
@RestController
@RequestMapping(value="/category")
public class CategoryService implements Filter{
	
	public static final Logger logger = LoggerFactory.getLogger(CategoryService.class);
		
		
	private final CategoryDAL categorydal;

	public CategoryService(CategoryDAL categorydal) {
		this.categorydal = categorydal;
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
		public ResponseEntity<?>  saveCategory(@RequestBody Category category) {
			System.out.println("-------- saveCategory-------------");
			try {
				System.out.println("Category name -->"+category.getName());

				 // Store into parent table to show in first data table view
                 categorydal.saveCategory(category);
                 category.setStatus("success");					
  				 return new ResponseEntity<Category>(category, HttpStatus.CREATED);

			  }
			catch(Exception e) {
			   logger.info("Exception ------------->"+e.getMessage());
			   e.printStackTrace();
		   }
			
		   finally{
			   
		   }
		   return new ResponseEntity<Category>(category, HttpStatus.CREATED);
		}

	  // Load
	  @CrossOrigin(origins = "http://localhost:8080")	  
	  @GetMapping(value="/load",produces=MediaType.APPLICATION_JSON_VALUE) 
	  public  ResponseEntity<?> loadCategory() {
	  logger.info("------------- Inside loadPurchase-----------------");
	  List<Category> categorylist= new ArrayList<Category>();
	  try {
	  logger.info("-----------Inside loadPurchase Called----------");
	  categorylist=categorydal.loadCategory(categorylist);	 
	  return new ResponseEntity<List<Category>>(categorylist, HttpStatus.CREATED);
	  
	  }catch(Exception e)
	  { logger.info("loadPurchase Exception ------------->"+e.getMessage());
	  e.printStackTrace(); }finally{
	  
	  } 
	  return new ResponseEntity<List<Category>>(categorylist,HttpStatus.CREATED);
  
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
		
