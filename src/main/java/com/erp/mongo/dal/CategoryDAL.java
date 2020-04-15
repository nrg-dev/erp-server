package com.erp.mongo.dal;

import java.util.List;

import com.erp.mongo.model.Category;

public interface CategoryDAL {
	public boolean saveCategory(Category categoryt,int i);

	public List<Category> loadCategory(List<Category> categorylist);

	public Category getCategory(String categoryid);

	//public Category updateCategory(Category category);

	public boolean removeCategory(String categorycode);
}