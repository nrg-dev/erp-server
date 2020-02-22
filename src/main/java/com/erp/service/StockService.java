package com.erp.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
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
import com.erp.mongo.dal.SalesDAL;
import com.erp.mongo.dal.StockDAL;
import com.erp.mongo.model.Category;
import com.erp.mongo.model.Item;
import com.erp.mongo.model.POInvoice;
import com.erp.mongo.model.POInvoiceDetails;
import com.erp.mongo.model.POReturnDetails;
import com.erp.mongo.model.RandomNumber;
import com.erp.mongo.model.SOReturnDetails;
import com.erp.mongo.model.Stock;
import com.erp.mongo.model.StockDamage;
import com.erp.mongo.model.StockInDetails;
import com.erp.mongo.model.StockReturn;
import com.erp.util.Custom;

@SpringBootApplication
@RestController
@RequestMapping(value = "/stock")
public class StockService implements Filter {

	public static final Logger logger = LoggerFactory.getLogger(StockService.class);

	private final StockDAL stockdal;
	private final PurchaseDAL purchasedal;
	private final SalesDAL salesdal;
	private final RandomNumberDAL randomnumberdal;

	public StockService(StockDAL stockdal,PurchaseDAL purchasedal,SalesDAL salesdal,RandomNumberDAL randomnumberdal) {
		this.stockdal = stockdal;
		this.purchasedal = purchasedal;
		this.salesdal = salesdal;
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
		logger.info("------------- Inside loadStockReturn -----------------");
		List<POReturnDetails> poList = new ArrayList<POReturnDetails>();
		List<SOReturnDetails> soList = new ArrayList<SOReturnDetails>();
		List<Purchase> list = new ArrayList<Purchase>();
		Purchase po = null;
		Purchase so = null;
		try {
			poList = stockdal.loadPurchaseReturn(poList);
			logger.info("PO List Size ---------->"+poList.size());
			soList = stockdal.loadSalesReturn(soList);
			logger.info("SO List Size ---------->"+soList.size());
			for(int i=0; i<poList.size(); i++) {
				 logger.info("---- PO Date -- >"+poList.get(i).getPoDate());
				 po = new Purchase();
				 po.setPoDate(poList.get(i).getPoDate());
				 po.setReturnCategory("Purchase Return"+"  "+poList.get(i).getInvoicenumber());
				 po.setCategory(poList.get(i).getCategory());
				 po.setProductName(poList.get(i).getItemname());
				 po.setQuantity(poList.get(i).getQty());
				 po.setStatus(poList.get(i).getItemStatus());
				 po.setVendorName(poList.get(i).getVendorname());
				 po.setInvoiceNumber(poList.get(i).getInvoicenumber());
				 list.add(po);
			}
			logger.info("Add PO into List Value ----->"+list.get(0).getReturnCategory());
			for(int j=0; j<soList.size(); j++) {
				 so = new Purchase();
				 so.setPoDate(soList.get(j).getSoDate());
				 so.setReturnCategory("Sales Return"+"  "+soList.get(j).getInvoicenumber());
				 so.setCategory(soList.get(j).getCategory());
				 so.setProductName(soList.get(j).getItemname());
				 so.setQuantity(soList.get(j).getQty());
				 so.setStatus(soList.get(j).getItemStatus());
				 so.setVendorName(soList.get(j).getCustomername()); 
				 so.setInvoiceNumber(soList.get(j).getInvoicenumber());
				 list.add(so);
			}		
			logger.info("Add SO into List Value ----->"+list.get(0).getReturnCategory());
			return new ResponseEntity<List<Purchase>>(list, HttpStatus.CREATED);
		} catch (Exception e) {
			logger.info("loadStockReturn Exception ------------->" + e.getMessage());
			e.printStackTrace();
		} finally {

		}
		return new ResponseEntity<List<Purchase>>(list, HttpStatus.CREATED);
	}
	
	// Save Stock Return
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/saveStockReturn", method = RequestMethod.POST)
	public ResponseEntity<?> saveStockReturn(@RequestBody StockReturn stockreturn) {
		System.out.println("-------- Save Stock Damage -------------");
		RandomNumber randomnumber = null;
		int temp = 1;
		try {
			randomnumber = randomnumberdal.getStockDamageRandomNumber();
			String invoice = randomnumber.getStockreturninvoicecode() + randomnumber.getStockreturninvoicenumber();
			stockreturn.setStockReturnCode(invoice);
			System.out.println("Invoice Number -->" + stockreturn.getStockReturnCode());
			
			stockreturn.setAddedDate(Custom.getCurrentInvoiceDate());

			stockreturn = stockdal.saveStockReturn(stockreturn);
			if (stockreturn.getStatus().equalsIgnoreCase("success")) {
				boolean status = randomnumberdal.updateStockDamRandamNumber(randomnumber,temp);
			}
			return new ResponseEntity<StockReturn>(stockreturn, HttpStatus.CREATED);
		} catch (Exception e) {
			logger.info("StockReturn Exception ------------->" + e.getMessage());
			e.printStackTrace();
		}

		finally {

		}
		return new ResponseEntity<StockReturn>(stockreturn, HttpStatus.CREATED);
	}
	
