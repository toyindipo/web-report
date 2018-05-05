package com.activedge.report.dto;

import com.activedge.report.model.Orderable;

public class JoinObjectDTO implements Orderable {
	private Integer id;
	
	private ReportColumnDTO column1;
	
	private ReportColumnDTO column2;
	
	private String joinDescription;
	
	private int joinOrder;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ReportColumnDTO getColumn1() {
		return column1;
	}

	public void setColumn1(ReportColumnDTO column1) {
		this.column1 = column1;
	}

	public ReportColumnDTO getColumn2() {
		return column2;
	}

	public void setColumn2(ReportColumnDTO column2) {
		this.column2 = column2;
	}

	public String getJoinDescription() {
		return joinDescription;
	}

	public void setJoinDescription(String joinDescription) {
		this.joinDescription = joinDescription;
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
	
	public JoinObjectDTO() {}
	
	public JoinObjectDTO(Integer id, ReportColumnDTO column1, ReportColumnDTO column2, 
			String joinDescription, int joinOrder) {
		this.id = id;
		this.column1 = column1;
		this.column2 = column2;
		this.joinDescription = joinDescription;
		this.setJoinOrder(joinOrder);
	}
	
	public JoinObjectDTO(ReportColumnDTO column1, ReportColumnDTO column2, String joinDescription) {
		this(null, column1, column2, joinDescription, 0);
	}
	
	@Override
	public String toString() {
		return column1 + ":" + column2;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((column1 == null) ? 0 : column1.hashCode());
		result = prime * result + ((column2 == null) ? 0 : column2.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof JoinObjectDTO))
			return false;
		JoinObjectDTO other = (JoinObjectDTO) obj;
		if (column1 == null) {
			if (other.column1 != null)
				return false;
		} else if (!column1.equals(other.column1))
			return false;
		if (column2 == null) {
			if (other.column2 != null)
				return false;
		} else if (!column2.equals(other.column2))
			return false;
		return true;
	}		
	
}
