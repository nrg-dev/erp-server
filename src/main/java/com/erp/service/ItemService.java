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

import com.erp.bo.ErpBo;
import com.erp.mongo.dal.ItemDAL;
import com.erp.mongo.dal.RandomNumberDAL;
import com.erp.mongo.model.Category;
import com.erp.mongo.model.Item;
import com.erp.mongo.model.RandomNumber;

@SpringBootApplication
@RestController
@RequestMapping(value = "/item")
public class ItemService implements Filter {

	public static final Logger logger = LoggerFactory.getLogger(ItemService.class);

	@Autowired
	ErpBo erpBo;

	private final ItemDAL itemdal;
	private final RandomNumberDAL randomnumberdal;
	Item item=null;

	public ItemService(ItemDAL itemdal, RandomNumberDAL randomnumberdal) {
		this.itemdal = itemdal;
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

	// Save
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/productsave", method = RequestMethod.POST)
	public ResponseEntity<?> saveItem(@RequestBody Item item) {
		System.out.println("--------save product-------------");
		RandomNumber randomnumber = null;
		try {
			randomnumber = randomnumberdal.getCategoryRandomNumber();
			System.out.println("item Invoice random number-->" + randomnumber.getProductinvoicenumber());
			System.out.println("item Invoice random code-->" + randomnumber.getProductinvoicecode());
			String invoice = randomnumber.getProductinvoicecode() + randomnumber.getProductinvoicenumber();
			System.out.println("Invoice number -->" + invoice);
			System.out.println("category code--->" + item.getCategorycode());
			System.out.println("vendor code--->" + item.getVendorcode());
			if (item.getCategorycode() != null) {
				String[] parts1 = item.getCategorycode().split("-");
				String part2 = parts1[1];
				item.setCategorycode(part2);
			}
			if (item.getVendorcode() != null) {
				String[] parts2 = item.getVendorcode().split("-");
				String part3 = parts2[1]; // 034556
				item.setVendorcode(part3);
			}

			item.setProdcode(invoice);
			item = itemdal.saveItem(item);
			if (item.getStatus().equalsIgnoreCase("success")) {
				boolean status = randomnumberdal.updateCategoryRandamNumber(randomnumber, 2);

			}
			return new ResponseEntity<Item>(item, HttpStatus.CREATED);

		} catch (Exception e) {
			item.setStatus("failure");
			logger.info("Exception ------------->" + e.getMessage());
			e.printStackTrace();
		}

		finally {

		}
		return new ResponseEntity<Item>(item, HttpStatus.CREATED);
	}

	// load
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/load", method = RequestMethod.GET)
	public ResponseEntity<?> loadItem() {
		logger.info("------------- Inside ItemLoad-----------------");
		List<Item> itemlist = new ArrayList<Item>();
		try {
			logger.info("-----------Inside ItemLoad Called----------");
			itemlist = itemdal.loadItem(itemlist);
			for (Item item : itemlist) {
				System.out.println("product code -->" + item.getProdcode());

			}
			return new ResponseEntity<List<Item>>(itemlist, HttpStatus.CREATED);

		} catch (Exception e) {
			logger.info("loadPurchase Exception ------------->" + e.getMessage());
			e.printStackTrace();
		} finally {

		}
		return new ResponseEntity<List<Item>>(itemlist, HttpStatus.CREATED);

	}

	// get
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public ResponseEntity<?> geItem(String id) {
		logger.info("------------- Inside getTempPublicTree-----------------");
		Item item = null;
		try {
			logger.info("-----------Inside getTempPublicTree Called----------");
			item = itemdal.getItem(id);

		} catch (Exception e) {
			logger.info("Exception ------------->" + e.getMessage());
			e.printStackTrace();
		} finally {

		}
		return new ResponseEntity<Item>(item, HttpStatus.CREATED);

	}

	// Update
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	public ResponseEntity<?> updateproduct(@RequestBody Item item) {
		try {
			logger.info("--- Inside Product Edit ---");
			item = itemdal.updateItem(item);
			return new ResponseEntity<Item>(item, HttpStatus.CREATED);

		} catch (Exception e) {
			logger.info("Exception ------------->" + e.getMessage());
			e.printStackTrace();
		} finally {

		}
		return new ResponseEntity<Item>(item, HttpStatus.CREATED);
	}

	// Remove
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/remove", method = RequestMethod.DELETE)
	public ResponseEntity<?> removeItem(String prodcode) {
		try {
			item = new Item();
			logger.info("-----------Before Calling  removeItem ----------");
			System.out.println("Remove product code" + prodcode);
			itemdal.removeItem(prodcode);
			item.setStatus("Success");
			logger.info("-----------Successfully Called  removeCategory ----------");

		} catch (Exception e) {
			logger.info("Exception ------------->" + e.getMessage());
			item.setStatus("failure");
			e.printStackTrace();
		} finally {

		}
		return new ResponseEntity<Item>(item, HttpStatus.CREATED);
	}
	
	// Load
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/loadItemName", method = RequestMethod.GET)
	public ResponseEntity<?> loadItemName() {
		logger.info("------------- Inside loadItemName-----------------");
		List<Item> itemlist = new ArrayList<Item>();
		List<String> list = new ArrayList<String>();
		try {
			logger.info("-----------Inside loadItemName Called----------");
			itemlist = itemdal.loadItem(itemlist);
			for(Item item: itemlist) {
				System.out.println("Product name-->"+item.getProductname());
				list.add(item.getProductname()+"-"+item.getProdcode());
			}
	
			return new ResponseEntity<List<String>>(list, HttpStatus.CREATED);

		} catch (Exception e) {
			logger.info("loadItemName Exception ------------->" + e.getMessage());
			e.printStackTrace();
		} finally {

		}
		return new ResponseEntity<List<String>>(list, HttpStatus.CREATED);
	}

}
