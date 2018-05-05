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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="report_tables")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id", scope = ReportTable.class)
public class ReportTable implements Serializable {
	
	private static final long serialVersionUID = 1310066443451278434L;

	@Transient
	private Timestamp now = new Timestamp(System.currentTimeMillis());
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private Integer id;
	
	@Column(name="table_name")
	private String tableName;
	
	@Column(name="table_schema")
	private String tableSchema;
	
	@OneToMany(mappedBy="reportTable", fetch=FetchType.EAGER, 
			orphanRemoval=true)
	private Set<ReportColumn> reportColumns;
	
	@JoinColumn(name="report_template_id")
	@ManyToOne
	private ReportTemplate reportTemplate;
	
	@Column(name="created_at")
	private Timestamp createdAt = now;
	
	@Column(name="updated_at")
	private Timestamp updatedAt = now;	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	public String getTableSchema() {
		return tableSchema;
	}

	public void setTableSchema(String tableSchema) {
		this.tableSchema = tableSchema;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public ReportTemplate getReportTemplate() {
		return reportTemplate;
	}

	public void setReportTemplate(ReportTemplate reportTemplate) {
		this.reportTemplate = reportTemplate;
	}

	public Set<ReportColumn> getReportColumns() {
		return reportColumns;
	}

	public void setReportColumns(Set<ReportColumn> reportColumns) {
		this.reportColumns = reportColumns;
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
	
	public ReportTable() {
		this(null, null);
	}
	

	public ReportTable(String tableName, String tableSchema) {
		reportColumns = new HashSet<>();
		this.tableName = tableName;
		this.tableSchema = tableSchema;
	}

	@Override
    public boolean equals(Object that) {
        if (!(that instanceof ReportTable)) {
            return false;
        }
        ReportTable table = (ReportTable)that;
        if (table.id == null) {
            return false;
        }
        return table.id.equals(id);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (id == null ? 0 : id.intValue());
        return result;
    }
    
    @Override
    public String toString() {
    	return tableName;
    }
}
