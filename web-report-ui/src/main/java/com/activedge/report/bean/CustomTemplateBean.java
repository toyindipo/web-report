package com.activedge.report.bean;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;

import org.primefaces.model.DualListModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.activedge.report.connection.ConnectionMgr;
import com.activedge.report.converter.ColumnConverter;
import com.activedge.report.converter.CriteriaConverter;
import com.activedge.report.converter.JoinObjectConverter;
import com.activedge.report.converter.TableConverter;
import com.activedge.report.dto.JoinObjectDTO;
import com.activedge.report.dto.ReportColumnDTO;
import com.activedge.report.dto.ReportCriteriaDTO;
import com.activedge.report.dto.ReportObject;
import com.activedge.report.dto.ReportTableDTO;
import com.activedge.report.enums.CriteriaType;
import com.activedge.report.mapper.ModelMapper;
import com.activedge.report.model.JoinObject;
import com.activedge.report.model.ReportColumn;
import com.activedge.report.model.ReportCriteria;
import com.activedge.report.model.ReportTable;
import com.activedge.report.model.ReportTemplate;
import com.activedge.report.service.JoinObjectService;
import com.activedge.report.service.ReportColumnService;
import com.activedge.report.service.ReportCriteriaService;
import com.activedge.report.service.ReportTableService;
import com.activedge.report.service.ReportTemplateService;
import com.activedge.report.util.DeletePolicies;
import com.activedge.report.util.FileDownloader;
import com.activedge.report.util.PDFGenerator;
import com.activedge.report.util.ReportGenerator;
import com.activedge.report.util.ReportTableLoader;
import com.activedge.report.util.SessionUtils;
import com.activedge.report.util.TemplateUtil;

/**
 * @author Oladele Toyin
 * Since 9-Feb-2018
 * A JSF bean class for creating and editing user
 * defined report template
 */
/**
 * @author HP
 *
 */
@SuppressWarnings("serial")
@ViewScoped
@ManagedBean(name = "customTemplateBean")
public class CustomTemplateBean extends SpringBeanAutowiringSupport implements Serializable {
	@ManagedProperty(value = "#{paramBean}")
    private ParamBean paramBean;
	@ManagedProperty(value = "#{tableConverter}")
	private TableConverter tableConverter;
	@ManagedProperty(value = "#{columnConverter}")
	private ColumnConverter columnConverter;
	@ManagedProperty(value = "#{criteriaConverter}")
	private CriteriaConverter criteriaConverter;
	@ManagedProperty(value = "#{joinObjectConverter}")
	private JoinObjectConverter joinObjectConverter;
	
	@Autowired
	private ReportTemplateService templateService;
	@Autowired
	private ReportTableService tableService;
	@Autowired
	private ReportColumnService columnService;
	@Autowired
	private ReportCriteriaService criteriaService;
	@Autowired
	private JoinObjectService joinObjectService;
	private List<ReportTable> savedTableList;
	private Map<Integer, Integer> columnOrderValues;
	private List<ReportTableDTO> dbTables;
	private ReportTemplate template;
	private ReportTableDTO selectedTable;
	private List<ReportTableDTO> selectedTables;
	private Map<Integer, ReportColumn> savedColumnsMap;
	//Select field columns
	private List<ReportColumnDTO> selectedColumns;
	private List<ReportColumnDTO> savedSelectColumns;
	private List<ReportColumnDTO> templateColumns;
	private Map<String, ReportTableDTO> tableConverterMap;
	private Map<String, ReportColumnDTO> columnConverterMap;
	private DualListModel<ReportColumnDTO> columnPickList;
	private int tableIdGen = -1;
	private int columnIdGen = -1;
	private int joinObjectIdGen = -1;
	private int criteriaIdGen = -1;
	private List<JoinObjectDTO> savedJoinObjectList;
	private List<JoinObjectDTO> editJoinObjectList;
	private ReportColumnDTO selectedColumn;
	private Map<Integer, JoinObjectDTO> joinObjectConverterMap;
	private JoinObjectDTO selectedJoinObject;
	private JoinObjectDTO newJoinObject;
	private List<ReportCriteriaDTO> savedCriteriaList;
	private List<ReportCriteriaDTO> editCriteriaList;
	private Map<Integer, ReportCriteriaDTO> criteriaConverterMap;
	private ReportCriteriaDTO selectedCriteria;
	private ReportCriteriaDTO newCriteria;
	private Map<Integer, Integer> criteriaOrderValues;
	private Map<Integer, Integer> joinObjectOrderValues;
	
	private String templateId;
	
	//Reset column and table dto id... after save
	//Clear column on remove in picklist
	
	public JoinObjectDTO getSelectedJoinObject() {
		return selectedJoinObject;
	}

	public void setSelectedJoinObject(JoinObjectDTO selectedJoinObject) {
		this.selectedJoinObject = selectedJoinObject;
	}


