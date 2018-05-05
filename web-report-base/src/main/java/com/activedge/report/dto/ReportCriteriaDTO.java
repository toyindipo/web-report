package com.activedge.report.dto;

import com.activedge.report.enums.CriteriaType;
import com.activedge.report.model.Orderable;

public class ReportCriteriaDTO implements Orderable {
	private Integer id;
	
	private ReportColumnDTO reportColumn;
	
	private String value1;
	
	private String value2;
	
	private CriteriaType criteriaType;
	
	private int criteriaOrder;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


	public ReportColumnDTO getReportColumn() {
		return reportColumn;
	}

	public void setReportColumn(ReportColumnDTO reportColumn) {
		this.reportColumn = reportColumn;
	}

	public String getValue1() {
		return value1;
	}

	public void setValue1(String value1) {
		this.value1 = value1;
	}

	public String getValue2() {
		return value2;
	}

	public void setValue2(String value2) {
		this.value2 = value2;
	}

	public CriteriaType getCriteriaType() {
		return criteriaType;
	}

	public void setCriteriaType(CriteriaType criteriaType) {
		this.criteriaType = criteriaType;
	}
	
	public int getCriteriaOrder() {
		return criteriaOrder;
	}

	public void setCriteriaOrder(int criteriaOrder) {
		this.criteriaOrder = criteriaOrder;
	}
	
	public ReportCriteriaDTO() {}

	public ReportCriteriaDTO(ReportColumnDTO reportColumn, CriteriaType criteriaType) {
		this.reportColumn = reportColumn;
		this.criteriaType = criteriaType;
	}

	public ReportCriteriaDTO(Integer id, ReportColumnDTO reportColumn, String value1, String value2,
			CriteriaType criteriaType, int criteriaOrder) {
		this.id = id;
		this.reportColumn = reportColumn;
		this.value1 = value1;
		this.value2 = value2;
		this.criteriaType = criteriaType;
		this.criteriaOrder = criteriaOrder;
	}
	
	@Override
	public int getOrderValue() {
		return getCriteriaOrder();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((criteriaType == null) ? 0 : criteriaType.hashCode());
		result = prime * result + ((reportColumn == null) ? 0 : reportColumn.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ReportCriteriaDTO))
			return false;
		ReportCriteriaDTO other = (ReportCriteriaDTO) obj;
		if (criteriaType != other.criteriaType)
			return false;
		if (reportColumn == null) {
			if (other.reportColumn != null)
				return false;
		} else if (!reportColumn.equals(other.reportColumn))
			return false;
		return true;
	}

	@Override
	public String toString() {		
		return reportColumn + ":" + criteriaType.getSymbol();
	}
			
}
