package com.ausland.weixin.model.reqres;

import java.io.Serializable;
import java.util.List;

import com.ausland.weixin.model.db.LogisticPackage;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class QueryUploadLogisticPackageRes implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	List<LogisticPackage> records;
	String status;
	String errorDetails;
	 
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getErrorDetails() {
		return errorDetails;
	}
	public void setErrorDetails(String errorDetails) {
		this.errorDetails = errorDetails;
	}
	public List<LogisticPackage > getRecords() {
		return records;
	}
	public void setRecords(List<LogisticPackage> records) {
		this.records = records;
	}
 
	
}
