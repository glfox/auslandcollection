package com.ausland.weixin.model.reqres;

import com.ausland.weixin.model.zhonghuan.xml.Tel;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class QueryZhongHuanLastThreeMonthByPhoneNoRes {
    String status;
	String errorDetails;
	Tel trackingNos;
	public String getErrorDetails() {
		return errorDetails;
	}
	public void setErrorDetails(String errorDetails) {
		this.errorDetails = errorDetails;
	}
	
	public Tel getTrackingNos() {
		return trackingNos;
	}
	public void setTrackingNos(Tel trackingNos) {
		this.trackingNos = trackingNos;
	}
	
}
