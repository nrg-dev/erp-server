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
import com.erp.mongo.dal.RandomNumberDAL;
import com.erp.mongo.dal.SalesDAL;
import com.erp.mongo.model.Category;
import com.erp.mongo.model.Customer;
import com.erp.mongo.model.Item;
import com.erp.mongo.model.POInvoice;
import com.erp.mongo.model.POInvoiceDetails;
import com.erp.mongo.model.POReturnDetails;
import com.erp.mongo.model.RandomNumber;
import com.erp.mongo.model.SOInvoice;
import com.erp.mongo.model.SOInvoiceDetails;
import com.erp.mongo.model.SOReturnDetails;
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
		System.out.println("--------save Sales-------------");
		Sales sales = null;
		SOInvoice soinvoice = null;
		SOInvoiceDetails sodetails = null;
		RandomNumber randomnumber = null;
		int totalQty = 0;
		int totalPrice = 0;
		int totalitem = 0;
		try {
			sales = new Sales();
			ObjectMapper mapper = new ObjectMapper();
			System.out.println("Post Json -->" + salesorderarray);
			// logger.info("Vendor name --->"+vendorName);
			// Store into parent table to show in first data table view
			randomnumber = randomnumberdal.getRandamNumber();
			System.out.println("SO Invoice random number-->" + randomnumber.getSalesinvoicenumber());
			System.out.println("SO Invoice random code-->" + randomnumber.getSalesinvoicecode());
			String invoice = randomnumber.getSalesinvoicecode() + randomnumber.getSalesinvoicenumber();
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
					System.out.println("SO Date -->" + jObject.getString("sodate"));
					System.out.println("Customer Name -->" + jObject.getString("customername"));
					System.out.println("Delivery Cost -->" + jObject.getString("deliveryCost"));
					sales.setCustomerName(jObject.getString("customername"));
					sales.setDeliveryCost(jObject.getString("deliveryCost"));

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
								sodetails.setSoDate(Custom.getCurrentInvoiceDate());
								logger.info("POInvoice Date --->" + sodetails.getSoDate());
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
			soinvoice.setCustomername(sales.getCustomerName());
			soinvoice.setInvoicenumber(invoice);
			soinvoice.setStatus("Pending");
			soinvoice.setTotalqty(totalQty);
			soinvoice.setTotalprice(totalPrice);
			soinvoice.setTotalitem(totalitem); 
			soinvoice.setDeliveryprice(sales.getDeliveryCost()); 
			salesdal.saveSOInvoice(soinvoice);
			System.out.println("Service call start.....");
			sales.setStatus("success");
			boolean status = randomnumberdal.updateSalesRandamNumber(randomnumber);
			return new ResponseEntity<Sales>(sales, HttpStatus.CREATED);
		}

		catch (NullPointerException ne) {
			sales = new Sales();
			System.out.println("Inside null pointer exception ....");
			sales.setStatus("success");
			boolean status = randomnumberdal.updateRandamNumber(randomnumber);
			return new ResponseEntity<Sales>(sales, HttpStatus.CREATED);

		} catch (Exception e) {
			logger.info("Exception ------------->" + e.getMessage());
			e.printStackTrace();
		}

		finally {

		}
		return new ResponseEntity<Sales>(sales, HttpStatus.CREATED);
	}

	// load
	ArrayList<JSONArray> res = new ArrayList<JSONArray>();

	@CrossOrigin(origins = "http://localhost:8080")
	@GetMapping(value = "/load", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> loadSales() {
		logger.info("------------- Inside loadSales-----------------");
		List<SOInvoice> response = new ArrayList<SOInvoice>();
		List<Sales> responseList = new ArrayList<Sales>();
		try {
			logger.info("-----------Inside loadSales Called----------");
			response = salesdal.loadSales(response);
			return new ResponseEntity<List<SOInvoice>>(response, HttpStatus.CREATED);

		} catch (Exception e) {
			logger.info("loadSales Exception ------------->" + e.getMessage());
			e.printStackTrace();
		} finally {
		}
		return new ResponseEntity<List<Sales>>(responseList, HttpStatus.CREATED);

	}

	// get
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public ResponseEntity<?> getSales(String id) {
		logger.info("------------- Inside get Sales Order -----------------");
		List<SOInvoiceDetails> responseList = null;
		try {
			logger.info("Id ---------->" + id);
			responseList = salesdal.getSales(id);
		} catch (Exception e) {
			logger.info("getSales Exception ------------->" + e.getMessage());
			e.printStackTrace();
		} finally {

		}
		return new ResponseEntity<List<SOInvoiceDetails>>(responseList, HttpStatus.CREATED);
	}

	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/getCustomerDetails", method = RequestMethod.GET)
	public ResponseEntity<?> getCustomerDetails(String customername) {
		logger.info("------------- Inside get getCustomerDetails -----------------");
		Customer customer = null;
		Sales sales = null;
		try {
			logger.info("Customer Name -->" + customername);
			customer = new Customer();
			sales = new Sales();
			String[] res = customername.split("-");
			String customerCode = res[1];
			logger.info("After Split Customer Name -->" + customerCode);
			customer = salesdal.getCustomerDetails(customerCode);
			customer.setCustomerName(customer.getCustomerName());
			customer.setCity(customer.getCity());
			customer.setCountry(customer.getCountry());
			customer.setPhoneNumber(customer.getPhoneNumber());
			customer.setEmail(customer.getEmail());
		} catch (Exception e) {
			logger.info("getCustomerDetails Exception ------------->" + e.getMessage());
			e.printStackTrace();
		} finally {

		}
		return new ResponseEntity<Customer>(customer, HttpStatus.CREATED);
	}

	// Remove
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/remove", method = RequestMethod.DELETE)
	public ResponseEntity<?> removeSales(String invoiceNumber) {
		Sales sales = null;
		try {
			sales = new Sales();
			logger.info("----------- Before Calling  removeSales ----------");
			System.out.println("Sales code" + invoiceNumber);
			String status = salesdal.removeSales(invoiceNumber);
			sales.setStatus(status);
			logger.info("-----------Successfully Called  removeSales ----------");

		} catch (Exception e) {
			logger.info("removeSales Exception ------------->" + e.getMessage());
			sales.setStatus("failure");
			e.printStackTrace();
		} finally {

		}
		return new ResponseEntity<Sales>(sales, HttpStatus.CREATED);

	}

	// Remove
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/getUnitPrice", method = RequestMethod.GET)
	public ResponseEntity<?> getUnitPrice(String productName, String category) {
		Item item = null;
		try {
			item = new Item();
			logger.info("----------- Before Calling  getUnitPrice Sales ----------");
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
		Sales sales;
		List<Sales> responseList = new ArrayList<Sales>();
		try {
			logger.info("Category Name -->" + category);
			item = new ArrayList<Item>();
			String[] res = category.split("-");
			String categoryCode = res[1];
			logger.info("After Split Category Code -->" + categoryCode);
			item = salesdal.loadItem(categoryCode);
			for (Item itemList : item) {
				sales = new Sales();
				sales.setProductName(itemList.getProductname() + "-" + itemList.getProdcode());
				responseList.add(sales);
			}
			logger.info("-- list Size --->"+responseList.size());
		} catch (Exception e) {
			logger.info("loadItem Exception ------------->" + e.getMessage());
			e.printStackTrace();
		} finally {

		}
		return new ResponseEntity<List<Sales>>(responseList, HttpStatus.CREATED);
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
			logger.info("----------- Before Calling  remove Particular Sales ----------");
			System.out.println("ObjectID -->" + id);
			System.out.println("salesCode -->" + invoiceNumber);
			// ---- Check List Size from POInvoiceDetails Table
			responseList = salesdal.getSales(invoiceNumber);
			logger.info("List Size -->" + responseList.size());
			if (responseList.size() == 0 || responseList.size() == 1 ) {
				temp = 1;
			} else {
				temp = 2;
			}

			String status = salesdal.removePartId(id, invoiceNumber, temp);
			sales.setStatus(status);
			logger.info("-----------Successfully Called  removeSales  ----------");

		} catch (Exception e) {
			logger.info("removeSales Exception ------------->" + e.getMessage());
			sales.setStatus("failure");
			e.printStackTrace();
		} finally {

		}
		return new ResponseEntity<Sales>(sales, HttpStatus.CREATED);

	}
	
	// Update
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	public ResponseEntity<?> updateSales(@RequestBody SOInvoiceDetails sodetails) {
		try {
			logger.info("--- Inside Product Edit ---");
			sodetails = salesdal.updateSales(sodetails);
			return new ResponseEntity<SOInvoiceDetails>(sodetails, HttpStatus.CREATED);

		} catch (Exception e) {
			logger.info("updateSales Exception ------------->" + e.getMessage());
			e.printStackTrace();
		} finally {

		}
		return new ResponseEntity<SOInvoiceDetails>(sodetails, HttpStatus.CREATED);
	}
	
	// SaveReturn
		@CrossOrigin(origins = "http://localhost:4200")
		@PostMapping(value = "/saveReturn")
		public ResponseEntity<?> saveSalesReturn(@RequestBody String myReturnArray) {
			String temp = myReturnArray;
			System.out.println("Mapped value -->" + temp);
			System.out.println("--------save saveSalesReturn-------------");
			Sales sales = null;
			SOReturnDetails soreturndetails = null;
			RandomNumber randomnumber = null;
			List<SOReturnDetails> sotablist=new ArrayList<SOReturnDetails>();

			try {
				sales = new Sales();
				ObjectMapper mapper = new ObjectMapper();
				System.out.println("Post Json -->" + myReturnArray);
				randomnumber = randomnumberdal.getReturnRandamNumber();
				System.out.println("SO Return random number-->" + randomnumber.getSoreturninvoicenumber());
				System.out.println("SO Return random code-->" + randomnumber.getSoreturninvoicecode());
				String invoice = randomnumber.getSoreturninvoicecode() + randomnumber.getSoreturninvoicenumber();
				System.out.println("Sales Return Invoice number -->" + invoice);
				
				JSONArray jsonArray = new JSONArray(myReturnArray);
				ArrayList<String> list = new ArrayList<String>();
				System.out.println("length ====="+jsonArray.length());
				for(int i=0;i<jsonArray.length();i++){ 
					list.add(jsonArray.getString(i));
				   System.out.println("array list----"+jsonArray.getString(i)); 
			    }
				logger.info("List Size ---->"+list.size()); 
				String[] names=null; 
				for(int m=0;m<list.size();m++){
					names=list.get(m).split(","); 
					System.out.println("Sales names -->"+names[0]);  
					int c=0;
					for (String name : names) {
						 if(c!=6){
					         c++;
					      }else if(c==6){
					         if(!name.equalsIgnoreCase("]"))
					         { 
					          int v=0;
					          soreturndetails = new SOReturnDetails();
					          for(String value:names){
					           if(v==0){
					            v++;
					            soreturndetails.setSoDate(value.replace("[", ""));
					           }else if(v==1){
					        	   soreturndetails.setItemname(value);
						           v++;
						       }else if(v==2){
					        	   soreturndetails.setCategory(value);
						           v++;
						       }else if(v==3){
						    	   soreturndetails.setCustomername(value);
						           v++;
						       }else if(v==4){
						    	   soreturndetails.setQty(value);
						           v++;
						       }else if(v==5) {
						    	   soreturndetails.setItemStatus(value);
						           v++;
						       }else if(v==6) {
						    	   soreturndetails.setReturnStatus(value.replace("]", ""));
						       }

					          }
						      sotablist.add(soreturndetails);
					       }
					    }
					}
				}
				for (int i = 0; i < sotablist.size(); i++) {
					soreturndetails.setSoDate(sotablist.get(i).getSoDate());
					soreturndetails.setItemname(sotablist.get(i).getItemname());
					soreturndetails.setCategory(sotablist.get(i).getCategory()); 
					soreturndetails.setCustomername(sotablist.get(i).getCustomername());
					soreturndetails.setQty(sotablist.get(i).getQty());
					soreturndetails.setItemStatus(sotablist.get(i).getItemStatus()); 
					soreturndetails.setReturnStatus(sotablist.get(i).getReturnStatus());
					soreturndetails.setInvoicenumber(invoice);
					logger.info("Customer Name-------------->"+soreturndetails.getCustomername()); 
					logger.info("---- Before insert into Sales Return -----"+i);
					salesdal.insertReturn(soreturndetails);
					logger.info("---- After insert into Sales Return -----"+i);
				}
				sales.setStatus("success");
				boolean status = randomnumberdal.updateSalesReturnRandamNumber(randomnumber);
				return new ResponseEntity<Sales>(sales, HttpStatus.CREATED);
			}

			catch (NullPointerException ne) {
				sales = new Sales();
				System.out.println("Inside null pointer exception ....");
				sales.setStatus("success");
				boolean status = randomnumberdal.updateRandamNumber(randomnumber);
				return new ResponseEntity<Sales>(sales, HttpStatus.CREATED);

			} catch (Exception e) {
				logger.info("Exception ------------->" + e.getMessage());
				e.printStackTrace();
			}

			finally {

			}
			return new ResponseEntity<Sales>(sales, HttpStatus.CREATED);
		}

}
