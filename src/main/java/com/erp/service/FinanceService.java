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

//import org.springframework.beans.factory.annotation.Autowire;

//import javax.enterprise.inject.Produces;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.erp.mongo.dal.CategoryDAL;
import com.erp.mongo.dal.FinanceDAL;
import com.erp.mongo.model.Category;
import com.erp.mongo.model.Customer;
import com.erp.mongo.model.Employee;
import com.erp.mongo.model.PettyCash;
import com.erp.util.Custom;

@SpringBootApplication
@RestController
@RequestMapping(value = "/finance")
public class FinanceService implements Filter {

	public static final Logger logger = LoggerFactory.getLogger(FinanceService.class);
	PettyCash pettycash = null;
	private final FinanceDAL financedal;

	public FinanceService(FinanceDAL financedal) {
		this.financedal = financedal;
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

	
	// Load customer / vendor name for populate for auto text box
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/loadCustomerVendorName", method = RequestMethod.GET)
	public ResponseEntity<?> loadCustomerVendorName() {
		logger.info("------------- Inside loadCustomerVendorName-----------------");
		ArrayList<String> customervendorlist = null;
		try {
			logger.info("-----------Before Calling Load customer & vendor name list----------");
			customervendorlist = financedal.loadCustomerVendorName();
			logger.info("-----------Successfully Called Load customer & vendor name list------");

		} catch (Exception e) {
			logger.info("Exception ------------->" + e.getMessage());
			e.printStackTrace();
		} finally {

		}
		return new ResponseEntity<ArrayList<String>>(customervendorlist, HttpStatus.CREATED);
	}
	//save petty cash	
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ResponseEntity<?> savePettycash(@RequestBody PettyCash finance) {
		logger.info("------------- Inside savePettycash-----------------");
		try {
			finance.setAddeddate(Custom.getCurrentInvoiceDate());
			System.out.println("Current Date --->" + Custom.getCurrentDate());
			finance = financedal.save(finance);
			finance.setStatus("success");
			return new ResponseEntity<PettyCash>(finance, HttpStatus.CREATED);

		} catch (Exception e) {
			logger.info("savePettycash Exception ------------->" + e.getMessage());
			e.printStackTrace();
		} finally {

		}
		return new ResponseEntity<PettyCash>(finance, HttpStatus.CREATED);
	}
	
	// Load petty cash data
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/load", method = RequestMethod.GET)
	public ResponseEntity<?> load() {
		logger.info("-------------Inside load petty cash-----------------");
		List<PettyCash> pettycashlist = null;
		try {
			logger.info("-----------Before Calling load pettycash list----------");
			pettycashlist = financedal.load();
			logger.info("-----------Successfully Called load pettycash list----------");

		} catch (Exception e) {
			logger.info("Exception ------------->" + e.getMessage());
			e.printStackTrace();
		} finally {

		}
		return new ResponseEntity<List<PettyCash>>(pettycashlist, HttpStatus.CREATED);
	}
	
		// update
		@CrossOrigin(origins = "http://localhost:8080")
		@RequestMapping(value = "/update", method = RequestMethod.PUT)
		public ResponseEntity<?> updateCustomer(@RequestBody PettyCash pettycash) {
			try {
				pettycash = financedal.updatePettyCash(pettycash);
				return new ResponseEntity<PettyCash>(pettycash, HttpStatus.CREATED);
			} catch (Exception e) {
				logger.info("Exception ------------->" + e.getMessage());
			} finally {

			}
			return new ResponseEntity<PettyCash>(pettycash, HttpStatus.CREATED);
		}

		
		// Remove
		@CrossOrigin(origins = "http://localhost:8080")
		@RequestMapping(value = "/remove", method = RequestMethod.DELETE)
		public ResponseEntity<?> removePettyCash(String id) {
			try {
				System.out.println("Remove cust code is---->" + id);
				pettycash = new PettyCash();
				logger.info("-----------Before Calling  removeCustomer ----------");
				financedal.removePettyCash(id);
				pettycash.setStatus("success");
				logger.info("-----------Successfully Called  removeCustomer ----------");

			} catch (Exception e) {
				pettycash.setStatus("failure");
				logger.info("Exception ------------->"+ e.getMessage());
			} finally {

			}
			return new ResponseEntity<PettyCash>(pettycash, HttpStatus.CREATED);

		}
}
