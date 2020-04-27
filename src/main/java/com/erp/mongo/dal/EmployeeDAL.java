package com.erp.mongo.dal;

import java.util.List;

import com.erp.dto.EmployeeDto;
import com.erp.mongo.model.AbsentList;
import com.erp.mongo.model.ContractList;
import com.erp.mongo.model.DailyReport;
import com.erp.mongo.model.Employee;

public interface EmployeeDAL {

	public boolean save(Employee employeet,int temp);

	public List<Employee> load(List<Employee> list);

	public List<Employee> get(String id);
	
	public List<DailyReport> loadDailyReport(String employeecode,String date,String type);
	
	public List<AbsentList> loadAbsentList(String employeecode,String date,String type);	

	public List<ContractList> loadContractList(String employeecode);	

	public Employee update(Employee employee);

	public Employee remove(String employeecode);
	
	public boolean saveUpdateDailyReport(EmployeeDto employeeDto);

	public boolean saveAbsentList(EmployeeDto employeeDto);

	public boolean saveContractList(ContractList contractList);

	//public boolean updateAbsentList(AbsentList absentList);
	
	public boolean updateContractList(ContractList contractList);

	

}