package com.ausland.weixin.model.reqres;

import java.util.List;

import com.ausland.weixin.model.zhonghuan.xml.Back;
import com.ausland.weixin.model.zhonghuan.xml.Tel;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class QueryZhongHuanLastThreeMonthByPhoneNoRes {

	String errorDetails;
	Boolean onlyTrackingNo;
	List<QueryZhongHuanDetailsByTrackingNoRes> trackingDetails;
	Tel trackingNos;
	public String getErrorDetails() {
		return errorDetails;
	}
	public void setErrorDetails(String errorDetails) {
		this.errorDetails = errorDetails;
	}
	public Boolean getOnlyTrackingNo() {
		return onlyTrackingNo;
	}
	public void setOnlyTrackingNo(Boolean onlyTrackingNo) {
		this.onlyTrackingNo = onlyTrackingNo;
	}
	public List<QueryZhongHuanDetailsByTrackingNoRes> getTrackingDetails() {
		return trackingDetails;
	}
	public void setTrackingDetails(List<QueryZhongHuanDetailsByTrackingNoRes> trackingDetails) {
		this.trackingDetails = trackingDetails;
	}
	public Tel getTrackingNos() {
		return trackingNos;
	}
	public void setTrackingNos(Tel trackingNos) {
		this.trackingNos = trackingNos;
	}
	@Override
	public String toString() {
		return "QueryZhongHuanLastThreeMonthByPhoneNoRes [errorDetails=" + errorDetails + ", onlyTrackingNo="
				+ onlyTrackingNo + ", trackingDetails=" + trackingDetails + ", trackingNos=" + trackingNos + "]";
	}
	
}
