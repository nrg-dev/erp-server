package com.erp.service;

import java.io.IOException;
import java.text.DateFormat;
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
import com.erp.mongo.model.Item;
import com.erp.mongo.model.POInvoice;
import com.erp.mongo.model.POInvoiceDetails;
import com.erp.mongo.model.POReturnDetails;
import com.erp.mongo.model.RandomNumber;
import com.erp.mongo.model.Vendor;
import com.erp.util.AdditionOfString;
import com.erp.util.Custom;

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
					//System.out.println("Vendor Name -->" + jObject.getString("vendorname"));
					System.out.println("Delivery Cost -->" + jObject.getString("deliveryCost"));
					//purchase.setVendorName(jObject.getString("vendorname"));
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
								purchase.setVendorName(jObject.getString("vendorName"));
								podetails.setCategory(jObject.getString("category"));
								podetails.setItemname(jObject.getString("productName"));
								podetails.setDescription(jObject.getString("description"));
								podetails.setUnitprice(jObject.getString("unitPrice"));
								podetails.setQty(jObject.getString("quantity"));
								podetails.setSubtotal(jObject.getDouble("netAmount"));
								podetails.setPoDate(Custom.getCurrentInvoiceDate());
								podetails.setPaymentStatus("Not Paid");
								String str = jObject.getString("quantity");
								str = str.replaceAll("\\D", "");
								podetails.setRemainingQty(Integer.valueOf(str)); 
								totalQty += Integer.valueOf(str);
								logger.info("POInvoice Date --->" + podetails.getPoDate());
								purchasedal.savePurchase(podetails);
								
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
			poinvoice.setStatus("Pending");
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

		/*catch (NullPointerException ne) {
			purchase = new Purchase();
			System.out.println("Inside null pointer exception ....");
			purchase.setStatus("success");
			boolean status = randomnumberdal.updateRandamNumber(randomnumber);
			return new ResponseEntity<Purchase>(purchase, HttpStatus.CREATED);

		}*/ catch (Exception e) {
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
		List<POInvoiceDetails> podetail = new ArrayList<POInvoiceDetails>();
		Purchase purchase =null;
		try {
			logger.info("-----------Inside loadPurchase Called----------");
			response = purchasedal.loadPurchase(response);
			for(POInvoice res: response) {
				purchase = new Purchase();
				String itemnameList = "";
				String qtylist = "";
				String totalAmountlist = "";
				String prodList = "";
				podetail = purchasedal.getPurchase(res.getInvoicenumber());
				for(int i=0;i<podetail.size();i++) {
					logger.info("Product Name -->"+podetail.get(i).getItemname()); 
					//itemnameList = itemnameList+podetail.get(i).getItemname() + System.lineSeparator()+ System.lineSeparator();
					itemnameList = itemnameList+podetail.get(i).getItemname() + "\r\n";
					prodList = prodList + podetail.get(i).getItemname() + ","+ System.lineSeparator();
					logger.info("Qty -->"+podetail.get(i).getQty()); 
					//qtylist = qtylist+podetail.get(i).getQty() + System.lineSeparator()+ System.lineSeparator();
					qtylist = qtylist + podetail.get(i).getQty() + "\r\n";
					logger.info("Total -->"+podetail.get(i).getSubtotal()); 
					totalAmountlist = totalAmountlist+podetail.get(i).getSubtotal() + System.lineSeparator()+ System.lineSeparator();
				}
				 System.out.println("Particular invoice productList -->"+itemnameList);	
				 purchase.setInvoiceNumber(res.getInvoicenumber());
				 purchase.setPoDate(res.getInvoicedate());
				 purchase.setVendorName(res.getVendorname());
				 purchase.setTotalAmount(res.getTotalprice());
				 purchase.setDeliveryCost(res.getDeliveryprice());
				 purchase.setStatus(res.getStatus()); 
				 purchase.setProductName(itemnameList); 
				 purchase.setQuantity(qtylist);
				 purchase.setUnitPrice(totalAmountlist);
				 purchase.setTotalItem(res.getTotalitem());
				 purchase.setDescription(prodList);
				 responseList.add(purchase);
			}
			return new ResponseEntity<List<Purchase>>(responseList, HttpStatus.CREATED);
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

	// Get UnitPrize
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
			logger.info("Unit Price ----------"+item.getSellingprice());

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
	public ResponseEntity<?> updatePurchase(@RequestBody String purchaseeditarray) {
		String temp = purchaseeditarray;
		System.out.println("Edit Purchase value -->" + temp);
		System.out.println("-------- Update Purchase -------------");
		Purchase purchase = null;
		POInvoice poinvoice = null;
		POInvoiceDetails podetails = null;
		int totalQty = 0;
		int totalPrice = 0;
		int totalitem = 0;
		try {
			purchase = new Purchase();
			System.out.println("Get Purchase Json -->" + purchaseeditarray);
			ArrayList<String> list = new ArrayList<String>();
			JSONArray jsonArr = new JSONArray(purchaseeditarray);
			int remove = 0;
			if (jsonArr != null) {
				for (int i = 0; i < jsonArr.length(); i++) {
					list.add(jsonArr.get(i).toString());
					remove++;
				}
			}
			int postion = remove - 1;
			System.out.println("Edit Position-->" + postion);
			list.remove(postion);
			System.out.println("Edit Size -------->" + jsonArr.length());
			int l = 1;
			for (int i = 0; i < jsonArr.length(); i++) {
				System.out.println("Loop 1....");
				JSONArray arr2 = jsonArr.optJSONArray(i);
				if (jsonArr.optJSONArray(i) != null) {
					for (int j = 0; j < arr2.length(); j++) {
						System.out.println("Loop 2....");
						if (arr2.getJSONObject(j) != null) {
							JSONObject jObject = arr2.getJSONObject(j);
							System.out.println(jObject.getString("productName"));
							System.out.println(jObject.getString("category"));
							podetails = new POInvoiceDetails();
							podetails.setCategory(jObject.getString("category"));
							podetails.setItemname(jObject.getString("productName"));
							podetails.setDescription(jObject.getString("description"));
							podetails.setUnitprice(jObject.getString("price"));
							podetails.setQty(jObject.getString("quantity"));
							podetails.setSubtotal(jObject.getDouble("netAmount"));
							podetails.setLastUpdate(Custom.getCurrentInvoiceDate());
							podetails.setInvoicenumber(jObject.getString("invoiceNumber"));
							podetails.setPoDate(jObject.getString("poDate"));
							podetails.setId(jObject.getString("id"));
							String str = jObject.getString("quantity");
							str = str.replaceAll("\\D", "");
							totalQty += Integer.valueOf(str);
							podetails.setPaymentStatus("Not Paid");
							podetails.setRemainingQty(Integer.valueOf(str)); 
							purchasedal.updatePurchase(podetails);
							totalPrice += jObject.getDouble("netAmount");
							totalitem = j+1;
						} else {
							System.out.println("Null....");
						}
					}
				}else {
					System.out.println("Outer Null....");
				}
				l++;
			}
			poinvoice = new POInvoice(); 
			poinvoice = purchasedal.loadPOInvoice(podetails.getInvoicenumber());
			poinvoice.setInvoicenumber(podetails.getInvoicenumber());
			logger.info("Total Qty -->"+totalQty);
			logger.info("Total Price -->"+totalPrice);
			poinvoice.setTotalqty(totalQty);
			poinvoice.setTotalprice(totalPrice);
			logger.info("After PoInvoice Total Qty -->"+poinvoice.getTotalqty());
			logger.info("After PoInvoice Total Price -->"+poinvoice.getTotalprice());
			poinvoice.setTotalitem(totalitem); 
			purchasedal.updatePOInvoice(poinvoice);
			purchase.setStatus("success");
			return new ResponseEntity<Purchase>(purchase, HttpStatus.CREATED);

		} catch (Exception e) {
			logger.info("Exception ------------->" + e.getMessage());
			e.printStackTrace();
		} finally {

		}
		return new ResponseEntity<Purchase>(purchase, HttpStatus.CREATED);
	}

	// SaveReturn
	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping(value = "/saveReturn")
	public ResponseEntity<?> savePurchaseReturn(@RequestBody String returnarray) {
		String temp = returnarray;
		System.out.println("Mapped value -->" + temp);
		System.out.println("--------save savePurchaseReturn-------------");
		Purchase purchase = null;
		POReturnDetails poreturndetails = null;
		RandomNumber randomnumber = null;
		try {
			purchase = new Purchase();
			System.out.println("Post Json -->" + returnarray);
			
			JSONArray jsonArr = new JSONArray(returnarray);
			ArrayList<String> list = new ArrayList<String>();
			System.out.println("length ====="+jsonArr.length());
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
						randomnumber = randomnumberdal.getReturnRandamNumber();
						System.out.println("PO Return random number-->" + randomnumber.getPoreturninvoicenumber());
						System.out.println("PO Return random code-->" + randomnumber.getPoreturninvoicecode());
						String invoice = randomnumber.getPoreturninvoicecode() + randomnumber.getPoreturninvoicenumber();
						System.out.println("Return Invoice number -->" + invoice);
						if (arr2.getJSONObject(j) != null) {
							JSONObject jObject = arr2.getJSONObject(j);
							System.out.println(jObject.getString("productName"));
							System.out.println(jObject.getString("category"));
							poreturndetails = new POReturnDetails();
							poreturndetails.setInvoicenumber(invoice);// random table..
							poreturndetails.setVendorname(jObject.getString("vendorName"));
							poreturndetails.setCategory(jObject.getString("category"));
							poreturndetails.setItemname(jObject.getString("productName"));
							poreturndetails.setQty(jObject.getString("quantity"));
							poreturndetails.setItemStatus(jObject.getString("itemStatus"));
							poreturndetails.setReturnStatus(jObject.getString("returnStatus"));
							poreturndetails.setPoDate(Custom.getCurrentInvoiceDate());
							poreturndetails.setInvid(j+1); 
							logger.info("POInvoice Date --->" + poreturndetails.getPoDate());
							purchasedal.insertReturn(poreturndetails);
							logger.info("Invoice Number --->"+randomnumber.getPoreturninvoicenumber());
							boolean status = randomnumberdal.updateReturnRandamNumber(randomnumber);
							logger.info("After Increament Invoice Number --->"+randomnumber.getPoreturninvoicenumber());
							
						} else {
							System.out.println("Null....");
						}
						l++;
					}
				} else {
					System.out.println("Outer Null....");
				}
			}
			purchase.setStatus("success");
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
	
	// Load item name
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/loadVendorItem", method = RequestMethod.GET)
	public ResponseEntity<?> loadVendorItem(String vendorName) {
		logger.info("------------- Inside loadVendorItem -----------------");
		List<Item> itemlist = new ArrayList<Item>();
		try {
			String[] res = vendorName.split("-");
			String vendorCode = res[1];
			logger.info("After Split Vendor Code ------->" + vendorCode);
			itemlist = purchasedal.loadVendorItem(itemlist,vendorCode);
			for (Item item : itemlist) {
				System.out.println("product code -->" + item.getProdcode());

			}
			return new ResponseEntity<List<Item>>(itemlist, HttpStatus.CREATED);
		} catch (Exception e) {
			logger.info("loadVendorItem Exception ------------->" + e.getMessage());
			e.printStackTrace();
		} finally {

		}
		return new ResponseEntity<List<Item>>(itemlist, HttpStatus.CREATED);
	}

	//----- Filter Date Data ------
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/loadfilterData", method = RequestMethod.POST)
	public ResponseEntity<?> loadfilterData(@RequestBody Purchase purchase) {
		System.out.println("-------- loadfilterData ---------");
		List<POInvoice> response = new ArrayList<POInvoice>();
		List<Purchase> purlist = new ArrayList<Purchase>();
		List<POInvoiceDetails> podetail = new ArrayList<POInvoiceDetails>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat your_format = new SimpleDateFormat("dd/MM/yyyy");
		try {
			logger.info("-----------Inside loadfilterData Called----------");
			
			logger.info("From Date -->"+purchase.getFromdate());
			Date dt1 = format.parse(purchase.getFromdate());
			String fromdate = your_format.format(dt1);
			System.out.println("dd/MM/yyyy date -->"+fromdate);
			
			logger.info("To Date -->"+purchase.getTodate());
			Date dt2 = format.parse(purchase.getTodate());
			String todate = your_format.format(dt2);
			System.out.println("dd/MM/yyyy date -->"+todate);
			
			response = purchasedal.loadfilterData(response,fromdate,todate);
			for(POInvoice res: response) {
				purchase = new Purchase();
				String itemnameList = "";
				String qtylist = "";
				String totalAmountlist = "";
				String prodList = "";
				podetail = purchasedal.getPurchase(res.getInvoicenumber());
				for(int i=0;i<podetail.size();i++) {
					logger.info("Product Name -->"+podetail.get(i).getItemname()); 
					itemnameList = itemnameList+podetail.get(i).getItemname() + "\r\n";
					prodList = prodList + podetail.get(i).getItemname() + ","+ System.lineSeparator();
					logger.info("Qty -->"+podetail.get(i).getQty()); 
					qtylist = qtylist + podetail.get(i).getQty() + "\r\n";
					logger.info("Total -->"+podetail.get(i).getSubtotal()); 
					totalAmountlist = totalAmountlist+podetail.get(i).getSubtotal() + System.lineSeparator()+ System.lineSeparator();
				}
				 System.out.println("Particular invoice productList -->"+itemnameList);	
				 purchase.setInvoiceNumber(res.getInvoicenumber());
				 purchase.setPoDate(res.getInvoicedate());
				 purchase.setVendorName(res.getVendorname());
				 purchase.setTotalAmount(res.getTotalprice());
				 purchase.setDeliveryCost(res.getDeliveryprice());
				 purchase.setStatus(res.getStatus()); 
				 purchase.setProductName(itemnameList); 
				 purchase.setQuantity(qtylist);
				 purchase.setUnitPrice(totalAmountlist);
				 purchase.setTotalItem(res.getTotalitem());
				 purchase.setDescription(prodList);
				 purlist.add(purchase);
			}
			return new ResponseEntity<List<Purchase>>(purlist, HttpStatus.CREATED);
		} catch (Exception e) {
			logger.info("loadfilterData Exception ------------->" + e.getMessage());
			e.printStackTrace();
		} finally {

		}
		return new ResponseEntity<List<Purchase>>(purlist, HttpStatus.CREATED);
	}
}
