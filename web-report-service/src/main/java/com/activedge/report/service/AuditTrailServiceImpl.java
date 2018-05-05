package com.activedge.report.service;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.activedge.report.model.AuditTrail;
import com.activedge.report.repo.AuditTrailRepository;

@Service
public class AuditTrailServiceImpl implements AuditTrailService {
	private AuditTrailRepository auditTrailRepository;
	
	@Autowired
	public AuditTrailServiceImpl(AuditTrailRepository auditTrailRepository) {
		this.auditTrailRepository = auditTrailRepository;
	}
	
	@Transactional
	public AuditTrail saveAuditTrail(AuditTrail trail) {
		return auditTrailRepository.saveAndFlush(trail);
	}

	public List<AuditTrail> findTrailsByDate(Date startDate, Date endDate) {
		return auditTrailRepository.findTrailsByDate(startDate, endDate);
	}

}
