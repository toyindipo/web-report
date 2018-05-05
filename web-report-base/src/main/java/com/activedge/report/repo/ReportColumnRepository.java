package com.activedge.report.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.activedge.report.model.ReportColumn;

@Transactional
public interface ReportColumnRepository extends JpaRepository<ReportColumn, Integer> {

}
