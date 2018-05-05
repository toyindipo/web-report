package com.activedge.report.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.activedge.report.enums.CriteriaType;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="report_criteria")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id", scope = ReportCriteria.class)
public class ReportCriteria implements Serializable, Orderable {

	private static final long serialVersionUID = -438949539196217357L;

	@Transient
	private Timestamp now = new Timestamp(System.currentTimeMillis());
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private Integer id;
	
	@NotNull
	@JoinColumn(name="report_column_id")
	@ManyToOne
	private ReportColumn reportColumn;
	
	@NotNull
	@Column(name="value_1")
	private String value1;
	
	@Column(name="value_2")
	private String value2;
	
	@Enumerated(EnumType.STRING)
	@NotNull
	@Column(name="criteria_type")
	private CriteriaType criteriaType;
	
	@Column(name="criteria_order")
	private int criteriaOrder;	
	
	@ManyToOne
	@JoinColumn(name="report_template_id")
	private ReportTemplate reportTemplate;
	
	@Column(name="created_at")
	private Timestamp createdAt = now;
	
	@Column(name="updated_at")
	private Timestamp updatedAt = now;

	public ReportCriteria() {
		
	}

	public ReportCriteria(String value1, String value2, CriteriaType criteriaType, int criteriaOrder, 
			ReportColumn column) {
		this.value1 = value1;
		this.value2 = value2;
		this.criteriaType = criteriaType;
		this.criteriaOrder = criteriaOrder;
		this.reportColumn = column;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ReportColumn getReportColumn() {
		return reportColumn;
	}

	public void setReportColumn(ReportColumn reportColumn) {
		this.reportColumn = reportColumn;
	}

	public String getValue1() {
		return value1;
	}

	public void setValue1(String value1) {
		this.value1 = value1;
	}

	public String getValue2() {
		return value2;
	}

	public void setValue2(String value2) {
		this.value2 = value2;
	}

	public CriteriaType getCriteriaType() {
		return criteriaType;
	}

	public void setCriteriaType(CriteriaType criteriaType) {
		this.criteriaType = criteriaType;
	}

	public ReportTemplate getReportTemplate() {
		return reportTemplate;
	}

	public void setReportTemplate(ReportTemplate reportTemplate) {
		this.reportTemplate = reportTemplate;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	public int getCriteriaOrder() {
		return criteriaOrder;
	}

	public void setCriteriaOrder(int criteriaOrder) {
		this.criteriaOrder = criteriaOrder;
	}
	
	@Override
	public int getOrderValue() {
		return getCriteriaOrder();
	}

	@Override
    public boolean equals(Object that) {
        if (!(that instanceof ReportCriteria)) {
            return false;
        }
        ReportCriteria criteria = (ReportCriteria)that;
        if (criteria.id == null) {
            return false;
        }
        return criteria.id.equals(id);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (id == null ? 0 : id.intValue());
        return result;
    }
    
    @Override 
    public String toString() {
    	String stringVal = reportColumn + " " + criteriaType.getSymbol() + " ?";
    	if (criteriaType.equals(CriteriaType.BETWEEN)) {
    		stringVal = stringVal + " AND ?";
    	}
    	return stringVal;
    }
}
