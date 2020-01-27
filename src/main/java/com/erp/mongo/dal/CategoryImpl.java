package com.erp.mongo.dal;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.core.query.Update;
import com.erp.mongo.model.Category;


@Repository
public class CategoryImpl implements CategoryDAL {
	
	public static final Logger logger = LoggerFactory.getLogger(CategoryImpl.class);


	@Autowired
	private MongoTemplate mongoTemplate;

	

	public Category saveCategory(Category category) {
		mongoTemplate.save(category);
		return category;
	}
	public List<Category> loadCategory(List<Category> categorylist){
		return categorylist;
		
	}
	public Category getCategory(String categoryid) {
		Category category=null;
		return category;
		
	}
	public Category updateCategory(Category category) {
		return category;
		
	}
	public void removeCategory(String categoryid) {
		
	}
		
}
