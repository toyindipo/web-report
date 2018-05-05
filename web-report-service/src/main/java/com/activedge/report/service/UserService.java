package com.activedge.report.service;

import java.util.List;

import com.activedge.report.enums.RoleType;
import com.activedge.report.exception.UserNotInDBException;
import com.activedge.report.model.User;

public interface UserService {
	List<User> findAllUsers();
    User findUserByEmail(String email) throws UserNotInDBException;
    User findUserById(Integer id) throws UserNotInDBException;
    User saveOrUpdate(User user);
    User changeUserRole(User user, RoleType roleType);
}
