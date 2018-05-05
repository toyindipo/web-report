package com.activedge.report.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.activedge.report.model.ReportColumn;
import com.activedge.report.model.ReportTable;
import com.activedge.report.model.ReportTemplate;
import com.activedge.report.repo.ReportTableRepository;
import com.activedge.report.repo.ReportTemplateRepository;

@Service
public class ReportTableServiceImpl implements ReportTableService {
	private ReportTableRepository tableRepository;
	private ReportTemplateRepository templateRepository;
	private ReportColumnService columnService;
	
	@Autowired
	public ReportTableServiceImpl(ReportTableRepository tableRepository, 
			ReportTemplateRepository templateRepository, ReportColumnService columnService) {
		this.tableRepository = tableRepository;
		this.templateRepository = templateRepository;
		this.columnService = columnService;
	}

	public ReportTable findById(Integer id) {
		return tableRepository.findOne(id);
	}

	public List<ReportTable> findByTemplateId(Integer id) {
		return tableRepository.findByReportTemplateId(id);
	}
	
	@Transactional
	public void delete(Integer id) {
		ReportTable table = tableRepository.findOne(id);
		for (ReportColumn column: table.getReportColumns()) {
			columnService.deleteReportColumn(column.getId());
		}		
		
		ReportTemplate template = templateRepository.findOne(table.getReportTemplate()
				.getId());
		ReportTable tableToDelete = null;
		for (ReportTable tableItem: template.getReportTables()) {
			if (tableItem.getId().equals(id)) {
				tableToDelete = tableItem;
				break;
			}
		}
		if (tableToDelete != null) {
			tableToDelete.setReportTemplate(null);
			templateRepository.saveAndFlush(template);
		}
		
		tableRepository.delete(tableToDelete);
		tableRepository.flush();
	}

	@Override
	public ReportTable save(ReportTable table) {
		return tableRepository.saveAndFlush(table);
	}

}
