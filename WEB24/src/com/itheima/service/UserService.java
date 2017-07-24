package com.itheima.service;

import java.sql.SQLException;

import com.itheima.dao.UserDao;
import com.itheima.domain.User;

public class UserService {

	public User login(String username, String password) throws SQLException {
		UserDao dao = new UserDao();
		User user = dao.login(username, password);
		return user;
	}

}
