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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.erp.bo.ErpBo;
import com.erp.dto.EmployeeDto;
import com.erp.mongo.dal.EmployeeDAL;
import com.erp.mongo.dal.RandomNumberDAL;
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
		System.out.println("--------save employee-------------");
		RandomNumber randomnumber = null;
		try {
			randomnumber = randomnumberdal.getEmployeeRandamNumber();
			//System.out.println("Employee Invoice random number-->" + randomnumber.getEmployeeinvoicenumber());
			//System.out.println("Employee Invoice random code-->" + randomnumber.getEmployeeinvoicecode());
			String employeecode = randomnumber.getCode() + randomnumber.getNumber();
			System.out.println("Employee code-->" + employeecode);
			employee.setEmployeecode(employeecode);
			employee.setAddeddate(Custom.getCurrentInvoiceDate());
			System.out.println("Current Date --->" + Custom.getCurrentDate());
			employee = employeedal.save(employee);
			randomnumberdal.updateEmployeeRandamNumber(randomnumber);
			return new ResponseEntity<>(HttpStatus.OK); 

		} catch (Exception e) {
			logger.info("Exception ------------->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {

		}
	}

	// load
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/load", method = RequestMethod.GET)
	public ResponseEntity<?> load() {
		logger.info("------------- Inside Emplist Load-----------------");
		List<Employee> responseList = null;
		try {
			logger.info("-----------Inside Emplist Load Called----------");
			responseList = employeedal.load(responseList);
			return new ResponseEntity<List<Employee>>(responseList, HttpStatus.CREATED);

		} catch (Exception e) {
			logger.info("Exception ------------->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {

		}
	}

	// get
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public ResponseEntity<?> get(String id) {
		logger.info("------------- Inside get Employee -----------------");
		List<Employee> responseList = null;
		try {
			logger.info("-----------Inside get employee ----------");
			responseList = employeedal.get(id);
			return new ResponseEntity<List<Employee>>(responseList, HttpStatus.CREATED);

		} catch (Exception e) {
			logger.info("Exception ------------->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		} finally {

		}
	}

	
		@CrossOrigin(origins = "http://localhost:8080")
		@RequestMapping(value = "/loadDailyReport", method = RequestMethod.GET)
		public ResponseEntity<?> loadDailyReport(String id) {
			logger.info("loadDailyReport");
			logger.info("EmployeeService Id-->"+id);
			List<DailyReport> responseList = null;
			try {
				logger.info("Inside try loadDailyReport");
				responseList = employeedal.loadDailyReport(id);
				logger.info("List Size-->"+responseList.size());
				return new ResponseEntity<List<DailyReport>>(responseList, HttpStatus.OK);

			} catch (Exception e) {
				logger.info("Exception -->" + e.getMessage());
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

			} finally {

			}
		}
		
	// update
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@RequestBody Employee employee) {
		try {
			System.out.println("Employee update inside try--->" + employee.getEmployeecode());
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

		try {
			employee = new Employee();
			logger.info("-----------Before Calling  remove employee ----------");
			System.out.println("Remove employee code" + employeecode);
			employeedal.remove(employeecode);
			employee.setStatus("Success");
			logger.info("-----------Successfully Called  remo employee ----------");
			return new ResponseEntity<>(HttpStatus.OK); 

		} catch (Exception e) {
			logger.info("Exception ------------->" + e.getMessage());
			employee.setStatus("failure");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {

		}

	}
	
	    // Save Daily Report
		@CrossOrigin(origins = "http://localhost:8080")
		@RequestMapping(value = "/saveUpdateDailyReport", method = RequestMethod.POST)
		public ResponseEntity<?> saveUpdateDailyReport(@RequestBody EmployeeDto employeeDto) {
			logger.info("saveDailyReport");
			try {
				boolean status = employeedal.saveUpdateDailyReport(employeeDto);
				if(status) {
					return new ResponseEntity<>(HttpStatus.OK); 
				} else {
					return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); 
				}

			} catch (Exception e) {
				logger.info("Exception ------------->" + e.getMessage());
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500
			} finally {

			}
		}

}
