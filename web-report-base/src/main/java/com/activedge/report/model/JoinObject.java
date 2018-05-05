package com.activedge.report.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;;

/**
 * @author HP
 *
 */

@Entity
@Table(name="join_objects")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id", scope = JoinObject.class)
public class JoinObject implements Serializable, Orderable {
	private static final long serialVersionUID = 2975366915566375555L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private Integer id;

	@NotNull
	@ManyToOne
	@JoinColumn(name="column_1_id")
	private ReportColumn column1;

	@NotNull
	@ManyToOne
	@JoinColumn(name="column_2_id")
	private ReportColumn column2;
	
	@JoinColumn(name="join_order")
	private int joinOrder;
	
	@Column(name="description", columnDefinition="TEXT")
	private String joinDescription;
	
	@JoinColumn(name="report_template_id")
	@ManyToOne
	private ReportTemplate reportTemplate;

	public JoinObject() {
	}

	public JoinObject(ReportColumn column1, ReportColumn column2, int joinOrder, String joinDescription,
			ReportTemplate reportTemplate) {
		this.column1 = column1;
		this.column2 = column2;
		this.joinOrder = joinOrder;
		this.joinDescription = joinDescription;
		this.reportTemplate = reportTemplate;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ReportColumn getColumn1() {
		return column1;
	}

	public void setColumn1(ReportColumn column1) {
		this.column1 = column1;
	}

	public ReportColumn getColumn2() {
		return column2;
	}

	public void setColumn2(ReportColumn column2) {
		this.column2 = column2;
	}	

	public String getJoinDescription() {
		return joinDescription;
	}

	public void setJoinDescription(String joinDescription) {
		this.joinDescription = joinDescription;
	}

	public ReportTemplate getReportTemplate() {
		return reportTemplate;
	}

	public void setReportTemplate(ReportTemplate reportTemplate) {
		this.reportTemplate = reportTemplate;
	}	
	
	public int getJoinOrder() {
		return joinOrder;
	}

	public void setJoinOrder(int joinOrder) {
		this.joinOrder = joinOrder;
	}
	
	@Override
	public int getOrderValue() {
		return getJoinOrder();
	}

	@Override
    public boolean equals(Object that) {
        if (!(that instanceof JoinObject)) {
            return false;
        }
        JoinObject joinObject = (JoinObject)that;
        if (joinObject.id == null) {
            return false;
        }
        return joinObject.id.equals(id);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (id == null ? 0 : id.intValue());
        return result;
    }
    
    @Override
    public String toString() {
    	return column1.getReportTable() + " JOIN " + column2.getReportTable() +
    			" ON " + column1 + " = " + column2;
    }
    
    public String getJoinStringWithTable(String tableName) {
    	return " JOIN " + tableName + " ON " + column1 + " = " + column2;
    }
}
