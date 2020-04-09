package com.erp.mongo.dal;

import com.erp.mongo.model.RandomNumber;

public interface RandomNumberDAL {

	public RandomNumber getRandamNumber(int i);

	public boolean updateRandamNumber(RandomNumber rn,int id);

	// --- Vendor Dal Random Calling --
	public RandomNumber getVendorRandamNumber();
	//---- Customer Dal Random Calling ---
	public RandomNumber getCustomerRandamNumber();

	public boolean updateVendorRandamNumber(RandomNumber rn, int num);

	// --- Employee Dal Random Calling --
	public RandomNumber getEmployeeRandamNumber();

	public boolean updateEmployeeRandamNumber(RandomNumber rn);

	// ----Category and product RandomDAL Calling
	public RandomNumber getCategoryRandomNumber(int i);

	public boolean updateCategoryRandamNumber(RandomNumber rn, int num);

	// --- Add Promotion Dal Random Calling --
	public RandomNumber getdiscountRandamNumber();

	public boolean updatediscountRandamNumber(RandomNumber rn);

	public boolean updateSalesRandamNumber(RandomNumber randomnumber);
	public boolean updateSalesReturnRandamNumber(RandomNumber randomnumber);

	public RandomNumber getReturnRandamNumber(int i);
	public boolean updatePOReturnRandamNumber(RandomNumber randomnumber);

	
	public RandomNumber getStockDamageRandomNumber();
	public boolean updateStockDamRandamNumber(RandomNumber rn, int temp);

	public RandomNumber getStockRandamNumber();  
	public boolean updateStockRandamNumber(RandomNumber randomnumber,int temp);


}