	/**
	 * @param event JSF prerender event, generated on page load
	 */
	public void onLoad(ComponentSystemEvent event){        
        if (!FacesContext.getCurrentInstance().isPostback()) {            
            try {
            	String page = paramBean.getView();
            	if ("newCustomTemplate".equals(page)) {
            		template = new ReportTemplate();
            		loadDBTables();
                	setTableColumnConverterMaps();                	
            		initFields();
            		injectMapIntoConverters();
            	} else {
        			templateId = paramBean.getTemplateId();
	            	if (templateId == null || templateId.isEmpty()) {
	                	SessionUtils.addMessage("Unable to retrieve template data", 
	                			FacesMessage.SEVERITY_FATAL);
	                } else {
	                	if (template == null || 
	                			(Integer.parseInt(templateId) != template.getId().intValue())) {
	                		loadDBTables();
	                    	setTableColumnConverterMaps();
	                    	
	                		initFields();
	                		initTemplate();
		                	initTables();
		                	loadSavedColumn();
		                	initColumnOrderValues();
		                	initJoinObjects();
		                	initCriteriaObjects();
		                	injectMapIntoConverters();
	                	}	                		                	
	                }
            	}
			} catch (ClassNotFoundException | SQLException e) {
				SessionUtils.addMessage("Error loading db tables", 
            			FacesMessage.SEVERITY_FATAL);
				e.printStackTrace();
			}                      
        }
    }
	
	private void initFields() {		
		selectedColumns = new ArrayList<>();
		savedSelectColumns = new ArrayList<>();
		templateColumns = new ArrayList<>();
		selectedTables = new ArrayList<>();
		columnPickList = new DualListModel<>();
		savedJoinObjectList = new ArrayList<>();
		savedCriteriaList = new ArrayList<>();
		editJoinObjectList = new ArrayList<>();
		editCriteriaList = new ArrayList<>();
		joinObjectConverterMap = new HashMap<>();
		criteriaConverterMap = new HashMap<>();
		newJoinObject = new JoinObjectDTO();
		newCriteria = new ReportCriteriaDTO();
		savedColumnsMap = new HashMap<>();
		criteriaOrderValues = new HashMap<>();
		joinObjectOrderValues = new HashMap<>();
	}
		
	/**
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * Loads the metadata of database tables that data for reporting will be fetched from
	 */
	private void loadDBTables() throws ClassNotFoundException, SQLException {
		ConnectionMgr connectionMgr = new ConnectionMgr();
		ReportTableLoader tableLoader = new ReportTableLoader(connectionMgr);
		dbTables = tableLoader.loadTables();	
	}
	
	public List<ReportTableDTO> getTemplateDBTables() {
		return dbTables.stream().filter(table -> 
			tableSaved(table)
		).collect(Collectors.toList());
	}
	
	/**
	 * Fetch template from database
	 */
	private void initTemplate() {
		template = templateService.findById(Integer.parseInt(paramBean.getTemplateId()));
		if (template == null) {
			SessionUtils.getNavigationHandler().
			performNavigation("customTemplates?faces-redirect=true&view=customTemplates");
		}
	}
	
	/**
	 * Load all tables defined for a template
	 */
	private void initTables() {
		savedTableList = tableService.findByTemplateId(Integer.parseInt(templateId));
	}
	
	private void injectMapIntoConverters() {
		tableConverter.setTableConverterMap(tableConverterMap);
		columnConverter.setColumnConverterMap(columnConverterMap);
		criteriaConverter.setCriteriaConverterMap(criteriaConverterMap);
		joinObjectConverter.setJoinObjectConverterMap(joinObjectConverterMap);
	}
	
	/**
	 * Load all join objects defined for a template
	 * Load the join objects into a joinObjectConverterMap field, that is required for 
	 * the JoinObjectConverter class 
	 */
	private void initJoinObjects() {
		List<JoinObject> joinObjects = joinObjectService.findByTemplateId(Integer.parseInt(templateId));
		TemplateUtil.sortOrderable(joinObjects);
		for (JoinObject joinObject: joinObjects) {
			JoinObjectDTO joinObjectDTO = ModelMapper.joinObjectToDTO(joinObject);
			savedJoinObjectList.add(joinObjectDTO);
			joinObjectConverterMap.put(joinObject.getId(), joinObjectDTO);
			joinObjectOrderValues.put(joinObject.getId(), joinObject.getJoinOrder());
		}
		TemplateUtil.sortOrderable(savedJoinObjectList);
		editJoinObjectList.addAll(savedJoinObjectList);
	}
	
	/**
	 * Load all criteria (where fields) objects defined for a template
	 * Load the criteria (where fields) objects into a criteriaConverterMap field
	 * , that is required for the ReportCriteriaConverter class 
	 */
	private void initCriteriaObjects() {
		List<ReportCriteria> criterias = criteriaService.findByTemplateId(Integer.parseInt(templateId));
		TemplateUtil.sortOrderable(criterias);
		for (ReportCriteria criteria: criterias) {
			ReportCriteriaDTO criteriaDTO = ModelMapper.criteriaToDTO(criteria);
			savedCriteriaList.add(criteriaDTO);
			criteriaConverterMap.put(criteria.getId(), criteriaDTO);
			criteriaOrderValues.put(criteria.getId(), criteria.getOrderValue());
		}
		TemplateUtil.sortOrderable(savedCriteriaList);
		editCriteriaList.addAll(savedCriteriaList);
	}
	
