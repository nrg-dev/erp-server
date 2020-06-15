package com.erp.mongo.dal;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.erp.mongo.model.Customer;
import com.erp.mongo.model.Item;
import com.erp.mongo.model.POInvoice;
import com.erp.mongo.model.POReturnDetails;
import com.erp.mongo.model.PurchaseOrder;
import com.erp.mongo.model.SOInvoice;
import com.erp.mongo.model.SOInvoiceDetails;
import com.erp.mongo.model.SOReturnDetails;
import com.erp.mongo.model.SalesOrder;
import com.erp.mongo.model.Transaction;

@Repository
public class SalesImpl implements SalesDAL {

	public static final Logger logger = LoggerFactory.getLogger(SalesImpl.class);

	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Value("${paymentphase2.status}")
	private String paymentstatus2;
	
	@Value("${salesorderphase2.status}")
	private String salesorderstatus2;

	/*
	 * @Autowired ErpBo investmentBo1;
	 */

	// Save SO Invoice
	public SOInvoice saveSOInvoice(SOInvoice soinvoice) {
		logger.info("Before save Invoice");
		mongoTemplate.save(soinvoice);
		logger.info("After save Invoice");
		return soinvoice;

	}

	// Save SO Invoice details
	@Override
	public SOInvoiceDetails saveSales(SOInvoiceDetails salesorder) {
		logger.info("Before save SO Invoice details");
		mongoTemplate.save(salesorder);
		logger.info("After save SO Invoice details");
		return salesorder;
	}

	public List<Customer> loadCustomerList(List<Customer> list) {
		list = mongoTemplate.findAll(Customer.class);// .find(query, OwnTree.class); return
		return list;

	}

	public List<SOInvoice> loadSales(List<SOInvoice> list) {
		list = mongoTemplate.findAll(SOInvoice.class);// .find(query, OwnTree.class); return
		return list;

	}

	// get Purchase on Impl
	@Override
	public List<SOInvoiceDetails> getSales(String id) {
		List<SOInvoiceDetails> sodetaillist;
		Query query = new Query();
		query.addCriteria(Criteria.where("invoicenumber").is(id));
		sodetaillist = mongoTemplate.find(query, SOInvoiceDetails.class);
		return sodetaillist;
	}

	@Override
	public Customer getCustomerDetails(String customerCode) {
		Customer customer;
		Query query = new Query();
		query.addCriteria(Criteria.where("custcode").is(customerCode));
		customer = mongoTemplate.findOne(query, Customer.class);
		return customer;
	}

	// revmoe
	@Override
	public String removeSales(String invoiceNumber) {
		String response = "failure";
		Query query = new Query();
		query.addCriteria(Criteria.where("invoicenumber").is(invoiceNumber));
		mongoTemplate.remove(query, SOInvoiceDetails.class);
		mongoTemplate.remove(query, SOInvoice.class);
		response = "Success";
		return response;
	}

	// revmoe
	@Override
	public String removePartId(String id, String invoiceNumber, int temp) {
		String response = "failure";
		Query query = new Query();
		Query query2 = new Query();
		query.addCriteria(new Criteria().andOperator(Criteria.where("id").is(id),
				Criteria.where("invoicenumber").is(invoiceNumber)));
		if (temp == 1) {
			mongoTemplate.remove(query, SOInvoiceDetails.class);
			query2.addCriteria(Criteria.where("invoicenumber").is(invoiceNumber));
			mongoTemplate.remove(query2, SOInvoice.class);
		} else if (temp == 2) {
			mongoTemplate.remove(query, SOInvoiceDetails.class);
		}
		response = "Success";
		return response;
	}

	@Override
	public List<Item> loadItem(String categoryCode) {
		List<Item> list;
		Query query = new Query();
		query.addCriteria(Criteria.where("categorycode").is(categoryCode));
		list = mongoTemplate.find(query, Item.class);
		return list;
	}

	@Override
	public Item getUnitPrice(String productCode, String categoryCode) {
		Item item;
		Query query = new Query();
		query.addCriteria(Criteria.where("prodcode").is(productCode));
		/*
		 * query.addCriteria( new Criteria().andOperator(
		 * Criteria.where("categorycode").is(categoryCode),
		 * Criteria.where("prodcode").is(productCode) ) );
		 */
		item = mongoTemplate.findOne(query, Item.class);
		return item;
	}
	
