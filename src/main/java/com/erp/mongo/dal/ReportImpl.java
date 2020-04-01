package com.erp.mongo.dal;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import com.erp.mongo.model.Employee;
import com.erp.mongo.model.POInvoice;
import com.erp.mongo.model.SOInvoice;

@Repository
public class ReportImpl implements ReportDAL {

	public static final Logger logger = LoggerFactory.getLogger(ReportImpl.class);

	@Autowired
	private MongoTemplate mongoTemplate;

	// employee load
	public List<Employee> employeeReport(List<Employee> employeelist) {
		logger.info("employeeReport");
		employeelist = mongoTemplate.findAll(Employee.class);
		return employeelist;

	}

	// purchase load
	public List<POInvoice> purchaseReport(List<POInvoice> purchaselist) {
		logger.info("purchaseReport");
		purchaselist = mongoTemplate.findAll(POInvoice.class);
		return purchaselist;

	}

	// sales load
	public List<SOInvoice> salesReport(List<SOInvoice> saleslist) {
		logger.info("salesReport");
		saleslist = mongoTemplate.findAll(SOInvoice.class);
		return saleslist;
	}

}
