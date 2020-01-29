package com.erp.mongo.dal;

import java.util.List;
import com.erp.mongo.model.Employee;

public interface EmployeeDAL {

	public Employee save(Employee employee); 
	public List<Employee> load(List<Employee> list);
	public List<Employee> get(String id);
	public Employee update(Employee employee);
	public void remove(String id);

	
}