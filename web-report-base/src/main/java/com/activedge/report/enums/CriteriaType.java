package com.activedge.report.enums;

import java.util.HashMap;
import java.util.Map;

public enum CriteriaType {
	GREATER_OR_EQUAL(">="), LESS_OR_EQUAL("<="), EQUAL("="), GREATER(">"), LESS("<"), 
		NOT_EQAUL("!="), BETWEEN("BETWEEN");
	private String symbol;
	private static final Map<String, CriteriaType> stringToEnum = 
			new HashMap<>();
	
	static {
		for (CriteriaType type: values()) {
			stringToEnum.put(type.symbol, type);
		}
	}
	
	private CriteriaType(String symbol) {
		this.symbol = symbol;
	}
	
	public String getSymbol() {
		return symbol;
	}
	
	public CriteriaType fromString(String symbol) {
		return stringToEnum.get(symbol);
	}
}
