package com.erp.mongo.dal;

import java.util.ArrayList;
import java.util.List;

import com.erp.mongo.model.PettyCash;

public interface FinanceDAL {
	public ArrayList<String> loadCustomerVendorName();

	public PettyCash save(PettyCash finance);

	public List<PettyCash> load();

	public PettyCash updatePettyCash(PettyCash pettycash);

	public PettyCash removePettyCash(String id);
}