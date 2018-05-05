/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.activedge.report.util;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author toyinoladele
 */
public class DateLib {
    private static final DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    private static final DateFormat sdfShort = new SimpleDateFormat("yyyy-MM-dd");
    private static final Calendar cal = Calendar.getInstance(); ;
    
    public static Date getDateFromString(String dateString) throws ParseException {
        return sdf.parse(dateString);
    }
    
    public static String getStringDate(Date date) {
        return sdf.format(date);
    }
    
    public static Time getTimeFromString(String dateString) throws ParseException {
    	return new Time(getDateFromString(dateString).getTime());
    }
    
    public static Timestamp getTimestampFromString(String dateString) throws ParseException {
    	return new Timestamp(getDateFromString(dateString).getTime());
    }
    
    public static String getStringShortDate(Date date) {
        return sdfShort.format(date);
    }
    
    public static Date getPaddedDate(Timestamp time) {
        cal.setTime(time);
        cal.add(Calendar.DATE, 1);
        cal.add(Calendar.SECOND, -1);
        return cal.getTime();
    }
}
