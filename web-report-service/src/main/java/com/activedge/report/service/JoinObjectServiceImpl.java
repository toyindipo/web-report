package com.activedge.report.service;

import java.util.List;

import com.activedge.report.model.ReportColumn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.activedge.report.model.JoinObject;
import com.activedge.report.model.ReportTemplate;
import com.activedge.report.repo.JoinObjectRepository;
import com.activedge.report.repo.ReportTemplateRepository;

@Service
public class JoinObjectServiceImpl implements JoinObjectService {
	
	private JoinObjectRepository joinObjectRepository;
	private ReportTemplateRepository templateRepository;
	
	@Autowired
	public JoinObjectServiceImpl(JoinObjectRepository joinObjectRepository, 
			ReportTemplateRepository templateRepository) {
		this.joinObjectRepository = joinObjectRepository;
		this.templateRepository = templateRepository;
	}
	
	public JoinObject findById(Integer id) {
		return joinObjectRepository.findOne(id);
	}

	public List<JoinObject> findByTemplateId(Integer templateId) {
		return joinObjectRepository.findByReportTemplateId(templateId);
	}

	@Transactional
	public JoinObject saveOrUpdate(JoinObject joinObject) {
		if (joinObject.getId() != null) {
			JoinObject previous = joinObjectRepository.findOne(joinObject.getId());
			previous.setJoinDescription(joinObject.getJoinDescription());
			joinObject = previous;
		}
		return joinObjectRepository.saveAndFlush(joinObject);
	}

	@Transactional
	public void delete(Integer id) {
		JoinObject joinObject = joinObjectRepository.findOne(id);
		ReportTemplate template = 
				templateRepository.findOne(joinObject.getReportTemplate().getId());
		JoinObject joinObjectToDelete = null;
		for (JoinObject joinItem: template.getJoinObjects()) {
			if (joinItem.getId().equals(id)) {
				joinObjectToDelete = joinItem;
				break;
			}
		}
		
		if (joinObjectToDelete != null) {
			joinObjectToDelete.setReportTemplate(null);
			templateRepository.saveAndFlush(template);
		}
		
		joinObjectRepository.delete(joinObjectToDelete);
		joinObjectRepository.flush();
	}
	
	public List<JoinObject> findByReportColumn(ReportColumn reportColumn) {
		return joinObjectRepository.findByReportColumn(reportColumn);
	}

	@Override
	public JoinObject updateOrderValue(Integer id, int order) {
		JoinObject joinObject = joinObjectRepository.findOne(id);
		joinObject.setJoinOrder(order);
		return joinObjectRepository.saveAndFlush(joinObject);
	}

}
