package com.ausland.weixin.model.reqres;

 
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UploadZhonghanCourierExcelRes {
 
	String fileName;
	String uploadResult;
	String errorDetails;
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getUploadResult() {
		return uploadResult;
	}
	public void setUploadResult(String uploadResult) {
		this.uploadResult = uploadResult;
	}
	public String getErrorDetails() {
		return errorDetails;
	}
	public void setErrorDetails(String errorDetails) {
		this.errorDetails = errorDetails;
	}
	 
	
}
