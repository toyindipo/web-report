package com.activedge.report.util;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;

public class JasperReportGenerator {
	@SuppressWarnings("deprecation")
	public static ByteArrayOutputStream getOutputStream(Connection connection, String sourceFile) 
				throws JRException {
		Map<String, Object> parameters = new HashMap<String, Object>();
		//compile jrxml file.
		JasperReport jasperReport = JasperCompileManager.compileReport(sourceFile);	  
		JasperPrint print = JasperFillManager.fillReport(jasperReport,
		            parameters, connection);
		JRPdfExporter exporter = new JRPdfExporter();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, stream);
        exporter.exportReport();
        return stream;
	}
}
