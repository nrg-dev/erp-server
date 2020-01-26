package com.erp.mongo.dal;

import java.util.List;

import com.erp.dto.Member;
import com.erp.mongo.model.Customer;
import com.erp.mongo.model.Employee;

import java.nio.file.Path;
//import java.util.List;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
public interface EmployeeDAL {

	public Employee save(Employee employee); 
	public List<Employee> get(String id);
	public Employee update(Employee employee);
	public List<Employee> load(List<Employee> list);
	public void remove(String id);

	
}