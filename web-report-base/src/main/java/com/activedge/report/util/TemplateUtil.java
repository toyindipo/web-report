package com.activedge.report.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.activedge.report.model.Orderable;
import com.activedge.report.model.TemplateObject;

public class TemplateUtil {
	public static <T extends TemplateObject> void 
		sortTemplatesByCreatedAt(List<T> templates) {
		sortTemplatesByCreatedAt(templates, true);
	}
	
	
	public static <T extends TemplateObject> void sortTemplatesByCreatedAt
			(List<T> templates, boolean acs) {
		int ascFlag = acs ? 1 : -1;
		Collections.sort(templates, new Comparator<TemplateObject>() {
			@Override
			public int compare(TemplateObject o1, TemplateObject o2) {
				return ascFlag * o1.getCreatedAt().compareTo(o2.getCreatedAt());
			}			
		});
	}
	
	public static <T extends Orderable> void sortOrderable(List<T> orderables) {
		Collections.sort(orderables, new Comparator<T>() {
			@Override
			public int compare(Orderable o1, Orderable o2) {
				return o1.getOrderValue() - o2.getOrderValue();
			}			
		});
	}
} 
