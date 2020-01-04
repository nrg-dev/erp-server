package com.erp.mongo.dal;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.core.query.Update;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;



//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.util.stream.Stream;

import com.erp.bo.ErpBo;
import com.erp.dto.Member;
import com.erp.model.UserDetail;
import com.erp.util.Email;
import com.ggl.mongo.model.Customer;
import com.ggl.mongo.model.Employee;


@Repository
public class EmployeeImpl implements EmployeeDAL {
	
	public static final Logger logger = LoggerFactory.getLogger(EmployeeImpl.class);


	@Autowired
	private MongoTemplate mongoTemplate;

	/*
	 * @Autowired ErpBo investmentBo1;
	 */
	
	
	public Employee save(Employee employee) {
		return employee;
	}
	public List<Employee> get(String id){
		List<Employee> list =null;
		return list;
	}
	public Employee update(Employee employee) {
		return employee;
	}
	public List<Employee> load(List<Employee> list){
		return list;
	}
	public void remove(String id) {
		
	}
	
	

		
}
