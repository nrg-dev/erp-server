package com.erp.mongo.dal;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;
import com.erp.mongo.model.Employee;


@Repository
public class ReportImpl implements ReportDAL {
	
	public static final Logger logger = LoggerFactory.getLogger(ReportImpl.class);


	@Autowired
	private MongoTemplate mongoTemplate;

	
	
	
	//load
	public List<Employee> employeeReport(List<Employee> employeelist){
		employeelist = mongoTemplate.findAll(Employee.class);
		return employeelist;
		
	}
	
	
}
