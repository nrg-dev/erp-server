package com.erp.mongo.dal;

import java.util.ArrayList;
import java.util.List;

import com.erp.mongo.model.Customer;
import com.erp.mongo.model.Item;
import com.erp.mongo.model.POReturnDetails;
import com.erp.mongo.model.PurchaseOrder;
import com.erp.mongo.model.SOInvoice;
import com.erp.mongo.model.SOInvoiceDetails;
import com.erp.mongo.model.SOReturnDetails;
import com.erp.mongo.model.SalesOrder;
import com.erp.mongo.model.Transaction;

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

	public SOInvoiceDetails updateSales(SOInvoiceDetails sales);

	public SOReturnDetails insertReturn(SOReturnDetails soreturndetails);
	
	public SOInvoice updateSOInvoice(SOInvoice soinvoice, int i);
	
	public SOInvoice loadSOInvoice(String invoicenumber);
	
	public ArrayList<String> loadCustomerName();

	public List<SOInvoice> loadfilterData(List<SOInvoice> response, String fromdate, String todate);

	public SalesOrder saveSO(SalesOrder salesorder);   
	public boolean updateSalesOrder(SalesOrder salesorder);
	public boolean removeSO(String id);
	public List<SalesOrder> loadSO();

	public boolean updateSO(String invoice,String[] value);

	public List<SOInvoice> loadInvoice(String paystatus);

	public List<SOReturnDetails> loadReturn(String paystatus);

	public Transaction saveTransaction(Transaction tran);

	public SOReturnDetails updateSOReturn(SOReturnDetails soret);  
	

}