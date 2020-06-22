package com.erp.service;

/*
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import ch.qos.logback.core.net.SyslogOutputStream;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.json.simple.parser.ParseException;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;*/

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.ws.rs.Produces;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.codehaus.jettison.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.erp.bo.ErpBo;
import com.erp.dto.Enquiry;
import com.erp.mongo.dal.RandomNumberDAL;
import com.erp.util.Custom;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;

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

@RestController
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
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
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

	// save Enquiry Details
	@PostMapping("/saveEnquiry")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String saveEnquiry(@RequestBody String myData) throws JSONException {
		logger.info("----- Inside saveEnquiry Method Calling -----");
		Gson gson = new Gson();
		logger.info("json Value----" + myData);
		JsonParser jsonParser = new JsonParser();
		JsonElement element = jsonParser.parse(myData);
		Enquiry enquiry = new Enquiry();
		
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

	// Load petty cash data
	@GetMapping("/loadEnquiry")
	@Produces(MediaType.APPLICATION_JSON)
	public String loadEnquiry() throws JSONException {
		logger.info("loadEnquiry"); 
		List<Enquiry> enquirylist = null;
		Gson gson = new Gson();
		try {
			logger.info("Before Calling load enquiry list");
			enquirylist = bo.loadEnquiry(enquirylist);
			logger.info("Successfully Called load enquiry list");

		} catch (Exception e) {
			logger.error("Exception-->" + e.getMessage());
		} finally {

		}
		return gson.toJson(enquirylist);
	}


}
