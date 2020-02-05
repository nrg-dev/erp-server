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
import com.erp.dto.Sales;
import com.erp.mongo.dal.PurchaseDAL;
import com.erp.mongo.dal.RandomNumberDAL;
import com.erp.mongo.dal.SalesDAL;
import com.erp.mongo.model.Category;
import com.erp.mongo.model.Customer;
import com.erp.mongo.model.Item;
import com.erp.mongo.model.POInvoice;
import com.erp.mongo.model.POInvoiceDetails;
import com.erp.mongo.model.RandomNumber;
import com.erp.mongo.model.SOInvoice;
import com.erp.mongo.model.SOInvoiceDetails;
import com.erp.mongo.model.Vendor;
import com.erp.util.Custom;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
@RestController
@RequestMapping(value = "/sales")
public class SalesService implements Filter {

	public static final Logger logger = LoggerFactory.getLogger(SalesService.class);

	@Autowired
	ErpBo erpBo;

	// private final RandamNumberRepository randamNumberRepository;

	private final SalesDAL salesdal;
	private final RandomNumberDAL randomnumberdal;

	public SalesService(SalesDAL salesdal, RandomNumberDAL randomnumberdal) {
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

	

	// ------- Load Vendor ----
	@CrossOrigin(origins = "http://localhost:8080")
	@GetMapping(value = "/loadCustomer", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> loadCustomerList() {
		logger.info("------------- Inside loadCustomer List Calling -----------------");
		List<Customer> response = new ArrayList<Customer>();
		List<Sales> responseList = new ArrayList<Sales>();
		Sales sales;
		try {
			logger.info("-----------Inside loadCustomer Called----------");
			response = salesdal.loadCustomerList(response);
			for (Customer customerlist : response) {
				sales = new Sales();
				sales.setCustomerName(customerlist.getCustomerName() + "-" + customerlist.getCustcode());
				responseList.add(sales);
			}
		} catch (Exception e) {
			logger.info("loadCustomer Exception ------------->" + e.getMessage());
			e.printStackTrace();
		} finally {
		}
		return new ResponseEntity<List<Sales>>(responseList, HttpStatus.CREATED);

	}
	

	// Save
	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping(value = "/save")
	public ResponseEntity<?> saveSales(@RequestBody String salesorderarray) {
		String temp = salesorderarray;
		System.out.println("Mapped value -->" + temp);
		System.out.println("--------save savePurchase-------------");
		Purchase purchase = null;
		SOInvoice soinvoice = null;
		SOInvoiceDetails sodetails = null;
		RandomNumber randomnumber = null;
		int totalQty = 0;
		int totalPrice = 0;
		int totalitem = 0;
		try {
			purchase = new Purchase();
			ObjectMapper mapper = new ObjectMapper();
			// System.out.println("Vendor Name -->"+purchase.getVendorName());
			System.out.println("Post Json -->" + salesorderarray);
			// logger.info("Vendor name --->"+vendorName);
			// Store into parent table to show in first data table view
			randomnumber = randomnumberdal.getRandamNumber();
			System.out.println("PO Invoice random number-->" + randomnumber.getPoinvoicenumber());
			System.out.println("PO Invoice random code-->" + randomnumber.getPoinvoicecode());
			String invoice = randomnumber.getPoinvoicecode() + randomnumber.getPoinvoicenumber();
			System.out.println("Invoice number -->" + invoice);
			ArrayList<String> list = new ArrayList<String>();
			JSONArray jsonArr = new JSONArray(salesorderarray);
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
								sodetails = new SOInvoiceDetails();
								sodetails.setInvoicenumber(invoice);// random table..
								sodetails.setCategory(jObject.getString("category"));
								sodetails.setItemname(jObject.getString("productName"));
								sodetails.setDescription(jObject.getString("description"));
								sodetails.setUnitprice(jObject.getString("unitPrice"));
								sodetails.setQty(jObject.getString("quantity"));
								sodetails.setSubtotal(jObject.getDouble("netAmount"));
								salesdal.saveSales(sodetails);
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
			soinvoice = new SOInvoice(); 
			soinvoice.setInvoicedate(Custom.getCurrentInvoiceDate());
			logger.info("Invoice Date --->" + soinvoice.getInvoicedate());
			soinvoice.setVendorname(purchase.getVendorName());
			soinvoice.setInvoicenumber(invoice);
			soinvoice.setStatus("Pending");
			soinvoice.setTotalqty(totalQty);
			soinvoice.setTotalprice(totalPrice);
			soinvoice.setTotalitem(totalitem); 
			soinvoice.setDeliveryprice(purchase.getDeliveryCost()); 
			salesdal.saveSOInvoice(soinvoice);
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
	public ResponseEntity<?> loadSales() {
		logger.info("------------- Inside loadPurchase-----------------");
		List<SOInvoice> response = new ArrayList<SOInvoice>();
		List<Sales> responseList = new ArrayList<Sales>();
		try {
			logger.info("-----------Inside loadPurchase Called----------");
			response = salesdal.loadSales(response);
			return new ResponseEntity<List<SOInvoice>>(response, HttpStatus.CREATED);

		} catch (Exception e) {
			logger.info("loadPurchase Exception ------------->" + e.getMessage());
			e.printStackTrace();
		} finally {
		}
		return new ResponseEntity<List<Sales>>(responseList, HttpStatus.CREATED);

	}

	// get
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public ResponseEntity<?> getPurchase(String id) {
		logger.info("------------- Inside get Sales Order -----------------");
		List<SOInvoiceDetails> responseList = null;
		try {
			logger.info("Id ---------->" + id);
			responseList = salesdal.getSales(id);
		} catch (Exception e) {
			logger.info("getPurchase Exception ------------->" + e.getMessage());
			e.printStackTrace();
		} finally {

		}
		return new ResponseEntity<List<SOInvoiceDetails>>(responseList, HttpStatus.CREATED);
	}

	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/getCustomerDetails", method = RequestMethod.GET)
	public ResponseEntity<?> getVendorDetails(String customername) {
		logger.info("------------- Inside get getVendorDetails -----------------");
		Customer customer = null;
		Sales sales = null;
		try {
			logger.info("Customer Name -->" + customername);
			customer = new Customer();
			sales = new Sales();
			String[] res = customername.split("-");
			String vendorCode = res[1];
			logger.info("After Split Vendor Name -->" + vendorCode);
			customer = salesdal.getCustomerDetails(vendorCode);
			customer.setCustomerName(customer.getCustomerName());
			customer.setCity(customer.getCity());
			customer.setCountry(customer.getCountry());
			customer.setPhoneNumber(customer.getPhoneNumber());
			customer.setEmail(customer.getEmail());
		} catch (Exception e) {
			logger.info("getVendorDetails Exception ------------->" + e.getMessage());
			e.printStackTrace();
		} finally {

		}
		return new ResponseEntity<Customer>(customer, HttpStatus.CREATED);
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
			String status = salesdal.removeSales(invoiceNumber);
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
			item = salesdal.getUnitPrice(productCode, categoryCode);
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
			item = salesdal.loadItem(categoryCode);
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
		Sales sales = null;
		List<SOInvoiceDetails> responseList = null;
		int temp;
		try {
			sales = new Sales();
			logger.info("----------- Before Calling  remove Particular Purchase ----------");
			System.out.println("ObjectID -->" + id);
			System.out.println("purchaseCode -->" + invoiceNumber);
			// ---- Check List Size from POInvoiceDetails Table
			responseList = salesdal.getSales(invoiceNumber);
			logger.info("List Size -->" + responseList.size());
			if (responseList.size() == 2) {
				temp = 1;
			} else {
				temp = 2;
			}

			String status = salesdal.removePartId(id, invoiceNumber, temp);
			sales.setStatus(status);
			logger.info("-----------Successfully Called  removePurchase ----------");

		} catch (Exception e) {
			logger.info("RemovePurchase Exception ------------->" + e.getMessage());
			sales.setStatus("failure");
			e.printStackTrace();
		} finally {

		}
		return new ResponseEntity<Sales>(sales, HttpStatus.CREATED);

	}
	
	// Update
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	public ResponseEntity<?> updatePurchase(@RequestBody SOInvoiceDetails sodetails) {
		try {
			logger.info("--- Inside Product Edit ---");
			sodetails = salesdal.updateSales(sodetails);
			return new ResponseEntity<SOInvoiceDetails>(sodetails, HttpStatus.CREATED);

		} catch (Exception e) {
			logger.info("Exception ------------->" + e.getMessage());
			e.printStackTrace();
		} finally {

		}
		return new ResponseEntity<SOInvoiceDetails>(sodetails, HttpStatus.CREATED);
	}

}
