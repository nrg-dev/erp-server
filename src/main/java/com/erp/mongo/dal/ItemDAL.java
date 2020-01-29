package com.erp.mongo.dal;

import java.util.List;

import com.erp.mongo.model.Item;

public interface ItemDAL {
	public Item saveItem(Item product);
	public List<Item> loadItem(List<Item> itemlist);
	public Item getItem(String itemid);
	public Item updateItem(Item item);
	public void removeItem(String itemid);	
}