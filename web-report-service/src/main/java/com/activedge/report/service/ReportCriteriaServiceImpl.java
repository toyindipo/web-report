package com.activedge.report.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.activedge.report.model.ReportCriteria;
import com.activedge.report.model.ReportTemplate;
import com.activedge.report.repo.ReportCriteriaRepository;
import com.activedge.report.repo.ReportTemplateRepository;

@Service
public class ReportCriteriaServiceImpl implements ReportCriteriaService {
	private ReportCriteriaRepository criteriaRepository;
	private ReportTemplateRepository templateRepository;
	
	@Autowired
	public ReportCriteriaServiceImpl(ReportCriteriaRepository criteriaRepository,
			ReportTemplateRepository templateRepository) {
		this.criteriaRepository = criteriaRepository;
		this.templateRepository = templateRepository;
	}

	public ReportCriteria findById(Integer id) {
		return criteriaRepository.findOne(id);
	}

	public List<ReportCriteria> findByTemplateId(Integer id) {
		return criteriaRepository.findByReportTemplateId(id);
	}

	@Transactional
	public ReportCriteria saveOrUpdate(ReportCriteria criteria) {
		if (criteria.getId() != null) {
			ReportCriteria previous = criteriaRepository.findOne(criteria.getId());
			previous.setCriteriaType(criteria.getCriteriaType());
			previous.setValue1(criteria.getValue1());
			previous.setValue2(criteria.getValue2());
			previous.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
			criteria = previous;
		}
		return criteriaRepository.saveAndFlush(criteria);
	}

	@Transactional
	public void delete(Integer id) {
		ReportCriteria criteria = criteriaRepository.findOne(id);
		ReportTemplate template = 
				templateRepository.findOne(criteria.getReportTemplate().getId());
		ReportCriteria criteriaToDelete = null;
		for (ReportCriteria criteriaItem: template.getReportCriterias()) {
			if (criteriaItem.getId().equals(id)) {
				criteriaToDelete = criteriaItem;
				break;
			}
		}
		
		if (criteriaToDelete != null) {
			criteriaToDelete.setReportTemplate(null);
			templateRepository.saveAndFlush(template);
		}
		
		criteriaRepository.delete(criteriaToDelete);
		criteriaRepository.flush();
	}
	public List<ReportCriteria> findByReportColumnId(Integer id) {
		return criteriaRepository.findByReportColumnId(id);
	}

	@Override
	public ReportCriteria updateOrderValue(Integer id, int order) {
		ReportCriteria criteria = criteriaRepository.findOne(id);
		criteria.setCriteriaOrder(order);
		return criteriaRepository.saveAndFlush(criteria);
	}

}
