package com.erp.service;

import org.springframework.beans.factory.annotation.Autowired;

//import org.springframework.beans.factory.annotation.Autowire;

//import javax.enterprise.inject.Produces;

 import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import org.springframework.http.ResponseEntity;

import com.erp.bo.ErpBo;
import com.erp.dto.Member;
import com.erp.mongo.dal.CustomerDAL;
import com.erp.mongo.dal.RandomNumberDAL;
import com.erp.mongo.model.Customer;
import com.erp.mongo.model.RandomNumber;
import com.erp.mongo.model.Vendor;
import com.erp.service.CustomerService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
@RestController
@RequestMapping(value="/customer")
public class CustomerService implements Filter{
	
	public static final Logger logger = LoggerFactory.getLogger(CustomerService.class);
		
	
	@Autowired
	ErpBo erpBo;


	//private final RandamNumberRepository randamNumberRepository;


	List<String> publicfiles = new ArrayList<String>();
	List<String> privatefiles = new ArrayList<String>();
	List<String> ownfiles = new ArrayList<String>();
	List<String> minifiles = new ArrayList<String>();
	
	private final CustomerDAL customerdal;
	private final RandomNumberDAL randomnumberdal;
	Customer customer = null;

	public CustomerService(CustomerDAL customerdal,RandomNumberDAL randomnumberdal) {
		//this.randamNumberDAL = randamNumberDAL;
		this.customerdal = customerdal;
		this.randomnumberdal=randomnumberdal;
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
		public ResponseEntity<?>  saveCustomer(@RequestBody Customer customer) {
			logger.info("country name -->"+customer.getCountry());
			System.out.println("--------save customer-------------");
			RandomNumber randomnumber=null;
			try {
				randomnumber = randomnumberdal.getVendorRandamNumber();
				System.out.println("Customer Invoice random number-->"+randomnumber.getCustomerinvoicenumber());
				System.out.println("Customer Invoice random code-->"+randomnumber.getCustomerinvoicecode());
                String invoice = randomnumber.getCustomerinvoicecode() + randomnumber.getCustomerinvoicenumber();
                System.out.println("Invoice number -->"+invoice);
                
				customer.setCustcode(invoice);
				customer=  customerdal.saveCustomer(customer);
				if(customer.getStatus().equalsIgnoreCase("success")) {
					boolean status = randomnumberdal.updateVendorRandamNumber(randomnumber,2);
				}
				return new ResponseEntity<Customer>(customer, HttpStatus.CREATED);			

		   }catch(Exception e) {
			   customer.setStatus("failure");
			   logger.info("Exception ------------->"+e.getMessage());
			   e.printStackTrace();
		   }
		   finally{
			   
		   }
		   return new ResponseEntity<Customer>(customer, HttpStatus.CREATED);
		}


		 // get
		@CrossOrigin(origins = "http://localhost:8080")
		@RequestMapping(value="/get",method=RequestMethod.GET)
		public ResponseEntity<?> geCustomer(String id)
		{
			logger.info("------------- Inside getTempPublicTree-----------------");
			List<Customer> responseList=null;
		   try {
				logger.info("-----------Inside getTempPublicTree Called----------");
				responseList=customerdal.getCustomer(id);
				
				}catch(Exception e){
				logger.info("Exception ------------->"+e.getMessage());
				e.printStackTrace();
			}finally{
				
			}
			return new ResponseEntity<List<Customer>>(responseList, HttpStatus.CREATED);

		}
			
		
		// update	
		@CrossOrigin(origins = "http://localhost:8080")
		@RequestMapping(value="/update",method=RequestMethod.PUT)
		public ResponseEntity<?>  updateCustomer(@RequestBody Customer customer) {
	   try 
	   {
		  customer=  customerdal.updateCustomer(customer);				  
		  return new ResponseEntity<Customer>(customer, HttpStatus.CREATED);
		   
	   }catch(Exception e) {
		   logger.info("Exception ------------->"+e.getMessage());
		   e.printStackTrace();
	   	}finally{
		   
	   	}
	  	return new ResponseEntity<Customer>(customer, HttpStatus.CREATED);
		}

				

		 // load
		@CrossOrigin(origins = "http://localhost:8080")
		@RequestMapping(value="/load",method=RequestMethod.GET)
		public ResponseEntity<?> loadCustomer()
		{
			logger.info("------------- Inside getTempOwnTree-----------------");
			List<Customer> responseList=null;
		   try {
				logger.info("-----------Inside getTempOwnTree Called----------");
				responseList=customerdal.loadCustomer(responseList);
				
				}catch(Exception e){
				logger.info("Exception ------------->"+e.getMessage());
				e.printStackTrace();
			}finally{
				
			}
			return new ResponseEntity<List<Customer>>(responseList, HttpStatus.CREATED);
	
		}

		// Remove
		@CrossOrigin(origins = "http://localhost:8080")
		@RequestMapping(value="/remove",method=RequestMethod.DELETE)
		public ResponseEntity<?> removeCustomer(String custcode)
		{


		   try {
			   customer = new Customer();
				logger.info("-----------Before Calling  removeCustomer ----------");
				customerdal.removeCustomer(custcode);
				customer.setStatus("Success");
				logger.info("-----------Successfully Called  removeCustomer ----------");
			
				}catch(Exception e){ 
					customer.setStatus("failure");

				logger.info("Exception ------------->"+e.getMessage());
				e.printStackTrace();
			}finally{
				
			}
			return new ResponseEntity<Customer>(customer, HttpStatus.CREATED);

		}
	    
		

		
	
	
			
	
		
		
		
		
			
			
	
			
	
}
		
