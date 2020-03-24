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
		System.out.println("-------- saveCategory-------------");
		RandomNumber randomnumber = null;
		try {
			randomnumber = randomnumberdal.getCategoryRandomNumber();
			String invoice = randomnumber.getCategoryinvoicecode() + randomnumber.getCategoryinvoicenumber();
			category.setCategorycode(invoice);
			System.out.println("Category name -->" + category.getName());

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
		logger.info("------------- Inside loadCategory-----------------");
		List<Category> categorylist = new ArrayList<Category>();
		try {
			logger.info("-----------Inside loadCategory Called----------");
			categorylist = categorydal.loadCategory(categorylist);
			return new ResponseEntity<List<Category>>(categorylist, HttpStatus.CREATED);

		} catch (Exception e) {
			logger.info("loadCategory Exception ------------->" + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {

		}
		//return new ResponseEntity<List<Category>>(categorylist, HttpStatus.CREATED);

	}

	// load allcategorylist
	/*
	 * @CrossOrigin(origins = "http://localhost:8080")
	 * 
	 * @RequestMapping(value="/loadAllCategory",method=RequestMethod.GET) public
	 * ResponseEntity<?> loadAllCategory() {
	 * logger.info("------------- Inside loadAllCategory-----------------");
	 * List<Category> categorylist= new ArrayList<Category>(); List<Category>
	 * allCategorylist= new ArrayList<Category>(); Category cat = new Category();
	 * try { logger.info("-----------Inside loadAllCategory Called----------");
	 * categorylist=categorydal.loadCategory(categorylist); for( int i=0;
	 * i<categorylist.size(); i++) { cat.setName((categorylist.get(i).getName()));
	 * allCategorylist.add(cat); } return new
	 * ResponseEntity<List<Category>>(allCategorylist, HttpStatus.CREATED);
	 * 
	 * }catch(Exception e) {
	 * logger.info("loadAllCategory Exception ------------->"+e.getMessage());
	 * e.printStackTrace(); }finally{
	 * 
	 * } return new
	 * ResponseEntity<List<Category>>(allCategorylist,HttpStatus.CREATED);
	 * 
	 * }
	 */

	// get
	/*
	 * @CrossOrigin(origins = "http://localhost:8080")
	 * 
	 * @RequestMapping(value="/get",method=RequestMethod.GET) public
	 * ResponseEntity<?> geCustomer(String id) {
	 * logger.info("------------- Inside getTempPublicTree-----------------");
	 * List<Customer> responseList=null; try {
	 * logger.info("-----------Inside getTempPublicTree Called----------");
	 * responseList=customerdal.getCustomer(id);
	 * 
	 * }catch(Exception e){ logger.info("Exception ------------->"+e.getMessage());
	 * e.printStackTrace(); }finally{
	 * 
	 * } return new ResponseEntity<List<Customer>>(responseList,
	 * HttpStatus.CREATED);
	 * 
	 * }
	 */

	// update
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	public ResponseEntity<?> updateCategory(@RequestBody Category category) {
		try {
			System.out.println("vendor code inside try--->" + category.getCategorycode());
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
			System.out.println("Remove Category code" + categorycode);
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
				System.out.println("category name-->"+cat.getName());
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
