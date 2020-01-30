package com.erp.mongo.dal;

import java.util.List;

import com.erp.dto.Member;
import com.erp.dto.Purchase;
import com.erp.mongo.model.Category;
import com.erp.mongo.model.Customer;
import com.erp.mongo.model.Item;
import com.erp.mongo.model.POInvoice;
import com.erp.mongo.model.POInvoiceDetails;
import com.erp.mongo.model.PurchaseOrder;
import com.erp.mongo.model.RandomNumber;
import com.erp.mongo.model.Vendor;

import java.nio.file.Path;
//import java.util.List;
import java.util.stream.Stream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
public interface PurchaseDAL {
	public POInvoice savePOInvoice(POInvoice poinvoice);
	public POInvoiceDetails savePurchase(POInvoiceDetails purchaseorder);
	public List<POInvoice> loadPurchase(List<POInvoice> list);
	public List<POInvoiceDetails> getPurchase(String id);
	public List<Vendor> loadVendorList(List<Vendor> response);
	public Vendor getVendorDetails(String id);
	public String removePurchase(String invoiceNumber);
	public String removePartId(String id, String invoiceNumber, int temp);
	public List<Item> loadItem(String categoryCode);
	public Item getUnitPrice(String productCode, String categoryCode);
	 
}