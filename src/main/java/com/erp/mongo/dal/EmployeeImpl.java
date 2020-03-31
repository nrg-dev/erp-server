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

import com.erp.dto.EmployeeDto;
import com.erp.mongo.model.AbsentList;
import com.erp.mongo.model.ContractList;
import com.erp.mongo.model.DailyReport;
import com.erp.mongo.model.Discount;
import com.erp.mongo.model.Employee;
import com.erp.mongo.model.POInvoiceDetails;

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
	
	public boolean saveUpdateDailyReport(EmployeeDto employeeDto) {
		boolean status;
		Update update = null;//new Update();
		Query query = null;//new Query();
		DailyReport dailyReport=null;
		try {
			if(employeeDto.getType().equalsIgnoreCase("save")) {
				dailyReport=new DailyReport(employeeDto.getEmployeecode(),employeeDto.getDate(),employeeDto.getReport());
				mongoTemplate.save(dailyReport);
			}
			else {
				update = new Update();
				query = new Query();
				query.addCriteria(Criteria.where("_id").is(employeeDto.getId()));
				update.set("report", employeeDto.getReport());
				mongoTemplate.updateFirst(query, update, DailyReport.class);
			}
			status=true;
			return status;
		}catch(Exception e) {
			logger.error("EmployeeImpl saveDailyReport error"+e.getMessage());
			status=false;
			return status;
		}
		finally {
			update = null;
			query = null;
		}
	}

	public boolean saveUpdateAbsentList(AbsentList absentList) {
		boolean status;
		try {
			mongoTemplate.save(absentList);
			status=true;
			return status;
		}catch(Exception e) {
			logger.error("EmployeeImpl saveAbsentList error"+e.getMessage());
			status=false;
			return status;
		}
	}

	public boolean saveUpdateContractList(ContractList contractList) {
		boolean status;
		try {
			mongoTemplate.save(contractList);
			status=true;
			return status;
		}catch(Exception e) {
			logger.error("EmployeeImpl saveContractList error"+e.getMessage());
			status=false;
			return status;
		}
	}
	

	// load
	public List<Employee> load(List<Employee> list) {
		list = mongoTemplate.findAll(Employee.class);
		return list;
	}

	public List<DailyReport> loadDailyReport(String id) {
		logger.info("EmployeeImpl loadDailyReport");
		logger.info("Id-->"+id);
		List<DailyReport> list=null;
		try {
			if(id == null) {		
			logger.info("Inside If");
			list = mongoTemplate.findAll(DailyReport.class);
			logger.info("EmployeeImpl All DailyReportSize-->"+list.size());

		}
		else {
			logger.info("Inside else");
			Query query = new Query();
			//query.addCriteria(Criteria.where("id").is(id));
			query.addCriteria(Criteria.where("_id").is(id));
			list = mongoTemplate.find(query,DailyReport.class);
			logger.info("EmployeeImpl Single DailyReportSize-->"+list.size());
		}
			return list;

		}catch(Exception e) {
			return list;
		}
		//return list;
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