	// Save StockDamage 
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ResponseEntity<?> saveStockDamage(@RequestBody StockDamage stockdamage) {
		System.out.println("-------- Save Stock Damage -------------");
		RandomNumber randomnumber = null;
		int temp = 2;
		try {
			randomnumber = randomnumberdal.getStockDamageRandomNumber();
			String invoice = randomnumber.getStockdamageinvoicecode() + randomnumber.getStockdamageinvoicenumber();
			stockdamage.setStockDamageCode(invoice);
			System.out.println("Product name -->" + stockdamage.getProductName());
			System.out.println("Invoice Number -->" + stockdamage.getStockDamageCode());
			stockdamage.setAddedDate(Custom.getCurrentInvoiceDate());
			stockdamage = stockdal.saveStockDamage(stockdamage);
			if (stockdamage.getStatus().equalsIgnoreCase("success")) {
				boolean status = randomnumberdal.updateStockDamRandamNumber(randomnumber,temp);
			}
			return new ResponseEntity<StockDamage>(stockdamage, HttpStatus.CREATED);
		} catch (Exception e) {
			logger.info("Stockdamage Exception ------------->" + e.getMessage());
			e.printStackTrace();
		}

		finally {

		}
		return new ResponseEntity<StockDamage>(stockdamage, HttpStatus.CREATED);
	}
	
	// Load
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/loadStockDamage", method = RequestMethod.GET)
	public ResponseEntity<?> loadStockDamage() {
		logger.info("------------- Inside loadStockDamage-----------------");
		List<StockDamage> damagelist = new ArrayList<StockDamage>();
		try {
			logger.info("-----------Inside loadStockDamage Called----------");
			damagelist = stockdal.loadStockDamage(damagelist);
			return new ResponseEntity<List<StockDamage>>(damagelist, HttpStatus.CREATED);
		} catch (Exception e) {
			logger.info("loadStockDamage Exception ------------->" + e.getMessage());
			e.printStackTrace();
		} finally {

		}
		return new ResponseEntity<List<StockDamage>>(damagelist, HttpStatus.CREATED);
	}
	
	// update
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	public ResponseEntity<?> updateStockDamage(@RequestBody StockDamage damage) {
		try {
			System.out.println("Stock code inside try--->" + damage.getStockDamageCode());
			damage = stockdal.updateDamage(damage);
			return new ResponseEntity<StockDamage>(damage, HttpStatus.CREATED);
		} catch (Exception e) {
			damage.setStatus("failure");
			logger.info("updateStockDamage Exception ------------->" + e.getMessage());
			e.printStackTrace();
		} finally {

		}
		return new ResponseEntity<StockDamage>(damage, HttpStatus.CREATED);
	}
	
	//----- get Invoice List ----
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/loadInvoice", method = RequestMethod.GET)
	public ResponseEntity<?> loadInvoice(String paymentOption) {
		logger.info("------------- Inside loadInvoice -----------------");
		List<POInvoiceDetails> polist = new ArrayList<POInvoiceDetails>();
		List<String> list = new ArrayList<String>();
		try {
			logger.info("-----------Inside loadInvoice Called ----------");
			polist = stockdal.loadInvoice(polist,paymentOption);
			for(POInvoiceDetails po : polist) {
				list.add(po.getInvoicenumber());
			}
			Set<String> set = new LinkedHashSet<>(); 
	        set.addAll(list); 
	        list.clear(); 
	        list.addAll(set); 
			return new ResponseEntity<List<String>>(list, HttpStatus.CREATED);
		} catch (Exception e) {
			logger.info("loadInvoice Exception ------------->" + e.getMessage());
			e.printStackTrace();
		} finally {

		}
		return new ResponseEntity<List<String>>(list, HttpStatus.CREATED);
	}
	
