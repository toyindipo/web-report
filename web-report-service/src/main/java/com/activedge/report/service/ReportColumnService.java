package com.activedge.report.service;

import com.activedge.report.model.ReportColumn;
/**
 * 
 * @author HP
 *
 */
public interface ReportColumnService {
	ReportColumn findById(Integer id);
	ReportColumn saveOrUpdateColumn(ReportColumn column);
	void deleteReportColumn(Integer columnId);
	ReportColumn updateSelectMode(Integer columnId, Boolean mode);
	ReportColumn updateJoinCount(Integer columnId, int count);
	ReportColumn updateCriteriaCount(Integer columnId, int count);
	ReportColumn updateOrderValue(Integer columnId, int order);
}
