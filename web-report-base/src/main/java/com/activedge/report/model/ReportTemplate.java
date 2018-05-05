package com.activedge.report.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="report_templates")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id", scope = ReportTemplate.class)
public class ReportTemplate implements Serializable, TemplateObject {	
	private static final long serialVersionUID = 2335820059181936975L;

	@Transient
	private Timestamp now = new Timestamp(System.currentTimeMillis());
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private Integer id;
	
	@Column(name="template_name")
	private String templateName;
	
	@Column(name="description", columnDefinition="TEXT")
	private String templateDescription;
	
	@Column(name="report_header")
	private String reportHeader;
	
	@Column(name="is_sql_defined")
	private boolean sqlStringDefined = false;
	
	@Column(name="sql_string", columnDefinition="TEXT")
	private String sqlString;
	
	@OneToMany(mappedBy="reportTemplate", fetch=FetchType.EAGER, 
			orphanRemoval=true)
	private Set<JoinObject> joinObjects;
	
	@OneToMany(mappedBy="reportTemplate", fetch=FetchType.EAGER, 
			orphanRemoval=false)
	private Set<ReportTable> reportTables;
	
	@OneToMany(mappedBy="reportTemplate", fetch=FetchType.EAGER, 
			orphanRemoval=false)
	private Set<ReportCriteria> reportCriterias;
	
	@Column(name="created_at")
	private Timestamp createdAt = now;
	
	@Column(name="updated_at")
	private Timestamp updatedAt = now;

	public ReportTemplate() {
		joinObjects = new HashSet<>();
		reportTables = new HashSet<>();
		reportCriterias = new HashSet<>();
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getTemplateDescription() {
		return templateDescription;
	}

	public void setTemplateDescription(String templateDescription) {
		this.templateDescription = templateDescription;
	}

	public String getReportHeader() {
		return reportHeader;
	}

	public void setReportHeader(String reportHeader) {
		this.reportHeader = reportHeader;
	}

	public boolean isSqlStringDefined() {
		return sqlStringDefined;
	}

	public void setSqlStringDefined(boolean sqlStringDefined) {
		this.sqlStringDefined = sqlStringDefined;
	}

	public String getSqlString() {
		return sqlString;
	}

	public void setSqlString(String sqlString) {
		this.sqlString = sqlString;
	}

	public Set<JoinObject> getJoinObjects() {
		return joinObjects;
	}

	public void setJoinObjects(Set<JoinObject> joinObjects) {
		this.joinObjects = joinObjects;
	}

	public Set<ReportTable> getReportTables() {
		return reportTables;
	}

	public void setReportTables(Set<ReportTable> reportTables) {
		this.reportTables = reportTables;
	}

	public Set<ReportCriteria> getReportCriterias() {
		return reportCriterias;
	}

	public void setReportCriterias(Set<ReportCriteria> reportCriterias) {
		this.reportCriterias = reportCriterias;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}
	
}
