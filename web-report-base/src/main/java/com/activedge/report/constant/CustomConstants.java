package com.activedge.report.constant;

import org.springframework.orm.jpa.vendor.Database;

public class CustomConstants {
	public static final String PASSWORD_KEY_ENCODER = "password_key_encoder";
	
	public static final String REPORT_DB_DRIVER 
		= "com.mysql.cj.jdbc.Driver";
	public static final String REPORT_DB_URL
		= "jdbc:mysql://localhost:3306/webreport?useSSL=false";
	public static final String REPORT_DB_USER = "root";
	public static final String REPORT_DB_PASSWORD = "pass";
	public static final String REPORT_DB_SCHEMA = "world";
	
	public static final String APP_DB_DRIVER 
		= "com.mysql.cj.jdbc.Driver";
	public static final String APP_DB_URL = "jdbc:mysql://localhost:3306/webreport?useSSL=false";
	public static final String APP_DB_USER = "root";
	public static final String APP_DB_PASSWORD = "pass";
	public static final String APP_DB_HIBERNATE_PLATFORM = 
			"org.hibernate.dialect.MySQL5Dialect";
	public static final String APP_DB_HIBERNATE_DDL_AUTO = 
			"create-drop";
	public static final Database APP_DATABASE = Database.MYSQL;
}
