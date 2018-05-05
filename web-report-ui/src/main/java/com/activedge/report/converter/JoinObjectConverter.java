package com.activedge.report.converter;

import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.activedge.report.dto.JoinObjectDTO;
import com.activedge.report.dto.ReportColumnDTO;
import com.activedge.report.dto.ReportTableDTO;

@ViewScoped
@ManagedBean
public class JoinObjectConverter implements Converter {
    private Map<Integer, JoinObjectDTO> joinObjectConverterMap;
	
	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		JoinObjectDTO joinObject = joinObjectConverterMap.get(Integer.parseInt(arg2));
		if (joinObject == null) {
			ReportColumnDTO column = new ReportColumnDTO("Not found", 0, "Not found", 
					new ReportTableDTO("Not found", "Not found"));
			return new JoinObjectDTO(column, column, null);
		} else {
			return joinObject;
		}
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		JoinObjectDTO joinObject = (JoinObjectDTO) arg2;
		return joinObject.getId().toString();
	}

	public Map<Integer, JoinObjectDTO> getJoinObjectConverterMap() {
		return joinObjectConverterMap;
	}

	public void setJoinObjectConverterMap(Map<Integer, JoinObjectDTO> joinObjectConverterMap) {
		this.joinObjectConverterMap = joinObjectConverterMap;
	}
}
