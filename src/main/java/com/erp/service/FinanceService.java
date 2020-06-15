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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.erp.dto.Purchase;
import com.erp.mongo.dal.FinanceDAL;
import com.erp.mongo.dal.PurchaseDAL;
import com.erp.mongo.dal.RandomNumberDAL;
import com.erp.mongo.dal.SalesDAL;
import com.erp.mongo.model.Item;
import com.erp.mongo.model.POInvoice;
import com.erp.mongo.model.POReturnDetails;
import com.erp.mongo.model.PettyCash;
import com.erp.mongo.model.RandomNumber;
import com.erp.mongo.model.SOInvoice;
import com.erp.mongo.model.SOReturnDetails;
import com.erp.mongo.model.Transaction;
import com.erp.util.Custom;

@SpringBootApplication
@RestController
@RequestMapping(value = "/finance")
public class FinanceService implements Filter {

	public static final Logger logger = LoggerFactory.getLogger(FinanceService.class);
	PettyCash pettycash = null;
	private final FinanceDAL financedal;
	private final PurchaseDAL purchasedal;
	private final SalesDAL salesdal;
	private final RandomNumberDAL randomnumberdal;
	
	@Value("${tran.currency}")
	private String currency;
	
	@Value("${poinvoutcash.desc}")
	private String poinvoutcash;
	@Value("${soinvoutcash.desc}")
	private String soinvoutcash;
	
	@Value("${poretcredit.desc}")
	private String poretcredit;
	@Value("${soretcredit.desc}")
	private String soretcredit;
	@Value("${poretvoucher.desc}")
	private String poretvoucher;
	@Value("${soretvoucher.desc}")
	private String soretvoucher;
	
	@Value("${paymentphase1.status}")
	private String paymentstatus1;
	@Value("${paymentphase2.status}")
	private String paymentstatus2;
	
	@Value("${transphase2.status}")
	private String transstatus2;

