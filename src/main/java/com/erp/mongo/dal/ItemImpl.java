package com.erp.mongo.dal;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Sort;
import com.erp.mongo.model.Discount;

//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;

import com.erp.mongo.model.Item;
import com.erp.mongo.model.Units;

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

	public List<Units> loadUnits(String id){
		List<Units> unitlist = new ArrayList<Units>();
		if(id!=null) {
			logger.info("DAO unit load all");
			unitlist = mongoTemplate.findAll(Units.class);
			logger.debug("DAO unit size -->" + unitlist.size());
		}else {
			
		}
		return unitlist;
	}

	// item load
	public List<Item> loadItem(String vendorcode,String category,String prodcode) {
		logger.debug("DAO Category-->" + category);
		logger.debug("DAO VendorCode-->" + vendorcode);
		logger.debug("DAO ProductCode-->" + prodcode);

		List<Item> itemlist = new ArrayList<Item>();
		
		if (vendorcode!=null && ( category==null && prodcode==null)) {
			Query query = new Query();
			logger.debug("DAO Vendor Code-->" + vendorcode);
			query.addCriteria(Criteria.where("vendorcode").is(vendorcode));
			itemlist = mongoTemplate.find(query, Item.class);
			logger.debug("DAO item size -->" + itemlist.size());

		} 
		
		else if (category.equalsIgnoreCase("all")) {
			Query query = new Query();
			query.with(new Sort(Sort.Direction.DESC, "prodcode"));
			query.fields().include("id");
			query.fields().include("prodcode");
			query.fields().include("productname");
			query.fields().include("categoryname");
			query.fields().include("vendorname");
			query.fields().include("vendorcode");
			query.fields().include("price");
			query.fields().include("margin");
			query.fields().include("tax");
			query.fields().include("unit");
			query.fields().include("sellingprice");
			logger.info("DAO item load all");
			itemlist = mongoTemplate.find(query,Item.class);
			logger.debug("DAO item size -->" + itemlist.size());

		} 
		else if (vendorcode!=null && category!=null && prodcode!=null) {
			Query query = new Query();
			logger.debug("DAO Vendor Code-->" + vendorcode);
			logger.debug("DAO Category-->" + category);
			logger.debug("DAO Product Code-->" + prodcode);
			query.addCriteria(Criteria.where("vendorcode")
					.is(vendorcode).and("prodcode").is(prodcode).and("categorycode").is(category));
			itemlist = mongoTemplate.find(query, Item.class);
			logger.debug("DAO item size -->" + itemlist.size());

		} 
		return itemlist;
	}

	// Discount load
	public List<Discount> loadDiscount(List<Discount> discountlist,String discount) {
		logger.debug("Fetech Type-->"+discount);
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
		logger.debug("[ItemImpl] Item Code-->"+item.getProdcode());
		logger.debug("[ItemImpl] Category Name-->"+item.getCategoryname());
		logger.debug("[ItemImpl] Category Code-->"+item.getCategorycode());
		logger.debug("[ItemImpl] Vendor Name-->"+item.getVendorname());
		logger.debug("[ItemImpl] Vendor Code-->"+item.getVendorcode());
		logger.debug("[ItemImpl] Item Name-->"+item.getProductname());
		logger.debug("[ItemImpl] Description Name-->"+item.getDescription());
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
		update.set("unit", item.getUnit());
		update.set("sellingprice", item.getSellingprice());
		update.set("productImage", item.getProductImage());
		mongoTemplate.updateFirst(query, update, Item.class);
		return item;

	}

	// Discount update
	public Discount updateDiscount(Discount discount) {
		logger.info("updateDiscount");
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

	// units save and update
	public boolean saveUnits(Units units) {
		if(units.getId()!=null) {
			logger.info("DAO Update Units");
			Update update = new Update();
			Query query = new Query();
			query.addCriteria(Criteria.where("id").is(units.getId()));
			update.set("unitname", units.getUnitname());
			update.set("unitsymbol", units.getUnitsymbol());
			update.set("quantityname", units.getQuantityname());
			update.set("quantitysymbol", units.getQuantitysymbol());
			update.set("dimensionsymbol", units.getDimensionsymbol());
			mongoTemplate.updateFirst(query, update, Units.class);
			// update
		}else {
			// save
			logger.info("DAO Save Units");
			mongoTemplate.save(units);
		}
		return true;
	}

	//------ Remove Units --------
	public Units removeUnit(String id) {
		Units response = null;
		Query query = new Query();
		try {
			query.addCriteria(Criteria.where("id").is(id));
			mongoTemplate.remove(query, Units.class);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			
		}
		return response;
	}

}
