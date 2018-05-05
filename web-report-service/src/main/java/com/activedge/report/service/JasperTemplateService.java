package com.activedge.report.service;

import java.util.List;

import com.activedge.report.model.JasperTemplate;

public interface JasperTemplateService {
	JasperTemplate findByid(Integer id);
	JasperTemplate saveOrUpdate(JasperTemplate template);
	List<JasperTemplate> findAll();
	void deleteTemplate(Integer id);
}
