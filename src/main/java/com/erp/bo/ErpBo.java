package com.erp.bo;

import com.erp.dto.User;

public interface ErpBo {

	public User userLogin(User user);

	public User Checkuser(User user, int temp); 
	
}
