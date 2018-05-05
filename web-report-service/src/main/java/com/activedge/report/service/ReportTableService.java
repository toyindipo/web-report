package com.activedge.report.service;

import java.util.List;

import com.activedge.report.model.ReportTable;

public interface ReportTableService {
	ReportTable findById(Integer id);
	List<ReportTable> findByTemplateId(Integer id);
	void delete(Integer id);
	ReportTable save(ReportTable table);
}