	//-------- Save FullStockIn Details -----
	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping(value = "/saveFullStockIn")
	public ResponseEntity<?> saveFullStockIn(@RequestBody String stockInarray) {
		String temp = stockInarray;
		System.out.println("-------- Save FullStockIn -------------");
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
			System.out.println("Post Json -->" + stockInarray);
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
			System.out.println("Position-->" + postion);
			list.remove(postion);
			System.out.println("Size -------->" + jsonArr.length());
			int l = 1;
			for (int i = 0; i < jsonArr.length(); i++) {
				JSONArray arr2 = jsonArr.optJSONArray(i);
				if (jsonArr.optJSONArray(i) != null) {
					for (int j = 0; j < arr2.length(); j++) {
						randomnumber = randomnumberdal.getStockRandamNumber();
						System.out.println("StockIn random number-->" + randomnumber.getStockIninvoicenumber());
						System.out.println("StockIn random code-->" + randomnumber.getStockIninvoicecode());
						String invoice = randomnumber.getStockIninvoicecode() + randomnumber.getStockIninvoicenumber();
						System.out.println("Invoice number -->" + invoice);
						if (arr2.getJSONObject(j) != null) {
							JSONObject jObject = arr2.getJSONObject(j);
							System.out.println(jObject.getString("productName"));
							System.out.println(jObject.getString("category"));
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
							
							addedQty = addedQty+stockIndetails.getQty() + ",";
							POInvoiceDetails podetails = new POInvoiceDetails();
							podetails.setLastUpdate(Custom.getCurrentInvoiceDate());
							podetails.setPaymentStatus("FullStockIn");
							podetails.setRemainingQty(0);
							recentStockList = recentStockList+stockIndetails.getQty() + ",";
							podetails = stockdal.updateFullPurchase(stockIndetails,podetails);
							itemnameList = itemnameList+stockIndetails.getItemname() + ",";
							categoryList = categoryList+stockIndetails.getCategory() + ",";
							
							boolean status = randomnumberdal.updateStockRandamNumber(randomnumber,tempNo);
						} else {
							System.out.println("Null....");
						}
					}
				} else {
					System.out.println("Outer Null....");
				}
				l++;
			}
			stock = new Stock(); 
			stock.setInvoicedate(Custom.getCurrentInvoiceDate());
			logger.info("Invoice Date --->" + stock.getInvoicedate());
			stock.setStockCategory("Purchase "+stockIndetails.getInvoicenumber());
			stock.setItemname(itemnameList);
			stock.setCategory(categoryList); 
			stock.setAddedqty(addedQty);
			stock.setRecentStock(recentStockList);
			stock.setStatus("StockIn"); 
			stockdal.saveStock(stock);
			System.out.println("Service call start.....");
			
			purchase.setStatus("success");
			return new ResponseEntity<Purchase>(purchase, HttpStatus.CREATED);
		}catch (Exception e) {
			logger.info("saveStockIn Exception ------------->" + e.getMessage());
			e.printStackTrace();
		}

