package com.erp.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.erp.dto.Member;
import com.erp.dto.User;

import com.erp.model.CountryDetail;
import com.erp.model.IndustryDetail;
import com.erp.model.RandamNumber;
import com.erp.model.UserDetail;
import com.erp.model.UserLogin;
import com.erp.util.Custom;
import com.erp.util.Email;

//import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;



@Repository
@Singleton
public class ErpDaoImpl implements ErpDao {

	public static final Logger logger = LoggerFactory.getLogger(ErpDaoImpl.class);

	@PersistenceContext(unitName="erp-pu")
	private EntityManager entityManager;
	
	 
	
			
}
