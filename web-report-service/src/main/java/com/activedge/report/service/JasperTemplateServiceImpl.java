package com.activedge.report.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.activedge.report.model.JasperTemplate;
import com.activedge.report.repo.JasperTemplateRepository;

@Service
public class JasperTemplateServiceImpl implements JasperTemplateService {
	private JasperTemplateRepository templateRepository;
	
	@Autowired
	public JasperTemplateServiceImpl(JasperTemplateRepository templateRepository) {
		this.templateRepository = templateRepository;
	}
	
	public JasperTemplate findByid(Integer id) {
		return templateRepository.findOne(id);
	}

	@Transactional
	public JasperTemplate saveOrUpdate(JasperTemplate template) {
		if (template.getId() != null) {
			JasperTemplate previous = templateRepository.findOne(template.getId());
			previous.setFilePath(template.getFilePath());
			previous.setTemplateDescription(template.getTemplateDescription());
			previous.setTemplateName(template.getTemplateName());
			previous.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
			template = previous;
		}
		return templateRepository.saveAndFlush(template);
	}

	public List<JasperTemplate> findAll() {
		return templateRepository.findAll();
	}

	@Transactional
	public void deleteTemplate(Integer id) {
		templateRepository.delete(id);
		templateRepository.flush();
	}

}
