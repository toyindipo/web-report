package com.activedge.report.util;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.activedge.report.dto.ColumnData;
import com.activedge.report.dto.ReportObject;
import com.activedge.report.enums.CriteriaType;
import com.activedge.report.model.JoinObject;
import com.activedge.report.model.ReportColumn;
import com.activedge.report.model.ReportCriteria;
import com.activedge.report.model.ReportTable;

public class ReportGenerator {
	public static ReportObject generateReportData(List<ReportColumn> columns, 
			ResultSet resultSet, String reportHeader) {
		ReportObject reportObject = new ReportObject(reportHeader);
		for (ReportColumn column: columns) {
			reportObject.getColumnData().add(
					new ColumnData(column.getColumnHeader(), column.getColumnName(), 
							column.getColumnType(), column.getColumnOrder()
							, column.getWidthRatio()));
		}
		try {
			while (resultSet.next()) {
				for (ColumnData columnData: reportObject.getColumnData()) {
					String stringVal = "";
					switch(columnData.getColumnType()) {
						case 9:	Date dateValue = resultSet.getDate(columnData.getColumnName());
								stringVal = DateLib.getStringDate(dateValue);
								break;
						case 92: Time timeValue = resultSet.getTime(columnData.getColumnName());
									stringVal = DateLib.getStringDate(timeValue);
								break;
						case 93:
						case 2013:
						case 2014: Timestamp timestampValue = 
									resultSet.getTimestamp(columnData.getColumnName());
									stringVal = DateLib.getStringDate(timestampValue);
						default:
							stringVal = resultSet.getString(columnData.getColumnName());						
					}
					columnData.getValues().add(stringVal);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return reportObject;
	}
	
	public static ResultSet getResultSet(Connection connection, String sqlString) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(sqlString);
		return statement.executeQuery();
	}
	
	public static ResultSet getResultSet(Connection connection, String sqlString, 
			List<ReportCriteria> criterias) throws SQLException, ParseException {
		PreparedStatement statement = connection.prepareStatement(sqlString);
		int count = 0;
		for (ReportCriteria criteria: criterias) {
			switch(criteria.getReportColumn().getColumnType()) {
				case 9:	Date dateValue = 
							new Date(DateLib.getDateFromString(criteria.getValue1()).getTime());
						statement.setDate(++count, dateValue);
						if (criteria.getCriteriaType().equals(CriteriaType.BETWEEN)) {
							dateValue = 
								new Date(DateLib.getDateFromString(criteria.getValue2()).getTime());
								statement.setDate(++count, dateValue);
						}
						break;
				case 92: Time timeValue = DateLib.getTimeFromString(criteria.getValue1());
							statement.setTime(++count, timeValue);
							if (criteria.getCriteriaType().equals(CriteriaType.BETWEEN)) {
								timeValue = DateLib.getTimeFromString(criteria.getValue2());
									statement.setTime(++count, timeValue);
							}
						break;
				case 93:
				case 2013:
				case 2014: Timestamp timestampValue = 
						DateLib.getTimestampFromString(criteria.getValue1());
						statement.setTimestamp(++count, timestampValue);
						if (criteria.getCriteriaType().equals(CriteriaType.BETWEEN)) {
							timestampValue = 
								DateLib.getTimestampFromString(criteria.getValue2());
								statement.setTimestamp(++count, timestampValue);
						}
				default:
					statement.setObject(++count, criteria.getValue1(), 
							criteria.getReportColumn().getColumnType());
					if (criteria.getCriteriaType().equals(CriteriaType.BETWEEN)) {
						statement.setObject(++count, criteria.getValue2(), 
								criteria.getReportColumn().getColumnType());
					}
			}
		}
		return statement.executeQuery();
	}
	
	public static String getSqlString(List<ReportTable> tables, 
			List<ReportColumn> columns, List<JoinObject> joins, 
			List<ReportCriteria> criterias) {
		if (columns == null || columns.isEmpty())
			throw new IllegalArgumentException("No column specified for selection");
		StringBuilder builder = new StringBuilder();
		builder.append(getSelectFields(columns));
		builder.append(" FROM ");
		if (joins == null || joins.isEmpty()) {
			builder.append(getSelectTables(tables));
		} else {
			builder.append(getJoinString(joins));
		}
		builder.append(" ");
		if (criterias != null && !criterias.isEmpty()) {
			builder.append(getCriteriaString(criterias));
		}
		builder.append(";");
		return builder.toString();
	}
	
	private static String getSelectFields(List<ReportColumn> columns) {
		StringBuilder selectBuilder = new StringBuilder("SELECT ");
		for (ReportColumn column: columns) {
			selectBuilder.append(column);
			selectBuilder.append(", ");
		}
		String selectString = selectBuilder.toString();
		return selectString.substring(0, selectString.length() - 2);
	}
	
	private static String getSelectTables(List<ReportTable> tables) {
		StringBuilder builder = new StringBuilder();
		List<ReportTable> selectTables = tables.stream().filter(table -> {
			return table.getReportColumns().stream().
					anyMatch(column -> column.getSelectField().equals(Boolean.TRUE));
		}).collect(Collectors.toList());
		
		for (ReportTable table: selectTables) {
			builder.append(table.toString());
			builder.append(", ");
		}
		String selectString = builder.toString();
		return selectString.substring(0, selectString.length() - 2);
	}
	
	private static String getJoinString(List<JoinObject> joins) {
		return generateJoinStmt(joins, new ArrayList<String>(), 0, joins.size()).toString();
	}
	
	private static String getCriteriaString(List<ReportCriteria> criterias) {
		StringBuilder criteriaBuilder = new StringBuilder("WHERE ");
		for (ReportCriteria criteria: criterias) {
			criteriaBuilder.append(criteria);
			criteriaBuilder.append(" AND ");
		}
		String criteriaString = criteriaBuilder.toString();
		return criteriaString.substring(0, criteriaString.length() - 5);
	}
	
	private static StringBuilder generateJoinStmt(List<JoinObject> joins, 
			List<String> addedTables, int count, int maxIteration) {
		List<JoinObject> notAdded = new ArrayList<>();
		boolean first = true;
		StringBuilder builder = new StringBuilder();
		for (JoinObject join: joins) {
			if (first) {
				builder.append("JOIN ");
				builder.append(join);
				builder.append(" ");
				addedTables.add(join.getColumn1().getReportTable().toString());
				addedTables.add(join.getColumn2().getReportTable().toString());
				first = false;
			} else {
				if (addedTables.contains(join.getColumn1().getReportTable().toString()) &&
						!addedTables.contains(join.getColumn2().getReportTable().toString())) {
						builder.append(join.
							getJoinStringWithTable(join.getColumn2().getReportTable().toString()));
				} else if (addedTables.contains(join.getColumn2().getReportTable().toString()) &&
						!addedTables.contains(join.getColumn1().getReportTable().toString())) {
						builder.append(join.
							getJoinStringWithTable(join.getColumn1().getReportTable().toString()));
				} else if (!addedTables.contains(join.getColumn1().getReportTable().toString()) &&
						!addedTables.contains(join.getColumn2().getReportTable().toString())) {
					notAdded.add(join);
				}
			}	
			
		}
		++ count;
		if (addedTables.isEmpty() || count >= maxIteration) {
			return builder;
		} else {
			return builder.append(generateJoinStmt(notAdded, addedTables, count, maxIteration));
		}
	}
}
