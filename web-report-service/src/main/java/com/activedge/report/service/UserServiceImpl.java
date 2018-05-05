package com.activedge.report.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.activedge.report.constant.CustomConstants;
import com.activedge.report.enums.RoleType;
import com.activedge.report.exception.UserNotInDBException;
import com.activedge.report.model.Role;
import com.activedge.report.model.User;
import com.activedge.report.repo.RoleRepository;
import com.activedge.report.repo.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	private UserRepository userRepository;
	private RoleRepository roleRepository;
	
	@Autowired
	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public List<User> findAllUsers() {
		return userRepository.findAll();
	}

	public User findUserByEmail(String email) throws UserNotInDBException {
		User user = userRepository.findByEmail(email);
        if(user == null){
            throw new UserNotInDBException("User with id " + email +" is not in the database");
        }
        return user;
	}

	public User findUserById(Integer id) throws UserNotInDBException {
		User user = userRepository.findOne(id);
        if(user == null){
            throw new UserNotInDBException("User with id " + id +" is not in the database");
        }
        return user;
	}
	
	@Transactional
	public User saveOrUpdate(User user) {
		if (user.getId() == null) {
			String password = user.getPassword().trim();
			StandardPasswordEncoder encoder = 
					new StandardPasswordEncoder(CustomConstants.PASSWORD_KEY_ENCODER);
			user.setPassword(encoder.encode(password));
			Role userRole = roleRepository.findByRoleType(RoleType.USER);
			user.setRole(userRole);
		} else {
			User previousUser = userRepository.findOne(user.getId());
			previousUser.setEmail(user.getEmail());
			previousUser.setFirstName(user.getFirstName());
			previousUser.setLastName(user.getLastName());
			previousUser.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
			user = previousUser;
		}
		return userRepository.saveAndFlush(user);
	}
	
	@Transactional
	public User changeUserRole(User user, RoleType roleType) {
		Role userRole = roleRepository.findByRoleType(RoleType.USER);
		user.setRole(userRole);
		user.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
		return userRepository.saveAndFlush(user);
	}
}
