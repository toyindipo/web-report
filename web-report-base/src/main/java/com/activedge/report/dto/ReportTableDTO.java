package com.activedge.report.dto;

import java.util.HashSet;
import java.util.Set;

public class ReportTableDTO {
	private Integer id;
	
	private String tableName;
	
	private String tableSchema;
	
	private Set<ReportColumnDTO> reportColumns;

	public String getTableName() {
		return tableName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Set<ReportColumnDTO> getReportColumns() {
		return reportColumns;
	}

	public void setReportColumns(Set<ReportColumnDTO> reportColumns) {
		this.reportColumns = reportColumns;
	}

	public ReportTableDTO() {
		this(null, null, new HashSet<ReportColumnDTO>());
	}

	public ReportTableDTO(String tableName, String tableSchema) {
		this(tableName, tableSchema, new HashSet<ReportColumnDTO>());		
	}
	
	public ReportTableDTO(String tableName, String tableSchema, Set<ReportColumnDTO> reportColumns) {
		this(null, tableName, tableSchema, new HashSet<ReportColumnDTO>());
	}
	
	public ReportTableDTO(Integer id, String tableName, String tableSchema, Set<ReportColumnDTO> reportColumns) {
		this.id = id;
		this.tableName = tableName;
		this.tableSchema = tableSchema;
		this.reportColumns = reportColumns;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tableName == null) ? 0 : tableName.hashCode());
		result = prime * result + ((tableSchema == null) ? 0 : tableSchema.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ReportTableDTO))
			return false;
		ReportTableDTO other = (ReportTableDTO) obj;
		if (tableName == null) {
			if (other.tableName != null)
				return false;
		} else if (!tableName.equals(other.tableName))
			return false;
		if (tableSchema == null) {
			if (other.tableSchema != null)
				return false;
		} else if (!tableSchema.equals(other.tableSchema))
			return false;
		return true;
	}
	
	@Override
    public String toString() {
    	return tableName;
    }
	
}
