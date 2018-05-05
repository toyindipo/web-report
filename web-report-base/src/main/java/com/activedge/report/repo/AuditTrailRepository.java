package com.activedge.report.repo;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.activedge.report.model.AuditTrail;

@Transactional
public interface AuditTrailRepository extends JpaRepository<AuditTrail, Integer> {
	@Query(value="Select a from AuditTrail a where "
            + "a.timeOfChange between ?1 and ?2 ORDER BY a.timeOfChange DESC")
    List<AuditTrail> findTrailsByDate(Date startDate,
                                             Date endDate);
}
