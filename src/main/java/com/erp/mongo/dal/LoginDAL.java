package com.erp.mongo.dal;

import java.util.List;

import com.erp.dto.User;
import com.erp.mongo.model.Login;

public interface LoginDAL {

	List<Login> userLogin(User user, List<Login> result); 

	

}