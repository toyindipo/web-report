package com.activedge.report.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.activedge.report.enums.RoleType;
import com.activedge.report.model.Role;
import com.activedge.report.repo.RoleRepository;

@Service
public class RoleServiceImpl implements RoleService {
	private RoleRepository roleRepository;
	
	@Autowired
	public RoleServiceImpl(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}	
	
	@Transactional
	public Role saveRole(RoleType roleType) {
		Role role = new Role(roleType);
		return roleRepository.saveAndFlush(role);
	}

	public List<Role> findAll() {
		return roleRepository.findAll();
	}

	public Role findByRoleType(RoleType roleType) {
		return roleRepository.findByRoleType(roleType);
	}

}
