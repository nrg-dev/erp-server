package com.erp.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.erp.dto.Purchase;
import com.erp.mongo.dal.PurchaseDAL;
import com.erp.mongo.dal.RandomNumberDAL;
import com.erp.mongo.dal.StockDAL;
import com.erp.mongo.model.POInvoice;
import com.erp.mongo.model.POInvoiceDetails;
import com.erp.mongo.model.POReturnDetails;
import com.erp.mongo.model.PurchaseOrder;
import com.erp.mongo.model.RandomNumber;
import com.erp.mongo.model.SOReturnDetails;
import com.erp.mongo.model.Stock;
import com.erp.mongo.model.StockDamage;
import com.erp.mongo.model.StockInDetails;
import com.erp.mongo.model.StockReturn;
import com.erp.mongo.model.Transaction;
import com.erp.util.Custom;

@SpringBootApplication
@RestController
@RequestMapping(value = "/stock")
public class StockService implements Filter {

	public static final Logger logger = LoggerFactory.getLogger(StockService.class);

	private final StockDAL stockdal;
	private final PurchaseDAL purchasedal;
	//private final SalesDAL salesdal;
	private final RandomNumberDAL randomnumberdal;

	public StockService(StockDAL stockdal, 
			RandomNumberDAL randomnumberdal, PurchaseDAL purchasedal) {
		this.stockdal = stockdal;
		this.purchasedal = purchasedal;
		//this.salesdal = salesdal;
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

	// load Stock Return Details
	@CrossOrigin(origins = "http://localhost:8080")
	@GetMapping(value = "/loadStockReturn", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> loadStockReturn() {
		logger.info("loadStockReturn");
		List<POReturnDetails> poList = new ArrayList<POReturnDetails>();
		List<SOReturnDetails> soList = new ArrayList<SOReturnDetails>();
		List<Purchase> list = new ArrayList<Purchase>();
		Purchase po = null;
		Purchase so = null;
		try {
			poList = stockdal.loadPurchaseReturn(poList);
			logger.debug("PO List Size-->" + poList.size());
			soList = stockdal.loadSalesReturn(soList);
			logger.debug("SO List Size-->" + soList.size());
			for (int i = 0; i < poList.size(); i++) {
				logger.info("---- PO Date -- >" + poList.get(i).getCreateddate());
				po = new Purchase();
				po.setPoDate(poList.get(i).getCreateddate());
				po.setReturnCategory("Purchase Return" + "  " + poList.get(i).getInvoicenumber());
				po.setCategory(poList.get(i).getCategory());
				po.setProductName(poList.get(i).getItemname());
				po.setQuantity(poList.get(i).getQty());
				po.setStatus(poList.get(i).getItemStatus());
				po.setVendorName(poList.get(i).getVendorname());
				po.setInvoiceNumber(poList.get(i).getInvoicenumber());
				list.add(po);
			}
			logger.debug("Add PO into List Value-->" + list.get(0).getReturnCategory());
			for (int j = 0; j < soList.size(); j++) {
				so = new Purchase();
				so.setPoDate(soList.get(j).getCreateddate());
				so.setReturnCategory("Sales Return" + "  " + soList.get(j).getInvoicenumber());
				so.setCategory(soList.get(j).getCategory());
				so.setProductName(soList.get(j).getItemname());
				so.setQuantity(soList.get(j).getQty());
				so.setStatus(soList.get(j).getItemStatus());
				so.setVendorName(soList.get(j).getCustomername());
				so.setInvoiceNumber(soList.get(j).getInvoicenumber());
				list.add(so);
			}
			logger.debug("Add SO into List Value-->" + list.get(0).getReturnCategory());
			return new ResponseEntity<List<Purchase>>(list, HttpStatus.CREATED);
		} catch (Exception e) {
			logger.error("Exception-->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {

		}
		// return new ResponseEntity<List<Purchase>>(list, HttpStatus.CREATED);
	}

	// Save Stock Return
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/saveStockReturn", method = RequestMethod.POST)
	public ResponseEntity<?> saveStockReturn(@RequestBody StockReturn stockreturn) {
		logger.info("saveStockReturn");
		RandomNumber randomnumber = null;
		int temp = 1;
		try {
			randomnumber = randomnumberdal.getStockDamageRandomNumber();
			String invoice = randomnumber.getCode() + randomnumber.getNumber();
			stockreturn.setStockReturnCode(invoice);
			logger.info("Invoice Number -->" + stockreturn.getStockReturnCode());
			stockreturn.setAddedDate(Custom.getCurrentInvoiceDate());
			stockreturn = stockdal.saveStockReturn(stockreturn);
			if (stockreturn.getStatus().equalsIgnoreCase("success")) {
				randomnumberdal.updateStockDamRandamNumber(randomnumber, temp);
			}
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Exception-->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		finally {

		}
		// return new ResponseEntity<StockReturn>(stockreturn, HttpStatus.CREATED);
	}

	// Save StockDamage
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ResponseEntity<?> saveStockDamage(@RequestBody StockDamage stockdamage) {
		logger.info("saveStockDamage");
		RandomNumber randomnumber = null;
		int temp = 2;
		try {
			randomnumber = randomnumberdal.getStockDamageRandomNumber();
			String invoice = randomnumber.getCode() + randomnumber.getNumber();
			stockdamage.setStockDamageCode(invoice);
			logger.debug("Product name-->" + stockdamage.getProductName());
			logger.debug("Invoice Number-->" + stockdamage.getStockDamageCode());
			stockdamage.setAddedDate(Custom.getCurrentInvoiceDate());
			stockdamage = stockdal.saveStockDamage(stockdamage);
			if (stockdamage.getStatus().equalsIgnoreCase("success")) {
				randomnumberdal.updateStockDamRandamNumber(randomnumber, temp);
			}
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Exception-->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		finally {

		}
		// return new ResponseEntity<StockDamage>(stockdamage, HttpStatus.CREATED);
	}

	// Load
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/loadStockDamage", method = RequestMethod.GET)
	public ResponseEntity<?> loadStockDamage() {
		logger.info("loadStockDamage");
		List<StockDamage> damagelist = new ArrayList<StockDamage>();
		try {
			damagelist = stockdal.loadStockDamage(damagelist);
			return new ResponseEntity<List<StockDamage>>(damagelist, HttpStatus.CREATED);
		} catch (Exception e) {
			logger.error("Exception-->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {

		}
		// return new ResponseEntity<List<StockDamage>>(damagelist, HttpStatus.CREATED);
	}

	// update
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	public ResponseEntity<?> updateStockDamage(@RequestBody StockDamage damage) {
		try {
			logger.debug("Stock code inside try-->" + damage.getStockDamageCode());
			damage = stockdal.updateDamage(damage);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			damage.setStatus("failure");
			logger.error("Exception-->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {

		}
		// return new ResponseEntity<StockDamage>(damage, HttpStatus.CREATED);
	}

	// ----- get Invoice List ----
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/loadInvoice", method = RequestMethod.GET)
	public ResponseEntity<?> loadInvoice(String paymentOption) {
		logger.info("loadInvoice");
		List<POInvoiceDetails> polist = new ArrayList<POInvoiceDetails>();
		List<String> list = new ArrayList<String>();
		try {
			polist = stockdal.loadInvoice(polist, paymentOption);
			for (POInvoiceDetails po : polist) {
				list.add(po.getInvoicenumber());
			}
			Set<String> set = new LinkedHashSet<>();
			set.addAll(list);
			list.clear();
			list.addAll(set);
			return new ResponseEntity<List<String>>(list, HttpStatus.CREATED);
		} catch (Exception e) {
			logger.info("Exception-->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {

		}
		// return new ResponseEntity<List<String>>(list, HttpStatus.CREATED);
	}

	// -------- Save FullStockIn Details -----
	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping(value = "/saveFullStockIn")
	public ResponseEntity<?> saveFullStockIn(@RequestBody String stockInarray) {
		logger.info("saveFullStockIn");
		String temp = stockInarray;
		Purchase purchase = null;
		Stock stock = null;
		StockInDetails stockIndetails = null;
		RandomNumber randomnumber = null;
		String addedQty = "";
		String recentStockList = "";
		String itemnameList = "";
		String categoryList = "";
		int tempNo = 1;
		int addtotalQty = 0;
		int totalQty = 0;
		try {
			purchase = new Purchase();
			logger.info("Post Json -->" + stockInarray);
			// Store into parent table to show in first data table view
			ArrayList<String> list = new ArrayList<String>();
			JSONArray jsonArr = new JSONArray(stockInarray);
			int remove = 0;
			if (jsonArr != null) {
				for (int i = 0; i < jsonArr.length(); i++) {
					list.add(jsonArr.get(i).toString());
					remove++;
				}
			}
			int postion = remove - 1;
			logger.info("Position-->" + postion);
			list.remove(postion);
			logger.info("Size -------->" + jsonArr.length());
			// int l = 1;
			for (int i = 0; i < jsonArr.length(); i++) {
				JSONArray arr2 = jsonArr.optJSONArray(i);
				if (jsonArr.optJSONArray(i) != null) {
					for (int j = 0; j < arr2.length(); j++) {
						randomnumber = randomnumberdal.getStockRandamNumber();
						//logger.info("StockIn random number-->" + randomnumber.getStockIninvoicenumber());
						//logger.info("StockIn random code-->" + randomnumber.getStockIninvoicecode());
						String invoice = randomnumber.getCode() + randomnumber.getNumber();
						logger.info("Invoice number -->" + invoice);
						if (arr2.getJSONObject(j) != null) {
							JSONObject jObject = arr2.getJSONObject(j);
							logger.info(jObject.getString("productName"));
							logger.info(jObject.getString("category"));
							stockIndetails = new StockInDetails();
							stockIndetails.setStockInNumber(invoice);
							stockIndetails.setInvoicenumber(jObject.getString("invoiceNumber"));
							stockIndetails.setId(jObject.getString("id"));
							stockIndetails.setCategory(jObject.getString("category"));
							stockIndetails.setItemname(jObject.getString("productName"));
							stockIndetails.setDescription(jObject.getString("description"));
							stockIndetails.setQty(jObject.getString("quantity"));
							stockIndetails.setUnitprice(jObject.getString("price"));
							stockIndetails.setPoDate(jObject.getString("poDate"));
							stockIndetails.setSubtotal(jObject.getDouble("netAmount"));
							logger.info("StockIn Date --->" + stockIndetails.getPoDate());
							stockdal.saveStockIn(stockIndetails);
							addedQty = addedQty + stockIndetails.getQty() + ",";
							POInvoiceDetails podetails = new POInvoiceDetails();
							podetails.setLastUpdate(Custom.getCurrentInvoiceDate());
							podetails.setPaymentStatus("FullStockIn");
							podetails.setRemainingQty(0);
							recentStockList = recentStockList + stockIndetails.getQty() + ",";
							podetails = stockdal.updateFullPurchase(stockIndetails, podetails);
							itemnameList = itemnameList + stockIndetails.getItemname() + ",";
							categoryList = categoryList + stockIndetails.getCategory() + ",";
							randomnumberdal.updateStockRandamNumber(randomnumber, tempNo);
						} else {
							logger.info("Null....");
						}
					}
				} else {
					logger.info("Outer Null....");
				}
				// l++;
			}
			stock = new Stock();
			stock.setInvoicedate(Custom.getCurrentInvoiceDate());
			logger.info("Invoice Date --->" + stock.getInvoicedate());
			stock.setStockCategory("Purchase " + stockIndetails.getInvoicenumber());
			stock.setItemname(itemnameList);
			stock.setCategory(categoryList);
			stock.setAddedqty(Integer.valueOf(addedQty));
			stock.setRecentStock(Integer.valueOf(recentStockList));
			stock.setStatus("StockIn");
			stockdal.saveStock(stock);
			logger.info("Service call start.....");

			purchase.setStatus("success");
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			logger.info("saveStockIn Exception ------------->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		finally {

		}
		// return new ResponseEntity<Purchase>(purchase, HttpStatus.CREATED);
	}

	// -------- Save FullStockIn Details -----
	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping(value = "/savePartialStockIn")
	public ResponseEntity<?> savePartialStockIn(@RequestBody String stockInarray) {
		logger.info("savePartialStockIn");
		Purchase purchase = null;
		Stock stock = null;
		StockInDetails stockIndetails = null;
		RandomNumber randomnumber = null;
		String addedQty = "";
		String recentStockList = "";
		String itemnameList = "";
		String categoryList = "";
		int tempNo = 1;
		try {
			purchase = new Purchase();
			logger.debug("Post Json-->" + stockInarray);
			ArrayList<String> list = new ArrayList<String>();
			JSONArray jsonArr = new JSONArray(stockInarray);
			int remove = 0;
			if (jsonArr != null) {
				for (int i = 0; i < jsonArr.length(); i++) {
					list.add(jsonArr.get(i).toString());
					remove++;
				}
			}
			int postion = remove - 1;
			logger.info("Position-->" + postion);
			list.remove(postion);
			logger.info("Size -------->" + jsonArr.length());
			// int l = 1;
			for (int i = 0; i < jsonArr.length(); i++) {
				JSONArray arr2 = jsonArr.optJSONArray(i);
				if (jsonArr.optJSONArray(i) != null) {
					for (int j = 0; j < arr2.length(); j++) {
						randomnumber = randomnumberdal.getStockRandamNumber();
						///logger.info("StockIn random number-->" + randomnumber.getStockIninvoicenumber());
						//logger.info("StockIn random code-->" + randomnumber.getStockIninvoicecode());
						String invoice = randomnumber.getCode() + randomnumber.getNumber();
						logger.info("Invoice number -->" + invoice);
						if (arr2.getJSONObject(j) != null) {
							JSONObject jObject = arr2.getJSONObject(j);
							logger.info(jObject.getString("productName"));
							logger.info(jObject.getString("category"));
							stockIndetails = new StockInDetails();
							stockIndetails.setStockInNumber(invoice);
							stockIndetails.setInvoicenumber(jObject.getString("invoiceNumber"));
							stockIndetails.setId(jObject.getString("id"));
							stockIndetails.setCategory(jObject.getString("category"));
							stockIndetails.setItemname(jObject.getString("productName"));
							stockIndetails.setDescription(jObject.getString("description"));
							stockIndetails.setQty(jObject.getString("quantity"));
							stockIndetails.setUnitprice(jObject.getString("price"));
							stockIndetails.setPoDate(jObject.getString("poDate"));
							stockIndetails.setSubtotal(jObject.getDouble("netAmount"));
							logger.info("StockIn Date --->" + stockIndetails.getPoDate());
							stockdal.saveStockIn(stockIndetails);
							addedQty = addedQty + stockIndetails.getQty() + ",";
							POInvoiceDetails podetails = new POInvoiceDetails();
							podetails = stockdal.loadStockInTotal(stockIndetails);
							int totalQty = 0;
							// for(int n=0; n<stockIn.size(); n++) {
							// logger.info("Size -->"+stockIn.size()+"---"+n+"th TotalQty
							// ====>"+stockIn.get(n).getQty());
							String str = podetails.getQty();
							str = str.replaceAll("\\D", "");
							totalQty += Integer.valueOf(str);
							logger.info("Stock RecentStock Total ====>" + totalQty);
							// }
							recentStockList = recentStockList + totalQty + ",";
							String str1 = stockIndetails.getQty();
							str1 = str1.replaceAll("\\D", "");
							int stocktotalQty = Integer.valueOf(str1);
							podetails.setRemainingQty(totalQty - Integer.valueOf(stocktotalQty));
							logger.info("Remaining Qty --------->" + podetails.getRemainingQty());
							if (podetails.getPaymentStatus().equalsIgnoreCase("PartialStockIn")
									&& podetails.getRemainingQty() == 0) {
								podetails.setPaymentStatus("FullStockIn");
							} else {
								podetails.setPaymentStatus("PartialStockIn");
							}
							podetails = stockdal.updatePurchase(podetails);
							itemnameList = itemnameList + stockIndetails.getItemname() + ",";
							categoryList = categoryList + stockIndetails.getCategory() + ",";
							randomnumberdal.updateStockRandamNumber(randomnumber, tempNo);
						} else {
							logger.info("Null....");
						}
					}
				} else {
					logger.info("Outer Null....");
				}
				// l++;
			}
			stock = new Stock();
			stock.setInvoicedate(Custom.getCurrentInvoiceDate());
			logger.info("Invoice Date --->" + stock.getInvoicedate());
			stock.setStockCategory("Purchase " + stockIndetails.getInvoicenumber());
			stock.setItemname(itemnameList);
			stock.setCategory(categoryList);
			stock.setAddedqty(Integer.valueOf(addedQty));
			stock.setRecentStock(Integer.valueOf(recentStockList));
			stock.setStatus("StockIn");
			Stock st = new Stock();
			st = stockdal.loadStockInvoice(stock.getStockCategory(),1);
			if (st != null) {
				logger.info("----------- Stock Category match--------" + st.getId());
				logger.info("StockInCategory  --->" + stock.getStockCategory());
				logger.info("Itemname  --->" + stock.getItemname());
				logger.info("Category  --->" + stock.getCategory());
				logger.info("Addedqty  --->" + stock.getAddedqty());
				logger.info("RecentStock  --->" + stock.getRecentStock());
				logger.info("Status  --->" + stock.getStatus());
				stockdal.updateStock(stock, st.getId());
			} else {
				logger.info("----------- Stock Category not match--------");
				stockdal.saveStock(stock);
			}

			logger.info("Service call start.....");

			purchase.setStatus("success");
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Exception-->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		finally {

		}
		// return new ResponseEntity<Purchase>(purchase, HttpStatus.CREATED);
	}

	// Load StockInDetails
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/loadStock", method = RequestMethod.GET)
	public ResponseEntity<?> loadStock(String status) {
		logger.info("loadStock");
		List<Stock> stocklist = new ArrayList<Stock>();
		try {
			stocklist = stockdal.loadStock(stocklist, status);
			return new ResponseEntity<List<Stock>>(stocklist, HttpStatus.CREATED);
		} catch (Exception e) {
			logger.info("Exception-->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {

		}
		// return new ResponseEntity<List<Stock>>(stocklist, HttpStatus.CREATED);
	}

	// Save StockOut
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/saveStockOut", method = RequestMethod.POST)
	public ResponseEntity<?> saveStockOut(@RequestBody Stock stock) {
		logger.info("saveStockOut");
		RandomNumber randomnumber = null;
		try {
			randomnumber = randomnumberdal.getStockRandamNumber();
			//logger.info("StockOut Invoice random number-->" + randomnumber.getStockOutinvoicenumber());
			//logger.info("StockOut Invoice random code-->" + randomnumber.getStockOutinvoicecode());
			String invoice = randomnumber.getCode() + randomnumber.getNumber();
			logger.info("Invoice number -->" + invoice);
			stock.setInvoicedate(Custom.getCurrentInvoiceDate());
			stock.setInvoicenumber(invoice);
			stock.setStockCategory(stock.getStockOutCategory());
			stock.setRecentStock(stock.getAddedqty());
			stock.setStatus("StockOut");
			stock = stockdal.saveStockOut(stock);
			if (stock.getStatus().equalsIgnoreCase("success")) {
				randomnumberdal.updateStockRandamNumber(randomnumber, 2);
			}
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Exception-->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {

		}
	}
	
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/createStock", method = RequestMethod.POST)
	public ResponseEntity<?> createStock(@RequestBody String invoiceNumber) {
		logger.info("createStock");
		logger.debug("Invoice Number-->"+invoiceNumber );
		Stock stock = null;
		List<PurchaseOrder> polist = new ArrayList<PurchaseOrder>();
		POInvoice poinv = new POInvoice();
		try {
			polist = purchasedal.loadPO(2,invoiceNumber);
			for(PurchaseOrder po :polist) {
				stock = new Stock();
				stock.setCategory(po.getCategoryname());
				stock.setCategorycode(po.getCategorycode());
				stock.setItemname(po.getProductname());
				stock.setItemcode(po.getProductcode());
				stock.setUnit(po.getUnit()); 
				stock.setRecentStock(po.getQty()); 
				stock.setStatus("Stock In"); 
				stock.setInvoicedate(Custom.getCurrentInvoiceDate());
				stock.setInvoicenumber(po.getPocode());
				stockdal.saveStock(stock);
				logger.debug("Item Code -->"+stock.getItemcode());
				Stock st = new Stock();
				st = stockdal.loadStockInvoice(stock.getItemcode(),2);
				long currentStock = po.getQty()+st.getRecentStock();
				stock.setAddedqty(currentStock); 
				stockdal.updateStock(stock,"all");
				
			}

			poinv = purchasedal.loadPOInvoice(invoiceNumber);
			purchasedal.updatePOInvoice(poinv,1);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Exception-->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {

		}
	}
	
	// update
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/updateStock", method = RequestMethod.PUT)
	public ResponseEntity<?> updateStock(@RequestBody Stock stock) {
		try {
			logger.debug("Stock Id-->" + stock.getId());
			logger.debug("Recent Stock-->" + stock.getRecentStock());
			stock = stockdal.updateStock(stock,"update");
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			stock.setStatus("failure");
			logger.error("Exception-->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {

		}
	}

}
