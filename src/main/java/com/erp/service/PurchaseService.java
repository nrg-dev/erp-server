package com.erp.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

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

import com.erp.bo.ErpBo;
import com.erp.dto.POInvoiceDto;
import com.erp.dto.Purchase;
import com.erp.mongo.dal.PurchaseDAL;
import com.erp.mongo.dal.RandomNumberDAL;
import com.erp.mongo.model.Item;
import com.erp.mongo.model.POInvoice;
import com.erp.mongo.model.POInvoiceDetails;
import com.erp.mongo.model.POReturnDetails;
import com.erp.mongo.model.PurchaseOrder;
import com.erp.mongo.model.RandomNumber;
import com.erp.mongo.model.Transaction;
import com.erp.mongo.model.Vendor;
import com.erp.util.Custom;
import com.erp.util.PDFGenerator;

@SpringBootApplication
@RestController
@RequestMapping(value = "/purchase")
public class PurchaseService implements Filter {

	public static final Logger logger = LoggerFactory.getLogger(PurchaseService.class);

	@Autowired
	ErpBo erpBo;
	
	@Value("${tran.currency}")
	private String currency;
	
	@Value("${stockphase1.status}")
	private String stockstatus1;
	@Value("${stockphase2.status}")
	private String stockstatus2;
	
	@Value("${paymentphase1.status}")
	private String paymentstatus1;
	@Value("${paymentphase2.status}")
	private String paymentstatus2;
	
	@Value("${purchaseorderphase1.status}")
	private String purchaseorderstatus1;
	
	@Value("${transphase1.status}")
	private String transstatus1;
	
	@Value("${poinvcash.desc}")
	private String poinvcash;
	
	@Value("${poretcash.desc}")
	private String poretcash;

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
		logger.info("loadVendorList");
		List<Vendor> response = new ArrayList<Vendor>();
		List<Purchase> responseList = new ArrayList<Purchase>();
		Purchase purhase;
		try {
			response = purchasedal.loadVendorList(response);
			for (Vendor venList : response) {
				purhase = new Purchase();
				purhase.setVendorName(venList.getVendorName() + "-" + venList.getVendorcode());
				responseList.add(purhase);
			}
			return new ResponseEntity<List<Purchase>>(responseList, HttpStatus.CREATED);
		} catch (Exception e) {
			logger.error("Exception-->"+e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400
		} finally {
		}

	}
	

