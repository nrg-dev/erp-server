package com.erp.mongo.dal;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.erp.mongo.model.Employee;

@Repository
public class EmployeeImpl implements EmployeeDAL {

	public static final Logger logger = LoggerFactory.getLogger(EmployeeImpl.class);

	@Autowired
	private MongoTemplate mongoTemplate;

	/*
	 * @Autowired ErpBo investmentBo1;
	 */

	// save
	public Employee save(Employee employee) {
		mongoTemplate.save(employee);
		return employee;
	}

	// load
	public List<Employee> load(List<Employee> list) {
		list = mongoTemplate.findAll(Employee.class);
		return list;
	}

	// get
	public List<Employee> get(String id) {
		List<Employee> list = null;
		return list;
	}

	// update
	public Employee update(Employee employee) {

		Update update = new Update();
		Query query = new Query();
		query.addCriteria(Criteria.where("employeecode").is(employee.getEmployeecode()));
		update.set("name", employee.getName());
		update.set("rank", employee.getRank());
		update.set("phonenumber", employee.getPhonenumber());
		update.set("address", employee.getAddress());
		update.set("email", employee.getEmail());
		update.set("dob", employee.getDob());
		update.set("contractnumber", employee.getContractnumber());
		update.set("npwp", employee.getNpwp());
		update.set("bpjs", employee.getBpjs());
		update.set("monthlysalary", employee.getMonthlysalary());
		update.set("workHour", employee.getWorkHour());
		update.set("annualLeave", employee.getAnnualLeave());
		mongoTemplate.updateFirst(query, update, Employee.class);
		return employee;

	}

	// remove
	public Employee remove(String employeecode) {
		Employee response = null;
		Query query = new Query();
		query.addCriteria(Criteria.where("employeecode").is(employeecode));
		mongoTemplate.remove(query, Employee.class);
		return response;
	}

}
