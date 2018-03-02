package com.ausland.weixin.model.reqres;

import java.util.List;

 
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UploadLogisticPackageRes {

	List<LogisticPackageRecord> records;
 
	String errorDetails;
	 
	public String getErrorDetails() {
		return errorDetails;
	}
	public void setErrorDetails(String errorDetails) {
		this.errorDetails = errorDetails;
	}
	public List<LogisticPackageRecord> getRecords() {
		return records;
	}
	public void setRecords(List<LogisticPackageRecord> records) {
		this.records = records;
	}
 
	@Override
	public String toString() {
		return "UploadLogisticPackageRes [records=" + records + ", errorDetails="
				+ errorDetails + "]";
	}
	
	
}
