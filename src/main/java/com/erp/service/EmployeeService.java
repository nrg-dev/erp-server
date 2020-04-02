package com.erp.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

//import org.springframework.beans.factory.annotation.Autowire;

//import javax.enterprise.inject.Produces;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.erp.bo.ErpBo;
import com.erp.dto.EmployeeDto;
import com.erp.mongo.dal.EmployeeDAL;
import com.erp.mongo.dal.RandomNumberDAL;
import com.erp.mongo.model.AbsentList;
import com.erp.mongo.model.ContractList;
import com.erp.mongo.model.DailyReport;
import com.erp.mongo.model.Employee;
import com.erp.mongo.model.RandomNumber;
import com.erp.util.Custom;

@SpringBootApplication
@RestController
@RequestMapping(value = "/employee")
public class EmployeeService implements Filter {

	public static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

	@Autowired
	ErpBo investmentBo;

	// private final RandamNumberRepository randamNumberRepository;

	private final EmployeeDAL employeedal;
	private final RandomNumberDAL randomnumberdal;
	Employee employee=null;

	public EmployeeService(EmployeeDAL employeedal, RandomNumberDAL randomnumberdal) {
		this.employeedal = employeedal;
		this.randomnumberdal = randomnumberdal;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {

		HttpServletResponse response = (HttpServletResponse) res;
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("Access-Control-Allow-Methods", "POST,PUT, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, remember-me");
		chain.doFilter(req, res);
	}

	@Override
	public void init(FilterConfig filterConfig) {
	}

	@Override
	public void destroy() {
	}

	// Save
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ResponseEntity<?> save(@RequestBody Employee employee) {
		logger.info("save employee");
		RandomNumber randomnumber = null;
		try {
			randomnumber = randomnumberdal.getEmployeeRandamNumber();
			//System.out.println("Employee Invoice random number-->" + randomnumber.getEmployeeinvoicenumber());
			//System.out.println("Employee Invoice random code-->" + randomnumber.getEmployeeinvoicecode());
			String employeecode = randomnumber.getCode() + randomnumber.getNumber();
			logger.info("Employee code-->" + employeecode);
			employee.setEmployeecode(employeecode);
			employee.setAddeddate(Custom.getCurrentInvoiceDate());
			logger.info("Current Date-->" + Custom.getCurrentDate());
			employee = employeedal.save(employee);
			randomnumberdal.updateEmployeeRandamNumber(randomnumber);
			return new ResponseEntity<>(HttpStatus.OK); 

		} catch (Exception e) {
			logger.error("Exception-->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {

		}
	}

	// load
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/load", method = RequestMethod.GET)
	public ResponseEntity<?> load() {
		logger.info("load emloyee");
		List<Employee> responseList = null;
		try {
			responseList = employeedal.load(responseList);
			return new ResponseEntity<List<Employee>>(responseList, HttpStatus.CREATED);
		} catch (Exception e) {
			logger.info("Exception-->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {

		}
	}

	// get
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public ResponseEntity<?> get(String employeecode) {
		logger.info("get employee");
		List<Employee> responseList = null;
		try {
			responseList = employeedal.get(employeecode);
			return new ResponseEntity<List<Employee>>(responseList, HttpStatus.CREATED);
		} catch (Exception e) {
			logger.info("Exception-->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		} finally {

		}
	}

	// get & load daily report
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/loadDailyReport", method = RequestMethod.GET)
	public ResponseEntity<?> loadDailyReport(String id) {
		logger.info("loadDailyReport");
		logger.info("EmployeeService Id-->"+id);
		List<DailyReport> responseList = null;
		try {
			responseList = employeedal.loadDailyReport(id);
			logger.info("List Size-->"+responseList.size());
			return new ResponseEntity<List<DailyReport>>(responseList, HttpStatus.OK);

		} catch (Exception e) {
			logger.info("Exception-->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		} finally {

		}
	}
	
		// get & load AbsentList
		@CrossOrigin(origins = "http://localhost:8080")
		@RequestMapping(value = "/loadAbsentList", method = RequestMethod.GET)
		public ResponseEntity<?> loadAbsentList(String employeecode,String date,String type) {
			logger.info("loadDailyReport");
			logger.info("EmployeeService Code-->"+employeecode);
			List<AbsentList> responseList = null;
			try {
				responseList = employeedal.loadAbsentList(employeecode,date,type);
				logger.info("List Size-->"+responseList.size());
				return new ResponseEntity<List<AbsentList>>(responseList, HttpStatus.OK);

			} catch (Exception e) {
				logger.info("Exception-->" + e.getMessage());
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

			} finally {

			}
		}
		
		// get & load ContractList
		@CrossOrigin(origins = "http://localhost:8080")
		@RequestMapping(value = "/loadContractList", method = RequestMethod.GET)
		public ResponseEntity<?> loadContractList(String employeecode) {
			logger.info("loadDailyReport");
			logger.info("EmployeeService Id-->"+employeecode);
			List<ContractList> responseList = null;
			try {
				responseList = employeedal.loadContractList(employeecode);
				logger.info("List Size-->"+responseList.size());
				return new ResponseEntity<List<ContractList>>(responseList, HttpStatus.OK);

			} catch (Exception e) {
				logger.info("Exception-->" + e.getMessage());
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

			} finally {

			}
		}
		
	// update
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@RequestBody Employee employee) {
		logger.info("update employee");
		try {
			logger.info("Employee update inside try--->" + employee.getEmployeecode());
			employee = employeedal.update(employee);
			return new ResponseEntity<>(HttpStatus.OK); 

		} catch (Exception e) {
			logger.info("Exception ------------->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {

		}
	}

	// Remove
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/remove", method = RequestMethod.DELETE)
	public ResponseEntity<?> remove(String employeecode) {
		logger.info("remove employee");
		try {
			employee = new Employee();
			logger.info("-----------Before Calling  remove employee ----------");
			employeedal.remove(employeecode);
			logger.info("-----------Successfully Called  remo employee ----------");
			return new ResponseEntity<>(HttpStatus.OK); 

		} catch (Exception e) {
			logger.info("Exception-->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {

		}

	}
	
	    // Save Daily Report
		@CrossOrigin(origins = "http://localhost:8080")
		@RequestMapping(value = "/saveDailyReport", method = RequestMethod.POST)
		public ResponseEntity<?> saveDailyReport(@RequestBody EmployeeDto employeeDto) {
			logger.info("saveDailyReport");
			try {
				boolean status = employeedal.saveUpdateDailyReport(employeeDto);
				if(status) {
					return new ResponseEntity<>(HttpStatus.OK); 
				} else {
					return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); 
				}

			} catch (Exception e) {
				logger.info("Exception-->" + e.getMessage());
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500
			} finally {

			}
		}
		
			// Save Daily Report
			@CrossOrigin(origins = "http://localhost:8080")
			@RequestMapping(value = "/updateDailyReport", method = RequestMethod.PUT)
			public ResponseEntity<?> updateDailyReport(@RequestBody EmployeeDto employeeDto) {
				logger.info("updateDailyReport");
				try {
					boolean status = employeedal.saveUpdateDailyReport(employeeDto);
					if(status) {
						return new ResponseEntity<>(HttpStatus.OK); 
					} else {
						return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); 
					}

				} catch (Exception e) {
					logger.info("Exception-->" + e.getMessage());
					return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500
				} finally {

				}
			}
				
		
		// Save Absent
		@CrossOrigin(origins = "http://localhost:8080")
		@RequestMapping(value = "/saveAbsent", method = RequestMethod.POST)
		public ResponseEntity<?> saveAbsent(@RequestBody AbsentList absentList) {
			logger.info("saveAbsent");
			try {
				boolean status = employeedal.saveAbsentList(absentList);
				if(status) {
					return new ResponseEntity<>(HttpStatus.OK); 
				} else {
					return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); 
				}

			} catch (Exception e) {
				logger.info("Exception-->" + e.getMessage());
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500
			} finally {

			}
		}
		
		// Update Absent
		@CrossOrigin(origins = "http://localhost:8080")
		@RequestMapping(value = "/updateAbsent", method = RequestMethod.PUT)
		public ResponseEntity<?> updateAbsent(@RequestBody AbsentList absentList) {
			logger.info("updateAbsent");
			try {
				boolean status = employeedal.updateAbsentList(absentList);
				if(status) {
					return new ResponseEntity<>(HttpStatus.OK); 
				} else {
					return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); 
				}

			} catch (Exception e) {
				logger.info("Exception-->" + e.getMessage());
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500
			} finally {

			}
		}
		
		// Save Contract
		@CrossOrigin(origins = "http://localhost:8080")
		@RequestMapping(value = "/saveContract", method = RequestMethod.POST)
		public ResponseEntity<?> saveContract(@RequestBody ContractList contractList) {
			logger.info("saveContract");
			try {
				boolean status = employeedal.saveContractList(contractList);
				if(status) {
					return new ResponseEntity<>(HttpStatus.OK); 
				} else {
					return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); 
				}

			} catch (Exception e) {
				logger.info("Exception-->" + e.getMessage());
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500
			} finally {

			}
		}
				
		// Update Contract
		@CrossOrigin(origins = "http://localhost:8080")
		@RequestMapping(value = "/updateContract", method = RequestMethod.PUT)
		public ResponseEntity<?> updateContract(@RequestBody ContractList contractList) {
			logger.info("updateContract");
			try {
				boolean status = employeedal.updateContractList(contractList);
				if(status) {
					return new ResponseEntity<>(HttpStatus.OK); 
				} else {
					return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); 
				}

			} catch (Exception e) {
				logger.info("Exception-->" + e.getMessage());
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500
			} finally {

			}
		}
				

}
