package com.erp.mongo.dal;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.erp.mongo.model.PettyCash;
import com.erp.mongo.model.Transaction;

public interface FinanceDAL {
	public ArrayList<String> loadCustomerVendorName();

	public PettyCash save(PettyCash finance);

	public List<PettyCash> load();

	public PettyCash updatePettyCash(PettyCash pettycash);

	public PettyCash removePettyCash(String id);

	public List<Transaction> loadProfitLoss();

	public List<Transaction> loadfilterProfitData(List<Transaction> trans, String startdate, String enddate) throws ParseException; 
}