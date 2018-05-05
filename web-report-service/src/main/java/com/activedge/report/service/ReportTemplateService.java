package com.activedge.report.service;

import java.util.List;

import com.activedge.report.model.ReportTemplate;

public interface ReportTemplateService {
	ReportTemplate findById(Integer id);
	ReportTemplate saveOrUpdate(ReportTemplate template);
	List<ReportTemplate> findAll();
	void deleteTemplate(Integer id);
}
