package com.activedge.report.bean;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.activedge.report.util.SessionUtils;


@SuppressWarnings("serial")
@ManagedBean(name = "paramBean")
@ViewScoped
public class ParamBean implements Serializable {
    public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	private String view;
    private String templateId;
    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }   
    
    public void setViewParam() {
        String fromParam = SessionUtils.getRequestParam("view");
        if (fromParam != null) {
            view = fromParam;
        }
    }
}
