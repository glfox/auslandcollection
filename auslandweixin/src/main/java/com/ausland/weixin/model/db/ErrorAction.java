package com.ausland.weixin.model.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="erroraction")
public class ErrorAction {

	@Id
	@Column(name="id")	
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	Integer errorId;
    
	@Column(name="errorModule", length=64)
	String errorModule;
	
	@Column(name="errorAction", length=128)
	String errorAction;
	
	@Column(length=2048, nullable = false)
	String errorDetails;
	
	@Column(length=64)
	String createdBy;
	
	@Column(length=64, nullable = false)
	String createdDateTime;
	
	@Column(length=64)
	String lastUpdatedDateTime;
	
	@Column(length=64)
	String status;

	public Integer getErrorId() {
		return errorId;
	}

	public void setErrorId(Integer errorId) {
		this.errorId = errorId;
	}

	public String getErrorModule() {
		return errorModule;
	}

	public void setErrorModule(String errorModule) {
		this.errorModule = errorModule;
	}

	public String getErrorAction() {
		return errorAction;
	}

	public void setErrorAction(String errorAction) {
		this.errorAction = errorAction;
	}

	public String getErrorDetails() {
		return errorDetails;
	}

	public void setErrorDetails(String errorDetails) {
		this.errorDetails = errorDetails;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedDateTime() {
		return createdDateTime;
	}

	public void setCreatedDateTime(String createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

	public String getLastUpdatedDateTime() {
		return lastUpdatedDateTime;
	}

	public void setLastUpdatedDateTime(String lastUpdatedDateTime) {
		this.lastUpdatedDateTime = lastUpdatedDateTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
