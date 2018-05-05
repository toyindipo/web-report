package com.activedge.report.service;

import java.util.List;

import com.activedge.report.enums.RoleType;
import com.activedge.report.model.Role;

public interface RoleService {
	Role saveRole(RoleType roleType);
	List<Role> findAll();
	Role findByRoleType(RoleType roleType);
}
