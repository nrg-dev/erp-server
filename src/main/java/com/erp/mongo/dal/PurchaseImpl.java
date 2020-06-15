package com.erp.mongo.dal;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.erp.mongo.model.Employee;
import com.erp.mongo.model.Item;
import com.erp.mongo.model.POInvoice;
import com.erp.mongo.model.POInvoiceDetails;
import com.erp.mongo.model.POReturnDetails;
import com.erp.mongo.model.PurchaseOrder;
import com.erp.mongo.model.SOInvoice;
import com.erp.mongo.model.Transaction;
import com.erp.mongo.model.Vendor;

import org.springframework.data.domain.Sort; 
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

@Repository
public class PurchaseImpl implements PurchaseDAL {

	public static final Logger logger = LoggerFactory.getLogger(PurchaseImpl.class);

	@Autowired
	private MongoTemplate mongoTemplate;

	@Value("${stockphase1.status}")
	private String stockstatus1;
	@Value("${stockphase2.status}")
	private String stockstatus2;
	
	@Value("${paymentphase1.status}")
	private String paymentstatus1;
	@Value("${paymentphase2.status}")
	private String paymentstatus2;
	
	@Value("${invoicephase1.status}")
	private String invoicestatus1;
	@Value("${invoicephase2.status}")
	private String invoicestatus2;
	
	@Value("${purchaseorderphase2.status}")
	private String purchaseorderstatus2;
	
	/*
	 * @Autowired ErpBo investmentBo1;
	 */

	// Save PO Invoice
	public POInvoice savePOInvoice(POInvoice poinvoice) {
		logger.info("savePOInvoice");
		logger.info("Before save Invoice");
		mongoTemplate.save(poinvoice);
		logger.info("After save Invoice");
		return poinvoice;
	}

	// Save PO Invoice details
	@Override
	public POInvoiceDetails savePurchase(POInvoiceDetails purchaseorder) {
		logger.info("Before save PO Invoice details");
		mongoTemplate.save(purchaseorder);
		logger.info("After save Invoice details");
		return purchaseorder;
	}

	public List<Vendor> loadVendorList(List<Vendor> list) {
		list = mongoTemplate.findAll(Vendor.class);// .find(query, OwnTree.class); return
		return list;

	}

	/*
	 * public List<POInvoice> loadPurchase(List<POInvoice> list) { //
	 * List<PurchaseOrder> list = mongoTemplate.findAll(POInvoice.class);//
	 * .find(query, OwnTree.class); return return list; }
	 */
	
	public List<POInvoice> loadInvoice(String paystatus){
		// List<PurchaseOrder>
		List<POInvoice> list = new ArrayList<POInvoice>();
		if(paystatus.equalsIgnoreCase("All")) {
			Query query = new Query();
		    query.with(new Sort(new Order(Direction.DESC, "invoicenumber")));
			list = mongoTemplate.find(query,POInvoice.class);
		}else if(paystatus.equalsIgnoreCase("Pending")) {
			Query query = new Query();
		    query.with(new Sort(new Order(Direction.DESC, "invoicenumber")));
		    query.addCriteria(Criteria.where("paymentstatus").is(paystatus));
			list = mongoTemplate.find(query,POInvoice.class);
		}		
		logger.debug("Size-->"+list.size());
		for (POInvoice e : list) {
		    logger.debug("Invoice Number -->"+e.getInvoicenumber());    
		}
		
		//List<POInvoice> list = mongoTemplate.findAll(POInvoice.class);// Load Invoice
		return list;
	
	}


	// get Purchase on Impl
	@Override
	public List<POInvoiceDetails> getPurchase(String invoiceNumber) {
		List<POInvoiceDetails> podetaillist;
		Query query = new Query();
		query.addCriteria(Criteria.where("invoicenumber").is(invoiceNumber));
		podetaillist = mongoTemplate.find(query, POInvoiceDetails.class);
		return podetaillist;
	}

	@Override
	public Vendor getVendorDetails(String vendorCode) {
		Vendor vendor;
		Query query = new Query();
		query.addCriteria(Criteria.where("vendorcode").is(vendorCode));
		vendor = mongoTemplate.findOne(query, Vendor.class);
		return vendor;
	}

	// remove
	@Override
	public String removePurchase(String invoiceNumber) {
		String response = "failure";
		Query query = new Query();
		query.addCriteria(Criteria.where("invoicenumber").is(invoiceNumber));
		mongoTemplate.remove(query, POInvoiceDetails.class);
		mongoTemplate.remove(query, POInvoice.class);
		response = "Success";
		return response;
	}

