package com.activedge.report.service;

import java.util.List;

import com.activedge.report.model.JoinObject;
import com.activedge.report.model.ReportColumn;

public interface JoinObjectService {
	JoinObject findById(Integer id);
	List<JoinObject> findByTemplateId(Integer templateId);
	List<JoinObject> findByReportColumn(ReportColumn reportColumn);
	JoinObject saveOrUpdate(JoinObject joinObject);
	JoinObject updateOrderValue(Integer joinObjectId, int order);
	void delete(Integer id);
}