	/**
	 * Store the column order values of template column, so as to determine which
	 * column(s) order position were modified during edit
	 */
	private void initColumnOrderValues() {
		columnOrderValues = new HashMap<>();
		for (ReportTable table: savedTableList) {
			ModelMapper.storeColumnsOrders(columnOrderValues, 
					table.getReportColumns());
		}
	}	
	
	/**
	 * Creates maps of table and column lists for the TableConverter and ColummConverter
	 * classes
	 */
	private void setTableColumnConverterMaps() {
		if (tableConverterMap == null || tableConverterMap.isEmpty()) {
			tableConverterMap = new HashMap<>();
			columnConverterMap = new HashMap<>();
			for (ReportTableDTO table: dbTables) {
				String tableKey = table.getTableSchema() == null ? table.getTableName() :
					table.getTableSchema() + ":" + table.getTableName();
				tableConverterMap.put(tableKey, table);
				for (ReportColumnDTO column: table.getReportColumns()) {
					columnConverterMap.put(table.getTableName() + ":" + column.getColumnName(), 
							column);				
				}
			}
		}	
	}
	
	/**
	 * Load all columns defined for a template into templateColumns field
	 * Load the columns defined as select fields into selectedColumns field
	 */
	private void loadSavedColumn() {
		for (ReportTable table: savedTableList) {
			templateColumns.addAll(ModelMapper.tableToDTO(table).getReportColumns());
			for (ReportColumn column: table.getReportColumns()) {
				savedColumnsMap.put(column.getId(), column);
			}
		}
		TemplateUtil.sortOrderable(templateColumns);
		for (ReportColumnDTO column: templateColumns) {
			if (column.getSelectField().equals(Boolean.TRUE)) {
				selectedColumns.add(column);
				savedSelectColumns.add(column);
			}
		}
	}
	
	/**
	 * Event method that is called when a table is selected from the list of database tables
	 */
	public void reportFieldTableSelect() {
		addIdToSelectedTable(selectedTable);
		setColumnPickListData();
	}
	
	/**
	 * Method that is called so as to update list of selected columns from the list of
	 * columns in the column dual picklist on the frontend
	 * It sets the columns in the left part of the picklist(source) as unselected
	 * and sets the columns in the right part of the picklist(target) as selected
	 */
	public void updateSelectedColumnList() {		
		for (ReportColumnDTO column: columnPickList.getSource()) {
			if (!columnSaved(column)) {
				column.setSelectField(Boolean.FALSE);
				selectedColumns.remove(column);
			}
		}
		
		for (ReportColumnDTO column: columnPickList.getTarget()) {
			column.setSelectField(Boolean.TRUE);
			
			if (!selectedColumns.contains(column)) {				
				selectedColumns.add(column);
			}
		}
		orderColumnsInList();
	}
	
	/**
	 * Sets the order value of selected column fields, based on its arrangement in the order
	 * list, on the UI
	 */
	public void orderColumnsInList() {
		for (int i = 0; i < selectedColumns.size(); i++) {
			selectedColumns.get(i).setColumnOrder(i);
		}
	}
	
	/**
	 * @param tableDTO tableDTO is a list of ReportTableDTO
	 * it sets the id of the selected tableDTO to the corresponding id in the savedTableList
	 * if the table has being saved before in the template, or to a negative incremental value
	 * for easy retrieval of tableDTO when saving a ReportColumn object from a ReportColumnDTO
	 */
	private void addIdToSelectedTable(ReportTableDTO tableDTO) {
		if (tableDTO.getId() == null){
			for (ReportTable table: savedTableList) {
			    if (table.getTableName().equals(tableDTO.getTableName()) &&
                        ((table.getTableSchema() != null && table.getTableSchema().
                                equals(tableDTO.getTableSchema())) || (table.getTableSchema() == null &&
                                tableDTO.getTableSchema() == null))) {
                    tableDTO.setId(table.getId());
                    break;
                }
			}
		}
		if (tableDTO.getId() == null) {
			tableDTO.setId(tableIdGen--);
		}
	}
	
	/**
	 * Sets the data in the column picklist on UI, when a table is selected
	 */
	public void setColumnPickListData() {		
		List<ReportColumnDTO> selectedColumns = new ArrayList<>();
		List<ReportColumnDTO> unSelectedColumns = new ArrayList<>();
		for (ReportColumnDTO column: selectedTable.getReportColumns()) {
			addColumnId(column);
			
			if (column.getSelectField().equals(Boolean.TRUE)) {				
				selectedColumns.add(column);
			} else {
				unSelectedColumns.add(column);
			}
		}
		columnPickList.setSource(unSelectedColumns);
		columnPickList.setTarget(selectedColumns);
	}
	
	/**
	 * @param column column is a ReportColumnDTO object from a column picklist on UI
	 * it sets the id of the selected column to the corresponding id in the templateColumsList
	 * which contains list of saved template colums, if the column has being saved before in the 
	 * template, or to a negative incremental value
	 * for easy retrieval of tableDTO when saving a ReportColumn object from a ReportColumnDTO 
	 */
	private void addColumnId(ReportColumnDTO column) {
		if (column.getId() == null) {
			int index = templateColumns.indexOf(column);
			if (index != -1) {
				column.setId(templateColumns.get(index).getId());
			} else {
				column.setId(columnIdGen--);
			}
		}
	}
	
