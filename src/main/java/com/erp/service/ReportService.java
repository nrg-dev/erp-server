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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.erp.mongo.dal.ReportDAL;
import com.erp.mongo.model.Employee;
import com.erp.mongo.model.POInvoice;
import com.erp.mongo.model.SOInvoice;

@SpringBootApplication
@RestController
@RequestMapping(value = "/reports")
public class ReportService implements Filter {

	public static final Logger logger = LoggerFactory.getLogger(ReportService.class);

	private final ReportDAL reportdal;

	public ReportService(ReportDAL reportdal) {
		this.reportdal = reportdal;
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

	//employee report load
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/employeeReport", method = RequestMethod.GET)
	public ResponseEntity<?> employeeReport() {
		logger.info("------------- Inside employeeReport-----------------");
		List<Employee> employeelist = new ArrayList<Employee>();
		try {
			logger.info("-----------Inside employeeReport Called----------");
			employeelist = reportdal.employeeReport(employeelist);
			return new ResponseEntity<List<Employee>>(employeelist, HttpStatus.CREATED);

		} catch (Exception e) {
			logger.info("employeeReport Exception ------------->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {

		}

	}
	
	//purchase report load
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/purchaseReport", method = RequestMethod.GET)
	public ResponseEntity<?> purchaseReport() {
		logger.info("------------- Inside purchaseReport-----------------");
		List<POInvoice> purchaselist = new ArrayList<POInvoice>();
		try {
			logger.info("-----------Inside purchaseReport Called----------");
			purchaselist = reportdal.purchaseReport(purchaselist);
			return new ResponseEntity<List<POInvoice>>(purchaselist, HttpStatus.CREATED);

		} catch (Exception e) {
			logger.info("purchaseReport Exception ------------->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {

		}

	}
	
	//sales report load
		@CrossOrigin(origins = "http://localhost:8080")
		@RequestMapping(value = "/salesReport", method = RequestMethod.GET)
		public ResponseEntity<?> salesReport() {
			logger.info("------------- Inside salesReport-----------------");
			List<SOInvoice> saleslist = new ArrayList<SOInvoice>();
			try {
				logger.info("-----------Inside salesReport Called----------");
				saleslist = reportdal.salesReport(saleslist);
				return new ResponseEntity<List<SOInvoice>>(saleslist, HttpStatus.CREATED);

			} catch (Exception e) {
				logger.info("salesReport Exception ------------->" + e.getMessage());
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			} finally {

			}

		}

}