		finally {

		}
		return new ResponseEntity<Purchase>(purchase, HttpStatus.CREATED);
	}
		
	//-------- Save FullStockIn Details -----
	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping(value = "/savePartialStockIn")
	public ResponseEntity<?> savePartialStockIn(@RequestBody String stockInarray) {
		System.out.println("-------- Save PartialStockIn -------------");
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
			System.out.println("Post Json -->" + stockInarray);			
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
			System.out.println("Position-->" + postion);
			list.remove(postion);
			System.out.println("Size -------->" + jsonArr.length());
			int l = 1;
			for (int i = 0; i < jsonArr.length(); i++) {
				JSONArray arr2 = jsonArr.optJSONArray(i);
				if (jsonArr.optJSONArray(i) != null) {
					for (int j = 0; j < arr2.length(); j++) {
						randomnumber = randomnumberdal.getStockRandamNumber();
						System.out.println("StockIn random number-->" + randomnumber.getStockIninvoicenumber());
						System.out.println("StockIn random code-->" + randomnumber.getStockIninvoicecode());
						String invoice = randomnumber.getStockIninvoicecode() + randomnumber.getStockIninvoicenumber();
						System.out.println("Invoice number -->" + invoice);
						if (arr2.getJSONObject(j) != null) {
							JSONObject jObject = arr2.getJSONObject(j);
							System.out.println(jObject.getString("productName"));
							System.out.println(jObject.getString("category"));
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
							
							addedQty = addedQty+stockIndetails.getQty() + ",";
							POInvoiceDetails podetails = new POInvoiceDetails();
							podetails = stockdal.loadStockInTotal(stockIndetails);
							int totalQty = 0;
							//for(int n=0; n<stockIn.size(); n++) {
								//logger.info("Size -->"+stockIn.size()+"---"+n+"th TotalQty ====>"+stockIn.get(n).getQty());
								String str = podetails.getQty();
								str = str.replaceAll("\\D", "");
								totalQty += Integer.valueOf(str);
								logger.info("Stock RecentStock Total ====>"+totalQty);
							//}
							recentStockList = recentStockList+totalQty + ",";
							
							String str1 = stockIndetails.getQty();
							str1 = str1.replaceAll("\\D", "");
							int stocktotalQty = Integer.valueOf(str1);
							podetails.setRemainingQty(totalQty-Integer.valueOf(stocktotalQty));
							logger.info("Remaining Qty --------->"+podetails.getRemainingQty());
							if(podetails.getPaymentStatus().equalsIgnoreCase("PartialStockIn") && 
									podetails.getRemainingQty() == 0) {
								podetails.setPaymentStatus("FullStockIn");
							}else {
								podetails.setPaymentStatus("PartialStockIn");
							}
							podetails = stockdal.updatePurchase(podetails);
							itemnameList = itemnameList+stockIndetails.getItemname() + ",";
							categoryList = categoryList+stockIndetails.getCategory() + ",";
							
							boolean status = randomnumberdal.updateStockRandamNumber(randomnumber,tempNo);
						} else {
							System.out.println("Null....");
						}
					}
				} else {
					System.out.println("Outer Null....");
				}
				l++;
			}
			stock = new Stock(); 
			stock.setInvoicedate(Custom.getCurrentInvoiceDate());
			logger.info("Invoice Date --->" + stock.getInvoicedate());
			stock.setStockCategory("Purchase "+stockIndetails.getInvoicenumber());
			stock.setItemname(itemnameList);
			stock.setCategory(categoryList); 
			stock.setAddedqty(addedQty);
			stock.setRecentStock(recentStockList);
			stock.setStatus("StockIn"); 
			Stock st = new Stock();
			st = stockdal.loadStockInvoice(stock.getStockCategory());
			if(st != null) {
				logger.info("----------- Stock Category match--------"+st.getId());
				logger.info("StockInCategory  --->"+stock.getStockCategory());
				logger.info("Itemname  --->"+stock.getItemname());
				logger.info("Category  --->"+stock.getCategory());
				logger.info("Addedqty  --->"+stock.getAddedqty());
				logger.info("RecentStock  --->"+stock.getRecentStock());
				logger.info("Status  --->"+stock.getStatus());
				stockdal.updateStock(stock,st.getId());
			}else {
				logger.info("----------- Stock Category not match--------");
				stockdal.saveStock(stock);
			}		
			
			System.out.println("Service call start.....");
			
			purchase.setStatus("success");
			return new ResponseEntity<Purchase>(purchase, HttpStatus.CREATED);
		}catch (Exception e) {
			logger.info("saveStockIn Exception ------------->" + e.getMessage());
			e.printStackTrace();
		}

		finally {

		}
		return new ResponseEntity<Purchase>(purchase, HttpStatus.CREATED);
	}

	// Load StockInDetails
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/loadStock", method = RequestMethod.GET)
	public ResponseEntity<?> loadStock(String status) {
		logger.info("------------- Inside loadStock-----------------");
		List<Stock> stocklist = new ArrayList<Stock>();
		try {
			logger.info("-----------Inside loadStock Called----------");
			stocklist = stockdal.loadStock(stocklist,status);
			return new ResponseEntity<List<Stock>>(stocklist, HttpStatus.CREATED);
		} catch (Exception e) {
			logger.info("loadStock Exception ------------->" + e.getMessage());
			e.printStackTrace();
		} finally {

		}
		return new ResponseEntity<List<Stock>>(stocklist, HttpStatus.CREATED);
	}
	
	// Save StockOut
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/saveStockOut", method = RequestMethod.POST)
	public ResponseEntity<?> saveStockOut(@RequestBody Stock stock) {
		System.out.println("-------- saveStockOut ---------");
		RandomNumber randomnumber = null;
		try {
			randomnumber = randomnumberdal.getStockRandamNumber();
			System.out.println("StockOut Invoice random number-->" + randomnumber.getStockOutinvoicenumber());
			System.out.println("StockOut Invoice random code-->" + randomnumber.getStockOutinvoicecode());
			String invoice = randomnumber.getStockOutinvoicecode() + randomnumber.getStockOutinvoicenumber();
			System.out.println("Invoice number -->" + invoice);

			stock.setInvoicedate(Custom.getCurrentInvoiceDate());
			stock.setInvoicenumber(invoice);
			stock.setStockCategory(stock.getStockOutCategory());
			stock.setRecentStock(stock.getAddedqty()); 
			stock.setStatus("StockOut"); 
			stock = stockdal.saveStockOut(stock);
			if (stock.getStatus().equalsIgnoreCase("success")) {
				boolean status = randomnumberdal.updateStockRandamNumber(randomnumber, 2);
			}
			return new ResponseEntity<Stock>(stock, HttpStatus.CREATED);
		} catch (Exception e) {
			logger.info("Exception ------------->" + e.getMessage());
			e.printStackTrace();
		} finally {

		}
		return new ResponseEntity<Stock>(stock, HttpStatus.CREATED);
	}
	
}
