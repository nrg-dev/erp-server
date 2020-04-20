package com.erp.mongo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Units {

	@Id
	private String id;
	private String unitname;
	private String unitsymbol	;
	private String quantityname	;
	private String quantitysymbol	;
	private String dimensionsymbol ;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUnitname() {
		return unitname;
	}
	public void setUnitname(String unitname) {
		this.unitname = unitname;
	}
	public String getUnitsymbol() {
		return unitsymbol;
	}
	public void setUnitsymbol(String unitsymbol) {
		this.unitsymbol = unitsymbol;
	}
	public String getQuantityname() {
		return quantityname;
	}
	public void setQuantityname(String quantityname) {
		this.quantityname = quantityname;
	}
	
	
	public String getQuantitysymbol() {
		return quantitysymbol;
	}
	public void setQuantitysymbol(String quantitysymbol) {
		this.quantitysymbol = quantitysymbol;
	}
	public String getDimensionsymbol() {
		return dimensionsymbol;
	}
	public void setDimensionsymbol(String dimensionsymbol) {
		this.dimensionsymbol = dimensionsymbol;
	}

	
	 
}
