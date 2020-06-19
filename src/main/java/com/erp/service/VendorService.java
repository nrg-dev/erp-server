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

import com.erp.mongo.dal.RandomNumberDAL;
import com.erp.mongo.dal.VendorDAL;
import com.erp.mongo.model.RandomNumber;
import com.erp.mongo.model.Vendor;
import com.erp.util.Custom;

@SpringBootApplication
@RestController
@RequestMapping(value = "/vendor")
public class VendorService implements Filter {

	public static final Logger logger = LoggerFactory.getLogger(VendorService.class);

	/*
	 * @Autowired ErpBo erpBo;
	 * 
	 */
	// private final RandamNumberRepository randamNumberRepository;

	private final VendorDAL vendordal;
	private final RandomNumberDAL randomnumberdal;
	Vendor vendor = null;

	public VendorService(VendorDAL vendordal, RandomNumberDAL randomnumberdal) {
		this.randomnumberdal = randomnumberdal;
		this.vendordal = vendordal;
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

	// Save
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ResponseEntity<?> saveVendor(@RequestBody Vendor vendor) {
		logger.info("saveVendor");
		RandomNumber randomnumber = null;
		try {
			randomnumber = randomnumberdal.getVendorRandamNumber();
			String invoice = randomnumber.getCode() + randomnumber.getNumber();
			logger.debug("Invoice number-->" + invoice);
			vendor.setVendorcode(invoice);
			vendor.setAddeddate(Custom.getCurrentInvoiceDate());
			vendor.setStatus("Active"); 
			logger.debug("Current Date-->" + Custom.getCurrentDate());
			logger.debug("Vendor Image Base 64 -->" + vendor.getVendorbase64());
			vendor = vendordal.saveVendor(vendor);
			if (vendor.getStatus().equalsIgnoreCase("success")) {
				randomnumberdal.updateVendorRandamNumber(randomnumber, 1);
			}
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Exception-->"+e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {

		}
	}

	// load
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/load", method = RequestMethod.GET)
	public ResponseEntity<?> loadVendor() {
		logger.info("loadVendor");
		List<Vendor> responselist = null;
		try {
			responselist = vendordal.loadVendor(responselist);
			logger.debug("Vendor List Size-->"+responselist.size());
			/*
			 * if(responselist.size()>0) { throw new Exception("Vendor Error"); }
			 */
			return new ResponseEntity<List<Vendor>>(responselist, HttpStatus.CREATED);

		} catch (Exception e) {
			logger.error("Exception-->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {

		}

	}

	// Load
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/loadvendornamecode", method = RequestMethod.GET)
	public ResponseEntity<?> loadvendornamecode() {
		logger.info("loadvendornamecode");
		List<Vendor> vendorlist = new ArrayList<Vendor>();
		List<String> list = new ArrayList<String>();
		try {
			logger.info("Before Calling loadVendor");
			vendorlist = vendordal.loadVendor(vendorlist);
			logger.info("Successfully Calling loadVendor");
			for (Vendor vendor : vendorlist) {
				logger.debug("vendor name-->" + vendor.getVendorName());
				list.add(vendor.getVendorName() + "-" + vendor.getVendorcode());
			}

			return new ResponseEntity<List<String>>(list, HttpStatus.CREATED);

		} catch (Exception e) {
			logger.error("Exception-->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {

		}
	}

	// get
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public ResponseEntity<?> geVendor(String vendorcode) {
		logger.info("geVendor");
		List<Vendor> responseList = null;
		try {
			logger.info("Before Calling getVendor");
			responseList = vendordal.getVendor(vendorcode);
			logger.info("Successfully Calling getVendor");
			return new ResponseEntity<List<Vendor>>(responseList, HttpStatus.CREATED);

		} catch (Exception e) {
			logger.error("Exception-->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {

		}
	}

	// update
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	public ResponseEntity<?> updateVendor(@RequestBody Vendor vendor) {
		logger.info("updateVendor");
		try {
			logger.debug("vendor update inside try-->" + vendor.getVendorcode());
			vendor = vendordal.updateVendor(vendor);
			return new ResponseEntity<>(HttpStatus.OK);

		} catch (Exception e) {
			logger.error("Exception-->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {

		}
	}

	// Remove
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/remove", method = RequestMethod.DELETE)
	public ResponseEntity<?> removeVendor(String vendorcode) {
		logger.info("removeVendor");
		try {
			vendor = new Vendor();
			logger.debug("Remove vendor code-->" + vendorcode);
			logger.info("Before Calling removeVendor");
			vendordal.removeVendor(vendorcode);
			logger.info("Successfully Calling removeVendor");
			vendor.setStatus("success");
			return new ResponseEntity<>(HttpStatus.OK);

		} catch (Exception e) {
			logger.error("Exception-->" + e.getMessage());
			vendor.setStatus("failure");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		} finally {

		}

	}

}