	/**
	 * Create the first column of a join object
	 */
	public void addJoinObjectColumn1() {
	    if (selectedColumn != null) {
            addIdToSelectedTable(selectedTable);
            addColumnId(selectedColumn);
            newJoinObject.setColumn1(selectedColumn.clone());
        }
	}
	
	/**
	 * Create the second column of a join object
	 */
	public void addJoinObjectColumn2() {
		addIdToSelectedTable(selectedTable);
		addColumnId(selectedColumn);
		newJoinObject.setColumn2(selectedColumn.clone());
	}	
	
	/**
	 * Add the new join object to the list of join objects to be created
	 */
	public void addJoinObject() {
		if (newJoinObject.getColumn1() == null || newJoinObject.getColumn2() == null) {
			SessionUtils.addMessage("Add columns for the join object", 
        			FacesMessage.SEVERITY_ERROR);
		} else if (editJoinObjectList.contains(newJoinObject) || savedJoinObjectList.contains(newJoinObject)) {
			SessionUtils.addMessage("Join object already defined", 
        			FacesMessage.SEVERITY_ERROR);
		} else {
			newJoinObject.setId(joinObjectIdGen --);
			editJoinObjectList.add(newJoinObject);
			joinObjectConverterMap.put(newJoinObject.getId(), newJoinObject);
			newJoinObject = new JoinObjectDTO();
		}
	}
	
		
	public JoinObjectDTO getNewJoinObject() {
		return newJoinObject;
	}

	public void setNewJoinObject(JoinObjectDTO newJoinObject) {
		this.newJoinObject = newJoinObject;
	}

	public TableConverter getTableConverter() {
		return tableConverter;
	}

	public void setTableConverter(TableConverter tableConverter) {
		this.tableConverter = tableConverter;
	}

	public ColumnConverter getColumnConverter() {
		return columnConverter;
	}

	public void setColumnConverter(ColumnConverter columnConverter) {
		this.columnConverter = columnConverter;
	}

	/**
	 * Sets the selected column on UI as the criteria column
	 */
	public void addCriteriaColumn() {
		newCriteria.setReportColumn(selectedColumn);
	}
	
	/**
	 * Adds the new criteria object to the list of edited criteria objects
	 */
	public void addCriteriaToList() {
		if (newCriteria.getReportColumn() == null) {
			SessionUtils.addMessage("Select criteria column", 
        			FacesMessage.SEVERITY_ERROR);
		} else {
			newCriteria.setId(criteriaIdGen --);
			criteriaConverterMap.put(newCriteria.getId(), newCriteria);
			editCriteriaList.add(newCriteria);
			newCriteria = new ReportCriteriaDTO();
		}
	}
	
	public boolean sqlStringSpecified() {
		return template.isSqlStringDefined();
	}
	
		
	public String saveTemplate() {
		if (template.isSqlStringDefined()) {
			if (template.getSqlString() == null || template.getSqlString().isEmpty()) {
				SessionUtils.addMessage("Specifiy SQL string", 
            			FacesMessage.SEVERITY_ERROR);
				return null;
			} else {
				template.setSqlString(template.getSqlString().trim().toUpperCase());
				if (!template.getSqlString().startsWith("SELECT") || 
					template.getSqlString().indexOf(';') != template.getSqlString().length() - 1) {
					SessionUtils.addMessage("Invalid SQL string", 
	            			FacesMessage.SEVERITY_ERROR);
					return null;
				} 
			}
		}
		template = templateService.saveOrUpdate(template);
		paramBean.setTemplateId(String.valueOf(template.getId()));
		paramBean.setView("viewCustomTemplate");
		return "viewCustomTemplate";
	}
	
	public String openColumnPage() {
		paramBean.setTemplateId(String.valueOf(template.getId()));
		paramBean.setView("editCustomColumns");
		return "editCustomColumns";
	}
	
	public String openTemplatePage() {
		paramBean.setTemplateId(String.valueOf(template.getId()));
		paramBean.setView("viewCustomTemplate");
		return "viewCustomTemplate";
	}
	
	public String openJoinPage() {
		paramBean.setTemplateId(String.valueOf(template.getId()));
		paramBean.setView("editCustomJoinFields");
		return "editCustomJoinFields";
	}
	
	public String openCriteriaPage() {
		paramBean.setTemplateId(String.valueOf(template.getId()));
		paramBean.setView("editCustomCriteriaFields");
		return "editCustomCriteriaFields";
	}
	
	public void updateSelectedColumnsOrderValues() {
		orderColumnsInList();
		for (ReportColumnDTO columnDTO: selectedColumns) {
			if (columnSaved(columnDTO)) {
				if (!columnOrderValues.containsKey(columnDTO.getId()) || 
					!columnOrderValues.get(columnDTO.getId()).equals(columnDTO.getColumnOrder())) {
					updateColumnOrder(columnDTO);
					columnOrderValues.put(columnDTO.getId(), columnDTO.getColumnOrder());
				}
			}
		}
		SessionUtils.addMessage("Column order updated", 
    			FacesMessage.SEVERITY_INFO);
	}
	
