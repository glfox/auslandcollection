package com.ausland.weixin.model.reqres;

import java.io.Serializable;

import com.ausland.weixin.model.zhonghuan.xml.Back;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class QueryZhongHuanDetailsByTrackingNoRes implements Serializable{

	String errorDetails;
	String status;
	Back back;
	 
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
	public Back getBack() {
		return back;
	}
	public void setBack(Back back) {
		this.back = back;
	}
	@Override
	public String toString() {
		return "QueryZhongHuanDetailsByTrackingNoRes [errorDetails=" + errorDetails + ", status=" + status + ", back="
				+ back + "]";
	}
	
}