    // Create Invoice
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/createInvoice", method = RequestMethod.POST)
	public ResponseEntity<?> createInvoice(@RequestBody POInvoiceDto poinvoicedto) {
		logger.info("createInvoice");
		logger.debug("Invoice Date-->" + poinvoicedto.getCreateddate());
		logger.debug("Sub Total-->" + poinvoicedto.getSubtotal());
		logger.debug("Delivery Charge-->" + poinvoicedto.getDeliverycharge());
		logger.debug("Total Price-->" + poinvoicedto.getTotalprice());
		logger.debug("Payment Type-->" + poinvoicedto.getPaymenttype());
		RandomNumber randomnumber = null;
		int randomId=10;
		Purchase purchase = new Purchase();
		int randomtrId=19;
		Transaction tran = new Transaction();
		try {
			randomnumber = randomnumberdal.getRandamNumber(randomId);
			String invoice = randomnumber.getCode() + randomnumber.getNumber();
			logger.debug("Invoice number-->" + invoice);
			// Update Invoice Number and get Vendor name and code
			purchasedal.updatePO(invoice,poinvoicedto.getOrdernumbers());
			POInvoice poinvoice = new POInvoice();
			poinvoice.setInvoicedate(poinvoicedto.getCreateddate());
			logger.debug("Invoice Date-->" + poinvoice.getInvoicedate());
			poinvoice.setInvoicenumber(invoice);
			poinvoice.setStatus(purchaseorderstatus1);
			poinvoice.setStockstatus(stockstatus1);
			poinvoice.setPaymenttype(poinvoicedto.getPaymenttype());
			if(poinvoicedto.getPaymenttype().equalsIgnoreCase("cash")) {
				poinvoice.setPaymentstatus(paymentstatus2); 
			}else {
				poinvoice.setPaymentstatus(paymentstatus1); 
			}
			poinvoice.setSubtotal(poinvoicedto.getSubtotal());
			poinvoice.setDeliveryprice(poinvoicedto.getDeliverycharge());
			poinvoice.setTotalprice(poinvoicedto.getSubtotal()+poinvoicedto.getDeliverycharge());
			for(long qty:poinvoicedto.getQty()) {
				poinvoice.setQty(qty);
			}
			logger.debug("Qty-->"+poinvoice.getQty());
			for(String vencode:poinvoicedto.getVendorcode()) {
				purchase.setPaymentStatus(vencode);
			}
			logger.info("Vendor Code ----->"+purchase.getPaymentStatus());
			for(String prod:poinvoicedto.getProductname()) {
				poinvoice.setProductname(prod);
			}
			logger.debug("Product Name-->"+poinvoice.getProductname());
			Vendor vendor = purchasedal.getVendorDetails(purchase.getPaymentStatus());
			purchase.setVendorName(vendor.getVendorName());
			purchase.setVendorCity(vendor.getCity());
			purchase.setVendorCountry(vendor.getCountry());
			purchase.setVendorPhone(vendor.getPhoneNumber());
			purchase.setVendorEmail(vendor.getEmail()); 
			logger.info("--------- Before Calling PDF Generator -----------");
			poinvoice.setVendorname(vendor.getVendorName());
			poinvoice.setVendorcode(vendor.getVendorcode());
			String base64=PDFGenerator.getBase64(poinvoice,purchase);
			logger.info("--------- After Calling PDF Generator -----------");
			poinvoice.setBase64(base64);
			purchasedal.savePOInvoice(poinvoice);
			// Update Random number table
			randomnumberdal.updateRandamNumber(randomnumber,randomId);
			logger.info("createInvoice done!");

			//-- Transaction Table Insert
			if(poinvoicedto.getPaymenttype().equalsIgnoreCase("cash")) {
				logger.info("Payment Type is cash!");
				randomnumber = randomnumberdal.getRandamNumber(randomtrId);
				String traninvoice = randomnumber.getCode() + randomnumber.getNumber();
				logger.debug("Transaction Invoice number-->" + traninvoice);
				tran.setTransactionnumber(traninvoice);
				tran.setTransactiondate(Custom.getCurrentInvoiceDate());
				tran.setDescription(poinvcash);
				tran.setInvoicenumber(invoice);
				tran.setCredit(0);
				tran.setDebit(poinvoicedto.getSubtotal());
				tran.setStatus(transstatus1);
				tran.setCurrency(currency);
				purchasedal.saveTransaction(tran);
				randomnumberdal.updateRandamNumber(randomnumber,randomtrId);
				logger.info("Transation Insert done!");
			}else {
				logger.info("Payment Type is not cash!");
			}
			
			return new ResponseEntity<>(HttpStatus.OK); // 200

		}catch(Exception e) {
			logger.error("Exception-->"+e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 400

		}
	
	}
	@CrossOrigin(origins = "http://localhost:8080")
	@GetMapping(value = "/loadInvoice", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> loadInvoice() {
		logger.info("loadInvoice");
		List<POInvoice> responselist = new ArrayList<POInvoice>();
		String paystatus = "All";
		try {
			responselist = purchasedal.loadInvoice(paystatus);
			return new ResponseEntity<List<POInvoice>>(responselist, HttpStatus.OK);				
		} catch (Exception e) {
			logger.error("Exception-->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400
		} finally {
		}
	}

	/*
	 * // Save
	 * 
	 * @CrossOrigin(origins = "http://localhost:4200")
	 * 
	 * @PostMapping(value = "/save") public ResponseEntity<?>
	 * savePurchase(@RequestBody String purchasesearcharray) { String temp =
	 * purchasesearcharray; logger.info("Mapped value -->" + temp);
	 * logger.info("--------save savePurchase-------------"); Purchase purchase =
	 * null; POInvoice poinvoice = null; POInvoiceDetails podetails = null;
	 * RandomNumber randomnumber = null; int totalQty = 0; int totalPrice = 0; int
	 * totalitem = 0; try { purchase = new Purchase(); //
	 * logger.info("Vendor Name -->"+purchase.getVendorName());
	 * logger.info("Post Json -->" + purchasesearcharray); //
	 * logger.info("Vendor name --->"+vendorName); // Store into parent table to
	 * show in first data table view randomnumber =
	 * randomnumberdal.getRandamNumber(1);
	 * //logger.info("PO Invoice random number-->" +
	 * randomnumber.getPoinvoicenumber()); //logger.info("PO Invoice random code-->"
	 * + randomnumber.getPoinvoicecode()); String invoice = randomnumber.getCode() +
	 * randomnumber.getNumber(); logger.info("Invoice number -->" + invoice);
	 * ArrayList<String> list = new ArrayList<String>(); JSONArray jsonArr = new
	 * JSONArray(purchasesearcharray); int remove = 0; if (jsonArr != null) { for
	 * (int i = 0; i < jsonArr.length(); i++) { list.add(jsonArr.get(i).toString());
	 * remove++; } } int postion = remove - 1; logger.info("Position-->" + postion);
	 * list.remove(postion); logger.info("Size -------->" + jsonArr.length()); int l
	 * = 1; for (int i = 0; i < jsonArr.length(); i++) { logger.info("Loop 1...." +
	 * i); JSONArray arr2 = jsonArr.optJSONArray(i); if (l == jsonArr.length()) {
	 * logger.info("Last Value"); JSONObject jObject = arr2.getJSONObject(0);
	 * logger.info("PO Date -->" + jObject.getString("podate")); //
	 * logger.info("Vendor Name -->" + jObject.getString("vendorname"));
	 * logger.info("Delivery Cost -->" + jObject.getString("deliveryCost")); //
	 * purchase.setVendorName(jObject.getString("vendorname"));
	 * purchase.setDeliveryCost(jObject.getString("deliveryCost"));
	 * 
	 * } else { if (jsonArr.optJSONArray(i) != null) { // JSONArray arr2 =
	 * jsonArr.optJSONArray(i); for (int j = 0; j < arr2.length(); j++) {
	 * logger.info("Loop 2...." + j); if (arr2.getJSONObject(j) != null) {
	 * JSONObject jObject = arr2.getJSONObject(j);
	 * logger.info(jObject.getString("productName"));
	 * logger.info(jObject.getString("category")); podetails = new
	 * POInvoiceDetails(); podetails.setInvoicenumber(invoice);// random table..
	 * purchase.setVendorName(jObject.getString("vendorName"));
	 * podetails.setCategory(jObject.getString("category"));
	 * podetails.setItemname(jObject.getString("productName"));
	 * podetails.setDescription(jObject.getString("description"));
	 * podetails.setUnitprice(jObject.getString("unitPrice"));
	 * podetails.setQty(jObject.getString("quantity"));
	 * podetails.setSubtotal(jObject.getDouble("netAmount"));
	 * podetails.setPoDate(Custom.getCurrentInvoiceDate());
	 * podetails.setPaymentStatus("Not Paid"); String str =
	 * jObject.getString("quantity"); str = str.replaceAll("\\D", "");
	 * podetails.setRemainingQty(Integer.valueOf(str)); totalQty +=
	 * Integer.valueOf(str); logger.info("POInvoice Date --->" +
	 * podetails.getPoDate()); purchasedal.savePurchase(podetails);
	 * 
	 * totalPrice += jObject.getDouble("netAmount"); totalitem = j + 1; } else {
	 * logger.info("Null...."); } } } else { logger.info("Outer Null...."); } } l++;
	 * } poinvoice = new POInvoice();
	 * poinvoice.setInvoicedate(Custom.getCurrentInvoiceDate());
	 * logger.info("Invoice Date --->" + poinvoice.getInvoicedate());
	 * poinvoice.setVendorname(purchase.getVendorName());
	 * poinvoice.setInvoicenumber(invoice); poinvoice.setStatus("Pending");
	 * //poinvoice.setTotalqty(totalQty); poinvoice.setTotalprice(totalPrice);
	 * //poinvoice.setTotalitem(totalitem);
	 * //poinvoice.setDeliveryprice(purchase.getDeliveryCost());
	 * purchasedal.savePOInvoice(poinvoice); logger.info("Service call start.....");
	 * // purchase.setStatus("success");
	 * randomnumberdal.updateRandamNumber(randomnumber); return new
	 * ResponseEntity<>(HttpStatus.OK); }
	 * 
	 * catch (Exception e) { logger.info("Exception ------------->" +
	 * e.getMessage()); return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400
	 * }
	 * 
	 * finally { poinvoice = null; purchase = null; poinvoice = null; podetails =
	 * null; randomnumber = null; } }
	 */

	// load
	ArrayList<JSONArray> res = new ArrayList<JSONArray>();

	/*
	 * @CrossOrigin(origins = "http://localhost:8080")
	 * 
	 * @GetMapping(value = "/load", produces = MediaType.APPLICATION_JSON_VALUE)
	 * public ResponseEntity<?> loadPurchase() {
	 * logger.info("------------- Inside loadPurchase-----------------");
	 * List<POInvoice> response = new ArrayList<POInvoice>(); List<Purchase>
	 * responseList = new ArrayList<Purchase>(); List<POInvoiceDetails> podetail =
	 * new ArrayList<POInvoiceDetails>(); Purchase purchase = null; try {
	 * logger.info("-----------Inside loadPurchase Called----------"); response =
	 * purchasedal.loadPurchase(response); for (POInvoice res : response) { purchase
	 * = new Purchase(); String itemnameList = ""; String qtylist = ""; String
	 * totalAmountlist = ""; String prodList = ""; podetail =
	 * purchasedal.getPurchase(res.getInvoicenumber()); for (int i = 0; i <
	 * podetail.size(); i++) { logger.info("Product Name -->" +
	 * podetail.get(i).getItemname()); // itemnameList =
	 * itemnameList+podetail.get(i).getItemname() + // System.lineSeparator()+
	 * System.lineSeparator(); itemnameList = itemnameList +
	 * podetail.get(i).getItemname() + "\r\n"; prodList = prodList +
	 * podetail.get(i).getItemname() + "," + System.lineSeparator();
	 * logger.info("Qty -->" + podetail.get(i).getQty()); // qtylist =
	 * qtylist+podetail.get(i).getQty() + System.lineSeparator()+ //
	 * System.lineSeparator(); qtylist = qtylist + podetail.get(i).getQty() +
	 * "\r\n"; logger.info("Total -->" + podetail.get(i).getSubtotal());
	 * totalAmountlist = totalAmountlist + podetail.get(i).getSubtotal() +
	 * System.lineSeparator() + System.lineSeparator(); }
	 * logger.info("Particular invoice productList -->" + itemnameList);
	 * purchase.setInvoiceNumber(res.getInvoicenumber());
	 * purchase.setPoDate(res.getInvoicedate());
	 * purchase.setVendorName(res.getVendorname());
	 * purchase.setTotalAmount(res.getTotalprice());
	 * //purchase.setDeliveryCost(res.getDeliveryprice());
	 * purchase.setStatus(res.getStatus()); purchase.setProductName(itemnameList);
	 * purchase.setQuantity(qtylist); purchase.setUnitPrice(totalAmountlist);
	 * //purchase.setTotalItem(res.getTotalitem());
	 * purchase.setDescription(prodList); responseList.add(purchase); } return new
	 * ResponseEntity<List<Purchase>>(responseList, HttpStatus.CREATED); } catch
	 * (Exception e) { logger.info("loadPurchase Exception ------------->" +
	 * e.getMessage()); return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400
	 * } finally { } }
	 */

	// get
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public ResponseEntity<?> getPurchase(String id) {
		logger.info("getPurchase");
		List<POInvoiceDetails> responseList = null;
		try {
			logger.debug("Id-->" + id);
			responseList = purchasedal.getPurchase(id);
		} catch (Exception e) {
			logger.error("Exception-->"+e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400
		} finally {

		}
		return new ResponseEntity<List<POInvoiceDetails>>(responseList, HttpStatus.CREATED);
	}

	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/getVendorDetails", method = RequestMethod.GET)
	public ResponseEntity<?> getVendorDetails(String vendorname) {
		logger.info("getVendorDetails");
		Vendor vendor = null;
		Purchase purchase = null;
		try {
			logger.debug("Vendor Name-->" +vendorname);
			vendor = new Vendor();
			purchase = new Purchase();
			String[] res = vendorname.split("-");
			String vendorCode = res[1];
			logger.debug("After Split Vendor Name-->" +vendorCode);
			vendor = purchasedal.getVendorDetails(vendorCode);
			purchase.setVendorName(vendor.getVendorName());
			purchase.setVendorCity(vendor.getCity());
			purchase.setVendorCountry(vendor.getCountry());
			purchase.setVendorPhone(vendor.getPhoneNumber());
			purchase.setVendorEmail(vendor.getEmail());
		} catch (Exception e) {
			logger.error("Exception-->"+e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400
		} finally {

		}
		return new ResponseEntity<Purchase>(purchase, HttpStatus.CREATED);
	}

	// Remove
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/remove", method = RequestMethod.DELETE)
	public ResponseEntity<?> removePurchase(String invoiceNumber) {
		logger.info("removePurchase");
		Purchase purchase = null;
		try {
			purchase = new Purchase();
			logger.info("Before Calling  removePurchase");
			logger.debug("purchase code" + invoiceNumber);
			String status = purchasedal.removePurchase(invoiceNumber);
			purchase.setStatus(status);
			logger.info("Successfully Called  removePurchase");

		} catch (Exception e) {
			logger.error("Exception-->"+e.getMessage());
			purchase.setStatus("failure");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400
		} finally {

		}
		return new ResponseEntity<Purchase>(purchase, HttpStatus.CREATED);

	}

	// Get UnitPrize
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/getUnitPrice", method = RequestMethod.GET)
	public ResponseEntity<?> getUnitPrice(String productName, String category) {
		logger.info("getUnitPrice");
		Item item = null;
		try {
			item = new Item();
			logger.debug("Product Name-->" + productName);
			logger.debug("category-->" + category);
			String[] res = productName.split("-");
			String productCode = res[1];
			logger.debug("After Split productCode-->" + productCode);
			String[] response = category.split("-");
			String categoryCode = response[1];
			logger.debug("After Split categoryCode -->" + categoryCode);
			item = purchasedal.getUnitPrice(productCode, categoryCode);
			logger.debug("Unit Price-->" + item.getSellingprice());
			return new ResponseEntity<Item>(item, HttpStatus.CREATED);

		} catch (Exception e) {
			logger.error("Exception-->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400
		} finally {

		}
		// return new ResponseEntity<Item>(item, HttpStatus.CREATED);
	}

	// ------- Load Item --
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/loadItem", method = RequestMethod.GET)
	public ResponseEntity<?> loadItem(String category) {
		logger.info("loadItem");
		List<Item> item = null;
		Purchase purchase;
		List<Purchase> responseList = new ArrayList<Purchase>();
		try {
			logger.debug("Category Name-->" + category);
			item = new ArrayList<Item>();
			String[] res = category.split("-");
			String categoryCode = res[1];
			logger.debug("After Split Category Code-->" + categoryCode);
			item = purchasedal.loadItem(categoryCode);
			for (Item itemList : item) {
				purchase = new Purchase();
				purchase.setProductName(itemList.getProductname() + "-" + itemList.getProdcode());
				responseList.add(purchase);
			}
			logger.debug("List Size-->" + responseList.size());
			return new ResponseEntity<List<Purchase>>(responseList, HttpStatus.CREATED);
		} catch (Exception e) {
			logger.error("Exception-->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400
		} finally {

		}
		// return new ResponseEntity<List<Purchase>>(responseList, HttpStatus.CREATED);
	}

	// ------- Load Purchase Order --
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/savePO", method = RequestMethod.POST)
	public ResponseEntity<?> savePO(@RequestBody PurchaseOrder purchaseorder) {
		logger.info("savePO");
		RandomNumber randomnumber = null;
		int randomId=6;
		try {
			randomnumber = randomnumberdal.getRandamNumber(randomId);
			String pocode = randomnumber.getCode() + randomnumber.getNumber();
			logger.debug("purchase code-->" + pocode);
			purchaseorder.setPocode(pocode);
			purchaseorder = purchasedal.savePO(purchaseorder);
			if (purchaseorder.getStatus().equalsIgnoreCase("success")) {
				randomnumberdal.updateRandamNumber(randomnumber,randomId);
			}
		} catch (Exception e) {
			logger.error("Exception-->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400
		} finally {

		}
		return new ResponseEntity<>(HttpStatus.OK); // 200
	}

	// ------- Load Purchase Order --
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/loadPO", method = RequestMethod.GET)
	public ResponseEntity<?> loadPO() {
		logger.info("loadPO");
		List<PurchaseOrder> polist = null;// new ArrayList<PurchaseOrder>();
		try {
			polist = purchasedal.loadPO(1,"all");
			return new ResponseEntity<List<PurchaseOrder>>(polist, HttpStatus.CREATED);

		} catch (Exception e) {
			logger.error("Exception-->"+e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {

		}
	}
	
		// Remove PO Order
		@CrossOrigin(origins = "http://localhost:8080")
		@RequestMapping(value = "/removePO", method = RequestMethod.DELETE)
		public ResponseEntity<?> removePO(String id) {
			logger.info("removePO");
			logger.debug("Service PO delete Id-->"+id);
			try {
				boolean stauts = purchasedal.removePO(id);
				if(stauts) {
					return new ResponseEntity<>(HttpStatus.OK); 
				}
				return new ResponseEntity<>(HttpStatus.OK); 
			} catch (Exception e) {
				logger.error("Exception-->"+e.getMessage());
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			} finally {

			}
		}

		// Update PO
		@CrossOrigin(origins = "http://localhost:8080")
		@RequestMapping(value = "/updatePurchaseOrder", method = RequestMethod.PUT)
		public ResponseEntity<?> updatePurchaseOrder(@RequestBody PurchaseOrder purchaseorder) {
			logger.info("updatePurchaseOrder");
			try {
				boolean stauts = purchasedal.updatePurchaseOrder(purchaseorder);
				if(stauts) {
					return new ResponseEntity<>(HttpStatus.OK); 
				}
				else {
					return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);// 500 Internal Server error

				}
			} catch (Exception e) {
				logger.error("Exception-->"+e.getMessage());
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			} finally {

			}
		
		}
	
	// Remove
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/removePartId", method = RequestMethod.DELETE)
	public ResponseEntity<?> removePartId(String id, String invoiceNumber) {
		logger.info("removePartId");
		List<POInvoiceDetails> responseList = null;
		int temp = 0;
		try {
			// purchase = new Purchase();
			logger.debug("ObjectID -->"+id);
			logger.debug("purchaseCode -->"+invoiceNumber);
			// ---- Check List Size from POInvoiceDetails Table
			responseList = purchasedal.getPurchase(invoiceNumber);
			logger.debug("List Size-->" + responseList.size());
			if (responseList.size() == 0 || responseList.size() == 1) {
				temp = 1;
			} else {
				temp = 2;
			}
			purchasedal.removePartId(id, invoiceNumber, temp);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Exception-->"+e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {

		}
	}

	/*
	 * // Update
	 * 
	 * @CrossOrigin(origins = "http://localhost:8080")
	 * 
	 * @RequestMapping(value = "/update", method = RequestMethod.PUT) public
	 * ResponseEntity<?> updatePurchase(@RequestBody String purchaseeditarray) {
	 * String temp = purchaseeditarray; logger.info("Edit Purchase value -->" +
	 * temp); logger.info("-------- Update Purchase -------------"); Purchase
	 * purchase = null; POInvoice poinvoice = null; POInvoiceDetails podetails =
	 * null; int totalQty = 0; int totalPrice = 0; int totalitem = 0; try { purchase
	 * = new Purchase(); logger.info("Get Purchase Json -->" + purchaseeditarray);
	 * ArrayList<String> list = new ArrayList<String>(); JSONArray jsonArr = new
	 * JSONArray(purchaseeditarray); int remove = 0; if (jsonArr != null) { for (int
	 * i = 0; i < jsonArr.length(); i++) { list.add(jsonArr.get(i).toString());
	 * remove++; } } int postion = remove - 1; logger.info("Edit Position-->" +
	 * postion); list.remove(postion); logger.info("Edit Size -------->" +
	 * jsonArr.length()); // int l = 1; for (int i = 0; i < jsonArr.length(); i++) {
	 * logger.info("Loop 1...."); JSONArray arr2 = jsonArr.optJSONArray(i); if
	 * (jsonArr.optJSONArray(i) != null) { for (int j = 0; j < arr2.length(); j++) {
	 * logger.info("Loop 2...."); if (arr2.getJSONObject(j) != null) { JSONObject
	 * jObject = arr2.getJSONObject(j);
	 * logger.info(jObject.getString("productName"));
	 * logger.info(jObject.getString("category")); podetails = new
	 * POInvoiceDetails(); podetails.setCategory(jObject.getString("category"));
	 * podetails.setItemname(jObject.getString("productName"));
	 * podetails.setDescription(jObject.getString("description"));
	 * podetails.setUnitprice(jObject.getString("price"));
	 * podetails.setQty(jObject.getString("quantity"));
	 * podetails.setSubtotal(jObject.getDouble("netAmount"));
	 * podetails.setLastUpdate(Custom.getCurrentInvoiceDate());
	 * podetails.setInvoicenumber(jObject.getString("invoiceNumber"));
	 * podetails.setPoDate(jObject.getString("poDate"));
	 * podetails.setId(jObject.getString("id")); String str =
	 * jObject.getString("quantity"); str = str.replaceAll("\\D", ""); totalQty +=
	 * Integer.valueOf(str); podetails.setPaymentStatus("Not Paid");
	 * podetails.setRemainingQty(Integer.valueOf(str));
	 * purchasedal.updatePurchase(podetails); totalPrice +=
	 * jObject.getDouble("netAmount"); totalitem = j + 1; } else {
	 * logger.info("Null...."); } } } else { logger.info("Outer Null...."); } //
	 * l++; } poinvoice = new POInvoice(); poinvoice =
	 * purchasedal.loadPOInvoice(podetails.getInvoicenumber());
	 * poinvoice.setInvoicenumber(podetails.getInvoicenumber());
	 * logger.info("Total Qty -->" + totalQty); logger.info("Total Price -->" +
	 * totalPrice); //poinvoice.setTotalqty(totalQty);
	 * poinvoice.setTotalprice(totalPrice);
	 * //logger.info("After PoInvoice Total Qty -->" + poinvoice.getTotalqty());
	 * logger.info("After PoInvoice Total Price -->" + poinvoice.getTotalprice());
	 * //poinvoice.setTotalitem(totalitem); purchasedal.updatePOInvoice(poinvoice);
	 * return new ResponseEntity<>(HttpStatus.OK);
	 * 
	 * } catch (Exception e) { logger.info("Exception ------------->" +
	 * e.getMessage()); return new ResponseEntity<>(HttpStatus.BAD_REQUEST); }
	 * finally { poinvoice=null; } }
	 */

	/*
	 * // SaveReturn
	 * 
	 * @CrossOrigin(origins = "http://localhost:4200")
	 * 
	 * @PostMapping(value = "/saveReturn") public ResponseEntity<?>
	 * savePurchaseReturn(@RequestBody String returnarray) { String temp =
	 * returnarray; logger.info("Mapped value -->" + temp);
	 * logger.info("--------save savePurchaseReturn-------------"); Purchase
	 * purchase = null; POReturnDetails poreturndetails = null; RandomNumber
	 * randomnumber = null; try { purchase = new Purchase();
	 * logger.info("Post Json -->" + returnarray);
	 * 
	 * JSONArray jsonArr = new JSONArray(returnarray); ArrayList<String> list = new
	 * ArrayList<String>(); logger.info("length =====" + jsonArr.length()); int
	 * remove = 0; if (jsonArr != null) { for (int i = 0; i < jsonArr.length(); i++)
	 * { list.add(jsonArr.get(i).toString()); remove++; } } int postion = remove -
	 * 1; logger.info("Position-->" + postion); list.remove(postion);
	 * logger.info("Size -------->" + jsonArr.length()); // int l = 1; for (int i =
	 * 0; i < jsonArr.length(); i++) { JSONArray arr2 = jsonArr.optJSONArray(i); if
	 * (jsonArr.optJSONArray(i) != null) { for (int j = 0; j < arr2.length(); j++) {
	 * randomnumber = randomnumberdal.getReturnRandamNumber(1); //
	 * logger.info("PO Return random number-->" +
	 * randomnumber.getPoreturninvoicenumber()); //
	 * logger.info("PO Return random code-->" +
	 * randomnumber.getPoreturninvoicecode()); String invoice =
	 * randomnumber.getCode() + randomnumber.getNumber();
	 * logger.info("Return Invoice number -->" + invoice); if (arr2.getJSONObject(j)
	 * != null) { JSONObject jObject = arr2.getJSONObject(j);
	 * logger.info(jObject.getString("productName"));
	 * logger.info(jObject.getString("category")); poreturndetails = new
	 * POReturnDetails(); poreturndetails.setInvoicenumber(invoice);// random
	 * table.. poreturndetails.setVendorname(jObject.getString("vendorName"));
	 * poreturndetails.setCategory(jObject.getString("category"));
	 * poreturndetails.setItemname(jObject.getString("productName"));
	 * poreturndetails.setQty(jObject.getString("quantity"));
	 * poreturndetails.setItemStatus(jObject.getString("itemStatus"));
	 * poreturndetails.setReturnStatus(jObject.getString("returnStatus"));
	 * poreturndetails.setPoDate(Custom.getCurrentInvoiceDate());
	 * poreturndetails.setInvid(j + 1); logger.info("POInvoice Date --->" +
	 * poreturndetails.getPoDate()); purchasedal.insertReturn(poreturndetails);
	 * //logger.info("Invoice Number --->" +
	 * randomnumber.getPoreturninvoicenumber());
	 * randomnumberdal.updatePOReturnRandamNumber(randomnumber); //logger.info( //
	 * "After Increament Invoice Number --->" +
	 * randomnumber.getPoreturninvoicenumber());
	 * 
	 * } else { logger.info("Null...."); } // l++; } } else {
	 * logger.info("Outer Null...."); } } return new
	 * ResponseEntity<>(HttpStatus.OK); }
	 * 
	 * catch (NullPointerException ne) { purchase = new Purchase();
	 * logger.info("Inside null pointer exception ....");
	 * purchase.setStatus("success");
	 * randomnumberdal.updateRandamNumber(randomnumber); return new
	 * ResponseEntity<>(HttpStatus.BAD_REQUEST); // return new
	 * ResponseEntity<Purchase>(purchase, HttpStatus.CREATED); } catch (Exception e)
	 * { logger.info("Exception ------------->" + e.getMessage()); return new
	 * ResponseEntity<>(HttpStatus.BAD_REQUEST); }
	 * 
	 * finally { purchase = null; poreturndetails = null; randomnumber = null; }
	 * 
	 * }
	 */

	// Load item name
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/loadVendorItem", method = RequestMethod.GET)
	public ResponseEntity<?> loadVendorItem(String vendorName) {
		logger.info("loadVendorItem");
		List<Item> itemlist = new ArrayList<Item>();
		try {
			String[] res = vendorName.split("-");
			String vendorCode = res[1];
			logger.debug("After Split Vendor Code-->"+vendorCode);
			itemlist = purchasedal.loadVendorItem(itemlist, vendorCode);
			for (Item item : itemlist) {
				logger.debug("product code-->" + item.getProdcode());
			}
			return new ResponseEntity<List<Item>>(itemlist, HttpStatus.CREATED);
		} catch (Exception e) {
			logger.error("Exception-->"+e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {

		}
	}

	// ----- Filter Date Data ------
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/loadfilterData", method = RequestMethod.POST)
	public ResponseEntity<?> loadfilterData(@RequestBody Purchase purchase) {
		logger.info("loadfilterData");
		List<POInvoice> response = new ArrayList<POInvoice>();
		List<Purchase> purlist = new ArrayList<Purchase>();
		List<POInvoiceDetails> podetail = new ArrayList<POInvoiceDetails>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat your_format = new SimpleDateFormat("dd/MM/yyyy");
		try {
			logger.debug("From Date-->" + purchase.getFromdate());
			Date dt1 = format.parse(purchase.getFromdate());
			String fromdate = your_format.format(dt1);
			logger.debug("dd/MM/yyyy date-->" + fromdate);
			logger.debug("To Date-->" + purchase.getTodate());
			Date dt2 = format.parse(purchase.getTodate());
			String todate = your_format.format(dt2);
			logger.debug("dd/MM/yyyy date -->" + todate);
			response = purchasedal.loadfilterData(response, fromdate, todate);
			for (POInvoice res : response) {
				purchase = new Purchase();
				String itemnameList = "";
				String qtylist = "";
				String totalAmountlist = "";
				String prodList = "";
				podetail = purchasedal.getPurchase(res.getInvoicenumber());
				for (int i = 0; i < podetail.size(); i++) {
					logger.debug("Product Name -->"+podetail.get(i).getItemname());
					itemnameList = itemnameList + podetail.get(i).getItemname() + "\r\n";
					prodList = prodList + podetail.get(i).getItemname() + "," + System.lineSeparator();
					logger.debug("Qty -->" + podetail.get(i).getQty());
					qtylist = qtylist + podetail.get(i).getQty() + "\r\n";
					logger.debug("Total -->" + podetail.get(i).getSubtotal());
					totalAmountlist = totalAmountlist + podetail.get(i).getSubtotal() + System.lineSeparator()
							+ System.lineSeparator();
				}
				logger.debug("Particular invoice productList-->" + itemnameList);
				purchase.setInvoiceNumber(res.getInvoicenumber());
				purchase.setPoDate(res.getInvoicedate());
				purchase.setVendorName(res.getVendorname());
				purchase.setTotalAmount(res.getTotalprice());
				//purchase.setDeliveryCost(res.getDeliveryprice());
				purchase.setStatus(res.getStatus());
				purchase.setProductName(itemnameList);
				purchase.setQuantity(qtylist);
				purchase.setUnitPrice(totalAmountlist);
				//purchase.setTotalItem(res.getTotalitem());
				purchase.setDescription(prodList);
				purlist.add(purchase);
			}
			return new ResponseEntity<List<Purchase>>(purlist, HttpStatus.OK);

		} catch (Exception e) {
			logger.error("Exception-->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {

		}
	}
	
	// Create Return
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/createReturn", method = RequestMethod.POST)
	public ResponseEntity<?> createReturn(@RequestBody POReturnDetails poreturn) {
		logger.info("createReturn");
		logger.debug("Vendor Name-->" + poreturn.getVendorname());
		logger.debug("Item Name-->" + poreturn.getItemname());
		logger.debug("Invoiced Qty-->" + poreturn.getInvoicedqty());
		logger.debug("Date-->" + poreturn.getInvoiceddate());
		logger.debug("Item Status-->" + poreturn.getItemStatus());
		logger.debug("Payment Status-->" + poreturn.getReturnStatus());
		logger.debug("Qty-->" + poreturn.getQty());
		logger.debug("Price-->" + poreturn.getPrice());
		logger.debug("PoCode-->" + poreturn.getPocode());
		RandomNumber randomnumber = null;
		int randomId=8;
		int randomtrId=19;
		Transaction trans = new Transaction();
		try {
			randomnumber = randomnumberdal.getRandamNumber(randomId);
			String invoice = randomnumber.getCode() + randomnumber.getNumber();
			logger.debug("Invoice number -->" + invoice);
			poreturn.setInvoicenumber(invoice);
			poreturn.setCreateddate(Custom.getCurrentInvoiceDate());
			if(poreturn.getReturnStatus().equalsIgnoreCase("cash")) {
				poreturn.setPaymentstatus(paymentstatus2); 
			}else {
				poreturn.setPaymentstatus(paymentstatus1); 
			}
			purchasedal.insertReturn(poreturn);
			randomnumberdal.updateRandamNumber(randomnumber,randomId);
			logger.info("createReturn done!");
			
			//-- Transaction Table Insert
			if(poreturn.getReturnStatus().equalsIgnoreCase("cash")) {
				logger.info("Payment Type is cash!");
				randomnumber = randomnumberdal.getRandamNumber(randomtrId);
				String traninvoice = randomnumber.getCode() + randomnumber.getNumber();
				logger.debug("Return Transaction Invoice number-->" + traninvoice);
				trans.setTransactionnumber(traninvoice);
				trans.setTransactiondate(Custom.getCurrentInvoiceDate());
				trans.setDescription(poretcash);
				trans.setInvoicenumber(invoice);
				long totalAmount = poreturn.getPrice() * Integer.valueOf(poreturn.getQty());
				trans.setCredit(0);
				trans.setDebit(totalAmount);
				trans.setStatus(transstatus1);
				trans.setCurrency(currency);
				purchasedal.saveTransaction(trans);
				randomnumberdal.updateRandamNumber(randomnumber,randomtrId);
				logger.info("Return Transation Insert done!");
			}else {
				logger.info("Payment Type is credit and voucher!");
			}	
			return new ResponseEntity<>(HttpStatus.OK); // 200
		}catch(Exception e) {
			logger.error("Exception-->"+e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 400
		}
	}
	
	@CrossOrigin(origins = "http://localhost:8080")
	@GetMapping(value = "/loadReturn", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> loadReturn() {
		logger.info("-------- loadReturn ----------");
		List<POReturnDetails> responselist = new ArrayList<POReturnDetails>();
		String paystatus = "All";
		try {
			responselist = purchasedal.loadReturn(paystatus);
			return new ResponseEntity<List<POReturnDetails>>(responselist, HttpStatus.OK);				
		} catch (Exception e) {
			logger.error("Exception-->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400
		} finally {
		}
	}
	
}
