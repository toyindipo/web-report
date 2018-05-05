package com.activedge.report.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.activedge.report.model.ReportTable;

@Transactional
public interface ReportTableRepository extends JpaRepository<ReportTable, Integer> {
	List<ReportTable> findByReportTemplateId(Integer id);
}
