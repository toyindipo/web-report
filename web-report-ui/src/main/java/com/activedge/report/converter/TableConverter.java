package com.activedge.report.converter;

import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.activedge.report.dto.ReportTableDTO;

@ViewScoped
@ManagedBean
public class TableConverter implements Converter {
    private Map<String, ReportTableDTO> tableConverterMap;

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		ReportTableDTO table = tableConverterMap.get(arg2);
		if (table == null) {
			return new ReportTableDTO("Not found", "Not found");
		} else {
			return table;
		}
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		ReportTableDTO table = (ReportTableDTO) arg2;
		String tableKey = table.getTableSchema() == null ? table.getTableName() :
			table.getTableSchema() + ":" + table.getTableName();
		return tableKey;
	}

	public Map<String, ReportTableDTO> getTableConverterMap() {
		return tableConverterMap;
	}

	public void setTableConverterMap(Map<String, ReportTableDTO> tableConverterMap) {
		this.tableConverterMap = tableConverterMap;
	}		
}
