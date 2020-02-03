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

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

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

import com.erp.bo.ErpBo;
import com.erp.dto.Purchase;
import com.erp.mongo.dal.PurchaseDAL;
import com.erp.mongo.dal.RandomNumberDAL;
import com.erp.mongo.model.Category;
import com.erp.mongo.model.Item;
import com.erp.mongo.model.POInvoice;
import com.erp.mongo.model.POInvoiceDetails;
import com.erp.mongo.model.RandomNumber;
import com.erp.mongo.model.Vendor;
import com.erp.util.Custom;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
@RestController
@RequestMapping(value = "/purchase")
public class PurchaseService implements Filter {

	public static final Logger logger = LoggerFactory.getLogger(PurchaseService.class);

	@Autowired
	ErpBo erpBo;

	// private final RandamNumberRepository randamNumberRepository;

	private final PurchaseDAL purchasedal;
	private final RandomNumberDAL randomnumberdal;

	public PurchaseService(PurchaseDAL purchasedal, RandomNumberDAL randomnumberdal) {
		this.purchasedal = purchasedal;
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

	

	// ------- Load Vendor ----
	@CrossOrigin(origins = "http://localhost:8080")
	@GetMapping(value = "/loadVendor", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> loadVendorList() {
		logger.info("------------- Inside loadVendor List Calling -----------------");
		List<Vendor> response = new ArrayList<Vendor>();
		List<Purchase> responseList = new ArrayList<Purchase>();
		Purchase purhase;
		try {
			logger.info("-----------Inside loadVendor Called----------");
			response = purchasedal.loadVendorList(response);
			for (Vendor venList : response) {
				purhase = new Purchase();
				purhase.setVendorName(venList.getVendorName() + "-" + venList.getVendorcode());
				responseList.add(purhase);
			}
		} catch (Exception e) {
			logger.info("loadVendor Exception ------------->" + e.getMessage());
			e.printStackTrace();
		} finally {
		}
		return new ResponseEntity<List<Purchase>>(responseList, HttpStatus.CREATED);

	}
	/*
	 * // Save
	 * 
	 * @CrossOrigin(origins = "http://localhost:8080")
	 * 
	 * @RequestMapping(value="/save",method=RequestMethod.POST) public
	 * ResponseEntity<?> savePurchase(@RequestBody String purchsearray) {
	 * System.out.println("--------save savePurchase-------------"); Purchase
	 * purchase=null; POInvoice poinvoice=null; POInvoiceDetails podetails=null;
	 * RandomNumber randomnumber=null; try { ObjectMapper mapper = new
	 * ObjectMapper(); System.out.println("Json -->"+purchsearray); randomnumber =
	 * randomnumberdal.getRandamNumber();
	 * System.out.println("PO Invoice random number-->"+randomnumber.
	 * getPoinvoicenumber());
	 * System.out.println("PO Invoice random code-->"+randomnumber.getPoinvoicecode(
	 * )); String invoice = randomnumber.getPoinvoicecode() +
	 * randomnumber.getPoinvoicenumber();
	 * System.out.println("Invoice number -->"+invoice); ArrayList<String> list =
	 * new ArrayList<String>(); JSONArray jsonArr = new JSONArray(purchsearray); int
	 * len = jsonArr.length(); int remove = 0; if (jsonArr != null) { for (int
	 * i=0;i<jsonArr.length();i++){ list.add(jsonArr.get(i).toString()); remove++; }
	 * } int postion = remove-1; System.out.println("Position-->"+postion);
	 * list.remove(postion); System.out.println("Size -------->"+jsonArr.length());
	 * int l=1;
	 * 
	 * for (int i = 0; i < jsonArr.length(); i++) {
	 * System.out.println("Loop 1...."+i); JSONArray arr2 = jsonArr.optJSONArray(i);
	 * 
	 * if(l==jsonArr.length()) { System.out.println("Last Value"); JSONObject
	 * jObject = arr2.getJSONObject(0);
	 * System.out.println("PO Date -->"+jObject.getString("podate"));
	 * System.out.println("Vendor Name -->"+jObject.getString("vendorname")); }
	 * 
	 * else { if(jsonArr.optJSONArray(i)!=null) { //JSONArray arr2 =
	 * jsonArr.optJSONArray(i); for (int j = 0; j < arr2.length(); j++) {
	 * System.out.println("Loop 2...."+j); if(arr2.getJSONObject(j)!=null) {
	 * JSONObject jObject = arr2.getJSONObject(j);
	 * System.out.println(jObject.getString("productName"));
	 * System.out.println(jObject.getString("category")); podetails = new
	 * POInvoiceDetails(); podetails.setInvoicenumber(invoice);// random table..
	 * podetails.setCategory(jObject.getString("category"));
	 * podetails.setItemname(jObject.getString("productName"));
	 * podetails.setDescription(jObject.getString("description"));
	 * podetails.setUnitprice(jObject.getString("unitPrice"));
	 * podetails.setQty(jObject.getString("quantity"));
	 * podetails.setSubtotal(jObject.getDouble("netAmount"));
	 * podetails.setVendorname("Alex Ubalton-VEN2");
	 * podetails.setPoDate(Custom.getCurrentInvoiceDate());
	 * purchasedal.savePurchase(podetails); } else { System.out.println("Null....");
	 * } } } else { System.out.println("Outer Null...."); } } l++; } poinvoice=new
	 * POInvoice(); poinvoice.setInvoicedate(Custom.getCurrentInvoiceDate());
	 * logger.info("Invoice Date --->"+poinvoice.getInvoicedate());
	 * poinvoice.setInvoicenumber(invoice); poinvoice.setStatus("Waiting");
	 * poinvoice.setVendorname("Alex Ubalton-VEN2"); poinvoice.setTotalqty(100);
	 * poinvoice.setTotalprice(1000000); purchasedal.savePOInvoice(poinvoice);
	 * System.out.println("Service call start....."); purchase.setStatus("success");
	 * return new ResponseEntity<Purchase>(purchase, HttpStatus.CREATED);
	 * 
	 * }
	 * 
	 * catch(NullPointerException ne) { purchase=new Purchase();
	 * System.out.println("Inside null pointer exception ....");
	 * purchase.setStatus("success"); boolean status =
	 * randomnumberdal.updateRandamNumber(randomnumber);
	 * 
	 * return new ResponseEntity<Purchase>(purchase, HttpStatus.CREATED);
	 * 
	 * } catch(Exception e) {
	 * logger.info("Exception ------------->"+e.getMessage()); e.printStackTrace();
	 * }
	 * 
	 * finally{
	 * 
	 * } return new ResponseEntity<Purchase>(purchase, HttpStatus.CREATED); } 8
	 */

	// Save
	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping(value = "/save")
	public ResponseEntity<?> savePurchase(@RequestBody String purchasesearcharray) {
		String temp = purchasesearcharray;
		System.out.println("Mapped value -->" + temp);
		System.out.println("--------save savePurchase-------------");
		Purchase purchase = null;
		POInvoice poinvoice = null;
		POInvoiceDetails podetails = null;
		RandomNumber randomnumber = null;
		int totalQty = 0;
		int totalPrice = 0;
		int totalitem = 0;
		try {
			purchase = new Purchase();
			ObjectMapper mapper = new ObjectMapper();
			// System.out.println("Vendor Name -->"+purchase.getVendorName());
			System.out.println("Post Json -->" + purchasesearcharray);
			// logger.info("Vendor name --->"+vendorName);
			// Store into parent table to show in first data table view
			randomnumber = randomnumberdal.getRandamNumber();
			System.out.println("PO Invoice random number-->" + randomnumber.getPoinvoicenumber());
			System.out.println("PO Invoice random code-->" + randomnumber.getPoinvoicecode());
			String invoice = randomnumber.getPoinvoicecode() + randomnumber.getPoinvoicenumber();
			System.out.println("Invoice number -->" + invoice);
			ArrayList<String> list = new ArrayList<String>();
			JSONArray jsonArr = new JSONArray(purchasesearcharray);
			int len = jsonArr.length();
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
				System.out.println("Loop 1...." + i);
				JSONArray arr2 = jsonArr.optJSONArray(i);
				if (l == jsonArr.length()) {
					System.out.println("Last Value");
					JSONObject jObject = arr2.getJSONObject(0);
					System.out.println("PO Date -->" + jObject.getString("podate"));
					System.out.println("Vendor Name -->" + jObject.getString("vendorname"));
					System.out.println("Delivery Cost -->" + jObject.getString("deliveryCost"));
					purchase.setVendorName(jObject.getString("vendorname"));
					purchase.setDeliveryCost(jObject.getString("deliveryCost"));

				} else {
					if (jsonArr.optJSONArray(i) != null) {
						// JSONArray arr2 = jsonArr.optJSONArray(i);
						for (int j = 0; j < arr2.length(); j++) {
							System.out.println("Loop 2...." + j);
							if (arr2.getJSONObject(j) != null) {
								JSONObject jObject = arr2.getJSONObject(j);
								System.out.println(jObject.getString("productName"));
								System.out.println(jObject.getString("category"));
								podetails = new POInvoiceDetails();
								podetails.setInvoicenumber(invoice);// random table..
								podetails.setCategory(jObject.getString("category"));
								podetails.setItemname(jObject.getString("productName"));
								podetails.setDescription(jObject.getString("description"));
								podetails.setUnitprice(jObject.getString("unitPrice"));
								podetails.setQty(jObject.getString("quantity"));
								podetails.setSubtotal(jObject.getDouble("netAmount"));
								podetails.setsNo(j+1); 
								purchasedal.savePurchase(podetails);
								totalQty += jObject.getInt("quantity");
								totalPrice += jObject.getDouble("netAmount");
								totalitem = j+1;
							} else {
								System.out.println("Null....");
							}
						}
					} else {
						System.out.println("Outer Null....");
					}
				}
				l++;
			}
			poinvoice = new POInvoice();
			poinvoice.setInvoicedate(Custom.getCurrentInvoiceDate());
			logger.info("Invoice Date --->" + poinvoice.getInvoicedate());
			poinvoice.setVendorname(purchase.getVendorName());
			poinvoice.setInvoicenumber(invoice);
			poinvoice.setStatus("Waiting");
			poinvoice.setTotalqty(totalQty);
			poinvoice.setTotalprice(totalPrice);
			poinvoice.setTotalitem(totalitem); 
			poinvoice.setDeliveryprice(purchase.getDeliveryCost()); 
			purchasedal.savePOInvoice(poinvoice);
			System.out.println("Service call start.....");
			purchase.setStatus("success");
			boolean status = randomnumberdal.updateRandamNumber(randomnumber);
			return new ResponseEntity<Purchase>(purchase, HttpStatus.CREATED);
		}

		catch (NullPointerException ne) {
			purchase = new Purchase();
			System.out.println("Inside null pointer exception ....");
			purchase.setStatus("success");
			boolean status = randomnumberdal.updateRandamNumber(randomnumber);
			return new ResponseEntity<Purchase>(purchase, HttpStatus.CREATED);

		} catch (Exception e) {
			logger.info("Exception ------------->" + e.getMessage());
			e.printStackTrace();
		}

		finally {

		}
		return new ResponseEntity<Purchase>(purchase, HttpStatus.CREATED);
	}

	// load
	ArrayList<JSONArray> res = new ArrayList<JSONArray>();

	@CrossOrigin(origins = "http://localhost:8080")
	@GetMapping(value = "/load", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> loadPurchase() {
		logger.info("------------- Inside loadPurchase-----------------");
		List<POInvoice> response = new ArrayList<POInvoice>();
		List<Purchase> responseList = new ArrayList<Purchase>();
		try {
			logger.info("-----------Inside loadPurchase Called----------");
			response = purchasedal.loadPurchase(response);
			return new ResponseEntity<List<POInvoice>>(response, HttpStatus.CREATED);

		} catch (Exception e) {
			logger.info("loadPurchase Exception ------------->" + e.getMessage());
			e.printStackTrace();
		} finally {
		}
		return new ResponseEntity<List<Purchase>>(responseList, HttpStatus.CREATED);

	}

	// get
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public ResponseEntity<?> getPurchase(String id) {
		logger.info("------------- Inside get Purchase -----------------");
		List<POInvoiceDetails> responseList = null;
		try {
			logger.info("Id ---------->" + id);
			responseList = purchasedal.getPurchase(id);
		} catch (Exception e) {
			logger.info("getPurchase Exception ------------->" + e.getMessage());
			e.printStackTrace();
		} finally {

		}
		return new ResponseEntity<List<POInvoiceDetails>>(responseList, HttpStatus.CREATED);
	}

	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/getVendorDetails", method = RequestMethod.GET)
	public ResponseEntity<?> getVendorDetails(String vendorname) {
		logger.info("------------- Inside get getVendorDetails -----------------");
		Vendor vendor = null;
		Purchase purchase = null;
		try {
			logger.info("Vendor Name -->" + vendorname);
			vendor = new Vendor();
			purchase = new Purchase();
			String[] res = vendorname.split("-");
			String vendorCode = res[1];
			logger.info("After Split Vendor Name -->" + vendorCode);
			vendor = purchasedal.getVendorDetails(vendorCode);
			purchase.setVendorName(vendor.getVendorName());
			purchase.setVendorCity(vendor.getCity());
			purchase.setVendorCountry(vendor.getCountry());
			purchase.setVendorPhone(vendor.getPhoneNumber());
			purchase.setVendorEmail(vendor.getEmail());
		} catch (Exception e) {
			logger.info("getVendorDetails Exception ------------->" + e.getMessage());
			e.printStackTrace();
		} finally {

		}
		return new ResponseEntity<Purchase>(purchase, HttpStatus.CREATED);
	}

	// Remove
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/remove", method = RequestMethod.DELETE)
	public ResponseEntity<?> removePurchase(String invoiceNumber) {
		Purchase purchase = null;
		try {
			purchase = new Purchase();
			logger.info("-----------Before Calling  removePurchase ----------");
			System.out.println("purchase code" + invoiceNumber);
			String status = purchasedal.removePurchase(invoiceNumber);
			purchase.setStatus(status);
			logger.info("-----------Successfully Called  removePurchase ----------");

		} catch (Exception e) {
			logger.info("RemovePurchase Exception ------------->" + e.getMessage());
			purchase.setStatus("failure");
			e.printStackTrace();
		} finally {

		}
		return new ResponseEntity<Purchase>(purchase, HttpStatus.CREATED);

	}

	// Remove
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/getUnitPrice", method = RequestMethod.GET)
	public ResponseEntity<?> getUnitPrice(String productName, String category) {
		Item item = null;
		try {
			item = new Item();
			logger.info("----------- Before Calling  getUnitPrice Purchase ----------");
			System.out.println("Product Name -->" + productName);
			System.out.println("category -->" + category);
			
			String[] res = productName.split("-");
			String productCode = res[1];
			logger.info("After Split productCode -->" + productCode);
			String[] response = category.split("-");
			String categoryCode = response[1];
			logger.info("After Split categoryCode -->" + categoryCode);
			
			item = purchasedal.getUnitPrice(productCode, categoryCode);
			logger.info("Unit Price ----------"+item.getPrice());

		} catch (Exception e) {
			logger.info("getUnitPrice Exception ------------->" + e.getMessage());
			e.printStackTrace();
		} finally {

		}
		return new ResponseEntity<Item>(item, HttpStatus.CREATED);
	}
	
	//------- Load Item --
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/loadItem", method = RequestMethod.GET)
	public ResponseEntity<?> loadItem(String category) {
		logger.info("------------- Inside loadItem -----------------");
		List<Item> item = null; 
		Purchase purchase;
		List<Purchase> responseList = new ArrayList<Purchase>();
		try {
			logger.info("Category Name -->" + category);
			item = new ArrayList<Item>();
			String[] res = category.split("-");
			String categoryCode = res[1];
			logger.info("After Split Category Code -->" + categoryCode);
			item = purchasedal.loadItem(categoryCode);
			for (Item itemList : item) {
				purchase = new Purchase();
				purchase.setProductName(itemList.getProductname() + "-" + itemList.getProdcode());
				responseList.add(purchase);
			}
			logger.info("-- list Size --->"+responseList.size());
		} catch (Exception e) {
			logger.info("loadItem Exception ------------->" + e.getMessage());
			e.printStackTrace();
		} finally {

		}
		return new ResponseEntity<List<Purchase>>(responseList, HttpStatus.CREATED);
	}

	// Remove
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/removePartId", method = RequestMethod.DELETE)
	public ResponseEntity<?> removePartId(String id, String invoiceNumber) {
		Purchase purchase = null;
		List<POInvoiceDetails> responseList = null;
		int temp = 0; 
		try {
			purchase = new Purchase();
			logger.info("----------- Before Calling remove Particular Purchase ----------");
			System.out.println("ObjectID -->" + id);
			System.out.println("purchaseCode -->" + invoiceNumber);
			// ---- Check List Size from POInvoiceDetails Table
			responseList = purchasedal.getPurchase(invoiceNumber);
			logger.info("List Size -->" + responseList.size());
			if (responseList.size() == 0 || responseList.size() == 1 ) {
				temp = 1;
			} else {
				temp = 2;
			}

			String status = purchasedal.removePartId(id, invoiceNumber, temp);
			purchase.setStatus(status);
			logger.info("-----------Successfully Called  removePurchase ----------");

		} catch (Exception e) {
			logger.info("RemovePurchase Exception ------------->" + e.getMessage());
			purchase.setStatus("failure");
			e.printStackTrace();
		} finally {

		}
		return new ResponseEntity<Purchase>(purchase, HttpStatus.CREATED);

	}
	
	// Update
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	public ResponseEntity<?> updatePurchase(@RequestBody POInvoiceDetails purchase) {
		try {
			logger.info("--- Inside Product Edit ---");
			purchase = purchasedal.updatePurchase(purchase);
			return new ResponseEntity<POInvoiceDetails>(purchase, HttpStatus.CREATED);

		} catch (Exception e) {
			logger.info("Exception ------------->" + e.getMessage());
			e.printStackTrace();
		} finally {

		}
		return new ResponseEntity<POInvoiceDetails>(purchase, HttpStatus.CREATED);
	}

	

}
