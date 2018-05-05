package com.activedge.report.dto;

import java.util.List;

public class ReportObject {
	private String reportHeader;
	private List<ColumnData> columnData;
	public String getReportHeader() {
		return reportHeader;
	}
	public void setReportHeader(String reportHeader) {
		this.reportHeader = reportHeader;
	}
	public List<ColumnData> getColumnData() {
		return columnData;
	}
	public void setColumnData(List<ColumnData> columnData) {
		this.columnData = columnData;
	}
	public ReportObject(String reportHeader, List<ColumnData> columnData) {
		this.reportHeader = reportHeader;
		this.columnData = columnData;
	}
	
	public ReportObject(String reportHeader) {
		this(reportHeader, null);
	}
	
	public ReportObject() {
		this(null);
	}
	
}
