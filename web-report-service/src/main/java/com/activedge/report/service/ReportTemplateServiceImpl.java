package com.activedge.report.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.activedge.report.model.ReportTemplate;
import com.activedge.report.repo.ReportTemplateRepository;

@Service
public class ReportTemplateServiceImpl implements ReportTemplateService {
	private ReportTemplateRepository templateRepository;
	
	@Autowired
	public ReportTemplateServiceImpl(ReportTemplateRepository templateRepository) {
		this.templateRepository = templateRepository;
	}
	
	public ReportTemplate findById(Integer id) {
		return templateRepository.findOne(id);
	}

	@Transactional
	public ReportTemplate saveOrUpdate(ReportTemplate template) {
		if (template.getId() != null) {
			ReportTemplate previous = templateRepository.findOne(template.getId());
			previous.setReportHeader(template.getReportHeader());
			previous.setSqlString(template.getSqlString());
			previous.setSqlStringDefined(template.isSqlStringDefined());
			previous.setTemplateDescription(template.getTemplateDescription());
			previous.setTemplateName(template.getTemplateName());
			previous.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
			template = previous;			
		}
		return templateRepository.saveAndFlush(template);
	}

	public List<ReportTemplate> findAll() {
		return templateRepository.findAll();
	}

	@Transactional
	public void deleteTemplate(Integer id) {
		templateRepository.delete(id);
		templateRepository.flush();
	}

}
