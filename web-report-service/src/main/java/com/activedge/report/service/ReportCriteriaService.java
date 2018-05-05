package com.activedge.report.service;

import java.util.List;

import com.activedge.report.model.ReportCriteria;

public interface ReportCriteriaService {
	ReportCriteria findById(Integer id);
	List<ReportCriteria> findByTemplateId(Integer id);
	List<ReportCriteria> findByReportColumnId(Integer id);
	ReportCriteria saveOrUpdate(ReportCriteria reportCriteria);
	ReportCriteria updateOrderValue(Integer criteriaId, int order);
	void delete(Integer id);
}
