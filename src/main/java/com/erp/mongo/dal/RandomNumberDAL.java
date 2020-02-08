package com.erp.mongo.dal;

import java.util.List;

import com.erp.dto.Member;
import com.erp.mongo.model.Customer;
import com.erp.mongo.model.POInvoice;
import com.erp.mongo.model.POInvoiceDetails;
import com.erp.mongo.model.PurchaseOrder;
import com.erp.mongo.model.RandomNumber;

import java.nio.file.Path;
//import java.util.List;
import java.util.stream.Stream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
public interface RandomNumberDAL {

	public RandomNumber getRandamNumber();
	public boolean updateRandamNumber(RandomNumber rn);
	

	//--- Vendor Dal Random Calling --
	public RandomNumber getVendorRandamNumber();
	public boolean updateVendorRandamNumber(RandomNumber rn,int num);
	
	//--- Employee Dal Random Calling --
	public RandomNumber getEmployeeRandamNumber();
	public boolean updateEmployeeRandamNumber(RandomNumber rn);
	
	
	//----Category and product RandomDAL Calling
	public RandomNumber getCategoryRandomNumber();
	public boolean updateCategoryRandamNumber(RandomNumber rn,int num);
	
	
	//--- Add Promotion Dal Random Calling --
	public RandomNumber getdiscountRandamNumber();
	public boolean updatediscountRandamNumber(RandomNumber rn);
	public boolean updateSalesRandamNumber(RandomNumber randomnumber);
	public RandomNumber getReturnRandamNumber();
	public boolean updateReturnRandamNumber(RandomNumber randomnumber);
	public boolean updateSalesReturnRandamNumber(RandomNumber randomnumber);
	public RandomNumber getStockDamageRandomNumber();    
	public boolean updateStockDamRandamNumber(RandomNumber rn, int temp); 
	
	
}