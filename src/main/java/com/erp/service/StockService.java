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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.erp.dto.Purchase;
import com.erp.mongo.dal.PurchaseDAL;
import com.erp.mongo.dal.SalesDAL;
import com.erp.mongo.dal.StockDAL;
import com.erp.mongo.model.POInvoiceDetails;
import com.erp.mongo.model.POReturnDetails;
import com.erp.mongo.model.SOReturnDetails;

@SpringBootApplication
@RestController
@RequestMapping(value = "/stock")
public class StockService implements Filter {

	public static final Logger logger = LoggerFactory.getLogger(StockService.class);

	private final StockDAL stockdal;
	private final PurchaseDAL purchasedal;
	private final SalesDAL salesdal;

	public StockService(StockDAL stockdal,PurchaseDAL purchasedal,SalesDAL salesdal) {
		this.stockdal = stockdal;
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

	
	// load Stock Return Details 
	@CrossOrigin(origins = "http://localhost:8080")
	@GetMapping(value = "/loadStockReturn", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> loadStockReturn() {
		logger.info("------------- Inside loadStockReturn -----------------");
		List<POReturnDetails> poList = null;
		List<SOReturnDetails> soList = new ArrayList<SOReturnDetails>();
		List<Purchase> list = new ArrayList<Purchase>();
		Purchase purchase = new Purchase();
		try {
			poList = stockdal.loadPurchaseReturn(poList);
			soList = stockdal.loadSalesReturn(soList);
			for(int i=0; i<poList.size(); i++) {
				 poList = new ArrayList<POReturnDetails>();
				 purchase.setPoDate(poList.get(i).getPoDate());
				 purchase.setReturnCategory("Purchase Return"+"  "+poList.get(i).getInvoicenumber());
				 purchase.setCategory(poList.get(i).getCategory());
				 purchase.setProductName(poList.get(i).getItemname());
				 purchase.setQuantity(poList.get(i).getQty());
				 purchase.setStatus(poList.get(i).getItemStatus());
				 purchase.setInvoiceNumber(poList.get(i).getInvoicenumber());
				 list.add(purchase);
			}
			for(int j=0; j<soList.size(); j++) {
				soList = new ArrayList<SOReturnDetails>();
				 purchase.setPoDate(soList.get(j).getSoDate());
				 purchase.setReturnCategory("Sales Return"+"  "+soList.get(j).getInvoicenumber());
				 purchase.setCategory(soList.get(j).getCategory());
				 purchase.setProductName(soList.get(j).getItemname());
				 purchase.setQuantity(soList.get(j).getQty());
				 purchase.setStatus(soList.get(j).getItemStatus());
				 purchase.setInvoiceNumber(soList.get(j).getInvoicenumber());
				 list.add(purchase);
			}
		} catch (Exception e) {
			logger.info("loadStockReturn Exception ------------->" + e.getMessage());
			e.printStackTrace();
		} finally {

		}
		return new ResponseEntity<List<Purchase>>(list, HttpStatus.CREATED);
	}
	

}
