package com.activedge.report.util;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.activedge.report.connection.ConnectionMgr;
import com.activedge.report.constant.CustomConstants;
import com.activedge.report.dto.ReportColumnDTO;
import com.activedge.report.dto.ReportTableDTO;

public class ReportTableLoader {
	private ConnectionMgr connectionMgr;
	private List<ReportTableDTO> reportTables;
	public ReportTableLoader(ConnectionMgr connectionMgr) {
		this.connectionMgr = connectionMgr;
	}
	
	public List<ReportTableDTO> loadTables() throws ClassNotFoundException, SQLException {
		if (reportTables == null) {
			DatabaseMetaData databaseMetaData =  connectionMgr.getConnection().getMetaData();
			ResultSet tablesRS = databaseMetaData.
					getTables(null, CustomConstants.REPORT_DB_SCHEMA, "%", 
							new String[] {"TABLE"});
			reportTables = new ArrayList<>();
			while(tablesRS.next()) {
			    ReportTableDTO table = new ReportTableDTO(tablesRS.getString(3), tablesRS.getString(2));
			    ResultSet columnsRS = databaseMetaData.getColumns
			    		(null, CustomConstants.REPORT_DB_SCHEMA, table.getTableName(), "%");
			    while (columnsRS.next()) {
			    	ReportColumnDTO column = new ReportColumnDTO(columnsRS.getString(4), 
			    			columnsRS.getInt(5), columnsRS.getString(6), table);
			    	table.getReportColumns().add(column);
			    }
			    reportTables.add(table);
			}
			connectionMgr.closeConnection();
		}
		return reportTables;		
	}
}
