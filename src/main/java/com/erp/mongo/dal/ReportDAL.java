package com.erp.mongo.dal;

import java.util.List;

import com.erp.mongo.model.Category;
import com.erp.mongo.model.Employee;

public interface ReportDAL {

	public List<Employee> employeeReport(List<Employee> employeelist);


}