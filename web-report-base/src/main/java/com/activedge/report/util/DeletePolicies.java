package com.activedge.report.util;

import com.activedge.report.model.ReportColumn;

public class DeletePolicies {
	public static boolean canDeleteColumn(ReportColumn column) {
		return column.getSelectField().equals(Boolean.FALSE) &&
				column.getCriteriaCount() == 0 &&
				column.getCriteriaCount() == 0;
	}
}
