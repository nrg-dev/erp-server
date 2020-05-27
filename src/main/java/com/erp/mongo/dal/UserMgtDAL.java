package com.erp.mongo.dal;

import java.util.List;
import com.erp.mongo.model.UserRole;

public interface UserMgtDAL {

	public UserRole save(UserRole finance);

	public List<UserRole> load();

	public UserRole updateUser(UserRole userrole);

	public UserRole removeUser(String id);
}