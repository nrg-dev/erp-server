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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.erp.dto.Purchase;
import com.erp.mongo.dal.FinanceDAL;
import com.erp.mongo.dal.PurchaseDAL;
import com.erp.mongo.dal.SalesDAL;
import com.erp.mongo.model.POInvoice;
import com.erp.mongo.model.PettyCash;
import com.erp.mongo.model.SOInvoice;
import com.erp.util.Custom;

@SpringBootApplication
@RestController
@RequestMapping(value = "/finance")
public class FinanceService implements Filter {

	public static final Logger logger = LoggerFactory.getLogger(FinanceService.class);
	PettyCash pettycash = null;
	private final FinanceDAL financedal;
	private final PurchaseDAL purchasedal;
	private final SalesDAL salesdal;

	public FinanceService(FinanceDAL financedal,PurchaseDAL purchasedal,SalesDAL salesdal) {
		this.financedal = financedal;
		this.purchasedal = purchasedal;
		this.salesdal = salesdal;
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
		logger.info("loadCustomerVendorName");
		ArrayList<String> customervendorlist = null;
		try {
			logger.info("Before Calling Load customer & vendor name list");
			customervendorlist = financedal.loadCustomerVendorName();
			logger.info("Successfully Called Load customer & vendor name list");
			return new ResponseEntity<ArrayList<String>>(customervendorlist, HttpStatus.CREATED);

		} catch (Exception e) {
			logger.error("Exception-->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {

		}
	}

	// save petty cash
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ResponseEntity<?> savePettycash(@RequestBody PettyCash finance) {
		logger.info("savePettycash");
		try {
			finance.setStatus("Active"); 
			logger.debug("PettyCash Id-->"+finance.getId());
			if(finance.getId() != null) {
				finance = financedal.updatePettyCash(finance);
			}else {
				finance = financedal.save(finance);
			}
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Exception-->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {

		}
	}

	// Load petty cash data
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/load", method = RequestMethod.GET)
	public ResponseEntity<?> load() {
		logger.info("load");
		List<PettyCash> pettycashlist = null;
		try {
			logger.info("Before Calling load pettycash list");
			pettycashlist = financedal.load();
			logger.info("Successfully Called load pettycash list");
			return new ResponseEntity<List<PettyCash>>(pettycashlist, HttpStatus.CREATED);

		} catch (Exception e) {
			logger.error("Exception-->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {

		}
	}

	/*// update
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	public ResponseEntity<?> updatePetty(@RequestBody PettyCash pettycash) {
		try {
			pettycash = financedal.updatePettyCash(pettycash);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Exception-->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		} finally {

		}
	}*/

	// Remove
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/remove", method = RequestMethod.DELETE)
	public ResponseEntity<?> removePettyCash(String id) {
		try {
			logger.debug("Remove -->" + id);
			pettycash = new PettyCash();
			logger.info("Before Calling  removePettyCash");
			financedal.removePettyCash(id);
			pettycash.setStatus("success");
			logger.info("Successfully Called  removePettyCash");
			return new ResponseEntity<>(HttpStatus.OK);

		} catch (Exception e) {
			pettycash.setStatus("failure");
			logger.error("Exception-->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		} finally {

		}
	}
	
	@CrossOrigin(origins = "http://localhost:8080")
	@GetMapping(value = "/loadInvoice", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> loadInvoice() {
		logger.info("----- loadInvoice -------");
		List<SOInvoice> solist = new ArrayList<SOInvoice>();
		List<POInvoice> polist = new ArrayList<POInvoice>();
		List<Purchase> responselist = new ArrayList<Purchase>();
		Purchase purchase = null;
		try {
			polist = purchasedal.loadInvoice();
			solist = salesdal.loadInvoice();
			
			for (POInvoice po : polist) {
				purchase = new Purchase();
				purchase.setInvoiceNumber(po.getInvoicenumber());
				purchase.setFromdate(po.getInvoicedate());
				purchase.setTotalAmount(po.getTotalprice());
				purchase.setInvoicetype("Purchase Invoice");
				purchase.setStatus(po.getStatus());
				purchase.setPaymentStatus(po.getPaymentstatus());
				responselist.add(purchase);
			}
			
			for (SOInvoice so : solist) {
				purchase = new Purchase();
				purchase.setInvoiceNumber(so.getInvoicenumber());
				purchase.setFromdate(so.getInvoicedate());
				purchase.setTotalAmount(so.getTotalprice());
				purchase.setInvoicetype("Sales Invoice");
				purchase.setStatus(so.getStatus());
				purchase.setPaymentStatus(so.getPaymentstatus());
				responselist.add(purchase);   
			}
			
			return new ResponseEntity<List<Purchase>>(responselist, HttpStatus.OK);				
		} catch (Exception e) {
			logger.error("Exception-->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
		} finally {
		}
	}
	
}
