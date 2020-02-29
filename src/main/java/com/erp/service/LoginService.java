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
import com.erp.dto.User;
import com.erp.mongo.dal.RandomNumberDAL;
import com.erp.mongo.dal.VendorDAL;
import com.erp.mongo.model.Category;
import com.erp.mongo.model.RandomNumber;
import com.erp.mongo.model.Vendor;
import com.erp.util.Custom;

@SpringBootApplication
@RestController
@RequestMapping(value = "/login")
public class LoginService implements Filter {

	public static final Logger logger = LoggerFactory.getLogger(LoginService.class);

	@Autowired
	ErpBo bo;
	
	private final RandomNumberDAL randomnumberdal;

	public LoginService( RandomNumberDAL randomnumberdal) {
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

	@CrossOrigin(origins = "http://localhost:80")
	@RequestMapping(value="/login",method=RequestMethod.GET)
	public ResponseEntity<?>  loginUser(@RequestBody String username,@RequestBody String password) {
		logger.info("---------- Inside LoginUser -------------------");
		logger.info("User Name ---------------->"+username);
		logger.info("Password  ---------------->"+password);
		User user = null;
		try {
				user = new User();
				user.setUsername(username);
				user.setPassword(password);
				user = bo.userLogin(user);
				logger.info("Status --->"+user.getStatus());
				logger.info("User Type -->"+user.getUserRole());			
		}catch(Exception e) {
			user.setStatus("Network Error Please try again");
			logger.info("Exception ------------->"+e.getMessage());
		}finally {
			
		}
		return new ResponseEntity<User>(user, HttpStatus.OK);
	  }

}
