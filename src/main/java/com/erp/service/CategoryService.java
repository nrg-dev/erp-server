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

import com.erp.mongo.dal.CategoryDAL;
import com.erp.mongo.dal.RandomNumberDAL;
import com.erp.mongo.model.Category;
import com.erp.mongo.model.RandomNumber;

@SpringBootApplication
@RestController
@RequestMapping(value = "/category")
public class CategoryService implements Filter {

	public static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

	private final CategoryDAL categorydal;
	private final RandomNumberDAL randomnumberdal;
	Category category = null;

	public CategoryService(CategoryDAL categorydal, RandomNumberDAL randomnumberdal) {
		this.categorydal = categorydal;
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
	public ResponseEntity<?> saveCategory(@RequestBody Category category) {
		logger.info("saveCategory");
		RandomNumber randomnumber = null;
		try {
			randomnumber = randomnumberdal.getCategoryRandomNumber();
			String categorycode = randomnumber.getCode() + randomnumber.getNumber();
			category.setCategorycode(categorycode);
			logger.info("Category name -->" + category.getName());
			category = categorydal.saveCategory(category);
			if (category.getStatus().equalsIgnoreCase("success")) {
				randomnumberdal.updateCategoryRandamNumber(randomnumber, 1);
			}
			return new ResponseEntity<>(HttpStatus.OK); 

		} catch (Exception e) {
			logger.info("Exception ------------->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		finally {

		}
	}

	// Load
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/load", method = RequestMethod.GET)
	public ResponseEntity<?> loadCategory() {
		logger.info("loadCategory");
		List<Category> categorylist = new ArrayList<Category>();
		try {
			logger.info("loadCategory");
			categorylist = categorydal.loadCategory(categorylist);
			logger.info("loadCategory");
			if(categorylist.size()>0) {
				return new ResponseEntity<List<Category>>(categorylist, HttpStatus.OK);
			}
			else {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT); // No data

			}

		} catch (Exception e) {
			logger.info("loadCategory Exception-->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {

		}
	}

	// update
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	public ResponseEntity<?> updateCategory(@RequestBody Category category) {
		logger.info("updateCategory");
		try {
			logger.info("Category code --->" + category.getCategorycode());
			category = categorydal.updateCategory(category);
			return new ResponseEntity<>(HttpStatus.OK);

		} catch (Exception e) {
			logger.info("Exception ------------->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {

		}
		//return new ResponseEntity<Category>(category, HttpStatus.CREATED);
	}

	// Remove
	// Remove
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/remove", method = RequestMethod.DELETE)
	public ResponseEntity<?> removeCategory(String categorycode) {
		try {
			category = new Category();
			logger.info("-----------Before Calling  removeCategory ----------");
			logger.info("Remove Category code" + categorycode);
			categorydal.removeCategory(categorycode);
			category.setStatus("Success");
			logger.info("-----------Successfully Called  removeCategory ----------");
			return new ResponseEntity<>(HttpStatus.OK);

		} catch (Exception e) {
			logger.info("Exception ------------->" + e.getMessage());
			category.setStatus("failure");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		} finally {

		}
	}

	
	// Load
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/loadCategoryName", method = RequestMethod.GET)
	public ResponseEntity<?> loadCategoryName() {
		logger.info("------------- Inside loadCategoryName-----------------");
		List<Category> categorylist = new ArrayList<Category>();
		List<String> list = new ArrayList<String>();
		try {
			logger.info("-----------Inside loadCategoryName Called----------");
			categorylist = categorydal.loadCategory(categorylist);
			for(Category cat: categorylist) {
				logger.info("category name-->"+cat.getName());
				list.add(cat.getName()+"-"+cat.getCategorycode());
			}
	
			return new ResponseEntity<List<String>>(list, HttpStatus.CREATED);

		} catch (Exception e) {
			logger.info("loadCategoryName Exception ------------->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {

		}
		//return new ResponseEntity<List<String>>(list, HttpStatus.CREATED);
	}
}
