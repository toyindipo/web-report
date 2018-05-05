package com.activedge.report.service;

import java.sql.Date;
import java.util.List;

import com.activedge.report.model.AuditTrail;

public interface AuditTrailService {
	AuditTrail saveAuditTrail(AuditTrail trail);
	List<AuditTrail> findTrailsByDate(Date startDate, Date endDate);
}
