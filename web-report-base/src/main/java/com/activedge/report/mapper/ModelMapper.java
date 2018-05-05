package com.activedge.report.mapper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.activedge.report.dto.JoinObjectDTO;
import com.activedge.report.dto.ReportColumnDTO;
import com.activedge.report.dto.ReportCriteriaDTO;
import com.activedge.report.dto.ReportTableDTO;
import com.activedge.report.model.JoinObject;
import com.activedge.report.model.ReportColumn;
import com.activedge.report.model.ReportCriteria;
import com.activedge.report.model.ReportTable;

public class ModelMapper {
	public static <T> Set<T> fromListToSet(List<T> list) {
		Set<T> set = new HashSet<>();
		for (T e: list) {
			set.add(e);
		}
		return set;
	}
	
	public static Map<Integer, ReportTableDTO> tableListToMap(List<ReportTable> tables) {
		Map<Integer, ReportTableDTO> tableMap = new HashMap<>();
		for (ReportTable table: tables) {
			tableMap.put(table.getId(), tableToDTO(table));
		}
		return tableMap;
	}
	
	public static Map<Integer, Integer> storeColumnsOrders(Map<Integer, Integer> map, 
				Set<ReportColumn> columns) {
		for (ReportColumn column: columns) {
			map.put(column.getId(), column.getColumnOrder());
		}
		return map;
	}
	
	public static ReportColumn dtoToColumn(ReportColumnDTO columnDTO) {
		ReportColumn column = 
			new ReportColumn(columnDTO.getColumnName(), columnDTO.getColumnType(),
					columnDTO.getColumnTypeLabel(), columnDTO.getColumnOrder(), 
					columnDTO.getColumnHeader(), columnDTO.getWidthRatio(), columnDTO.getSelectField());
		return column;
	}
	
	public static ReportTable dtoToTable(ReportTableDTO tableDTO) {
		ReportTable table = new ReportTable(tableDTO.getTableName(), tableDTO.getTableSchema());
		for (ReportColumnDTO columnDTO: tableDTO.getReportColumns()) {
			table.getReportColumns().add(dtoToColumn(columnDTO));
		}
		return table;
	}
	
	public static ReportColumnDTO columnToDTO(ReportColumn column, ReportTableDTO table) {
		ReportColumnDTO columnDTO = new ReportColumnDTO(column.getId(), column.getColumnName(), 
				column.getColumnType(), column.getSelectField(), column.getColumnTypeLabel(), table, 
				column.getWidthRatio(), column.getColumnOrder(), 
				column.getJoinCount(), column.getCriteriaCount());
		return columnDTO;
	}
	
	public static ReportTableDTO tableToDTO(ReportTable table) {
		Set<ReportColumnDTO> reportColumns = new HashSet<>();
		
		ReportTableDTO tableDTO = new ReportTableDTO(table.getId(), table.getTableName(), 
				table.getTableSchema(), reportColumns);
		for (ReportColumn column: table.getReportColumns()) {
			reportColumns.add(columnToDTO(column, tableDTO));
		}
		return tableDTO;
	}
	
	public static JoinObjectDTO joinObjectToDTO(JoinObject joinObject) {
		return new JoinObjectDTO(joinObject.getId(), columnToDTO(joinObject.getColumn1(), null),
				columnToDTO(joinObject.getColumn2(), null), joinObject.getJoinDescription(), 
				joinObject.getJoinOrder());
	}
	
	public static ReportCriteriaDTO criteriaToDTO(ReportCriteria criteria) {
		return new ReportCriteriaDTO(criteria.getId(), 
				columnToDTO(criteria.getReportColumn(), null),criteria.getValue1(), criteria.getValue2(),
				criteria.getCriteriaType(), criteria.getCriteriaOrder());
	}
}
