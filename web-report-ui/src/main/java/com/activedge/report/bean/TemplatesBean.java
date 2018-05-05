package com.activedge.report.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.activedge.report.datatable.TemplateDataModel;
import com.activedge.report.model.JasperTemplate;
import com.activedge.report.model.ReportTemplate;
import com.activedge.report.model.TemplateObject;
import com.activedge.report.service.JasperTemplateService;
import com.activedge.report.service.ReportTemplateService;
import com.activedge.report.util.TemplateUtil;

@SuppressWarnings("serial")
@ViewScoped
@ManagedBean(name = "templatesBean")
public class TemplatesBean extends SpringBeanAutowiringSupport implements Serializable {
	@ManagedProperty(value = "#{paramBean}")
    private ParamBean paramBean;
	@Autowired
	private ReportTemplateService templateService;
	
	@Autowired
	private JasperTemplateService jasperTemplateService;	
	private TemplateObject selectedTemplate;	
	private TemplateDataModel<TemplateObject> templateModel;
	
	public void onLoad(ComponentSystemEvent event) {        
        if (!FacesContext.getCurrentInstance().isPostback()) {
        	paramBean.setViewParam();        	
        	List<ReportTemplate> reportTemplates = new ArrayList<>();
        	List<JasperTemplate> jasperTemplates = new ArrayList<>();
        	if (!"jasperTemplates".equals(paramBean.getView())) {        		
        		reportTemplates = templateService.findAll();
        	} else if (!"customTemplates".equals(paramBean.getView())) {
        		jasperTemplates = jasperTemplateService.findAll();
        	}
        	
    		List<TemplateObject> templates = new ArrayList<>();
    		templates.addAll(reportTemplates);
    		templates.addAll(jasperTemplates);
    		TemplateUtil.sortTemplatesByCreatedAt(templates, false);
    		templateModel = new TemplateDataModel<>(templates);        	
        }        
	}
	
	public String viewTemplate() {
		paramBean.setTemplateId(String.valueOf(selectedTemplate.getId()));
		if (selectedTemplate instanceof ReportTemplate) {			
			paramBean.setView("viewCustomTemplate");
			return "viewCustomTemplate";			
		} else {
			paramBean.setView("viewJasperTemplate");
			return "viewJasperTemplate";
		}
	}

	public TemplateDataModel<TemplateObject> getTemplateModel() {
		return templateModel;
	}

	public void setTemplateModel(TemplateDataModel<TemplateObject> templateModel) {
		this.templateModel = templateModel;
	}

	public TemplateObject getSelectedTemplate() {
		return selectedTemplate;
	}

	public void setSelectedTemplate(TemplateObject selectedTemplate) {
		this.selectedTemplate = selectedTemplate;
	}	

	public ParamBean getParamBean() {
		return paramBean;
	}

	public void setParamBean(ParamBean paramBean) {
		this.paramBean = paramBean;
	}
}
