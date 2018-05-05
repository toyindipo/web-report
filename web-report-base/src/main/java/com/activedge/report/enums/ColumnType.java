package com.activedge.report.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Oladele Toyin
 *
 */
public enum ColumnType {
	BIGINT(-5), BOOLEAN(16), CHAR(6), DATE(91), DECIMAL(3), DOUBLE(8), FLOAT(6), INTEGER(4), 
	LONGNVARCHAR(-16), LONGVARCHAR(-1), NCHAR(-15), NUMERIC(2), NVARCHAR(-9), REAL(7), SMALLINT(5),
	TIME(92), TIME_WITH_TIMEZONE(2013), TIMESTAMP(93), TIMESTAMP_WITH_TIMEZONE(2014), TINIYINT(-6),
	VARCHAR(12);
	
	private int typeValue;
	private static final Map<Integer, ColumnType> integerToEnum = 
			new HashMap<>();
	
	static {
		for (ColumnType type: values()) {
			integerToEnum.put(type.typeValue, type);
		}
	}
	
	private ColumnType(int typeValue) {
		this.typeValue = typeValue;		
	}
	
	public int getIntValue() {
		return typeValue;
	}
	
	public ColumnType fromInteger(Integer intValue) {
		return integerToEnum.get(intValue);
	}
}
