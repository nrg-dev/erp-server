package com.erp.mongo.dal;

import java.util.List;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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
import org.springframework.data.domain.Sort; 
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

@Repository
public class EmployeeImpl implements EmployeeDAL {

	public static final Logger logger = LoggerFactory.getLogger(EmployeeImpl.class);

	@Autowired
	private MongoTemplate mongoTemplate;

	/*
	 * @Autowired ErpBo investmentBo1;
	 */

	// save
	public boolean save(Employee employee,int temp) {
		logger.debug("Employee Code-->"+employee.getEmployeecode());
		logger.debug("Employee name-->"+employee.getName());
		logger.debug("rank-->"+employee.getRank());
		logger.debug("phonenumber-->"+employee.getPhonenumber());
		logger.debug("email-->"+employee.getEmail());
		logger.debug("dob-->"+employee.getDob());
		logger.debug("contractnumber-->"+employee.getContractnumber());
		logger.debug("npwp-->"+employee.getNpwp());
		logger.debug("bpjs-->"+employee.getBpjs());
		logger.debug("monthlysalary-->"+employee.getMonthlysalary());
		logger.debug("workHour-->"+employee.getWorkHour());
		logger.debug("annualLeave-->"+employee.getAnnualLeave());
		logger.debug("Department Name-->"+employee.getDepartmentname());
		logger.debug("Locatiob-->"+employee.getLocation());
		boolean status = false;
		try {
			// Update
			if(employee.getEmployeecode()!=null && temp==1) {
				logger.info("Inside Upate");
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
				update.set("departmentname", employee.getDepartmentname());
				update.set("location", employee.getLocation());
				mongoTemplate.updateFirst(query, update, Employee.class);
				status=true;
			}
			// Save
			else {
				logger.info("Inside Save");
				mongoTemplate.save(employee);
				status=true;		
			}
			return status;
		}catch(Exception e) {
			logger.error("Exception-->"+e.getMessage());
			status=false;
			return status;
		}

	}
	
