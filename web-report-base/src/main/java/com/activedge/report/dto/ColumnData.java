package com.activedge.report.dto;

import java.util.ArrayList;
import java.util.List;

import com.activedge.report.model.Orderable;

public class ColumnData implements Orderable {
	private String columnHeader;
	private String columnName;
	private int columnType;
	private int order;
	private float widthRatio;
	private List<String> values;
	
	public String getColumnHeader() {
		return columnHeader;
	}
	public void setColumnHeader(String columnHeader) {
		this.columnHeader = columnHeader;
	}
	public int getColumnType() {
		return columnType;
	}
	public void setColumnType(int columnType) {
		this.columnType = columnType;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public float getWidthRatio() {
		return widthRatio;
	}
	public void setWidthRatio(float widthRatio) {
		this.widthRatio = widthRatio;
	}
	public List<String> getValues() {
		return values;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public void setValues(List<String> values) {
		this.values = values;
	}
	public ColumnData(String columnHeader, String columnName, int columnType, int order, float widthRatio, List<String> values) {
		this.columnHeader = columnHeader;
		this.columnName = columnName;
		this.columnType = columnType;
		this.order = order;
		this.widthRatio = widthRatio;
		this.values = values;
	}
	public ColumnData(String columnHeader, String columnName, int columnType, int order, float widthRatio) {
		this(columnHeader, columnName, columnType, order, widthRatio, new ArrayList<>());
	}
	
	public ColumnData() {
		values = new ArrayList<>();
	}
	@Override
	public int getOrderValue() {
		return getOrder();
	}
}
