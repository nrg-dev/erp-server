package com.erp.mongo.dal;

import java.util.List;

import com.erp.mongo.model.Category;
import com.erp.mongo.model.Discount;
import com.erp.mongo.model.Item;

public interface ItemDAL {
	public Item saveItem(Item product);
	public Discount saveDiscount(Discount discount);
	
	public List<Item> loadItem(List<Item> itemlist,String category);
	public List<Discount> loadDiscount(List<Discount> discountlist);
	
	public Item getItem(String itemid);
	
	public Item updateItem(Item item);
	public Discount updateDiscount(Discount discount);
	
	public Item removeItem(String productcode);	
	public Discount removeDiscount(String discountcode);
}