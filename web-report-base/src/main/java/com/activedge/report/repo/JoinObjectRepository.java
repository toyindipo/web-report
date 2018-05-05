package com.activedge.report.repo;

import java.util.List;

import com.activedge.report.model.ReportColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.activedge.report.model.JoinObject;

@Transactional
public interface JoinObjectRepository extends JpaRepository<JoinObject, Integer> {
	List<JoinObject> findByReportTemplateId(Integer id);
	@Query("Select j from JoinObject j where j.column1 = ?1 or j.column2 = ?1")
	List<JoinObject> findByReportColumn(ReportColumn reportColumn);
}
