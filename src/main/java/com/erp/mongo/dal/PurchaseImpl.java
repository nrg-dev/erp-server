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

import com.erp.dto.Purchase;
import com.erp.mongo.model.Item;
import com.erp.mongo.model.POInvoice;
import com.erp.mongo.model.POInvoiceDetails;
import com.erp.mongo.model.POReturnDetails;
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
		System.out.println("Before save Invoice");
		mongoTemplate.save(poinvoice);
		System.out.println("After save Invoice");
		return poinvoice;
	}

	// Save PO Invoice details
	@Override
	public POInvoiceDetails savePurchase(POInvoiceDetails purchaseorder) {
		System.out.println("Before save PO Invoice details");
		mongoTemplate.save(purchaseorder);
		System.out.println("After save Invoice details");
		return purchaseorder;
	}

	/*
	 * @Override public PurchaseOrder savePurchase(PurchaseOrder purchaseorder) {
	 * //mongoTemplate.insert(customer);//(query, RandamNumber.class);
	 * mongoTemplate.save(purchaseorder); //po.setStatus("success"); return
	 * purchaseorder; }
	 */
	public List<Vendor> loadVendorList(List<Vendor> list) {
		list = mongoTemplate.findAll(Vendor.class);// .find(query, OwnTree.class); return
		return list;

	}

	public List<POInvoice> loadPurchase(List<POInvoice> list) {
		// List<PurchaseOrder>
		list = mongoTemplate.findAll(POInvoice.class);// .find(query, OwnTree.class); return
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

	// revmoe
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

	// revmoe
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
		update.set("poDate", purchase.getPodate());
		update.set("lastUpdate", purchase.getLastupdate());
		update.set("paymentStatus", purchase.getPaymentstatus());
		update.set("remainingAmount", purchase.getRemainingqty());
		
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
	
	// update POInvoice
	@Override
	public POInvoice updatePOInvoice(POInvoice purchase) {
		Update update = new Update();
		Query query = new Query();
		query.addCriteria(Criteria.where("invoicenumber").is(purchase.getInvoicenumber()));
		
		update.set("invoicedate", purchase.getInvoicedate());
		update.set("invoicenumber", purchase.getInvoicenumber());
		update.set("vendorname", purchase.getVendorname());
		update.set("deliveryprice", purchase.getDeliveryprice());
		update.set("totalqty", purchase.getTotalqty());
		update.set("totalprice", purchase.getTotalprice());
		update.set("totalitem", purchase.getTotalitem());
		update.set("status", purchase.getStatus());
		mongoTemplate.updateFirst(query, update, POInvoice.class);
		return purchase;
	}

	// Save PO Return details
	@Override
	public POReturnDetails insertReturn(POReturnDetails purchasereturn) {
		System.out.println("Before save PO Return details");
		mongoTemplate.save(purchasereturn);
		System.out.println("After save PO Return details");
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
}