	private ReportTable getTableFromList(Integer id) {
		for (ReportTable savedTable: savedTableList) {
			if (savedTable.getId().equals(id)) {
				return savedTable;
			}
		}
		return null;
	}
	
	private ReportTable getOrCreateColumnTable(ReportColumnDTO columnDTO) {
		ReportTable table;
		if (!tableSaved(columnDTO.getTable())) {
			table = saveTableAndUpdateDTO(columnDTO.getTable());
		} else {
			table = getTableFromList(columnDTO.getTable().getId());
		}
		return table;
	}
	
	public void saveNewColumns() {
		for (ReportColumnDTO columnDTO: selectedColumns) {
			if (!columnSaved(columnDTO)) {
				ReportTable table = getOrCreateColumnTable(columnDTO);
				saveColumnAndUpdateDTO(columnDTO, table);
			}
		}
		SessionUtils.addMessage("Report Columns saved", 
    			FacesMessage.SEVERITY_INFO);
	}
	
	public void updateSelectedColumn() {
		ReportColumn column = ModelMapper.dtoToColumn(selectedColumn);
		column.setId(selectedColumn.getId());
		columnService.saveOrUpdateColumn(column);
	}
	
	public boolean columnSaved(ReportColumnDTO columnDTO) {
		return columnDTO.getId() != null && columnDTO.getId().intValue() >= 1;
	}
	
	public boolean tableSaved(ReportTableDTO tableDTO) {
		if (tableDTO.getId() == null || tableDTO.getId().intValue() < 1) {
			return false;
		}
			
		ReportTable	table = getTableFromList(tableDTO.getId());
		return table != null;
	}
	
	private ReportColumn saveColumnAndUpdateDTO(ReportColumnDTO columnDTO, ReportTable table) {
		ReportColumn column = ModelMapper.dtoToColumn(columnDTO);
		column.setReportTable(table);
		column = columnService.saveOrUpdateColumn(column);
		savedColumnsMap.put(column.getId(), column);
		columnDTO.setId(column.getId());
		if (column.getSelectField().equals(Boolean.TRUE)) {
			columnOrderValues.put(column.getId(), column.getColumnOrder());
			if (!savedSelectColumns.contains(columnDTO)) {
				savedSelectColumns.add(columnDTO);
			}
		}		
		templateColumns.add(columnDTO);
		return column;
	}
	
	private ReportColumn updateColumnOrder(ReportColumnDTO column) {
		return columnService.updateOrderValue(column.getId(), column.getColumnOrder());
	}
	
	public void removeColumnAsSelectField() {
		if (columnSaved(selectedColumn)) {
			ReportColumn column = 
				columnService.updateSelectMode(selectedColumn.getId(), Boolean.FALSE);
			if (DeletePolicies.canDeleteColumn(column)) {
				deleteColumn(column, selectedColumn);
			}
		}
		selectedColumns.remove(selectedColumn);
		savedSelectColumns.remove(selectedColumn);
	}

	public List<ReportColumnDTO> getNewSelectColumns() {
		if (selectedColumns == null) {
			return Collections.EMPTY_LIST;
		} else {
			return selectedColumns.stream().filter(column -> !columnSaved(column)).collect(Collectors.toList());
		}
	}

	public List<ReportCriteriaDTO> getSavedCriteriaList() {
		if (editCriteriaList == null) {
			return Collections.EMPTY_LIST;
		} else {
			return editCriteriaList.stream().filter(criteria -> criteriaSaved(criteria)).collect(Collectors.toList());
		}
	}

	public List<ReportCriteriaDTO> getNewCriteriaList() {
		if (editCriteriaList == null) {
			return Collections.EMPTY_LIST;
		} else {
			return editCriteriaList.stream().filter(criteria -> !criteriaSaved(criteria)).collect(Collectors.toList());
		}
	}

	public List<JoinObjectDTO> getNewJoinObjectList() {
		if (editCriteriaList == null) {
			return Collections.EMPTY_LIST;
		} else {
			return editJoinObjectList.stream().filter(joinObject -> !joinObjectSaved(joinObject)).collect(Collectors.toList());
		}
	}
	
	public void saveSelectedColumn() {
		if (columnSaved(selectedColumn)) {
			ReportColumn column = ModelMapper.dtoToColumn(selectedColumn);
			column.setId(selectedColumn.getId());
			columnService.saveOrUpdateColumn(column);
		} else {
			ReportTable table = getOrCreateColumnTable(selectedColumn);
			saveColumnAndUpdateDTO(selectedColumn, table);
		}
		SessionUtils.addMessage("ReportColumn saved", 
    			FacesMessage.SEVERITY_INFO);
	}
	
	private void deleteColumn(ReportColumn column, ReportColumnDTO columnDTO) {
		columnService.deleteReportColumn(column.getId());
		savedColumnsMap.remove(column.getId());
		templateColumns.remove(columnDTO);
		columnOrderValues.remove(column.getId());
		columnDTO.setId(columnIdGen--);
	}
	
	
	
