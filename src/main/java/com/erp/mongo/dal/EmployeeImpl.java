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
		logger.info("Employee Status-->"+employee.getStatus());
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
				query.addCriteria(Criteria.where("employeecode").is(employeeDto.getId()));
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

	public boolean saveAbsentList(EmployeeDto employeeDto) {
		logger.info("Inside saveAbsentList");
		boolean status;
		Update update = null;//new Update();
		Query query = null;//new Query();
		AbsentList absentList=null;
		try {
			if(employeeDto.getType().equalsIgnoreCase("save")) {
				logger.info("Inside saveAbsentList save");
				absentList=new AbsentList(
						employeeDto.getEmployeecode(),employeeDto.getCheckinreason(),
						employeeDto.getCheckintime(),employeeDto.getCheckoutreason(),
						employeeDto.getCheckouttime(),employeeDto.getAbsent(),
						employeeDto.getReason(),employeeDto.getDate());
				mongoTemplate.save(absentList);
			}
			else {
				logger.info("Inside saveAbsentList update");
				logger.info("Employee code-->"+employeeDto.getEmployeecode());
				logger.info("Date-->"+employeeDto.getDate());
				logger.info("CheckInReason-->"+employeeDto.getCheckinreason());
				logger.info("CheckInTime-->"+employeeDto.getCheckintime());
				logger.info("CheckOutReason-->"+employeeDto.getCheckoutreason());
				logger.info("CheckOutTime-->"+employeeDto.getCheckouttime());
				logger.info("Absent-->"+employeeDto.getAbsent());
				logger.info("Reason-->"+employeeDto.getReason());
				update = new Update();
				query = new Query();
				query.addCriteria(Criteria.where("employeecode").is(employeeDto.getEmployeecode()));
				query.addCriteria(Criteria.where("date").is(employeeDto.getDate()));
				update.set("checkinreason", employeeDto.getCheckinreason());
				update.set("checkintime", employeeDto.getCheckintime());
				update.set("checkoutreason", employeeDto.getCheckoutreason());
				update.set("checkouttime", employeeDto.getCheckouttime());
				update.set("absent", employeeDto.getAbsent());
				update.set("reason", employeeDto.getReason());
				update.set("date", employeeDto.getDate());
				mongoTemplate.updateFirst(query, update, AbsentList.class);
			}
			status=true;
			return status;
		}catch(Exception e) {
			logger.error("EmployeeImpl saveAbsentList error"+e.getMessage());
			status=false;
			return status;
		}
		finally {
			update=null;
			query=null;
		}
	}

	public boolean saveContractList(ContractList contractList) {
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
	
	/*
	 * public boolean updateAbsentList(AbsentList absentList) { boolean status; try
	 * { Update update = new Update(); Query query = new Query();
	 * query.addCriteria(Criteria.where("employeecode").is(absentList.
	 * getEmployeecode())); update.set("checkinreason",
	 * absentList.getCheckinreason()); update.set("checkintime",
	 * absentList.getCheckintime()); update.set("checkoutreason",
	 * absentList.getCheckoutreason()); update.set("checkouttime",
	 * absentList.getCheckouttime()); update.set("absent", absentList.getAbsent());
	 * update.set("reason", absentList.getReason()); update.set("date",
	 * absentList.getDate()); mongoTemplate.updateFirst(query, update,
	 * AbsentList.class); status=true; return status; }catch(Exception e) {
	 * logger.error("EmployeeImpl saveAbsentList error"+e.getMessage());
	 * status=false; return status; } }
	 */

	public boolean updateContractList(ContractList contractList) {
		boolean status;
		try {
			Update update = new Update();
			Query query = new Query();
			query.addCriteria(Criteria.where("employeecode").is(contractList.getEmployeecode()));
			update.set("filetype", contractList.getFiletype());
			update.set("base64", contractList.getBase64());
			update.set("contractnumber", contractList.getContractnumber());
			update.set("date", contractList.getDate());
			mongoTemplate.updateFirst(query, update, ContractList.class);
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

	public List<AbsentList> loadAbsentList(String employeecode,String date,String type){
		List<AbsentList> list =null;
		if(type.equalsIgnoreCase("A")) {
			list = mongoTemplate.findAll(AbsentList.class);	
			logger.info("EmployeeImpl All loadAbsentList-->"+list.size());
		}
		if(type.equalsIgnoreCase("D")) {
			Query query = new Query();
			query.addCriteria(Criteria.where("employeecode").is(employeecode));
			query.addCriteria(Criteria.where("date").is(date));
			list = mongoTemplate.find(query,AbsentList.class);
			logger.info("EmployeeImpl Single loadAbsentList-->"+list.size());
		}
		if(type.equalsIgnoreCase("M")) {
			Query query = new Query();
			query.addCriteria(Criteria.where("employeecode").is(employeecode));
			list = mongoTemplate.find(query,AbsentList.class);
			logger.info("EmployeeImpl Month loadAbsentList-->"+list.size());
		}
		else {
			logger.info("EmployeeImpl No Type found");
		}
		return list;
	}
	
	public List<ContractList> loadContractList(String employeecode){
		List<ContractList> list =null;
		if(employeecode!=null) {
			list = mongoTemplate.findAll(ContractList.class);	
			logger.info("EmployeeImpl All loadContractList-->"+list.size());
		}
		else {
			Query query = new Query();
			query.addCriteria(Criteria.where("employeecode").is(employeecode));
			list = mongoTemplate.find(query,ContractList.class);
			logger.info("EmployeeImpl Single loadContractList-->"+list.size());
		}
		return list;
	}

	
	public List<DailyReport> loadDailyReport(String employeecode,String date,String type) {
		List<DailyReport> list =null;
		if(type.equalsIgnoreCase("A")) {
			list = mongoTemplate.findAll(DailyReport.class);	
			logger.info("EmployeeImpl All loadDailyReport-->"+list.size());
		}
		if(type.equalsIgnoreCase("D")) {
			Query query = new Query();
			query.addCriteria(Criteria.where("employeecode").is(employeecode));
			query.addCriteria(Criteria.where("date").is(date));
			list = mongoTemplate.find(query,DailyReport.class);
			logger.info("EmployeeImpl Single loadDailyReport-->"+list.size());
		}
		if(type.equalsIgnoreCase("M")) {
			Query query = new Query();
			query.addCriteria(Criteria.where("employeecode").is(employeecode));
			list = mongoTemplate.find(query,DailyReport.class);
			logger.info("EmployeeImpl Month loadDailyReport-->"+list.size());
		}
		else if(list.size()==0){
			logger.info("EmployeeImpl loadDailyReport No Type found");
		}
		return list;
	}

	
	// get
	public List<Employee> get(String employeecode) {
		logger.info("get");
		List<Employee> list=null;
		Query query = new Query();
		query.addCriteria(Criteria.where("employeecode").is(employeecode));
		list = mongoTemplate.find(query,Employee.class);
		logger.info("EmployeeImpl Single DailyReportSize-->"+list.size());
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
