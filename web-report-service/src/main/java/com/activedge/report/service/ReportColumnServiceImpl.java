package com.activedge.report.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.activedge.report.model.JoinObject;
import com.activedge.report.model.ReportColumn;
import com.activedge.report.model.ReportCriteria;
import com.activedge.report.model.ReportTable;
import com.activedge.report.repo.ReportColumnRepository;
import com.activedge.report.repo.ReportTableRepository;

@Service
public class ReportColumnServiceImpl implements ReportColumnService {
	private ReportColumnRepository columnRepository;
	private ReportCriteriaService criteriaService;
	private JoinObjectService joinObjectService;
	private ReportTableRepository tableRepository;
	
	@Autowired
	public ReportColumnServiceImpl(ReportColumnRepository columnRepository, ReportCriteriaService criteriaService,
			JoinObjectService joinObjectService, ReportTableRepository tableRepository) {
		this.columnRepository = columnRepository;
		this.criteriaService = criteriaService;
		this.joinObjectService = joinObjectService;
		this.tableRepository = tableRepository;
	}

	@Transactional
	public ReportColumn saveOrUpdateColumn(ReportColumn column) {
		if (column.getId() != null) {
			ReportColumn previous = columnRepository.findOne(column.getId());
			previous.setColumnHeader(column.getColumnHeader());
			previous.setWidthRatio(column.getWidthRatio());
			previous.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
			column = previous;
		}
		return columnRepository.saveAndFlush(column);
	}

	@Transactional
	public void deleteReportColumn(Integer id) {
		ReportColumn column = columnRepository.findOne(id);
		
		List<ReportCriteria> criterias = criteriaService.findByReportColumnId(id);
		for (ReportCriteria criteria: criterias) {
			criteriaService.delete(criteria.getId());
		}
		
		List<JoinObject> joinObjects = joinObjectService.findByReportColumn(column);
		for (JoinObject joinObject: joinObjects) {
			joinObjectService.delete(joinObject.getId());
		}
		
		ReportTable table = tableRepository.findOne(column.getReportTable().getId());
		ReportColumn columnToDelete = null;
		for (ReportColumn columnItem: table.getReportColumns()) {
			if (columnItem.getId().equals(id)) {
				columnToDelete = columnItem;
				break;
			}
		}
		columnToDelete.setReportTable(null);
		tableRepository.saveAndFlush(table);
		
		columnRepository.delete(columnToDelete);
		columnRepository.flush();
	}

	public ReportColumn findById(Integer id) {
		return columnRepository.findOne(id);
	}
	
	@Transactional
	public ReportColumn updateSelectMode(Integer columnId, Boolean mode) {
		ReportColumn column = columnRepository.findOne(columnId);
		column.setSelectField(mode);
		column.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
		return columnRepository.saveAndFlush(column);
	}
	
	@Transactional
	public ReportColumn updateJoinCount(Integer columnId, int count) {
		ReportColumn column = columnRepository.findOne(columnId);
		int newJoinCount = column.getJoinCount() + count;
		column.setJoinCount(newJoinCount);
		column.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
		return columnRepository.saveAndFlush(column);
	}
	
	@Transactional
	public ReportColumn updateCriteriaCount(Integer columnId, int count) {
		ReportColumn column = columnRepository.findOne(columnId);
		int newCriteriaCount = column.getCriteriaCount() + count;
		column.setCriteriaCount(newCriteriaCount);
		column.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
		return columnRepository.saveAndFlush(column);
	}

	@Override
	@Transactional
	public ReportColumn updateOrderValue(Integer columnId, int order) {
		ReportColumn column = columnRepository.findOne(columnId);
		column.setColumnOrder(order);
		column.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
		return columnRepository.saveAndFlush(column);
	}

}
