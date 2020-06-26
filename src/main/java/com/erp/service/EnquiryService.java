package com.erp.service;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

//import org.springframework.beans.factory.annotation.Autowire;

//import javax.enterprise.inject.Produces;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.erp.bo.ErpBo;
import com.erp.dto.Enquiry;
import com.erp.dto.User;
import com.erp.mongo.dal.RandomNumberDAL;
import com.erp.mongo.model.POReturnDetails;
import com.erp.util.Custom;

@SpringBootApplication
@RestController
@RequestMapping(value = "/enquiry")
public class EnquiryService implements Filter {

	public static final Logger logger = LoggerFactory.getLogger(EnquiryService.class);

	@Autowired
	ErpBo bo;
	
	@Value("${enquiry.userName}")
	private String username;
	@Value("${enquiry.passWord}")
	private String password;

	private final RandomNumberDAL randomnumberdal;

	public EnquiryService(RandomNumberDAL randomnumberdal) {
		this.randomnumberdal = randomnumberdal;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {

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
	
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/saveEnquiry", method = RequestMethod.POST)
	public ResponseEntity<?> saveEnquiry(@RequestBody Enquiry enquiry) {
		logger.info("saveEnquiry");
		try {
			enquiry.setStatus("Active");
			enquiry.setAddeddate(Custom.getCurrentInvoiceDate()); 
			logger.info("--- Before Calling Bo ----");
			enquiry = bo.saveEnquiry(enquiry);
			logger.info("--- After Calling Bo ----");
			return new ResponseEntity<>(HttpStatus.OK); 
		} catch (Exception e) {
			logger.error("Exception-->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		finally {

		}
	}
	
	@CrossOrigin(origins = "http://localhost:8080")
	@GetMapping(value = "/getUserAndPass", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getUserAndPass() {
		logger.info("-------- getUserAndPass ----------");
		User user = new User();
		try {
			logger.info("User Name --->"+username);
			logger.info("Passowrd --->"+password);
			user.setUsername(username);
			user.setPassword(password); 
			return new ResponseEntity<User>(user, HttpStatus.OK);				
		} catch (Exception e) {
			logger.error("Exception-->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400
		} finally {
		}
	}
	
	@CrossOrigin(origins = "http://localhost:8080")
	@GetMapping(value = "/loadEnquiry", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> loadEnquiry() {
		logger.info("-------- loadEnquiry ----------");
		List<Enquiry> enquirylist = new ArrayList<Enquiry>();
		try {
			enquirylist = bo.loadEnquiry(enquirylist);
			return new ResponseEntity<List<Enquiry>>(enquirylist, HttpStatus.OK);				
		} catch (Exception e) {
			logger.error("Exception-->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400
		} finally {
		}
	}
		
}