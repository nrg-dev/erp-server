package com.erp.mongo.dal;

import java.util.List;

import com.erp.dto.Purchase;
import com.erp.mongo.model.Item;
import com.erp.mongo.model.POInvoice;
import com.erp.mongo.model.POInvoiceDetails;
import com.erp.mongo.model.POReturnDetails;
import com.erp.mongo.model.PurchaseOrder;
import com.erp.mongo.model.Vendor;

public interface PurchaseDAL {
	public POInvoice savePOInvoice(POInvoice poinvoice);

	public POInvoiceDetails savePurchase(POInvoiceDetails purchaseorder);

	//public List<POInvoice> loadPurchase(List<POInvoice> list);
	public List<POInvoice> loadInvoice();

	public List<POInvoiceDetails> getPurchase(String id);

	public List<Vendor> loadVendorList(List<Vendor> response);

	public Vendor getVendorDetails(String id);

	public String removePurchase(String invoiceNumber);

	public String removePartId(String id, String invoiceNumber, int temp);

	public List<Item> loadItem(String categoryCode);

	public Item getUnitPrice(String productCode, String categoryCode);

	public POInvoiceDetails updatePurchase(POInvoiceDetails purchase);

	public POReturnDetails insertReturn(POReturnDetails poreturndetails);
	
	public POInvoice updatePOInvoice(POInvoice poinvoice);
	
	public POInvoice loadPOInvoice(String invoicenumber);

	public List<Item> loadVendorItem(List<Item> itemlist, String vendorCode);

	public List<POInvoice> loadfilterData(List<POInvoice> response,String fromdate, String todate); 
	
	public List<PurchaseOrder> loadPO(int i,String type);
	
	public PurchaseOrder savePO(PurchaseOrder purchaseorder);
	public boolean removePO(String id);
	public boolean updatePurchaseOrder(PurchaseOrder purchaseorder);
	public boolean updatePO(String invoice,String[] value);

	public List<POReturnDetails> loadReturn();
	


}