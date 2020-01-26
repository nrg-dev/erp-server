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
import org.springframework.stereotype.Service;

import com.erp.dao.ErpDao;
import com.erp.dto.Dropbox;
import com.erp.dto.Member;
import com.erp.dto.User;

import com.erp.model.CountryDetail;
import com.erp.model.IndustryDetail;
import com.erp.model.UserDetail;
import com.erp.model.UserLogin;
import com.erp.mongo.dal.CustomerDAL;
import com.erp.util.Custom;
import com.erp.util.Email;
import com.erp.util.ErpException;
import com.erp.mongo.model.Customer;


@Service("bo")
public class ErpBoImpl implements ErpBo{
	
	public static final Logger logger = LoggerFactory.getLogger(ErpBoImpl.class);

	
	/*
	 * @Value("${memeber.silver}") private String silver;
	 * 
	 * @Value("${memeber.gold}") private String gold;
	 * 
	 * @Value("${memeber.platinum}") private String platinum;
	 */
	 
	@Autowired
	ErpDao dao;
	
	/*
	 * @Autowired CustomerDAL customerdal;
	 */
	/*
	 * private final CustomerDAL customerdal;
	 * 
	 * public ErpBoImpl(CustomerDAL customerdal) { //this.randamNumberDAL =
	 * randamNumberDAL; this.customerdal = customerdal; }
	 */
	/*
	 * public Customer saveCustomer(Customer customer) { return
	 * customerdal.saveCustomer(customer); }
	 */
	
	
}
