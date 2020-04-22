package com.erp.mongo.dal;

import java.util.List;

import com.erp.mongo.model.Discount;
import com.erp.mongo.model.Item;
import com.erp.mongo.model.Units;

public interface ItemDAL {
	public Item saveItem(Item product);
	public Discount saveDiscount(Discount discount);
	public boolean saveUnits(Units units);

	public List<Item> loadItem(String vendorcode,String category,String prodcode);
	public List<Units> loadUnits(String id);
	
	public List<Discount> loadDiscount(List<Discount> discountlist,String discount);
	
	public Item getItem(String itemid);
	
	public Item updateItem(Item item);
	public Discount updateDiscount(Discount discount);
	
	public Item removeItem(String productcode);	
	public Discount removeDiscount(String discountcode);
	public Units removeUnit(String id); 
}