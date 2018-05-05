package com.activedge.report.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="audit_trails")
public class AuditTrail implements Serializable {
	@JsonIdentityInfo(
	        generator = ObjectIdGenerators.PropertyGenerator.class,
	        property = "id", scope = AuditTrail.class)
    private static final long serialVersionUID = 3515291880483274391L;
    @Transient
    private Timestamp now = new Timestamp(System.currentTimeMillis());

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @NotNull
    @Column
    private String action;

    @ManyToOne
    @JoinColumn(name="performed_by")
    private User performedBy;

    @NotNull
    @Column(name="table_name")
    private String tableName;

    @Column(name="content_before")
    private String contentBefore;

    @Column(name="content_after")
    private String contentAfter;

    @Column
    private Timestamp timeOfChange = now;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public User getPerformedBy() {
        return performedBy;
    }

    public void setPerformedBy(User performedBy) {
        this.performedBy = performedBy;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getContentBefore() {
        return contentBefore;
    }

    public void setContentBefore(String contentBefore) {
        this.contentBefore = contentBefore;
    }

    public String getContentAfter() {
        return contentAfter;
    }

    public void setContentAfter(String contentAfter) {
        this.contentAfter = contentAfter;
    }

    public Timestamp getTimeOfChange() {
        if (timeOfChange == null) {
            return null;
        }
        return new Timestamp(timeOfChange.getTime());
    }

    public void setTimeOfChange(Timestamp timeOfChange) {
        if (timeOfChange == null) {
            this.timeOfChange = null;
        } else {
            this.timeOfChange = new Timestamp(timeOfChange.getTime());
        }
    }

    public AuditTrail() {
    }

    public AuditTrail(String action, User performedBy, String tableName, String contentBefore, String contentAfter) {
        this.action = action;
        this.performedBy = performedBy;
        this.tableName = tableName;
        this.contentBefore = contentBefore;
        this.contentAfter = contentAfter;
    }
}
