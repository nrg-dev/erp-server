package com.erp.mongo.dal;

import java.util.List;

import com.erp.dto.Member;
import com.erp.mongo.model.Customer;

import java.nio.file.Path;
//import java.util.List;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
public interface CustomerDAL {

	public Customer saveCustomer(Customer customer);
	public List<Customer> getCustomer(String id);
	public Customer updateCustomer(Customer customer);
	public List<Customer> loadCustomer(List<Customer> list);
	public void removeCustomer(String id);

	
}