	private ReportTable saveTableAndUpdateDTO(ReportTableDTO tableDTO) {
		ReportTable table = new ReportTable(tableDTO.getTableName(), tableDTO.getTableSchema());
		table.setReportTemplate(template);
		table = tableService.save(table);
		savedTableList.add(table);
		tableDTO.setId(table.getId());
		return table;
	}
	
	public void deleteTable(ReportTable table) {
		tableService.delete(table.getId());
		savedTableList.remove(table);
	}
	
	private ReportColumn getReportColumnFromMap(Integer columnId) {
		return savedColumnsMap.get(columnId);
	}
	
	
	public void saveOrUpdateCriterias() {
		updateCriteriasOrderValues();
		for (ReportCriteriaDTO criteriaDTO: editCriteriaList) {
			if (!criteriaSaved(criteriaDTO)) {
				saveNewCriteria(criteriaDTO);
			}
		}
		SessionUtils.addMessage("ReportCriteria saved", 
    			FacesMessage.SEVERITY_INFO);
	}

	@Transactional
	public void saveSelectedCriteria() {
		if (criteriaSaved(selectedCriteria)) {
			ReportCriteria criteria = new ReportCriteria(selectedCriteria.getValue1(), 
					selectedCriteria.getValue2(),
					selectedCriteria.getCriteriaType(), 0, null);
			criteria.setId(selectedCriteria.getId());
			criteriaService.saveOrUpdate(criteria);
		} else {
			saveNewCriteria(selectedCriteria);
		}
		SessionUtils.addMessage("ReportCriteria saved", 
    			FacesMessage.SEVERITY_INFO);
	}
	

	private ReportCriteria saveNewCriteria(ReportCriteriaDTO criteriaDTO) {
		ReportColumn column = getReportColumnFromMap(criteriaDTO.getReportColumn().getId());
		
		ReportCriteria criteria = new ReportCriteria(criteriaDTO.getValue1(), criteriaDTO.getValue2(),
				criteriaDTO.getCriteriaType(), criteriaDTO.getCriteriaOrder(), column);
		criteria.setReportTemplate(template);
		criteria = criteriaService.saveOrUpdate(criteria);
		criteriaConverterMap.remove(criteriaDTO.getId());
		criteriaDTO.setId(criteria.getId());
		criteriaConverterMap.put(criteria.getId(), criteriaDTO);
		savedCriteriaList.add(criteriaDTO);
		columnService.updateCriteriaCount(column.getId(), 1);
		return criteria;
	}
	
	private void orderCriteriasInList() {
		for (int i = 0; i < editCriteriaList.size(); i++) {
			editCriteriaList.get(i).setCriteriaOrder(i);
		}
	}
	
	public boolean criteriaSaved(ReportCriteriaDTO criteria) {
		return criteria.getId() != null && criteria.getId().intValue() >= 1;
	}
	
	public void updateCriteriasOrderValues() {
		orderCriteriasInList();
		for (ReportCriteriaDTO criteriaDTO: editCriteriaList) {
			if (criteriaSaved(criteriaDTO)) {
				if (!criteriaOrderValues.containsKey(criteriaDTO.getId()) || 
						!criteriaOrderValues.get(criteriaDTO.getId()).equals
							(criteriaDTO.getOrderValue())) {
						updateCriteriaOrder(criteriaDTO);
						criteriaOrderValues.put(criteriaDTO.getId(), criteriaDTO.getOrderValue());
					}
			}
		}
		SessionUtils.addMessage("Criteria order updated", 
    			FacesMessage.SEVERITY_INFO);
	}
	
	private ReportCriteria updateCriteriaOrder(ReportCriteriaDTO criteriaDTO) {
		return criteriaService.updateOrderValue(criteriaDTO.getId(), 
				criteriaDTO.getCriteriaOrder());
	}

	@Transactional
	public void deleteSelectedCriteria() {
		if (criteriaSaved(selectedCriteria)) {
			ReportColumnDTO columnDTO = selectedCriteria.getReportColumn();
			criteriaService.delete(selectedCriteria.getId());			
			savedCriteriaList.remove(selectedCriteria);
			criteriaConverterMap.remove(selectedCriteria.getId());
			ReportColumn column = columnService.updateCriteriaCount(columnDTO.getId(), -1);
			if (DeletePolicies.canDeleteColumn(column)) {
				deleteColumn(column, columnDTO);
			}
		}
		editCriteriaList.remove(selectedCriteria);
	}

	@Transactional
	public void saveSelectedJoinObject() {
		if (joinObjectSaved(selectedJoinObject)) {
			JoinObject joinObject = new JoinObject();
			joinObject.setId(selectedJoinObject.getId());
			joinObject.setJoinDescription(selectedJoinObject.getJoinDescription());
			joinObjectService.saveOrUpdate(joinObject);
		} else {
			saveNewJoinObject(selectedJoinObject);
		}
		SessionUtils.addMessage("JoinObject saved", 
    			FacesMessage.SEVERITY_INFO);
	}

