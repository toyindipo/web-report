package com.activedge.report.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.activedge.report.enums.RoleType;
import com.activedge.report.model.Role;

@Transactional
public interface RoleRepository extends JpaRepository<Role, Integer> {
	Role findByRoleType(RoleType roleType);
}
