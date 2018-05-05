package com.activedge.report.datatable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.activedge.report.model.TemplateObject;

@SuppressWarnings("serial")
public class TemplateDataModel<T extends TemplateObject> 
	extends LazyDataModel<T> {
    
   private List<T> datasource;
    
   public TemplateDataModel(List<T> datasource) {
       this.datasource = datasource;
   }
    
   @Override
   public T getRowData(String rowKey) {
       for(TemplateObject template : datasource) {
           if(template.getId().equals(rowKey))
               return (T) template;
       }

       return null;
   }

   @Override
   public Object getRowKey(TemplateObject template) {
       return template.getId();
   }

   @Override
   public List<T> load(int first, int pageSize, 
           String sortField, SortOrder sortOrder, Map<String,Object> filters) {
       List<TemplateObject> data = new ArrayList<>();

       //filter
       for(TemplateObject template : datasource) {
           boolean match = true;

           if (filters != null) {
               for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
                   try {
                       String filterProperty = it.next();
                       Object filterValue = filters.get(filterProperty);
                       String fieldValue = String.valueOf(template.getClass().getField(filterProperty).get(template));

                       if(filterValue == null || fieldValue.startsWith(filterValue.toString())) {
                           match = true;
                   }
                   else {
                           match = false;
                           break;
                       }
                   } catch(Exception e) {
                       match = false;
                   }
               }
           }

           if(match) {
               data.add(template);
           }
       }

       //sort
       if(sortField != null) {
           Collections.sort(data, new TemplateSorter(sortField, sortOrder));
       }

       //rowCount
       int dataSize = data.size();
       this.setRowCount(dataSize);

       //paginate
       if(dataSize > pageSize) {
           try {
               return (List<T>) data.subList(first, first + pageSize);
           }
           catch(IndexOutOfBoundsException e) {
               return (List<T>) data.subList(first, first + (dataSize % pageSize));
           }
       }
       else {
           return (List<T>) data;
       }
   }
}