	@Transactional
	public void saveOrUpdateJoinObjects() {
		updateJoinObjectOrderValues();
		for (JoinObjectDTO joinObjectDTO: editJoinObjectList) {
			if (!joinObjectSaved(joinObjectDTO)) {
				saveNewJoinObject(joinObjectDTO);
			}
		}
		SessionUtils.addMessage("JoinObjects saved", 
    			FacesMessage.SEVERITY_INFO);
	}
	

	private void saveNewJoinObject(JoinObjectDTO joinObjectDTO) {
		if (joinObjectDTO.getColumn1().equals(joinObjectDTO.getColumn2()))
			return;
		ReportColumn column1 = getReportColumnFromMap(joinObjectDTO.getColumn1().getId());
		ReportColumn column2 = getReportColumnFromMap(joinObjectDTO.getColumn2().getId());
		
		if (column1 == null) {
			ReportTable table = getOrCreateColumnTable(joinObjectDTO.getColumn1());
			column1 = saveColumnAndUpdateDTO(joinObjectDTO.getColumn1(), table);
		}
		
		if (column2 == null) {
			ReportTable table2 = getOrCreateColumnTable(joinObjectDTO.getColumn2());
			column2 = saveColumnAndUpdateDTO(joinObjectDTO.getColumn2(), table2);
		}

		JoinObject joinObject = new JoinObject(column1, column2, joinObjectDTO.getJoinOrder(), 
				joinObjectDTO.getJoinDescription(), template);
		joinObject = joinObjectService.saveOrUpdate(joinObject);
		joinObjectConverterMap.remove(joinObjectDTO.getId());
		joinObjectDTO.setId(joinObject.getId());
		if (!savedJoinObjectList.contains(joinObjectDTO)) {
			savedJoinObjectList.add(joinObjectDTO);
		}
		joinObjectConverterMap.put(joinObject.getId(), joinObjectDTO);
		columnService.updateJoinCount(column1.getId(), 1);
		columnService.updateJoinCount(column2.getId(), 1);
	}
	
	public void deleteSelectedJoinObject() {
		if (joinObjectSaved(selectedJoinObject)) {
			ReportColumnDTO columnDTO1 = selectedJoinObject.getColumn1();
			ReportColumnDTO columnDTO2 = selectedJoinObject.getColumn2();
			joinObjectService.delete(selectedJoinObject.getId());
			savedJoinObjectList.remove(selectedJoinObject);
			joinObjectConverterMap.remove(selectedJoinObject.getId());
			ReportColumn column = columnService.updateCriteriaCount(columnDTO1.getId(), -1);
			if (DeletePolicies.canDeleteColumn(column)) {
				deleteColumn(column, columnDTO1);
			}
			
			column = columnService.updateCriteriaCount(columnDTO2.getId(), -1);
			if (DeletePolicies.canDeleteColumn(column)) {
				deleteColumn(column, columnDTO2);
			}
		}
		editJoinObjectList.remove(selectedJoinObject);
	}
	
	private void orderJoinObjectInList() {
		for (int i = 0; i < editJoinObjectList.size(); i++) {
			editJoinObjectList.get(i).setJoinOrder(i);
		}
	}
	
	public void updateJoinObjectOrderValues() {
		orderJoinObjectInList();
		for (JoinObjectDTO joinObjectDTO: editJoinObjectList) {
			if (joinObjectSaved(joinObjectDTO)) {
				if (!joinObjectOrderValues.containsKey(joinObjectDTO.getId()) || 
						!joinObjectOrderValues.get(joinObjectDTO.getId()).equals
							(joinObjectDTO.getOrderValue())) {
						updateJoinObjectOrder(joinObjectDTO);
						joinObjectOrderValues.put(joinObjectDTO.getId(), 
								joinObjectDTO.getOrderValue());
					}
			}
		}
		SessionUtils.addMessage("JoinObject order updated", 
    			FacesMessage.SEVERITY_INFO);
	}
	
	public void generateReport() {
		List<ReportTable> tables = tableService.findByTemplateId(template.getId());
		List<ReportColumn> selectFields = new ArrayList<>();
		tables.forEach(table -> {
			selectFields.addAll(table.getReportColumns().stream().filter(column -> 
					Boolean.TRUE.equals(column.getSelectField())).collect(Collectors.toList()));
		});
		if (selectFields.isEmpty()) {
			SessionUtils.addMessage("No column specified as select field", 
        			FacesMessage.SEVERITY_FATAL);
		} else {			
			ConnectionMgr connMgr = new ConnectionMgr();
			try {
				Connection connection = connMgr.getConnection();
				String sqlString = "";
				ResultSet resultSet;
				if (template.isSqlStringDefined()) {
					sqlString = template.getSqlString();
					resultSet = ReportGenerator.getResultSet(connection, sqlString);
				} else {
					List<JoinObject> joins = 
							joinObjectService.findByTemplateId(template.getId());
					List<ReportCriteria> criterias = 
							criteriaService.findByTemplateId(template.getId());
					sqlString = ReportGenerator.getSqlString(tables, selectFields, joins, criterias);
					resultSet = ReportGenerator.getResultSet(connection, sqlString, criterias);
				}
				ReportObject reportObject = ReportGenerator.
						generateReportData(selectFields, resultSet, template.getReportHeader());
				PDFGenerator pdfGenerator = new PDFGenerator();
				ByteArrayOutputStream reportStream = pdfGenerator.generatePDF(reportObject);
				new FileDownloader().downloadPDF(reportStream);
				
			} catch (ClassNotFoundException | SQLException | ParseException e) {
				SessionUtils.addMessage("Error getting report data", 
	        			FacesMessage.SEVERITY_FATAL);
				e.printStackTrace();
			} catch (IOException e) {
				SessionUtils.addMessage("Error generating PDF file", 
	        			FacesMessage.SEVERITY_FATAL);
				e.printStackTrace();
			} finally {
				connMgr.closeConnection();
			}
		}
	}
	