	// remove
	@Override
	public String removePartId(String id, String invoiceNumber, int temp) {
		String response = "failure";
		Query query = new Query();
		Query query2 = new Query();
		query.addCriteria(new Criteria().andOperator(Criteria.where("id").is(id),
				Criteria.where("invoicenumber").is(invoiceNumber)));
		if (temp == 1) {
			mongoTemplate.remove(query, POInvoiceDetails.class);
			query2.addCriteria(Criteria.where("invoicenumber").is(invoiceNumber));
			mongoTemplate.remove(query2, POInvoice.class);
		} else if (temp == 2) {
			mongoTemplate.remove(query, POInvoiceDetails.class);
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

	// update PoDetails
	@Override
	public POInvoiceDetails updatePurchase(POInvoiceDetails purchase) {
		Update update = new Update();
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(purchase.getId()));
		
		update.set("invoicenumber", purchase.getInvoicenumber());
		update.set("category", purchase.getCategory());
		update.set("itemname", purchase.getItemname());
		update.set("qty", purchase.getQty());
		update.set("description", purchase.getDescription());
		update.set("unitprice", purchase.getUnitprice());
		update.set("subtotal", purchase.getSubtotal());
		update.set("poDate", purchase.getPoDate());
		update.set("lastUpdate", purchase.getLastUpdate());
		update.set("paymentStatus", purchase.getPaymentStatus());
		update.set("remainingAmount", purchase.getRemainingQty());		
		mongoTemplate.updateFirst(query, update, POInvoiceDetails.class);

		return purchase;
	}
	
	@Override
	public POInvoice loadPOInvoice(String invoicenumber) {
		POInvoice poinvoice;
		Query query = new Query();
		query.addCriteria(Criteria.where("invoicenumber").is(invoicenumber));
		poinvoice = mongoTemplate.findOne(query, POInvoice.class);
		return poinvoice;
	}
	
	@Override public POInvoice updatePOInvoice(POInvoice purchase, int i) {
		logger.info("Update POInvoice Number --->"+purchase.getInvoicenumber());
		Update update = new Update();
		Query query = new Query();
		query.addCriteria(Criteria.where("invoicenumber").is(purchase.getInvoicenumber()));
		if(i == 1) {
			update.set("invoicedate", purchase.getInvoicedate());
			update.set("invoicenumber", purchase.getInvoicenumber());
			update.set("vendorname", purchase.getVendorname());
			update.set("vendorcode", purchase.getVendorcode());
			update.set("qty", purchase.getQty());
			update.set("subtotal", purchase.getSubtotal());
			update.set("deliveryprice",purchase.getDeliveryprice());
			update.set("totalprice", purchase.getTotalprice());
			update.set("status", invoicestatus2);
			update.set("stockstatus", stockstatus2);
			update.set("base64", purchase.getBase64());
			//mongoTemplate.updateFirst(query,update, POInvoice.class);
			mongoTemplate.findAndModify(query, update,
					new FindAndModifyOptions().returnNew(true), POInvoice.class);
		}else if(i == 2) {
			logger.debug("Transaction Based POInvoice Payment Status Update -->");
			update.set("paymentstatus", paymentstatus2);
			mongoTemplate.findAndModify(query, update,
					new FindAndModifyOptions().returnNew(true), POInvoice.class);
			logger.debug("After POInvoice Payment Status Update -->");
		}
		return purchase; 
	}
	
	/*
	 * // update POInvoice
	 * 
	 * @Override public POInvoice updatePOInvoice(POInvoice purchase) { Update
	 * update = new Update(); Query query = new Query();
	 * query.addCriteria(Criteria.where("invoicenumber").is(purchase.
	 * getInvoicenumber())); update.set("invoicedate", purchase.getInvoicedate());
	 * update.set("invoicenumber", purchase.getInvoicenumber());
	 * update.set("vendorname", purchase.getVendorname());
	 * update.set("deliveryprice", purchase.getDeliveryprice());
	 * update.set("totalqty", purchase.getTotalqty()); update.set("totalprice",
	 * purchase.getTotalprice()); update.set("totalitem", purchase.getTotalitem());
	 * update.set("status", purchase.getStatus()); mongoTemplate.updateFirst(query,
	 * update, POInvoice.class); return purchase; }
	 */
	// Save PO Return details
	@Override
	public POReturnDetails insertReturn(POReturnDetails purchasereturn) {
		logger.info("Before save PO Return details");
		mongoTemplate.save(purchasereturn);
		logger.info("After save PO Return details");
		return purchasereturn;
	}
	
	// Vendor item load
	public List<Item> loadVendorItem(List<Item> itemlist, String vendorCode) {
		logger.info("DAO VendorCode -->" + vendorCode);
		if (vendorCode.equalsIgnoreCase("") || vendorCode.equalsIgnoreCase(null)) {
			logger.info("DAO Vendor item load all");
			itemlist = mongoTemplate.findAll(Item.class);
			logger.debug("DAO item size -->" + itemlist.size());

		} else {
			Query query = new Query();
			query.addCriteria(Criteria.where("vendorcode").is(vendorCode));
			itemlist = mongoTemplate.find(query, Item.class);

		}

		return itemlist;
	}
	
	//----- Load PurchaseInvoice Based on date --
	public List<POInvoice> loadfilterData(List<POInvoice> list,String fromdate, String todate) {
		list = mongoTemplate.find(
                Query.query(Criteria.where("invoicedate").gte(fromdate).lt(todate)),POInvoice.class);
		return list;
	}
	
	public List<PurchaseOrder> loadPO(int temp,String invoice){
		List<PurchaseOrder> list=null;
		Query query = new Query();
		if(temp == 1) {
			logger.info("DAO Vendor item load all");
			query.with(new Sort(new Order(Direction.DESC, "pocode")));
			list = mongoTemplate.find(query,PurchaseOrder.class);
		}else if(temp == 2) {
			query.addCriteria(Criteria.where("invoicenumber").is(invoice));
			list = mongoTemplate.find(query,PurchaseOrder.class);
		}
		
		logger.debug("Size-->"+list.size());
		return list;
	}
	
	public PurchaseOrder savePO(PurchaseOrder purchaseorder) {
		logger.info("DAO PurchaseOrder");
		logger.debug("PO Number-->"+purchaseorder.getPocode());
		mongoTemplate.save(purchaseorder);
		purchaseorder.setStatus("success"); 
		return purchaseorder;
	}
	
	// Update PO With Invoice Number
	public boolean updatePO(String invoice,String[] value) {
		logger.info("DAO updatePO");
		Update update = null;//new Update();
		Query query = null;//new Query();
		try {
			for(String v:value) {
				update = new Update();
				query = new Query();
				logger.debug("PO numbers-->"+v);
				query.addCriteria(Criteria.where("pocode").is(v));
				update.set("invoicenumber", invoice);
				update.set("status", purchaseorderstatus2);
				mongoTemplate.updateFirst(query, update, PurchaseOrder.class);
			}
			logger.info("updatePO done!");
			return true;
		}catch(Exception e) {
			logger.error("Exception-->"+e.getMessage());
			return false;
		}finally {
			update=null;
			query=null;
		}
		
	}
	
	// Update PO order
	public boolean updatePurchaseOrder(PurchaseOrder purchaseorder) {
		Update update = new Update();
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(purchaseorder.getId()));
		update.set("categoryname", purchaseorder.getCategoryname());
		update.set("categorycode", purchaseorder.getCategorycode());
		update.set("productname", purchaseorder.getProductname());
		update.set("productcode", purchaseorder.getProductcode());
		update.set("vendorname", purchaseorder.getVendorname());
		update.set("vendorcode", purchaseorder.getVendorcode());
		update.set("qty", purchaseorder.getQty());
		update.set("unit", purchaseorder.getUnit());
		update.set("unitprice", purchaseorder.getUnitprice());
		update.set("subtotal", purchaseorder.getSubtotal());
		update.set("date", purchaseorder.getDate());
		update.set("description", purchaseorder.getDescription());
		mongoTemplate.updateFirst(query, update, PurchaseOrder.class);
		return true;
		}
			

