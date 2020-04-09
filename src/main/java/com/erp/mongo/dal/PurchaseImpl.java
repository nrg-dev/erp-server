package com.erp.mongo.dal;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.erp.mongo.model.Item;
import com.erp.mongo.model.POInvoice;
import com.erp.mongo.model.POInvoiceDetails;
import com.erp.mongo.model.POReturnDetails;
import com.erp.mongo.model.PurchaseOrder;
import com.erp.mongo.model.Vendor;

@Repository
public class PurchaseImpl implements PurchaseDAL {

	public static final Logger logger = LoggerFactory.getLogger(PurchaseImpl.class);

	@Autowired
	private MongoTemplate mongoTemplate;

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
	
	public List<POInvoice> loadInvoice(){
		// List<PurchaseOrder>
		List<POInvoice> list = mongoTemplate.findAll(POInvoice.class);// Load Invoice
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
			logger.info("DAO item size -->" + itemlist.size());

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
	
	public List<PurchaseOrder> loadPO(){
		List<PurchaseOrder> list=null;
		logger.info("DAO Vendor item load all");
		list = mongoTemplate.findAll(PurchaseOrder.class);
		return list;
	}
	
	public PurchaseOrder savePO(PurchaseOrder purchaseorder) {
		logger.info("DAO PurchaseOrder");
		logger.info("PO Number-->"+purchaseorder.getPocode());
		mongoTemplate.save(purchaseorder);
		purchaseorder.setStatus("success"); 
		return purchaseorder;
	}
	
	// Update PO With Invoice Number
	public boolean updatePO(String invoice,String[] value) {
		logger.info("DAO updatePO");
		Update update = null;//new Update();
		Query query = null;//new Query();
		String changestatus="Invoiced";
		try {
			for(String v:value) {
				update = new Update();
				query = new Query();
				logger.info("PO numbers-->"+v);
				query.addCriteria(Criteria.where("pocode").is(v));
				update.set("invoicenumber", invoice);
				update.set("status", changestatus);
				mongoTemplate.updateFirst(query, update, PurchaseOrder.class);
			}
			logger.info("updatePO done!");
			return true;
		}catch(Exception e) {
			logger.error("Exception-->"+e.getMessage());
			return false;
		}finally {
			changestatus=null;
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
			logger.info("PO deleted"+id);
			return true;
		}
		
		
}
