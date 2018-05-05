package com.activedge.report.datatable;

import java.util.Comparator;

import org.primefaces.model.SortOrder;

import com.activedge.report.model.TemplateObject;

public class TemplateSorter implements Comparator<TemplateObject> {
 
    private String sortField;
     
    private SortOrder sortOrder;
     
    public TemplateSorter(String sortField, SortOrder sortOrder) {
        this.sortField = sortField;
        this.sortOrder = sortOrder;
    }
 
    @Override
    public int compare(TemplateObject template1, TemplateObject template2) {
        try {
            Object value1 = TemplateObject.class.getField(this.sortField).get(template1);
            Object value2 = TemplateObject.class.getField(this.sortField).get(template2);
 
            int value = ((Comparable)value1).compareTo(value2);
             
            return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
        }
        catch(Exception e) {
            throw new RuntimeException();
        }
    }
}
