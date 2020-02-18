package com.erp.mongo.dal;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.erp.mongo.model.Customer;
import com.erp.mongo.model.Item;
import com.erp.mongo.model.POInvoice;
import com.erp.mongo.model.SOInvoice;
import com.erp.mongo.model.SOInvoiceDetails;
import com.erp.mongo.model.SOReturnDetails;

@Repository
public class SalesImpl implements SalesDAL {

	public static final Logger logger = LoggerFactory.getLogger(SalesImpl.class);

	@Autowired
	private MongoTemplate mongoTemplate;

	/*
	 * @Autowired ErpBo investmentBo1;
	 */

	// Save SO Invoice
	public SOInvoice saveSOInvoice(SOInvoice soinvoice) {
		System.out.println("Before save Invoice");
		mongoTemplate.save(soinvoice);
		System.out.println("After save Invoice");
		return soinvoice;

	}

	// Save SO Invoice details
	@Override
	public SOInvoiceDetails saveSales(SOInvoiceDetails salesorder) {
		System.out.println("Before save SO Invoice details");
		mongoTemplate.save(salesorder);
		System.out.println("After save SO Invoice details");
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
	public SOInvoice updateSOInvoice(SOInvoice purchase) {
		Update update = new Update();
		Query query = new Query();
		query.addCriteria(Criteria.where("invoicenumber").is(purchase.getInvoicenumber()));
		
		update.set("invoicedate", purchase.getInvoicedate());
		update.set("invoicenumber", purchase.getInvoicenumber());
		update.set("customername", purchase.getCustomername());
		update.set("deliveryprice", purchase.getDeliveryprice());
		update.set("totalqty", purchase.getTotalqty());
		update.set("totalprice", purchase.getTotalprice());
		update.set("totalitem", purchase.getTotalitem());
		update.set("status", purchase.getStatus());

		mongoTemplate.updateFirst(query, update, SOInvoice.class);
		return purchase;
	}

	// Save SO Invoice details
	@Override
	public SOReturnDetails insertReturn(SOReturnDetails salesreturn) {
		System.out.println("Before save SO Return details");
		mongoTemplate.save(salesreturn);
		System.out.println("After save SO Return details");
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
}
