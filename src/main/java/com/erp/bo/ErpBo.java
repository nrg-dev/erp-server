package com.erp.bo;

import java.util.List;

import com.erp.dto.Enquiry;
import com.erp.dto.User;

public interface ErpBo {

	public User userLogin(User user);

	public User Checkuser(User user, int temp);

	public Enquiry saveEnquiry(Enquiry enquiry);

	public List<Enquiry> loadEnquiry(List<Enquiry> enquirylist);   
	
}
