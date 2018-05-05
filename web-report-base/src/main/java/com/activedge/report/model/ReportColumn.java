package com.activedge.report.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * 
 * @author Oladele Toyin
 * @since 29jan2018 
 */

@Entity
@Table(name="report_columns")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id", scope = ReportColumn.class)
public class ReportColumn implements Serializable, Orderable {
	private static final long serialVersionUID = 7274456398583437037L;

	@Transient
	private Timestamp now = new Timestamp(System.currentTimeMillis());
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private Integer id;
	
	@Column(name="column_name")
	private String columnName;
	
	@Column(name="column_type")
	private int columnType;
	
	@Column(name="column_type_label")
	private String columnTypeLabel;
	
	@Column(name="column_order")
	private int columnOrder;
	
		
	@Column(name="column_header")
	private String columnHeader;
	
	@Column(name="width_ratio")
	private float widthRatio;
	
	@Column(name="join_count")
	private int joinCount;
	
	@Column(name="criteria_count")
	private int criteriaCount;
	
	@Column(name="select_field")
	private Boolean selectField = Boolean.FALSE;
	
	@JoinColumn(name="report_table_id")
	@ManyToOne
	private ReportTable reportTable;
	
	@Column(name="created_at")
	private Timestamp createdAt = now;
	
	@Column(name="updated_at")
	private Timestamp updatedAt = now;

	
	public ReportColumn() {
		
	}	

	public ReportColumn(String columnName, int columnType, String columnTypeLabel) {
		this.columnName = columnName;
		this.columnType = columnType;
		this.columnTypeLabel = columnTypeLabel;
	}
	
	public ReportColumn(String columnName, int columnType, String columnTypeLabel, 
			int columnOrder, String columnHeader,
			float widthRatio, Boolean selectField) {
		this.columnName = columnName;
		this.columnType = columnType;
		this.columnTypeLabel = columnTypeLabel;
		this.columnOrder = columnOrder;
		this.columnHeader = columnHeader;
		this.widthRatio = widthRatio;
		this.selectField = selectField;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getColumnHeader() {
		return columnHeader;
	}

	public void setColumnHeader(String columnHeader) {
		this.columnHeader = columnHeader;
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

	public Boolean getSelectField() {
		return selectField;
	}

	public void setSelectField(Boolean selectField) {
		this.selectField = selectField;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public int getColumnOrder() {
		return columnOrder;
	}

	public void setColumnOrder(int columnOrder) {
		this.columnOrder = columnOrder;
	}

	public float getWidthRatio() {
		return widthRatio;
	}

	public void setWidthRatio(float widthRatio) {
		this.widthRatio = widthRatio;
	}

	public ReportTable getReportTable() {
		return reportTable;
	}

	public void setReportTable(ReportTable reportTable) {
		this.reportTable = reportTable;
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
	
	@Override
	public int getOrderValue() {
		return getColumnOrder();
	}
		
	@Override
    public boolean equals(Object that) {
        if (!(that instanceof ReportColumn)) {
            return false;
        }
        ReportColumn column = (ReportColumn)that;
        if (column.id == null) {
            return false;
        }
        return column.id.equals(id);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (id == null ? 0 : id.intValue());
        return result;
    }
    
    @Override
    public String toString() {
    	String prefix = (reportTable == null || reportTable.getTableName() == null) ? "" :
    		reportTable + ".";
    	return prefix + columnName;
    }
}
