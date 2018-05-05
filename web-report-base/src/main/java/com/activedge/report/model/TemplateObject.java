package com.activedge.report.model;

import java.sql.Timestamp;

public interface TemplateObject {
	Integer getId();
	String getTemplateName();
	String getTemplateDescription();
	Timestamp getCreatedAt();
}
