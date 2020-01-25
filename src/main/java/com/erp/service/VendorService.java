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
import com.erp.mongo.dal.VendorDAL;
import com.erp.service.VendorService;
import com.ggl.mongo.model.Customer;
import com.ggl.mongo.model.Vendor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
@RestController
@RequestMapping(value="/vendor")
public class VendorService implements Filter{
	
	public static final Logger logger = LoggerFactory.getLogger(VendorService.class);
		
	
	@Autowired
	ErpBo erpBo;


	//private final RandamNumberRepository randamNumberRepository;



	
	private final VendorDAL vendordal;

	public VendorService(VendorDAL vendordal) {
		//this.randamNumberDAL = randamNumberDAL;
		this.vendordal = vendordal;
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
		public ResponseEntity<?>  saveVendor(@RequestBody Vendor vendor) {
			System.out.println("--------save customer-------------");
			try {	   
				vendor=  vendordal.saveVendor(vendor);					
				return new ResponseEntity<Vendor>(vendor, HttpStatus.CREATED);			

		   }catch(Exception e) {
			   logger.info("Exception ------------->"+e.getMessage());
			   e.printStackTrace();
		   }
		   finally{
			   
		   }
		   return new ResponseEntity<Vendor>(vendor, HttpStatus.CREATED);
		}


		// get
		@CrossOrigin(origins = "http://localhost:8080")
		@RequestMapping(value="/get",method=RequestMethod.GET)
		public ResponseEntity<?> geVendor(String id)
		{
			logger.info("------------- Inside get Vendor -----------------");
			List<Vendor> responseList=null;
		   try {
				logger.info("-----------Inside get Vendor Called----------");
				responseList=vendordal.getVendor(id);				
				}catch(Exception e){
				logger.info("Exception ------------->"+e.getMessage());
				e.printStackTrace();
			}finally{
				
			}
			return new ResponseEntity<List<Vendor>>(responseList, HttpStatus.CREATED);
		}
		
		// update	
		@CrossOrigin(origins = "http://localhost:8080")
		@RequestMapping(value="/update",method=RequestMethod.POST)
		public ResponseEntity<?>  updateCustomer(@RequestBody Vendor vendor) {
	   try 
	   {	   
		   vendor=  vendordal.updateVendor(vendor);				  
		  return new ResponseEntity<Vendor>(vendor, HttpStatus.CREATED);
		   
	   }catch(Exception e) {
		   logger.info("Exception ------------->"+e.getMessage());
		   e.printStackTrace();
	   	}finally{
		   
	   	}
	  	return new ResponseEntity<Vendor>(vendor, HttpStatus.CREATED);
		}				

		 // load
		@CrossOrigin(origins = "http://localhost:8080")
		@RequestMapping(value="/load",method=RequestMethod.GET)
		public ResponseEntity<?> loadVendor()
		{
			logger.info("------------- Inside getTempOwnTree-----------------");
			List<Vendor> responseList=null;
		   try {
				logger.info("-----------Inside getTempOwnTree Called----------");
				responseList=vendordal.loadVendor(responseList);
				
				}catch(Exception e){
				logger.info("Exception ------------->"+e.getMessage());
				e.printStackTrace();
			}finally{
				
			}
			return new ResponseEntity<List<Vendor>>(responseList, HttpStatus.CREATED);
	
		}

		// Remove
		@CrossOrigin(origins = "http://localhost:8080")
		@RequestMapping(value="/remove",method=RequestMethod.DELETE)
		public ResponseEntity<?> removeCustomer(String id)
		{
		   try {
				logger.info("-----------Before Calling  removeCustomer ----------");
				vendordal.removeVendor(id);
				logger.info("-----------Successfully Called  removeCustomer ----------");
			
				}catch(Exception e){ 
				logger.info("Exception ------------->"+e.getMessage());
				e.printStackTrace();
			}finally{
				
			}
			return new ResponseEntity<String>("ok", HttpStatus.CREATED);

		}
	    
		

		
	
	
			
	
		
		
		
		
			
			
	
			
	
}
		
