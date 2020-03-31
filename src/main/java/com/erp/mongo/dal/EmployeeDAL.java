package com.erp.mongo.dal;

import java.util.List;

import com.erp.dto.EmployeeDto;
import com.erp.mongo.model.AbsentList;
import com.erp.mongo.model.ContractList;
import com.erp.mongo.model.DailyReport;
import com.erp.mongo.model.Employee;

public interface EmployeeDAL {

	public Employee save(Employee employee);

	public List<Employee> load(List<Employee> list);

	public List<Employee> get(String id);
	
	public List<DailyReport> loadDailyReport(String id);	

	public Employee update(Employee employee);

	public Employee remove(String employeecode);
	
	public boolean saveUpdateDailyReport(EmployeeDto employeeDto);

	public boolean saveUpdateAbsentList(AbsentList absentList);

	public boolean saveUpdateContractList(ContractList contractList);

	

}