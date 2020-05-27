package com.erp.mongo.dal;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.erp.mongo.model.UserRole;

@Repository
public class UserMgtImpl implements UserMgtDAL {

	public static final Logger logger = LoggerFactory.getLogger(UserMgtImpl.class);

	@Autowired
	private MongoTemplate mongoTemplate;

	// save
	public UserRole save(UserRole user) {
		mongoTemplate.save(user);
		return user;
	}

	// load User list
	public List<UserRole> load() {
		List<UserRole> userlist = mongoTemplate.findAll(UserRole.class);
		return userlist;
	}

	public UserRole updateUser(UserRole user) {
		Update update = new Update();
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(user.getId()));
		update.set("invnumber", user.getInvnumber());
		update.set("username", user.getUsername());
		update.set("password", user.getPassword());
		update.set("userRole", user.getUserRole());
		update.set("menuItem", user.getMenuItem());
		update.set("subMenuItem", user.getSubMenuItem());
		update.set("departmentname", user.getDepartmentname());
		mongoTemplate.updateFirst(query, update, UserRole.class);
		return user;
	}

	public UserRole removeUser(String id) {
		UserRole response = null;
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(id));
		mongoTemplate.remove(query, UserRole.class);
		return response;
	}

}
