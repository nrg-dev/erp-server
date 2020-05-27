package com.erp.service;

import java.io.IOException;
import java.util.ArrayList;
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

import com.erp.mongo.dal.FinanceDAL;
import com.erp.mongo.dal.UserMgtDAL;
import com.erp.mongo.model.PettyCash;
import com.erp.mongo.model.UserRole;
import com.erp.util.Custom;

@SpringBootApplication
@RestController
@RequestMapping(value = "/userMgt")
public class UserMgtService implements Filter {

	public static final Logger logger = LoggerFactory.getLogger(UserMgtService.class);
	UserRole userrole = null;
	private final UserMgtDAL usermgtdal;

	public UserMgtService(UserMgtDAL usermgtdal) {
		this.usermgtdal = usermgtdal;
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

	// Load customer / vendor name for populate for auto text box
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/loaddepartmentname", method = RequestMethod.GET)
	public ResponseEntity<?> loaddepartmentname() {
		logger.info("loaddepartmentname");
		ArrayList<String> departmentlist = null;
		try {
			logger.info("Before Calling Load departmentname list");
			//departmentlist = usermgtdal.loadCustomerVendorName();
			logger.info("Successfully Called Load departmentname list");
			return new ResponseEntity<ArrayList<String>>(departmentlist, HttpStatus.CREATED);

		} catch (Exception e) {
			logger.error("Exception-->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {

		}
	}

	// save petty cash
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ResponseEntity<?> savePettycash(@RequestBody UserRole user) {
		logger.info("savePettycash");
		try {
			logger.debug("PettyCash Id-->"+user.getId());
			if(user.getId() != null) {
				user = usermgtdal.updateUser(user);
			}else {
				user = usermgtdal.save(user);
			}
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Exception-->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {

		}
	}

	// Load petty cash data
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/load", method = RequestMethod.GET)
	public ResponseEntity<?> load() {
		logger.info("load");
		List<UserRole> userlist = null;
		try {
			logger.info("Before Calling load pettycash list");
			userlist = usermgtdal.load();
			logger.info("Successfully Called load pettycash list");
			return new ResponseEntity<List<UserRole>>(userlist, HttpStatus.CREATED);

		} catch (Exception e) {
			logger.error("Exception-->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {

		}
	}

	/*// update
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	public ResponseEntity<?> updatePetty(@RequestBody PettyCash pettycash) {
		try {
			pettycash = usermgtdal.updatePettyCash(pettycash);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Exception-->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		} finally {

		}
	}*/

	// Remove
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/remove", method = RequestMethod.DELETE)
	public ResponseEntity<?> removePettyCash(String id) {
		try {
			logger.debug("Remove -->" + id);
			userrole = new UserRole();
			logger.info("Before Calling  removePettyCash");
			usermgtdal.removeUser(id);
			logger.info("Successfully Called  removePettyCash");
			return new ResponseEntity<>(HttpStatus.OK);

		} catch (Exception e) {
			logger.error("Exception-->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		} finally {

		}

	}
}
