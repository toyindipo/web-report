package com.activedge.report.bean;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.bean.ViewScoped;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.activedge.report.connection.ConnectionMgr;
import com.activedge.report.model.JasperTemplate;
import com.activedge.report.service.JasperTemplateService;
import com.activedge.report.util.FileDownloader;
import com.activedge.report.util.JasperReportGenerator;
import com.activedge.report.util.SessionUtils;

import net.sf.jasperreports.engine.JRException;

@SuppressWarnings("serial")
@ViewScoped
@ManagedBean(name = "jasperBean")
public class JasperBean extends SpringBeanAutowiringSupport implements Serializable {
	@ManagedProperty(value = "#{paramBean}")
    private ParamBean paramBean;
	
	@Autowired
	private JasperTemplateService templateService;	
	private JasperTemplate template;
	
	public void onLoad(ComponentSystemEvent event) {        
        if (!FacesContext.getCurrentInstance().isPostback()) {
        	paramBean.setViewParam();
			String page = paramBean.getView();
			if ("viewJasperTemplate".equals(page)) {
				String templateId = paramBean.getTemplateId();
            	if (templateId == null || templateId.isEmpty()) {
                	SessionUtils.addMessage("Unable to retrieve template data", 
                			FacesMessage.SEVERITY_FATAL);
                } else {
                	template = templateService.findByid(Integer.valueOf(templateId));
                }
             } else {
            	 template = new JasperTemplate();
             }
		}
	}
	
	public void generatePDF() {
		ConnectionMgr connMgr = new ConnectionMgr();
		Connection connection;
		try {
			connection = connMgr.getConnection();
			ByteArrayOutputStream stream = JasperReportGenerator.
					getOutputStream(connection, template.getFilePath());
			new FileDownloader().downloadPDF(stream);
		} catch (ClassNotFoundException | SQLException e) {
			SessionUtils.addMessage("Error creating db connection", 
        			FacesMessage.SEVERITY_FATAL);
			e.printStackTrace();
		} catch (JRException | IOException e) {
			SessionUtils.addMessage("Error generating report data", 
        			FacesMessage.SEVERITY_FATAL);
			e.printStackTrace();
		} finally {
			connMgr.closeConnection();
		}
		
	}
	
	public String saveTemplate() {
		template = templateService.saveOrUpdate(template);
		return "viewJasperTemplate?faces-redirect=true&view=all&templateId=" + 
				template.getId();
	}

	public ParamBean getParamBean() {
		return paramBean;
	}

	public void setParamBean(ParamBean paramBean) {
		this.paramBean = paramBean;
	}

	public JasperTemplate getTemplate() {
		return template;
	}

	public void setTemplate(JasperTemplate template) {
		this.template = template;
	}

}
