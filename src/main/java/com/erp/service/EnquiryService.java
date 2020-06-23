package com.erp.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

//import org.springframework.beans.factory.annotation.Autowire;

//import javax.enterprise.inject.Produces;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.erp.bo.ErpBo;
import com.erp.dto.Enquiry;
import com.erp.mongo.dal.RandomNumberDAL;
import com.erp.util.Custom;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

@SpringBootApplication
@RestController
@RequestMapping(value = "/enquiry")
public class EnquiryService implements Filter {

	public static final Logger logger = LoggerFactory.getLogger(EnquiryService.class);

	@Autowired
	ErpBo bo;

	private final RandomNumberDAL randomnumberdal;

	public EnquiryService(RandomNumberDAL randomnumberdal) {
		this.randomnumberdal = randomnumberdal;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
	    HttpServletResponse response = (HttpServletResponse) res;
	    response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
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
	
	@PostMapping("/saveEnquiry")
	@Produces(MediaType.APPLICATION_JSON)
	public String saveEnquiry(@RequestBody String myData) throws JSONException {
		  logger.info("----- Inside saveEnquiry Method Calling  -----");
		  Gson gson = new Gson();
		  Enquiry enquiry = new Enquiry();
		  JsonParser jsonParser = new JsonParser(); 
		  JsonElement element = jsonParser.parse(myData);
		  String name = element.getAsJsonObject().get("name").getAsString();  
		  String phonenumber = element.getAsJsonObject().get("phonenumber").getAsString();
		  String emailId = element.getAsJsonObject().get("email").getAsString(); 
		  String message = element.getAsJsonObject().get("msg").getAsString();
		  String country = element.getAsJsonObject().get("country").getAsString(); 
		  String enquirytype = element.getAsJsonObject().get("enquirytype").getAsString();
		 
		try {
			logger.info("------------ Inside Try Condition -------------");
			enquiry.setName(name); 
			enquiry.setPhonenumber(phonenumber);
			enquiry.setEmail_ID(emailId); 
			enquiry.setMessage(message);
			enquiry.setCountry(country); 
			enquiry.setEnquirytype(enquirytype);
			enquiry.setAddeddate(Custom.getCurrentInvoiceDate());
			enquiry.setStatus("Active"); 
			logger.info("Name ----------->" + name);
			logger.info("phonenumber ----------->" + phonenumber);
			logger.info("Before calling Bo------------->");
			enquiry = bo.saveEnquiry(enquiry);
			logger.info("Successfuly called Bo --------------->");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gson.toJson(enquiry);
	}
	
	@GetMapping("/loadEnquiry")
	@Produces(MediaType.APPLICATION_JSON)
	public String loadEnquiry() throws JSONException {
		List<Enquiry> enquirylist = null;
		Gson gson = new Gson();
		try {
			logger.info("Before Calling load enquiry list");
			enquirylist = bo.loadEnquiry(enquirylist);
			logger.info("Successfully Called load enquiry list");
		} catch (Exception e) {
			logger.info("Exception ------------->" + e.getMessage());
		} finally {

		}
		return gson.toJson(enquirylist);
	}


}