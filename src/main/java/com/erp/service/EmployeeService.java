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
import com.erp.mongo.dal.EmployeeDAL;
import com.erp.mongo.model.Employee;
import com.erp.mongo.model.Vendor;
import com.erp.service.EmployeeService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
@RestController
@RequestMapping(value="/employee")
public class EmployeeService implements Filter{
	
	public static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);
		
	
	@Autowired
	ErpBo investmentBo;


	//private final RandamNumberRepository randamNumberRepository;

	private final EmployeeDAL employeedal;

	List<String> publicfiles = new ArrayList<String>();
	List<String> privatefiles = new ArrayList<String>();
	List<String> ownfiles = new ArrayList<String>();
	List<String> minifiles = new ArrayList<String>();
	
	public EmployeeService(EmployeeDAL employeedal) {
		this.employeedal = employeedal;
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
		public ResponseEntity<?>  save(@RequestBody Employee employee) {
			try {	   
				employee=  employeedal.save(employee);				 
				   return new ResponseEntity<Employee>(employee, HttpStatus.CREATED);

			

		   }catch(Exception e) {
			   logger.info("Exception ------------->"+e.getMessage());
			   e.printStackTrace();
		   }
		   finally{
			   
		   }
		   return new ResponseEntity<Employee>(employee, HttpStatus.CREATED);
		}


		 // get
		@CrossOrigin(origins = "http://localhost:8080")
		@RequestMapping(value="/get",method=RequestMethod.GET)
		public ResponseEntity<?> get(String id)
		{
			logger.info("------------- Inside get Employee -----------------");
			List<Employee> responseList=null;
		   try {
				logger.info("-----------Inside get employee ----------");
				responseList=employeedal.get(id);
				
				}catch(Exception e){
				logger.info("Exception ------------->"+e.getMessage());
				e.printStackTrace();
			}finally{
				
			}
			return new ResponseEntity<List<Employee>>(responseList, HttpStatus.CREATED);

		}
			
		
		// update	
		@CrossOrigin(origins = "http://localhost:8080")
		@RequestMapping(value="/update",method=RequestMethod.POST)
		public ResponseEntity<?>  update(@RequestBody Employee employee) {
	   try 
	   {	   
		   employee=  employeedal.update(employee);				  
		  return new ResponseEntity<Employee>(employee, HttpStatus.CREATED);
		   
	   }catch(Exception e) {
		   logger.info("Exception ------------->"+e.getMessage());
		   e.printStackTrace();
	   	}finally{
		   
	   	}
	  	return new ResponseEntity<Employee>(employee, HttpStatus.CREATED);
		}

				

		 // load
		@CrossOrigin(origins = "http://localhost:8080")
		@RequestMapping(value="/load",method=RequestMethod.GET)
		public ResponseEntity<?> load()
		{
			logger.info("------------- Inside getTempOwnTree-----------------");
			List<Employee> responseList=null;
		   try {
				logger.info("-----------Inside getTempOwnTree Called----------");
				responseList=employeedal.load(responseList);
				
				}catch(Exception e){
				logger.info("Exception ------------->"+e.getMessage());
				e.printStackTrace();
			}finally{
				
			}
			return new ResponseEntity<List<Employee>>(responseList, HttpStatus.CREATED);
	
		}

		// Remove
		@CrossOrigin(origins = "http://localhost:8080")
		@RequestMapping(value="/remove",method=RequestMethod.DELETE)
		public ResponseEntity<?> remove(String id)
		{


		   try {
				logger.info("-----------Before Calling  remove employee ----------");
				employeedal.remove(id);
				logger.info("-----------Successfully Called  remo employee ----------");
			
				}catch(Exception e){ 
				//	member.setStatus("bad");

				logger.info("Exception ------------->"+e.getMessage());
				e.printStackTrace();
			}finally{
				
			}
			return new ResponseEntity<String>("ok", HttpStatus.CREATED);

		}
	    
		

		
	
	
			
	
		
		
		
		
			
			
	
			
	
}
		
