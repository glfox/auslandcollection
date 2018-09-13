package com.ausland.weixin.model.reqres;

 
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UploadPackingPhotoRes {
 
	String uploadResult;
	
	int successUploadedCount;
	
	int failedUploadedCount;
	
	String errorDetails;

	public String getUploadResult() {
		return uploadResult;
	}

	public void setUploadResult(String uploadResult) {
		this.uploadResult = uploadResult;
	}

	public int getSuccessUploadedCount() {
		return successUploadedCount;
	}

	public void setSuccessUploadedCount(int successUploadedCount) {
		this.successUploadedCount = successUploadedCount;
	}

	public int getFailedUploadedCount() {
		return failedUploadedCount;
	}

	public void setFailedUploadedCount(int failedUploadedCount) {
		this.failedUploadedCount = failedUploadedCount;
	}

	public String getErrorDetails() {
		return errorDetails;
	}

	public void setErrorDetails(String errorDetails) {
		this.errorDetails = errorDetails;
	}

	@Override
	public String toString() {
		return "UploadPackingPhotoRes [uploadResult=" + uploadResult + ", successUploadedCount=" + successUploadedCount
				+ ", failedUploadedCount=" + failedUploadedCount + ", errorDetails=" + errorDetails + "]";
	}
	 
}
