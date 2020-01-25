package com.ggl.mongo.model;

import org.json.JSONArray;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mongodb.util.JSON;

@Document
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class PurchaseOrder {

	
	
	@JsonIgnore
    public JSONArray purchaseorder;

	
	
	
	
	  public JSONArray getPurchaseorder() { return purchaseorder; }
	  
	  public void setPurchaseorder(JSONArray purchaseorder) { this.purchaseorder =
	  purchaseorder; }
	 
	
	
	//  @JsonProperty public String data;
	 
	/*
	 * @Id private String id; JSON po_order ; String status ;
	 * 
	 * public String getId() { return id; } public void setId(String id) { this.id =
	 * id; }
	 * 
	 * 
	 * public JSON getPo_order() { return po_order; } public void setPo_order(JSON
	 * po_order) { this.po_order = po_order; } public String getStatus() { return
	 * status; } public void setStatus(String status) { this.status = status; }
	 * 
	 * 
	 */

	/*
	 * public JSONArray getPurchaseorder() { return purchaseorder; }
	 * 
	 * public void setPurchaseorder(JSONArray purchaseorder) { this.purchaseorder =
	 * purchaseorder; }
	 */

	/*
	 * public String getData() { return data; }
	 * 
	 * public void setData(String data) { this.data = data; }
	 */

	
	
}
