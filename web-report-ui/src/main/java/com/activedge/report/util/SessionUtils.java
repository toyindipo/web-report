package com.activedge.report.util;

import java.util.Map;

import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionUtils {

    public static HttpSession getSession() {
        return (HttpSession) FacesContext.getCurrentInstance()
            .getExternalContext().getSession(false);
    }
    
    public static Map<String, Object> getSessionMap() {
        return FacesContext.getCurrentInstance()
            .getExternalContext().getSessionMap();
    }

    public static HttpServletRequest getRequest() {
        return (HttpServletRequest) FacesContext.getCurrentInstance()
            .getExternalContext().getRequest();
    }
    
    public static FacesContext getCurrentInstance() {
    	return FacesContext.getCurrentInstance();
    }
    
    public static ConfigurableNavigationHandler getNavigationHandler() {
    	ConfigurableNavigationHandler nav = (ConfigurableNavigationHandler)
    			getCurrentInstance().getApplication().getNavigationHandler();
    	return nav;
    }

    public static String getUserName() {
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(false);
            return session.getAttribute("username").toString();
    }

    public static String getSessionAttribute(String attribute) {
        HttpSession session = getSession();
        if (session != null)
            return (String) session.getAttribute(attribute);
        else
            return null;
    }
    
    public static String getRequestParam(String param) {
        Map<String, String> paramMap = FacesContext.getCurrentInstance().
                getExternalContext().getRequestParameterMap();
        return paramMap.get(param);
    }

    public static void addMessage(String message, FacesMessage.Severity severity) {
        FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(severity, message, message));
    }

    public static String getUserIPAddress() {
        HttpServletRequest request = 
                (HttpServletRequest) FacesContext.
                    getCurrentInstance().getExternalContext().getRequest();
        String remoteAddr = "";

        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
        }
        return remoteAddr;
    }
}
