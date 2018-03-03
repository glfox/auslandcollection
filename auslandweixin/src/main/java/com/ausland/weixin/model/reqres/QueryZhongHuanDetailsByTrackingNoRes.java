package com.ausland.weixin.model.reqres;

import com.ausland.weixin.model.zhonghuan.xml.Back;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class QueryZhongHuanDetailsByTrackingNoRes {

	String errorDetails;
	Back back;
	 
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
		return "QueryZhongHuanDetailsByTrackingNoRes [errorDetails=" + errorDetails + ", back=" + back + "]";
	}
	 
	
}