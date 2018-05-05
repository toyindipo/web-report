package com.activedge.report.dto;

import com.activedge.report.model.Orderable;

public class ReportColumnDTO implements Orderable {
	private Integer id;
	
	private String columnName;
	
	private int columnType;
	
	private String columnTypeLabel;
	
	private int columnOrder;
	
	private ReportTableDTO table;
	
	private Boolean selectField = Boolean.FALSE;
	
	private String columnHeader;
		
	private float widthRatio;	
	
	private boolean joinColumn;
	
	private int joinCount;
	
	private int criteriaCount;

	public ReportColumnDTO() {
	}
	
	public ReportColumnDTO(String columnName, int columnType, String columnTypeLabel, 
			ReportTableDTO table) {
		this(null, columnName, columnType, columnTypeLabel, table);
	}

	public ReportColumnDTO(Integer id, String columnName, int columnType, String columnTypeLabel, 
			ReportTableDTO table) {
		this(id, columnName, columnType, Boolean.FALSE, columnTypeLabel, table, 0, 0, 0, 0);
	}
	
	public Boolean getSelectField() {
		return selectField;
	}

	public void setSelectField(Boolean selectField) {
		this.selectField = selectField;
	}

	public ReportColumnDTO(Integer id, String columnName, int columnType, Boolean selectField,
			String columnTypeLabel, ReportTableDTO table, float widthRatio, int columnOrder, 
			int joinCount, int criteriaCount) {
		this.id = id;
		this.columnName = columnName;
		this.columnType = columnType;
		this.selectField = selectField;
		this.columnTypeLabel = columnTypeLabel;
		this.table = table;
		this.widthRatio = widthRatio;
		this.columnOrder = columnOrder;
		this.joinCount = joinCount;
		this.criteriaCount = criteriaCount;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public int getColumnType() {
		return columnType;
	}

	public void setColumnType(int columnType) {
		this.columnType = columnType;
	}

	public String getColumnTypeLabel() {
		return columnTypeLabel;
	}

	public void setColumnTypeLabel(String columnTypeLabel) {
		this.columnTypeLabel = columnTypeLabel;
	}

	public int getColumnOrder() {
		return columnOrder;
	}

	public void setColumnOrder(int columnOrder) {
		this.columnOrder = columnOrder;
	}

	public boolean isJoinColumn() {
		return joinColumn;
	}

	public void setJoinColumn(boolean joinColumn) {
		this.joinColumn = joinColumn;
	}
	
	public ReportTableDTO getTable() {
		return table;
	}

	public void setTable(ReportTableDTO table) {
		this.table = table;
	}

	public String getColumnHeader() {
		return columnHeader;
	}

	public void setColumnHeader(String columnHeader) {
		this.columnHeader = columnHeader;
	}

	public float getWidthRatio() {
		return widthRatio;
	}

	public void setWidthRatio(float widthRatio) {
		this.widthRatio = widthRatio;
	}

	public int getJoinCount() {
		return joinCount;
	}

	public void setJoinCount(int joinCount) {
		this.joinCount = joinCount;
	}

	public int getCriteriaCount() {
		return criteriaCount;
	}

	public void setCriteriaCount(int criteriaCount) {
		this.criteriaCount = criteriaCount;
	}
	
	@Override
	public int getOrderValue() {
		return getColumnOrder();
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((columnName == null) ? 0 : columnName.hashCode());
		result = prime * result + columnType;
		result = prime * result + ((columnTypeLabel == null) ? 0 : columnTypeLabel.hashCode());
		result = prime * result + ((table == null || table.getTableName() == null) ? 0 : 
			table.getTableName().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ReportColumnDTO))
			return false;
		ReportColumnDTO other = (ReportColumnDTO) obj;
		if (columnName == null) {
			if (other.columnName != null)
				return false;
		} else if (!columnName.equals(other.columnName))
			return false;
		if (columnType != other.columnType)
			return false;
		if (columnTypeLabel == null) {
			if (other.columnTypeLabel != null)
				return false;
		} else if (!columnTypeLabel.equals(other.columnTypeLabel))
			return false;
		
		if (table == null) {
			if (other.table != null)
				return false;
		} else {
			if (table.getTableName() == null) {
				if (other.table.getTableName() != null)
					return false;
			} else {
				if (!table.getTableName().equals(other.table.getTableName()))
					return false;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		String tableName = table == null || 
				table.getTableName() == null ? "N/A" : table.getTableName();
		return tableName + ":" + columnName;
	}
	
	@Override
	public ReportColumnDTO clone() {
		return new ReportColumnDTO(id, columnName, columnType, selectField,
			columnTypeLabel, table, widthRatio, columnOrder, 
			joinCount, criteriaCount);
	}
	
}
