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
import com.erp.mongo.dal.StockDAL;
import com.erp.mongo.model.Category;
import com.erp.mongo.model.Discount;
import com.erp.mongo.model.Item;
import com.erp.mongo.model.RandomNumber;
import com.erp.mongo.model.Stock;
import com.erp.mongo.model.Units;
import com.erp.util.Custom;

@SpringBootApplication
@RestController
@RequestMapping(value = "/item")
public class ItemService implements Filter {

	public static final Logger logger = LoggerFactory.getLogger(ItemService.class);

	@Autowired
	ErpBo erpBo;

	private final ItemDAL itemdal;
	private final RandomNumberDAL randomnumberdal;
	private final StockDAL stockdal;
	Item item = null;
	Discount discount = null;

	public ItemService(ItemDAL itemdal, RandomNumberDAL randomnumberdal, StockDAL stockdal) {
		this.itemdal = itemdal;
		this.randomnumberdal = randomnumberdal;
		this.stockdal = stockdal;
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
		logger.info("saveItem");
		RandomNumber randomnumber = null;
		Stock stock = new Stock();
		int randomId=16;
		try {
			randomnumber = randomnumberdal.getCategoryRandomNumber(2);
			//logger.info("item Invoice random number-->" + randomnumber.getProductinvoicenumber());
			//logger.info("item Invoice random code-->" + randomnumber.getProductinvoicecode());
			String invoice = randomnumber.getCode() + randomnumber.getNumber();
			logger.debug("Product number-->" + invoice);
			logger.debug("category code-->" + item.getCategorycode());
			logger.debug("vendor code-->" + item.getVendorcode());
			if (item.getCategorycode() != null) {
				String[] categorynamecode = item.getCategorycode().split("-");
				String categorycode = categorynamecode[1];
				String categoryname = categorynamecode[0];
				item.setCategorycode(categorycode);
				item.setCategoryname(categoryname);

			}
			if (item.getVendorcode() != null) {
				String[] vendornamecode = item.getVendorcode().split("-");
				String vendorname = vendornamecode[0];
				String vendorcode = vendornamecode[1];
				item.setVendorcode(vendorcode);
				item.setVendorname(vendorname);
			}

			item.setProdcode(invoice);
			logger.info("Product Image Base64 -->"+item.getProductImage());
			item = itemdal.saveItem(item);
			if (item.getStatus().equalsIgnoreCase("success")) {
				randomnumberdal.updateCategoryRandamNumber(randomnumber, 2);
			}
			stock.setCategory(item.getCategoryname());
			stock.setCategorycode(item.getCategorycode());
			stock.setItemname(item.getProductname());
			stock.setItemcode(item.getProdcode());
			stock.setUnit(item.getUnit()); 
			stock.setRecentStock(0); 
			stock.setStatus("Ready for Sales"); 
			stock.setInvoicedate(Custom.getCurrentInvoiceDate());
			stock.setInvoicenumber("NONE");
			/*randomnumber = randomnumberdal.getRandamNumber(randomId);
			String invoiceno = randomnumber.getCode() + randomnumber.getNumber();
			stock.setInvoicenumber(invoiceno);*/
			stockdal.saveStock(stock);
			/*if (stock.getStatus().equalsIgnoreCase("success")) {
				randomnumberdal.updateRandamNumber(randomnumber,randomId);
			}*/
			return new ResponseEntity<>(HttpStatus.OK);

		} catch (Exception e) {
			item.setStatus("failure");
			logger.error("Exception-->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		finally {

		}
	}

	// save promotion
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/addpromotionsave", method = RequestMethod.POST)
	public ResponseEntity<?> saveDiscount(@RequestBody Discount discount) {
		logger.info("saveDiscount");
		RandomNumber randomnumber = null;
		try {
			randomnumber = randomnumberdal.getdiscountRandamNumber();
			String invoice = randomnumber.getCode() + randomnumber.getNumber();
			discount.setDiscountcode(invoice);
			logger.debug("Discount from date-->" + discount.getFromdate_promotionperiod());
			logger.debug("Discount To date-->" + discount.getTodate_promotionperiod());

			discount = itemdal.saveDiscount(discount);
			if (discount.getStatus().equalsIgnoreCase("success")) {
				randomnumberdal.updatediscountRandamNumber(randomnumber);
			}
			return new ResponseEntity<>(HttpStatus.OK);

		} catch (Exception e) {
			logger.error("Exception-->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		finally {

		}
	}

	// load
		@CrossOrigin(origins = "http://localhost:8080")
		@RequestMapping(value = "/load", method = RequestMethod.GET)
		public ResponseEntity<?> loadItem(String vendorcode,String category,String prodcode) {
			logger.info("loadItem");
			List<Item> itemlist = null;
			try {
				logger.debug("Category Code or Name-->" + category);
				logger.info("Before Calling ItemLoad");
				itemlist = new ArrayList<Item>();
				itemlist = itemdal.loadItem(vendorcode,category,prodcode);
				logger.info("After Calling ItemLoad");
				for (Item item : itemlist) {
					logger.debug("product code-->" + item.getProdcode());
				}
				return new ResponseEntity<List<Item>>(itemlist, HttpStatus.CREATED);

			} catch (Exception e) {
				logger.error("Exception-->" + e.getMessage());
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			} finally {

			}
		}

	// save and update Units
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/saveunits", method = RequestMethod.POST)
	public ResponseEntity<?> saveUnits(@RequestBody Units units) {
		logger.info("saveUnits");
		try {
			if(units.getId()!=null) {
				// update
			}else {
				// save
				
			}
			boolean status = itemdal.saveUnits(units);
			if(status) {
				return new ResponseEntity<>(HttpStatus.OK);	
			}else {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}

		} catch (Exception e) {
			logger.error("Exception-->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		finally {

		}
	}

	
	

	// load units
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/loadunits", method = RequestMethod.GET)
	public ResponseEntity<?> loadUnits(String id) {
		logger.info("loadunits");
		List<Units> unitlist = null;
		try {
			logger.debug("Unit Id-->" + id);
			unitlist = new ArrayList<Units>();
			logger.info("Before Calling Unit load");
			unitlist = itemdal.loadUnits(id);
			logger.info("After Calling Unit load");
			logger.debug("Unit Size-->"+unitlist.size());
		   return new ResponseEntity<List<Units>>(unitlist, HttpStatus.CREATED);
		} catch (Exception e) {
			logger.error("Exception-->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {

		}
	}
	
	//-- Remove Units ---
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/removeUnit", method = RequestMethod.DELETE)
	public ResponseEntity<?> removeUnit(String id) {
		logger.info("------ Inside removeUnit -------");
		try {
			logger.debug("Remove ObjectID ------->" + id);
			itemdal.removeUnit(id);
			return new ResponseEntity<>(HttpStatus.OK);

		} catch (Exception e) {
			logger.error("Exception-->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		} finally {

		}
	}
	/*
	 * // Load only item name for auto text box search for promotion add // load
	 * 
	 * @CrossOrigin(origins = "http://localhost:8080")
	 * 
	 * @RequestMapping(value = "/loadItemName", method = RequestMethod.GET) public
	 * ResponseEntity<?> loadItemName() {
	 * logger.info("------------- Inside loadItemName-----------------"); List<Item>
	 * itemlist = new ArrayList<Item>(); List<String> itemnamecode = new
	 * ArrayList<String>();
	 * 
	 * try { logger.info("-----------Inside loadItemName Called----------");
	 * itemlist = itemdal.loadItem(itemlist); for (Item item : itemlist) {
	 * itemnamecode.add(item.getProductname()+"-"+item.getProdcode());
	 * logger.info("product code -->" + item.getProdcode());
	 * 
	 * } return new ResponseEntity<List<String>>(itemnamecode, HttpStatus.CREATED);
	 * 
	 * } catch (Exception e) { logger.info("loadItemName Exception ------------->" +
	 * e.getMessage()); return new ResponseEntity<>(HttpStatus.BAD_REQUEST); }
	 * finally {
	 * 
	 * } return new ResponseEntity<List<String>>(itemnamecode, HttpStatus.CREATED);
	 * 
	 * }
	 */
	// Add Promotion load
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/discountload", method = RequestMethod.GET)
	public ResponseEntity<?> loadDiscount(String discountType) {
		logger.info("loadDiscount");
		List<Discount> discountlist = new ArrayList<Discount>();
		try {
			logger.info("Before Calling loadDiscount");
			discountlist = itemdal.loadDiscount(discountlist, discountType);
			logger.info("After Calling loadDiscount");
			for (Discount disc : discountlist) {
				logger.debug("discount code-->" + disc.getDiscountcode());

			}
			return new ResponseEntity<List<Discount>>(discountlist, HttpStatus.CREATED);

		} catch (Exception e) {
			logger.error("Exception-->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {

		}

	}

	// get
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public ResponseEntity<?> geItem(String id) {
		logger.info("geItem");
		Item item = null;
		try {
			item = itemdal.getItem(id);
			return new ResponseEntity<Item>(item, HttpStatus.CREATED);
		} catch (Exception e) {
			logger.error("Exception-->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {

		}

	}

	// Update
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	public ResponseEntity<?> updateItem(@RequestBody Item item) {
		try {
			logger.info("Inside item Edit");
			logger.debug("Before Category Name -->" + item.getCategoryname());
			logger.debug("Before Category Code -->" + item.getCategorycode());
			if (item.getCategorycode() != null) {
				String[] categorynamecode = item.getCategorycode().split("-");
				String catname = categorynamecode[0];
				String catcode = categorynamecode[1];
				item.setCategoryname(catname);
				item.setCategorycode(catcode);
			}
			logger.debug("After Set Category Name-->" + item.getCategoryname());
			logger.debug("After Set Category Code-->" + item.getCategorycode());
			if (item.getVendorcode() != null) {
				String[] vendornamecode = item.getVendorcode().split("-");
				String vendorname = vendornamecode[0];
				String vendorcode = vendornamecode[1];
				item.setVendorname(vendorname);
				item.setVendorcode(vendorcode);
			}
			logger.debug("After Set Vendor Name-->" + item.getVendorname());
			logger.debug("After Set Vendor Code-->" + item.getVendorcode());
			item = itemdal.updateItem(item);
			return new ResponseEntity<>(HttpStatus.OK);

		} catch (Exception e) {
			logger.error("Exception-->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {

		}
	}

	// Discount Update
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/discountupdate", method = RequestMethod.PUT)
	public ResponseEntity<?> updatediscount(@RequestBody Discount discount) {
		logger.info("updatediscount");
		try {
			logger.debug("Discountcode-->" + discount.getDiscountcode());
			discount = itemdal.updateDiscount(discount);
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
	public ResponseEntity<?> removeItem(String prodcode) {
		logger.info("removeItem");
		try {
			item = new Item();
			logger.info("Before Calling  removeItem");
			logger.debug("Remove product code-->" + prodcode);
			itemdal.removeItem(prodcode);
			logger.info("After Calling  removeItem");
			item.setStatus("success");
			return new ResponseEntity<>(HttpStatus.OK);

		} catch (Exception e) {
			logger.error("Exception-->" + e.getMessage());
			item.setStatus("failure");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {

		}
	}

	// Discount Remove
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/discountremove", method = RequestMethod.DELETE)
	public ResponseEntity<?> discountremove(String discountcode) {
		logger.info("discountremove");
		try {
			discount = new Discount();
			logger.info("Before Calling  removeDiscount");
			logger.debug("Remove Discount code-->" + discountcode);
			itemdal.removeDiscount(discountcode);
			logger.info("Successfully Calling  removeDiscount");
			discount.setStatus("Success");
			return new ResponseEntity<>(HttpStatus.OK);

		} catch (Exception e) {
			logger.error("Exception-->" + e.getMessage());
			discount.setStatus("failure");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {

		}
	}

	// Load item name
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/loadItemName", method = RequestMethod.GET)
	public ResponseEntity<?> loadItemName() {
		logger.info("loadItemName");
		List<Item> itemlist = null;//new ArrayList<Item>();
		List<String> list = null;//new ArrayList<String>();
		try {
			itemlist = new ArrayList<Item>();
			list = new ArrayList<String>();
			logger.info("Before Calling loadItem");
			itemlist = itemdal.loadItem(null,"all",null);
			logger.info("After Calling loadItem");
			for (Item item : itemlist) {
				logger.debug("Product name-->" + item.getProductname());
				list.add(item.getProductname() + "-" + item.getProdcode());
			}

			return new ResponseEntity<List<String>>(list, HttpStatus.CREATED);

		} catch (Exception e) {
			logger.error("Exception-->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {

		}
	}

}
