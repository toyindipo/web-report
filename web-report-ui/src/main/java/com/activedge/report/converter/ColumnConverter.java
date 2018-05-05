package com.activedge.report.converter;

import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.activedge.report.dto.ReportColumnDTO;
import com.activedge.report.dto.ReportTableDTO;

@ViewScoped
@ManagedBean
public class ColumnConverter implements Converter {
    private Map<String, ReportColumnDTO> columnConverterMap;

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		ReportColumnDTO column = columnConverterMap.get(arg2);
		if (column == null) {
			return new ReportColumnDTO("Not found", 0, "Not found", 
					new ReportTableDTO("Not found", "Not found"));
		} else {
			return column;
		}
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		ReportColumnDTO column = (ReportColumnDTO) arg2;
		String tableName = column.getTable() == null ? "No table" : 
			column.getTable().getTableName();
		return tableName + ":" + column.getColumnName();
	}

	public Map<String, ReportColumnDTO> getColumnConverterMap() {
		return columnConverterMap;
	}

	public void setColumnConverterMap(Map<String, ReportColumnDTO> columnConverterMap) {
		this.columnConverterMap = columnConverterMap;
	}

}