	public FinanceService(FinanceDAL financedal,PurchaseDAL purchasedal,SalesDAL salesdal,RandomNumberDAL randomnumberdal) {
		this.financedal = financedal;
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

	// Load customer / vendor name for populate for auto text box
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/loadCustomerVendorName", method = RequestMethod.GET)
	public ResponseEntity<?> loadCustomerVendorName() {
		logger.info("loadCustomerVendorName");
		ArrayList<String> customervendorlist = null;
		try {
			logger.info("Before Calling Load customer & vendor name list");
			customervendorlist = financedal.loadCustomerVendorName();
			logger.info("Successfully Called Load customer & vendor name list");
			return new ResponseEntity<ArrayList<String>>(customervendorlist, HttpStatus.CREATED);

		} catch (Exception e) {
			logger.error("Exception-->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {

		}
	}

	// save petty cash
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ResponseEntity<?> savePettycash(@RequestBody PettyCash finance) {
		logger.info("savePettycash");
		try {
			finance.setStatus("Active"); 
			logger.debug("PettyCash Id-->"+finance.getId());
			if(finance.getId() != null) {
				finance = financedal.updatePettyCash(finance);
			}else {
				finance = financedal.save(finance);
			}
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Exception-->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {

		}
	}

	// Load petty cash data
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/load", method = RequestMethod.GET)
	public ResponseEntity<?> load() {
		logger.info("load");
		List<PettyCash> pettycashlist = null;
		try {
			logger.info("Before Calling load pettycash list");
			pettycashlist = financedal.load();
			logger.info("Successfully Called load pettycash list");
			return new ResponseEntity<List<PettyCash>>(pettycashlist, HttpStatus.CREATED);

		} catch (Exception e) {
			logger.error("Exception-->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {

		}
	}

	// Remove
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/remove", method = RequestMethod.DELETE)
	public ResponseEntity<?> removePettyCash(String id) {
		try {
			logger.debug("Remove -->" + id);
			pettycash = new PettyCash();
			logger.info("Before Calling  removePettyCash");
			financedal.removePettyCash(id);
			pettycash.setStatus("success");
			logger.info("Successfully Called  removePettyCash");
			return new ResponseEntity<>(HttpStatus.OK);

		} catch (Exception e) {
			pettycash.setStatus("failure");
			logger.error("Exception-->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		} finally {

		}
	}
	
	@CrossOrigin(origins = "http://localhost:8080")
	@GetMapping(value = "/loadInvoice", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> loadInvoice() {
		logger.info("----- loadInvoice -------");
		List<SOInvoice> solist = new ArrayList<SOInvoice>();
		List<POInvoice> polist = new ArrayList<POInvoice>();
		List<Purchase> responselist = new ArrayList<Purchase>();
		Purchase purchase = null;
		String paystatus = "Pending";
		try {
			polist = purchasedal.loadInvoice(paystatus);
			solist = salesdal.loadInvoice(paystatus);
			
			for (POInvoice po : polist) {
				purchase = new Purchase();
				purchase.setInvoiceNumber(po.getInvoicenumber());
				purchase.setFromdate(po.getInvoicedate());
				purchase.setTotalAmount(po.getTotalprice());
				purchase.setInvoicetype(poinvoutcash);
				purchase.setStatus(po.getStatus());
				purchase.setPaymentStatus(po.getPaymentstatus());
				responselist.add(purchase);
			}
			
			for (SOInvoice so : solist) {
				purchase = new Purchase();
				purchase.setInvoiceNumber(so.getInvoicenumber());
				purchase.setFromdate(so.getInvoicedate());
				purchase.setTotalAmount(so.getTotalprice());
				purchase.setInvoicetype(soinvoutcash);
				purchase.setStatus(so.getStatus());
				purchase.setPaymentStatus(so.getPaymentstatus());
				responselist.add(purchase);   
			}
			
			return new ResponseEntity<List<Purchase>>(responselist, HttpStatus.OK);				
		} catch (Exception e) {
			logger.error("Exception-->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
		} finally {
		}
	}
	
	// Make Invoice Payment
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/makeInvPayment", method = RequestMethod.POST)
	public ResponseEntity<?> makeInvPayment(@RequestBody Transaction trans) {
		logger.info("--------- makeInvPayment -------");
		logger.info("Invoice Number -->" + trans.getInvoicenumber());
		logger.info("Debit Amount -->" + trans.getDebit());
		RandomNumber randomnumber = null;
		int randomtrId=19;
		POInvoice poinv = new POInvoice();
		try {			
			//-- Transaction Invoice Table Insert
			randomnumber = randomnumberdal.getRandamNumber(randomtrId);
			String traninvoice = randomnumber.getCode() + randomnumber.getNumber();
			logger.debug("Invoice Transaction Invoice number-->" + traninvoice);
			trans.setTransactionnumber(traninvoice);
			trans.setTransactiondate(Custom.getCurrentInvoiceDate());
			trans.setDescription(poinvoutcash);
			trans.setInvoicenumber(trans.getInvoicenumber());
			trans.setCredit(0);
			trans.setDebit(trans.getDebit());
			trans.setStatus(transstatus2);
			trans.setCurrency(currency);
			purchasedal.saveTransaction(trans);
			randomnumberdal.updateRandamNumber(randomnumber,randomtrId);
			logger.info("Finance Make Invoice Payment Transation Insert done!");
			
			//---- Update Payment in SalesInvoice
			poinv.setInvoicenumber(trans.getInvoicenumber()); 
			purchasedal.updatePOInvoice(poinv,2);
			
			return new ResponseEntity<>(HttpStatus.OK); // 200
		}catch(Exception e) {
			logger.error("Exception-->"+e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 400
		}
	}

	// Receive Payment
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/receiveInvPayment", method = RequestMethod.POST)
	public ResponseEntity<?> receiveInvPayment(@RequestBody Transaction trans) {
		logger.info("--------- receiveInvPayment -------");
		logger.info("Invoice Number -->" + trans.getInvoicenumber());
		logger.info("Debit Amount -->" + trans.getCredit());
		RandomNumber randomnumber = null;
		int randomtrId=19;
		SOInvoice soinv = new SOInvoice();
		try {			
			//-- Transaction Table Insert
			randomnumber = randomnumberdal.getRandamNumber(randomtrId);
			String traninvoice = randomnumber.getCode() + randomnumber.getNumber();
			logger.debug("Invoice Transaction Invoice number-->" + traninvoice);
			trans.setTransactionnumber(traninvoice);
			trans.setTransactiondate(Custom.getCurrentInvoiceDate());
			trans.setDescription(soinvoutcash);
			trans.setInvoicenumber(trans.getInvoicenumber());
			trans.setCredit(trans.getCredit());
			trans.setDebit(0);
			trans.setStatus(transstatus2);
			trans.setCurrency(currency);
			salesdal.saveTransaction(trans);
			randomnumberdal.updateRandamNumber(randomnumber,randomtrId);
			logger.info("Finance Receive Invoice Payment Transation Insert done!");
			
			//---- Update Payment in SalesInvoice
			soinv.setInvoicenumber(trans.getInvoicenumber()); 
			salesdal.updateSOInvoice(soinv,2);
			
			return new ResponseEntity<>(HttpStatus.OK); // 200
		}catch(Exception e) {
			logger.error("Exception-->"+e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 400
		}
	}
	
	@CrossOrigin(origins = "http://localhost:8080")
	@GetMapping(value = "/loadReturn", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> loadReturn() {
		logger.info("----- loadReturn -------");
		List<SOReturnDetails> solist = new ArrayList<SOReturnDetails>();
		List<POReturnDetails> polist = new ArrayList<POReturnDetails>();
		List<Purchase> responselist = new ArrayList<Purchase>();
		Purchase purchase = null;
		String paystatus = "Pending";
		try {
			polist = purchasedal.loadReturn(paystatus);
			solist = salesdal.loadReturn(paystatus);
			
			for (POReturnDetails po : polist) {
				purchase = new Purchase();
				purchase.setInvoiceNumber(po.getInvoicenumber());
				purchase.setFromdate(po.getCreateddate());
				long totalAmount = po.getPrice() * Integer.valueOf(po.getQty());
				purchase.setTotalAmount(totalAmount);
				if(po.getReturnStatus().equalsIgnoreCase("credit")) {
					purchase.setInvoicetype(poretcredit);
				}else if(po.getReturnStatus().equalsIgnoreCase("voucher")) {
					purchase.setInvoicetype(poretvoucher);
				}
				purchase.setStatus(po.getStatus());
				purchase.setPaymentStatus(po.getPaymentstatus());
				responselist.add(purchase);
			}
			
			for (SOReturnDetails so : solist) {
				purchase = new Purchase();
				purchase.setInvoiceNumber(so.getInvoicenumber());
				purchase.setFromdate(so.getCreateddate());
				long totalAmount = so.getPrice() * Integer.valueOf(so.getQty());
				purchase.setTotalAmount(totalAmount);
				if(so.getReturnStatus().equalsIgnoreCase("credit")) {
					purchase.setInvoicetype(soretcredit);
				}else if(so.getReturnStatus().equalsIgnoreCase("voucher")) {
					purchase.setInvoicetype(soretvoucher);
				}
				purchase.setStatus(so.getStatus());
				purchase.setPaymentStatus(so.getPaymentstatus());
				responselist.add(purchase);   
			}
			logger.debug("Response List --->"+responselist.size()); 
			return new ResponseEntity<List<Purchase>>(responselist, HttpStatus.OK);				
		} catch (Exception e) {
			logger.error("Exception-->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
		} finally {
		}
	}
	
	// Make Invoice Payment
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/makeRetPayment", method = RequestMethod.POST)
	public ResponseEntity<?> makeRetPayment(@RequestBody Transaction trans) {
		logger.info("--------- makeRetPayment -------");
		logger.info("Invoice Number -->" + trans.getInvoicenumber());
		logger.info("Debit Amount -->" + trans.getDebit());
		logger.info("Invoice Type -->" + trans.getInvoicenumber());
		RandomNumber randomnumber = null;
		int randomtrId=19;
		POReturnDetails poret = new POReturnDetails();
		try {			
			//-- Transaction Return Table Insert
			randomnumber = randomnumberdal.getRandamNumber(randomtrId);
			String traninvoice = randomnumber.getCode() + randomnumber.getNumber();
			logger.debug("Return Transaction Invoice number-->" + traninvoice);
			trans.setTransactionnumber(traninvoice);
			trans.setTransactiondate(Custom.getCurrentInvoiceDate());
			trans.setDescription(trans.getDescription());
			trans.setInvoicenumber(trans.getInvoicenumber());
			trans.setCredit(0);
			trans.setDebit(trans.getDebit());
			trans.setStatus(transstatus2);
			trans.setCurrency(currency);
			purchasedal.saveTransaction(trans);
			randomnumberdal.updateRandamNumber(randomnumber,randomtrId);
			logger.info("Finance Make Return Payment Transation Insert done!");
			
			//---- Update Return Payment in Sales Return
			poret.setInvoicenumber(trans.getInvoicenumber()); 
			purchasedal.updatePOReturn(poret);
			
			return new ResponseEntity<>(HttpStatus.OK); // 200
		}catch(Exception e) {
			logger.error("Exception-->"+e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 400
		}
	}
	
	// Receive Payment
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/receiveRetPayment", method = RequestMethod.POST)
	public ResponseEntity<?> receiveRetPayment(@RequestBody Transaction trans) {
		logger.info("--------- receiveRetPayment -------");
		logger.info("Invoice Number -->" + trans.getInvoicenumber());
		logger.info("Debit Amount -->" + trans.getCredit());
		RandomNumber randomnumber = null;
		int randomtrId=19;
		SOReturnDetails soret = new SOReturnDetails();
		try {			
			//-- Transaction Return Table Insert
			randomnumber = randomnumberdal.getRandamNumber(randomtrId);
			String traninvoice = randomnumber.getCode() + randomnumber.getNumber();
			logger.debug("Return Transaction Invoice number-->" + traninvoice);
			trans.setTransactionnumber(traninvoice);
			trans.setTransactiondate(Custom.getCurrentInvoiceDate());
			trans.setDescription(trans.getDescription());
			trans.setInvoicenumber(trans.getInvoicenumber());
			trans.setCredit(trans.getCredit());
			trans.setDebit(0);
			trans.setStatus(transstatus2);
			trans.setCurrency(currency);
			salesdal.saveTransaction(trans);
			randomnumberdal.updateRandamNumber(randomnumber,randomtrId);
			logger.info("Finance Receive Return Payment Transation Insert done!");
			
			//---- Update Return Payment in Sales Return
			soret.setInvoicenumber(trans.getInvoicenumber()); 
			salesdal.updateSOReturn(soret);
			
			return new ResponseEntity<>(HttpStatus.OK); // 200
		}catch(Exception e) {
			logger.error("Exception-->"+e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 400
		}
	}
	
	// Load petty cash data
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/profitandloss", method = RequestMethod.GET)
	public ResponseEntity<?> profitandloss() {
		logger.info("profitandloss");
		List<Transaction> profitlosslist = null;
		try {
			logger.info("Before Calling load profitandloss list");
			profitlosslist = financedal.loadProfitLoss();
			logger.info("Successfully Called load profitandloss list");
			return new ResponseEntity<List<Transaction>>(profitlosslist, HttpStatus.CREATED);

		} catch (Exception e) {
			logger.error("Exception-->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {

		}
	}
	
	// Remove
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/filterByDate", method = RequestMethod.GET)
	public ResponseEntity<?> filterByDate(String fromdate, String todate) {
		List<Transaction> trans = new ArrayList<Transaction>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat your_format = new SimpleDateFormat("dd-MM-yyyy");
		try {
			logger.info("----------- Before Calling  filterByDate ----------");
			logger.info("From Date -->" + fromdate);
			logger.info("To Date -->" + todate);
			
			Date dt1 = format.parse(fromdate);
			String startdate = your_format.format(dt1);
			logger.debug("Start Date-->" + startdate);
			Date dt2 = format.parse(todate);
			String enddate = your_format.format(dt2);
			logger.debug("End Date -->" + enddate);
			trans = financedal.loadfilterProfitData(trans, startdate, enddate);
			return new ResponseEntity<List<Transaction>>(trans, HttpStatus.CREATED);
		} catch (Exception e) {
			logger.error("Exception-->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {

		}
	}
}
