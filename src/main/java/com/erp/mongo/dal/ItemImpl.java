package com.erp.mongo.dal;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;




//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;

import com.erp.mongo.model.Item;


@Repository
public class ItemImpl implements ItemDAL {
	
	public static final Logger logger = LoggerFactory.getLogger(ItemImpl.class);


	@Autowired
	private MongoTemplate mongoTemplate;

	

	public Item saveItem(Item Item) {
		return Item;
	}
	public List<Item> loadItem(List<Item> itemlist){
		return itemlist;
		
	}
	public Item getItem(String itemid) {
		Item item=null;
		return item;
		
	}
	public Item updateItem(Item item) {
		return item;
		
	}
	public void removeItem(String itemid) {
		
	}

		
}
