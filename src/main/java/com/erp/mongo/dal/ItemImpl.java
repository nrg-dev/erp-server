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
import com.erp.mongo.model.Discount;

//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;

import com.erp.mongo.model.Item;
import com.erp.mongo.model.Vendor;


@Repository
public class ItemImpl implements ItemDAL {
	
	public static final Logger logger = LoggerFactory.getLogger(ItemImpl.class);


	@Autowired
	private MongoTemplate mongoTemplate;

	
	//item save
	public Item saveItem(Item product) {
		mongoTemplate.save(product);
		product.setStatus("success");
		return product;
	}
	
	//item load
	public List<Item> loadItem(List<Item> itemlist){
		itemlist = mongoTemplate.findAll(Item.class);
		return itemlist;
		
	}
	//Discount load
		public List<Discount> loadDiscount(List<Discount> discountlist){
			discountlist = mongoTemplate.findAll(Discount.class);
			return discountlist;
			
		}
	//get
	public Item getItem(String itemid) {
		Item item=null;
		return item;
		
	}
	//update
	public Item updateItem(Item item) {
		Update update = new Update();
		Query query = new Query();
		query.addCriteria(Criteria.where("prodcode").is(item.getProdcode()));
		update.set("productname", item.getProductname());
		update.set("description", item.getDescription());
		update.set("price", item.getPrice());
		update.set("tax", item.getTax());
		update.set("margin", item.getMargin());
		update.set("sellingprice", item.getSellingprice());
		mongoTemplate.updateFirst(query, update, Item.class);
		return item;
		
	}
	//remove
	public Item removeItem(String prodcode) {
		Item response=null;
		Query query= new Query();
		query.addCriteria(Criteria.where("prodcode").is(prodcode));
		mongoTemplate.remove(query,Item.class);
		return response;
		
	}
	//item save
		public Discount saveDiscount(Discount discount) {
			mongoTemplate.save(discount);
			discount.setStatus("success");
			return discount;
		}
		
}
