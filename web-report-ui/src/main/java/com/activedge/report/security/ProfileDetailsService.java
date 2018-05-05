package com.activedge.report.security;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.activedge.report.config.ProfileDetails;
import com.activedge.report.model.User;
import com.activedge.report.repo.UserRepository;

@Service
public class ProfileDetailsService implements UserDetailsService {
	@Autowired
	private UserRepository userDAO;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userDAO.findByEmail(email);
		if (user != null) {
			user.setLastLogin(new Timestamp(System.currentTimeMillis()));
			userDAO.saveAndFlush(user);
			return new ProfileDetails(user);
		}
		throw new UsernameNotFoundException("User with email '" + email + "' not found");
	}

}