	public boolean saveUpdateDailyReport(EmployeeDto employeeDto) {
		boolean status;
		Update update = null;//new Update();
		Query query = null;//new Query();
		DailyReport dailyReport=null;
		try {
			query = new Query();		
			query.addCriteria(Criteria.where("employeecode").is(employeeDto.getEmployeecode()));
			query.addCriteria(Criteria.where("date").is(employeeDto.getDate()));
			List<DailyReport> list = mongoTemplate.find(query,DailyReport.class);
			if(list.size()>0) {
				// update
				update = new Update();
				query = new Query();
				query.addCriteria(Criteria.where("employeecode").is(employeeDto.getEmployeecode()));
				query.addCriteria(Criteria.where("date").is(employeeDto.getDate()));
				update.set("report", employeeDto.getReport());
				mongoTemplate.updateFirst(query, update, DailyReport.class);
				status=true;
			}
		else {
            // save
			dailyReport=new DailyReport(employeeDto.getEmployeecode(),employeeDto.getDate(),employeeDto.getReport());
			mongoTemplate.save(dailyReport);
			status=true;
		}
		return status;
	}
		/*try {
			
			 * if(employeeDto.getType().equalsIgnoreCase("save")) { dailyReport=new
			 * DailyReport(employeeDto.getEmployeecode(),employeeDto.getDate(),employeeDto.
			 * getReport()); mongoTemplate.save(dailyReport); } else { update = new
			 * Update(); query = new Query();
			 * query.addCriteria(Criteria.where("employeecode").is(employeeDto.getId()));
			 * update.set("report", employeeDto.getReport());
			 * mongoTemplate.updateFirst(query, update, DailyReport.class); } status=true;
			 * return status;
			 }*/ catch(Exception e) {
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
			
			query = new Query();
			query.addCriteria(Criteria.where("employeecode").is(employeeDto.getEmployeecode()));
			query.addCriteria(Criteria.where("date").is(employeeDto.getDate()));
			List<AbsentList> list = mongoTemplate.find(query,AbsentList.class);
			if(list.size()>0) {
				logger.info("Inside saveAbsentList update");
				logger.debug("Employee code-->"+employeeDto.getEmployeecode());
				logger.debug("Date-->"+employeeDto.getDate());
				logger.debug("CheckInReason-->"+employeeDto.getCheckinreason());
				logger.debug("CheckInTime-->"+employeeDto.getCheckintime());
				logger.debug("CheckOutReason-->"+employeeDto.getCheckoutreason());
				logger.debug("CheckOutTime-->"+employeeDto.getCheckouttime());
				logger.debug("Absent-->"+employeeDto.getAbsent());
				logger.debug("Reason-->"+employeeDto.getReason());
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
			else {

				logger.info("Inside saveAbsentList save");
				absentList=new AbsentList(
						employeeDto.getEmployeecode(),employeeDto.getCheckinreason(),
						employeeDto.getCheckintime(),employeeDto.getCheckoutreason(),
						employeeDto.getCheckouttime(),employeeDto.getAbsent(),
						employeeDto.getReason(),employeeDto.getDate());
				mongoTemplate.save(absentList);
			
			}
			/*
			 * if(employeeDto.getType().equalsIgnoreCase("save")) {
			 * logger.info("Inside saveAbsentList save"); absentList=new AbsentList(
			 * employeeDto.getEmployeecode(),employeeDto.getCheckinreason(),
			 * employeeDto.getCheckintime(),employeeDto.getCheckoutreason(),
			 * employeeDto.getCheckouttime(),employeeDto.getAbsent(),
			 * employeeDto.getReason(),employeeDto.getDate());
			 * mongoTemplate.save(absentList); } else {
			 * logger.info("Inside saveAbsentList update");
			 * logger.info("Employee code-->"+employeeDto.getEmployeecode());
			 * logger.info("Date-->"+employeeDto.getDate());
			 * logger.info("CheckInReason-->"+employeeDto.getCheckinreason());
			 * logger.info("CheckInTime-->"+employeeDto.getCheckintime());
			 * logger.info("CheckOutReason-->"+employeeDto.getCheckoutreason());
			 * logger.info("CheckOutTime-->"+employeeDto.getCheckouttime());
			 * logger.info("Absent-->"+employeeDto.getAbsent());
			 * logger.info("Reason-->"+employeeDto.getReason()); update = new Update();
			 * query = new Query();
			 * query.addCriteria(Criteria.where("employeecode").is(employeeDto.
			 * getEmployeecode()));
			 * query.addCriteria(Criteria.where("date").is(employeeDto.getDate()));
			 * update.set("checkinreason", employeeDto.getCheckinreason());
			 * update.set("checkintime", employeeDto.getCheckintime());
			 * update.set("checkoutreason", employeeDto.getCheckoutreason());
			 * update.set("checkouttime", employeeDto.getCheckouttime());
			 * update.set("absent", employeeDto.getAbsent()); update.set("reason",
			 * employeeDto.getReason()); update.set("date", employeeDto.getDate());
			 * mongoTemplate.updateFirst(query, update, AbsentList.class); }
			 */
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
		//Query query = new Query();
		//query.with(new Sort(Sort.Direction.DESC, "_id"));
	    Query query = new Query();//.with(new Sort("_id", "-1"));
	   // query.with(new Sort(new Order(Direction.ASC, "employeecode")));
	  // query.with(new Sort(new Order(Direction.DESC, "addeddate")));
	   query.with(new Sort(new Order(Direction.DESC, "employeecode")));
	    //List<MyClass> allObjects = mongoTemplate.find(query, MyClass.class);
		list = mongoTemplate.find(query,Employee.class);
		logger.debug("Size-->"+list.size());
		for (Employee e : list) {
		    logger.debug(e.getName());    
		   }
		return list;
	}

	public List<AbsentList> loadAbsentList(String employeecode,String date,String type){
		List<AbsentList> list =null;
		if(type.equalsIgnoreCase("A")) {
			list = mongoTemplate.findAll(AbsentList.class);	
			logger.debug("EmployeeImpl All loadAbsentList-->"+list.size());
		}
		if(type.equalsIgnoreCase("D")) {
			Query query = new Query();
			query.addCriteria(Criteria.where("employeecode").is(employeecode));
			query.addCriteria(Criteria.where("date").is(date));
			list = mongoTemplate.find(query,AbsentList.class);
			logger.debug("EmployeeImpl Single loadAbsentList-->"+list.size());
		}
		if(type.equalsIgnoreCase("M")) {
			Query query = new Query();
			query.addCriteria(Criteria.where("employeecode").is(employeecode));
			list = mongoTemplate.find(query,AbsentList.class);
			logger.debug("EmployeeImpl Month loadAbsentList-->"+list.size());
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
			logger.debug("EmployeeImpl All loadContractList-->"+list.size());
		}
		else {
			Query query = new Query();
			query.addCriteria(Criteria.where("employeecode").is(employeecode));
			list = mongoTemplate.find(query,ContractList.class);
			logger.debug("EmployeeImpl Single loadContractList-->"+list.size());
		}
		return list;
	}

	
	public List<DailyReport> loadDailyReport(String employeecode,String date,String type) {
		List<DailyReport> list =null;
		if(type.equalsIgnoreCase("A")) {
			list = mongoTemplate.findAll(DailyReport.class);	
			logger.debug("EmployeeImpl All loadDailyReport-->"+list.size());
		}
		if(type.equalsIgnoreCase("D")) {
			Query query = new Query();
			query.addCriteria(Criteria.where("employeecode").is(employeecode));
			query.addCriteria(Criteria.where("date").is(date));
			list = mongoTemplate.find(query,DailyReport.class);
			logger.debug("EmployeeImpl Single loadDailyReport-->"+list.size());
		}
		if(type.equalsIgnoreCase("M")) {
			Query query = new Query();
			query.addCriteria(Criteria.where("employeecode").is(employeecode));
			list = mongoTemplate.find(query,DailyReport.class);
			logger.debug("EmployeeImpl Month loadDailyReport-->"+list.size());
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
		logger.debug("EmployeeImpl Single DailyReportSize-->"+list.size());
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
		update.set("departmentname", employee.getDepartmentname());
		update.set("location", employee.getLocation());
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
