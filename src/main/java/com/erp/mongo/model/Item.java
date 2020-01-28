package com.erp.mongo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Item {

  @Id
  private String id		;
  String name 			;
  String description 	;
  String updateddate 	;
  String status     	;


public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}

public String getStatus() {
	return status;
}
public void setStatus(String status) {
	this.status = status;
}
  
  
  
	
}
