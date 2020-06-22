package com.erp.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.erp.dao.ErpDao;
import com.erp.dto.Dropbox;
import com.erp.dto.Enquiry;
import com.erp.dto.Member;
import com.erp.dto.User;

import com.erp.model.CountryDetail;
import com.erp.model.IndustryDetail;
import com.erp.model.UserDetail;
import com.erp.model.UserLogin;
import com.erp.mongo.dal.CustomerDAL;
import com.erp.mongo.dal.LoginDAL;
import com.erp.mongo.dal.PurchaseDAL;
import com.erp.mongo.dal.RandomNumberDAL;
import com.erp.mongo.dal.SalesDAL;
import com.erp.mongo.dal.StockDAL;
import com.erp.util.Custom;
import com.erp.util.Email;
import com.erp.util.ErpException;
import com.erp.mongo.model.Customer;
import com.erp.mongo.model.Login;
import com.erp.mongo.model.POReturnDetails;


@Service("bo")
public class ErpBoImpl implements ErpBo{
	
	public static final Logger logger = LoggerFactory.getLogger(ErpBoImpl.class);

	 
	private final LoginDAL logindal;
	private final RandomNumberDAL randomnumberdal;
	
	public ErpBoImpl(LoginDAL logindal,RandomNumberDAL randomnumberdal) {
		this.logindal = logindal;
		this.randomnumberdal = randomnumberdal;
	}
	
	@Override
	public User userLogin(User user){
		logger.info("userLogin");
		List<Login> result=null;
		try {
			user.setId(1);
			result = logindal.userLogin(user,result);
			if(result.size() > 0) {
				result=null;
				user.setId(2);
				result = logindal.userLogin(user,result);
				if(result.size() > 0) {
					if(result.get(0).getStatus().equalsIgnoreCase("Active")){
						user.setStatus("success");
					}
					if(result.get(0).getStatus().equalsIgnoreCase("De-Active")){
						user.setStatus("Your Account was De-Active");
					}
				}
				else {
					user.setStatus("Invalid Password");
				}
			}else {
				user.setStatus("Invalid User Name.");
			}
		}catch(Exception e){
			logger.error("BO Exception-->"+e.getMessage());
			user.setStatus("Network Error Please try again");

		}
		finally{
			result=null;
		}
		return user;
	}
	
	// ---------------- forget Password use check ------------------------------
	public User Checkuser(User user,int temp){
		if(temp ==1 ){
			user = logindal.Checkuser(user);
			logger.debug("User status-->"+user.getStatus());
		}
		
		if(temp == 2) {
			user = logindal.resetPassword(user);	 		
			logger.debug("User status-->"+user.getStatus());
		}
		
		return user;
	}
	
	public Enquiry saveEnquiry(Enquiry enquiry) {
		enquiry = logindal.saveEnquiry(enquiry);
		return enquiry;
	} 
	
	@Override
	public List<Enquiry> loadEnquiry(List<Enquiry> enquirylist) {
		enquirylist = logindal.loadEnquiry(enquirylist);
		return enquirylist;
	}

	
}