	//Update SoDetails
	@Override
	public SOInvoiceDetails updateSales(SOInvoiceDetails sales) {
		Update update = new Update();
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(sales.getId()));
		
		update.set("invoicenumber", sales.getInvoicenumber());
		update.set("category", sales.getCategory());
		update.set("itemname", sales.getItemname());
		update.set("qty", sales.getQty());
		update.set("description", sales.getDescription());
		update.set("unitprice", sales.getUnitprice());
		update.set("subtotal", sales.getSubtotal());
		update.set("soDate", sales.getSoDate());
		update.set("lastUpdate", sales.getLastUpdate());
		mongoTemplate.updateFirst(query, update, SOInvoiceDetails.class);
		return sales;
	}
	
	@Override
	public SOInvoice loadSOInvoice(String invoicenumber) {
		SOInvoice soinvoice;
		Query query = new Query();
		query.addCriteria(Criteria.where("invoicenumber").is(invoicenumber));
		soinvoice = mongoTemplate.findOne(query, SOInvoice.class);
		return soinvoice;
	}
	
	// update SOInvoice
	@Override
	public SOInvoice updateSOInvoice(SOInvoice purchase,int i) {		
		Update update = new Update();
		Query query = new Query();
		query.addCriteria(Criteria.where("invoicenumber").is(purchase.getInvoicenumber()));
		if(i == 1) {
			update.set("invoicedate", purchase.getInvoicedate());
			update.set("invoicenumber", purchase.getInvoicenumber());
			update.set("customername", purchase.getCustomername());
			update.set("deliveryprice", purchase.getDeliveryprice());
			update.set("qty", purchase.getQty());
			update.set("totalprice", purchase.getTotalprice());
			//update.set("totalitem", purchase.getTotalitem());
			update.set("status", purchase.getStatus());
			mongoTemplate.findAndModify(query, update,
					new FindAndModifyOptions().returnNew(true), SOInvoice.class);
			//mongoTemplate.updateFirst(query, update, SOInvoice.class);
		}else if(i == 2 ) {
			logger.debug("Transaction Based SOInvoice Payment Status Update -->");
			update.set("paymentstatus", paymentstatus2);
			mongoTemplate.findAndModify(query, update,
					new FindAndModifyOptions().returnNew(true), SOInvoice.class);
			logger.debug("After SOInvoice Payment Status Update -->");
		}
		return purchase;
	}

	// Save SO Invoice details
	@Override
	public SOReturnDetails insertReturn(SOReturnDetails salesreturn) {
		logger.info("Before save SO Return details");
		mongoTemplate.save(salesreturn);
		logger.info("After save SO Return details");
		return salesreturn;
	}
	
	//load customer name & code
	public ArrayList<String> loadCustomerName()
	{
		ArrayList<String> list = new ArrayList<String>();
		List<Customer> customerlist = mongoTemplate.findAll(Customer.class);
		for(Customer customer:customerlist) {
			logger.info("Customer name-->"+customer.getCustomerName());
			logger.info("Customer code-->"+customer.getCustcode());
			list.add(customer.getCustomerName()+"-"+customer.getCustcode());			
		}
		return list;		
	}
	
	//----- Load PurchaseInvoice Based on date --
	public List<SOInvoice> loadfilterData(List<SOInvoice> list,String fromdate, String todate) {
		list = mongoTemplate.find(
                Query.query(Criteria.where("invoicedate").gte(fromdate).lt(todate)), 
                SOInvoice.class);
		return list;
	}
	
	//------- Save SalesOrder Details ---------
	public SalesOrder saveSO(SalesOrder salesorder) {
		logger.info("------ DAO SalesOrder ------");
		logger.debug("SO Number-->"+salesorder.getSocode());
		mongoTemplate.save(salesorder);
		salesorder.setStatus("success"); 
		return salesorder;
	}
	
	// Update PO order
	public boolean updateSalesOrder(SalesOrder salesorder) {
		Update update = new Update();
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(salesorder.getId()));
		update.set("categoryname", salesorder.getCategoryname());
		update.set("categorycode", salesorder.getCategorycode());
		update.set("productname", salesorder.getProductname());
		update.set("productcode", salesorder.getProductcode());
		update.set("customername", salesorder.getCustomername());
		update.set("customercode", salesorder.getCustomercode());
		update.set("qty", salesorder.getQty());
		update.set("unit", salesorder.getUnit());
		update.set("unitprice", salesorder.getUnitprice());
		update.set("subtotal", salesorder.getSubtotal());
		update.set("date", salesorder.getDate());
		update.set("description", salesorder.getDescription());
		mongoTemplate.updateFirst(query, update, SalesOrder.class);
		return true;
	}
	
	// Remove
	public boolean removeSO(String id) {
		logger.info("SO delete Id-->"+id);
		logger.info("SO delete start");
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(id));
		mongoTemplate.remove(query, SalesOrder.class);
		logger.info("SO deleted"+id);
		return true;
	}
	
	public List<SalesOrder> loadSO(){
		List<SalesOrder> list=null;
		Query query = new Query();
		query.with(new Sort(new Order(Direction.DESC, "socode")));
		list = mongoTemplate.find(query,SalesOrder.class);
		logger.info("Size-->"+list.size());
		return list;
	}
	
	// Update SO With Invoice Number
	public boolean updateSO(String invoice,String[] value) {
		logger.info("----- DAO updateSO -----");
		Update update = null;
		Query query = null;
		try {
			for(String v:value) {
				update = new Update();
				query = new Query();
				logger.info("SO numbers-->"+v);
				query.addCriteria(Criteria.where("socode").is(v));
				update.set("invoicenumber", invoice);
				update.set("status", salesorderstatus2);
				mongoTemplate.updateFirst(query, update, SalesOrder.class);
			}
			logger.info("updateSO done!");
			return true;
		}catch(Exception e) {
			logger.error("Exception  -->"+e.getMessage());
			return false;
		}finally {
			update=null;
			query=null;
		}
	}
	
	public List<SOInvoice> loadInvoice(String paystatus){
		List<SOInvoice> list = new ArrayList<SOInvoice>();
		Query query = new Query();
		if(paystatus.equalsIgnoreCase("All")) {
		    query.with(new Sort(new Order(Direction.DESC, "invoicenumber")));
			list = mongoTemplate.find(query,SOInvoice.class);
		}else if(paystatus.equalsIgnoreCase("Pending")) {
		    query.with(new Sort(new Order(Direction.DESC, "invoicenumber")));
		    query.addCriteria(Criteria.where("paymentstatus").is(paystatus));
			list = mongoTemplate.find(query,SOInvoice.class);
		}		
		logger.info("Size-->"+list.size());
		for (SOInvoice e : list) {
		    logger.info("Invoice Number -->"+e.getInvoicenumber());    
		}
		return list;
	
	}
	
	@Override
	public List<SOReturnDetails> loadReturn(String paystatus) {
		List<SOReturnDetails> list = new ArrayList<SOReturnDetails>();
		if(paystatus.equalsIgnoreCase("All")) {
			Query query = new Query();
		    query.with(new Sort(new Order(Direction.DESC, "invoicenumber")));
			list = mongoTemplate.find(query,SOReturnDetails.class);
		}else if(paystatus.equalsIgnoreCase("Pending")) {
			Query query = new Query();
		    query.with(new Sort(new Order(Direction.DESC, "invoicenumber")));
		    query.addCriteria(Criteria.where("paymentstatus").is(paystatus));
			list = mongoTemplate.find(query,SOReturnDetails.class);
		}		
		logger.debug("Return List Size-->"+list.size());
		return list;
	}
	
	//--- Insert Transaction Table ---
	public Transaction saveTransaction(Transaction trans) {
		logger.info("DAO saveTransaction");
		mongoTemplate.save(trans);
		trans.setStatus("success"); 
		return trans;
	}
	
	//-------- Update SoReturn Table ----
	@Override 
	public SOReturnDetails updateSOReturn(SOReturnDetails soreturn) {
		logger.info("Update SOReturn Number --->"+soreturn.getInvoicenumber());
		Update update = new Update();
		Query query = new Query();
		query.addCriteria(Criteria.where("invoicenumber").is(soreturn.getInvoicenumber()));
		update.set("paymentstatus", paymentstatus2);
		mongoTemplate.findAndModify(query, update,
				new FindAndModifyOptions().returnNew(true), SOReturnDetails.class);
		logger.debug("After POReturn Payment Status Update -->");
		return soreturn; 
	}

}