		// Remove
		public boolean removePO(String id) {
			logger.info("PO delete Id-->"+id);
			logger.info("PO delete start");
			Query query = new Query();
			query.addCriteria(Criteria.where("_id").is(id));
			mongoTemplate.remove(query, PurchaseOrder.class);
			logger.debug("PO deleted"+id);
			return true;
		}
		
		@Override
		public List<POReturnDetails> loadReturn(String paystatus) {
			List<POReturnDetails> list = new ArrayList<POReturnDetails>();
			if(paystatus.equalsIgnoreCase("All")) {
				Query query = new Query();
			    query.with(new Sort(new Order(Direction.DESC, "invoicenumber")));
				list = mongoTemplate.find(query,POReturnDetails.class);
			}else if(paystatus.equalsIgnoreCase("Pending")) {
				Query query = new Query();
			    query.with(new Sort(new Order(Direction.DESC, "invoicenumber")));
			    query.addCriteria(Criteria.where("paymentstatus").is(paystatus));
				list = mongoTemplate.find(query,POReturnDetails.class);
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
		
		//-------- Update PoReturn Table ----
		@Override 
		public POReturnDetails updatePOReturn(POReturnDetails poret) {
			logger.info("Update POReturn Number --->"+poret.getInvoicenumber());
			Update update = new Update();
			Query query = new Query();
			query.addCriteria(Criteria.where("invoicenumber").is(poret.getInvoicenumber()));
			update.set("paymentstatus", paymentstatus2);
			mongoTemplate.findAndModify(query, update,
					new FindAndModifyOptions().returnNew(true), POReturnDetails.class);
			logger.debug("After POReturn Payment Status Update -->");
			return poret; 
		}
		
}
