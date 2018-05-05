package com.activedge.report.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.activedge.report.model.ReportCriteria;

@Transactional
public interface ReportCriteriaRepository extends JpaRepository<ReportCriteria, Integer> {
	List<ReportCriteria> findByReportTemplateId(Integer id);
	List<ReportCriteria> findByReportColumnId(Integer id);
}
