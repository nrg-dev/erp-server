package com.erp.mongo.dal;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.erp.mongo.model.Category;

@Repository
public class CategoryImpl implements CategoryDAL {

	public static final Logger logger = LoggerFactory.getLogger(CategoryImpl.class);

	@Autowired
	private MongoTemplate mongoTemplate;

	// save
	public Category saveCategory(Category category) {
		mongoTemplate.save(category);
		category.setStatus("success");
		return category;
	}

	// load
	public List<Category> loadCategory(List<Category> categorylist) {
		categorylist = mongoTemplate.findAll(Category.class);
		return categorylist;

	}

	// get
	public Category getCategory(String categoryid) {
		Category category = null;
		return category;

	}

	// update
	@Override
	public Category updateCategory(Category category) {
		logger.info("Category Code -->"+category.getCategorycode());
		Update update = new Update();
		Query query = new Query();
		query.addCriteria(Criteria.where("categorycode").is(category.getCategorycode()));
		update.set("name", category.getName());
		update.set("description", category.getDescription());
		mongoTemplate.updateFirst(query, update, Category.class);
		return category;

	}

	// remove
	public Category removeCategory(String categorycode) {
		Category response = null;
		Query query = new Query();
		query.addCriteria(Criteria.where("categorycode").is(categorycode));
		mongoTemplate.remove(query, Category.class);
		return response;
	}
}
