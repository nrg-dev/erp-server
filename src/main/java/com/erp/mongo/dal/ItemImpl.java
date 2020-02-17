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

import com.erp.mongo.model.Discount;

//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;

import com.erp.mongo.model.Item;

@Repository
public class ItemImpl implements ItemDAL {

	public static final Logger logger = LoggerFactory.getLogger(ItemImpl.class);

	@Autowired
	private MongoTemplate mongoTemplate;

	// item save
	public Item saveItem(Item product) {
		mongoTemplate.save(product);
		product.setStatus("success");
		return product;
	}

	// item load
	public List<Item> loadItem(List<Item> itemlist, String category) {
		logger.info("DAO Category type-->" + category);
		if (category.equalsIgnoreCase("all") || category.equalsIgnoreCase(null)) {
			logger.info("DAO item load all");
			itemlist = mongoTemplate.findAll(Item.class);
			logger.info("DAO item size -->" + itemlist.size());

		} else {
			Query query = new Query();
			query.addCriteria(Criteria.where("categorycode").is(category));
			itemlist = mongoTemplate.find(query, Item.class);

		}

		return itemlist;

	}

	// Discount load
	public List<Discount> loadDiscount(List<Discount> discountlist,String discount) {
		System.out.println("Fetech Type -->"+discount);
		Query query = new Query();
		query.addCriteria(Criteria.where("discountType").is(discount));
		discountlist = mongoTemplate.find(query,Discount.class);
		return discountlist;

	}

	// get
	public Item getItem(String itemid) {
		Item item = null;
		return item;

	}

	// update
	public Item updateItem(Item item) {
		logger.info("[ItemImpl] Item Code -->"+item.getProdcode());
		logger.info("[ItemImpl] Category Name -->"+item.getCategoryname());
		logger.info("[ItemImpl] Category Code -->"+item.getCategorycode());
		logger.info("[ItemImpl] Vendor Name -->"+item.getVendorname());
		logger.info("[ItemImpl] Vendor Code -->"+item.getVendorcode());
		logger.info("[ItemImpl] Item Name -->"+item.getProductname());
		logger.info("[ItemImpl] Description Name -->"+item.getDescription());
		Update update = new Update();
		Query query = new Query();
		query.addCriteria(Criteria.where("prodcode").is(item.getProdcode()));
		update.set("categoryname", item.getCategoryname());
		update.set("categorycode", item.getCategorycode());
		update.set("vendorname", item.getVendorname());
		update.set("vendorcode", item.getVendorcode());
		update.set("productname", item.getProductname());
		update.set("description", item.getDescription());
		update.set("price", item.getPrice());
		update.set("tax", item.getTax());
		update.set("margin", item.getMargin());
		update.set("sellingprice", item.getSellingprice());
		mongoTemplate.updateFirst(query, update, Item.class);
		return item;

	}

	// Discount update
	public Discount updateDiscount(Discount discount) {
		System.out.println("---Inside discount edit imp---");
		Update update = new Update();
		Query query = new Query();
		query.addCriteria(Criteria.where("discountcode").is(discount.getDiscountcode()));
		update.set("categorycode", discount.getCategorycode());
		update.set("productname", discount.getProductname());
		update.set("discount", discount.getDiscount());
		update.set("qty", discount.getQty());
		update.set("fromdate_promotionperiod", discount.getFromdate_promotionperiod());
		update.set("todate_promotionperiod", discount.getTodate_promotionperiod());

		mongoTemplate.updateFirst(query, update, Discount.class);
		return discount;

	}

	// remove
	public Item removeItem(String prodcode) {
		Item response = null;
		Query query = new Query();
		query.addCriteria(Criteria.where("prodcode").is(prodcode));
		mongoTemplate.remove(query, Item.class);
		return response;

	}

	// Discount remove
	public Discount removeDiscount(String discountcode) {
		Discount response = null;
		Query query = new Query();
		query.addCriteria(Criteria.where("discountcode").is(discountcode));
		mongoTemplate.remove(query, Discount.class);
		return response;

	}

	// item save
	public Discount saveDiscount(Discount discount) {
		mongoTemplate.save(discount);
		discount.setStatus("success");
		return discount;
	}

}
