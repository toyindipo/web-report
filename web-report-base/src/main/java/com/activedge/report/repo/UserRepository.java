package com.activedge.report.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.activedge.report.model.User;

@Transactional
public interface UserRepository extends JpaRepository<User, Integer> {
	User findByEmail(String email);
}
