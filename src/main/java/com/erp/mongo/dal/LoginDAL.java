package com.erp.mongo.dal;

import java.util.List;

import com.erp.dto.Enquiry;
import com.erp.dto.User;
import com.erp.mongo.model.Login;

public interface LoginDAL {

	public List<Login> userLogin(User user, List<Login> result);

	public User Checkuser(User user);

	public User resetPassword(User user);

	public Enquiry saveEnquiry(Enquiry enquiry);

	public List<Enquiry> loadEnquiry(List<Enquiry> enquirylist);  

	

}