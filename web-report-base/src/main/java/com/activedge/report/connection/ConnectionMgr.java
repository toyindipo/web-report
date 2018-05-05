package com.activedge.report.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.activedge.report.constant.CustomConstants;

public class ConnectionMgr {
	private Connection connection = null;
	
	public  Connection getConnection() throws ClassNotFoundException, SQLException {
		if (connection == null) {
			Class.forName(CustomConstants.REPORT_DB_DRIVER);
			connection = DriverManager.getConnection(CustomConstants.REPORT_DB_URL, 
					CustomConstants.REPORT_DB_USER, CustomConstants.REPORT_DB_PASSWORD);
		}
		return connection;
	}
	
	public void closeConnection() {
		if (connection != null) {
			try {
				connection.close();
			} catch(SQLException ex) {
				ex.printStackTrace();
			}
		}
	}
}
