package com.activedge.report.converter;

import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.activedge.report.dto.ReportColumnDTO;
import com.activedge.report.dto.ReportCriteriaDTO;
import com.activedge.report.dto.ReportTableDTO;
import com.activedge.report.enums.CriteriaType;

@ViewScoped
@ManagedBean
public class CriteriaConverter implements Converter {
    private Map<Integer, ReportCriteriaDTO> criteriaConverterMap;
	
	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		ReportCriteriaDTO criteria = criteriaConverterMap.get(Integer.parseInt(arg2));
		if (criteria == null) {
			ReportColumnDTO column = new ReportColumnDTO("Not found", 0, "Not found", 
					new ReportTableDTO("Not found", "Not found"));
			return new ReportCriteriaDTO(column, CriteriaType.BETWEEN);
		} else {
			return criteria;
		}
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		ReportCriteriaDTO criteria = (ReportCriteriaDTO) arg2;
		return criteria.getId().toString();
	}

	public Map<Integer, ReportCriteriaDTO> getCriteriaConverterMap() {
		return criteriaConverterMap;
	}

	public void setCriteriaConverterMap(Map<Integer, ReportCriteriaDTO> criteriaConverterMap) {
		this.criteriaConverterMap = criteriaConverterMap;
	}
}
