package com.erp.mongo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class UserRole {

	@Id
	private String id;
	private String invnumber;
	private String username;
	private String userrole;
	private String menuitem;
	private String submenuitem;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getInvnumber() {
		return invnumber;
	}
	public void setInvnumber(String invnumber) {
		this.invnumber = invnumber;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUserrole() {
		return userrole;
	}
	public void setUserrole(String userrole) {
		this.userrole = userrole;
	}
	public String getMenuitem() {
		return menuitem;
	}
	public void setMenuitem(String menuitem) {
		this.menuitem = menuitem;
	}
	public String getSubmenuitem() {
		return submenuitem;
	}
	public void setSubmenuitem(String submenuitem) {
		this.submenuitem = submenuitem;
	}
	
}