	private JoinObject updateJoinObjectOrder(JoinObjectDTO joinObjectDTO) {
		return joinObjectService.updateOrderValue(joinObjectDTO.getId(), 
				joinObjectDTO.getJoinOrder());
	}
	
	public boolean joinObjectSaved(JoinObjectDTO joinObject) {
		return joinObject.getId() != null && joinObject.getId().intValue() >= 1;
	}
		
	public ReportColumnDTO getSelectedColumn() {
		return selectedColumn;
	}

	public void setSelectedColumn(ReportColumnDTO selectedColumn) {
		this.selectedColumn = selectedColumn;
	}

	public ReportCriteriaDTO getSelectedCriteria() {
		return selectedCriteria;
	}

	public void setSelectedCriteria(ReportCriteriaDTO selectedCriteria) {
		this.selectedCriteria = selectedCriteria;
	}

	public ReportCriteriaDTO getNewCriteria() {
		return newCriteria;
	}

	public void setNewCriteria(ReportCriteriaDTO newCriteria) {
		this.newCriteria = newCriteria;
	}		

	public CriteriaConverter getCriteriaConverter() {
		return criteriaConverter;
	}

	public void setCriteriaConverter(CriteriaConverter criteriaConverter) {
		this.criteriaConverter = criteriaConverter;
	}

	public JoinObjectConverter getJoinObjectConverter() {
		return joinObjectConverter;
	}

	public void setJoinObjectConverter(JoinObjectConverter joinObjectConverter) {
		this.joinObjectConverter = joinObjectConverter;
	}

	public List<ReportCriteriaDTO> getEditCriteriaList() {
		return editCriteriaList;
	}

	public void setEditCriteriaList(List<ReportCriteriaDTO> editCriteriaList) {
		this.editCriteriaList = editCriteriaList;
	}


	public DualListModel<ReportColumnDTO> getColumnPickList() {
		return columnPickList;
	}

	public void setColumnPickList(DualListModel<ReportColumnDTO> columnPickList) {
		this.columnPickList = columnPickList;
	}

	public ParamBean getParamBean() {
		return paramBean;
	}


	public List<ReportTableDTO> getDbTables() {
		return dbTables;
	}
	
	public List<ReportColumnDTO> getTemplateColumns() {
		return templateColumns;
	}

	public void setTemplateColumns(List<ReportColumnDTO> templateColumns) {
		this.templateColumns = templateColumns;
	}

	public void setDbTables(List<ReportTableDTO> dbTables) {
		this.dbTables = dbTables;
	}

	public ReportTemplate getTemplate() {
		return template;
	}

	public void setTemplate(ReportTemplate template) {
		this.template = template;
	}

	public void setSavedSelectColumns(List<ReportColumnDTO> savedSelectColumns) {
		this.savedSelectColumns = savedSelectColumns;
	}

	public List<ReportColumnDTO> getSavedSelectColumns() {
		return savedSelectColumns;
	}

	public ReportTableDTO getSelectedTable() {
		return selectedTable;
	}

	public void setSelectedTable(ReportTableDTO selectedTable) {
		this.selectedTable = selectedTable;
	}

	public List<ReportTableDTO> getSelectedTables() {
		return selectedTables;
	}

	public void setSelectedTables(List<ReportTableDTO> selectedTables) {
		this.selectedTables = selectedTables;
	}	

	public List<ReportColumnDTO> getSelectedColumns() {
		return selectedColumns;
	}

	public void setSelectedColumns(List<ReportColumnDTO> selectedColumns) {
		this.selectedColumns = selectedColumns;
	}	

	public List<ReportTable> getSavedTableList() {
		return savedTableList;
	}

	public void setSavedTableList(List<ReportTable> savedTableList) {
		this.savedTableList = savedTableList;
	}

	public List<JoinObjectDTO> getSavedJoinObjectList() {
		return savedJoinObjectList;
	}

	public void setSavedJoinObjectList(List<JoinObjectDTO> savedJoinObjectList) {
		this.savedJoinObjectList = savedJoinObjectList;
	}

	public List<JoinObjectDTO> getEditJoinObjectList() {
		return editJoinObjectList;
	}

	public void setEditJoinObjectList(List<JoinObjectDTO> editJoinObjectList) {
		this.editJoinObjectList = editJoinObjectList;
	}

	public void setParamBean(ParamBean paramBean) {
		this.paramBean = paramBean;
	}
	
	public CriteriaType[] getCriteriaTypes() {
		return CriteriaType.values();
	}
}
