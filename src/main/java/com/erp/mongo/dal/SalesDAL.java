package com.erp.mongo.dal;

import java.util.List;

import com.erp.mongo.model.Customer;
import com.erp.mongo.model.Item;
import com.erp.mongo.model.SOInvoice;
import com.erp.mongo.model.SOInvoiceDetails;
import com.erp.mongo.model.SOReturnDetails;

public interface SalesDAL {
	public SOInvoice saveSOInvoice(SOInvoice soinvoice);

	public SOInvoiceDetails saveSales(SOInvoiceDetails purchaseorder);

	public List<SOInvoice> loadSales(List<SOInvoice> list);

	public List<SOInvoiceDetails> getSales(String id);

	public List<Customer> loadCustomerList(List<Customer> response);

	public Customer getCustomerDetails(String id);

	public String removeSales(String invoiceNumber);

	public String removePartId(String id, String invoiceNumber, int temp);

	public List<Item> loadItem(String categoryCode);

	public Item getUnitPrice(String productCode, String categoryCode);

	public SOInvoiceDetails updateSales(SOInvoiceDetails purchase);

	public SOReturnDetails insertReturn(SOReturnDetails soreturndetails);

}