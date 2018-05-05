/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.activedge.report.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author oladeletoyin
 */
public class FileDownloader {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");  
    
    public void downloadPDF(ByteArrayOutputStream outputStream) throws IOException {
        
        // Prepare.
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
       
        String dt = sdf.format(new Date());

        // Init servlet response.
        response.reset();
        // setting some response headers
        response.setHeader("Expires", "0");
        response.setHeader("Cache-Control",
            "must-revalidate, post-check=0, pre-check=0");
        response.setHeader("Pragma", "public");
        // setting the content type
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=\"report" + dt + ".pdf\"");
        // the contentlength
        response.setContentLength(outputStream.size());
        // write ByteArrayOutputStream to the ServletOutputStream
        try (OutputStream os = response.getOutputStream()) {
            outputStream.writeTo(os);
            os.flush();            
        }       

        // Inform JSF that it doesn't need to handle response.
        // This is very important, otherwise you will get the following exception in the logs:
        // java.lang.IllegalStateException: Cannot forward after response has been committed.
        facesContext.responseComplete();
    }   
    
}
