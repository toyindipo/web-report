/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.activedge.report.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import com.activedge.report.dto.ColumnData;
import com.activedge.report.dto.ReportObject;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
/**
 *
 * @author oladeletoyin
 */


public class PDFGenerator {
    private PdfFont font;
    private PdfFont bold;
      
       
    public ByteArrayOutputStream generatePDF(ReportObject reportObject) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(stream);
        PdfDocument pdf = new PdfDocument(writer);
        try(Document document = new Document(pdf,PageSize.A4.rotate())) {
            document.setMargins(20, 20, 20, 20);
            generateHeader(document, reportObject);            
            Table table = generateTable(reportObject);
            document.add(table);            
        }
        return stream;
    }
    
   
    private Table generateTable(ReportObject reportObject) {
        Table table;
        float[] widths = new float[reportObject.getColumnData().size()];
        for (int i = 0; i < reportObject.getColumnData().size(); i++) {
        	widths[i] = reportObject.getColumnData().get(i).getWidthRatio();
        }
        table = new Table(widths);

        table.setWidthPercent(100);
        writeTableHeader(table, reportObject.getColumnData());
        writeToTable(table, reportObject.getColumnData());

        return table;
    }
    
    private void generateHeader(Document doc, ReportObject reportObject) throws IOException {
        font = PdfFontFactory.createFont(FontConstants.HELVETICA);
        bold = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
        Paragraph title = new Paragraph(reportObject.getReportHeader()).setFont(bold);
        title.setTextAlignment(TextAlignment.CENTER);
        doc.add(title);
        writeBlankParagraph(doc, 1);          
    }
    
    private void writeTableHeader(Table table, List<ColumnData> columnData) {
        for (ColumnData data: columnData) {
        	writeCell(table, data.getColumnHeader(), true);
        }
    }
    
        
    //This method also computes number of failed and posted transactions
    //for Report Object
    private void writeToTable(Table table, List<ColumnData> columnData) {
    	for (int i = 0; i < columnData.get(0).getValues().size(); i++) {
    		for (ColumnData data: columnData) {
    			writeCell(table, data.getValues().get(i), false);
    		}
    	}
    }
    
    private void writeCell(Table table, String text, boolean isHeader) {
        if (text == null || text.isEmpty()) {
            text = " ";
        }
        if (isHeader) {
            table.addHeaderCell(new Cell().add(new Paragraph(text).setFont(bold)));
        } else {
            table.addCell(new Cell().add(new Paragraph(text).setFont(font)));
        }
    }
    
    private void writeBlankParagraph(Document doc, int times) {
        for (int i = 0; i < times; i++) {
            doc.add(new Paragraph(" "));
        }       
    }
}
