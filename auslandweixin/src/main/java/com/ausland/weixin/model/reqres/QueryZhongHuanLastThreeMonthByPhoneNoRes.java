package com.ausland.weixin.model.reqres;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class QueryZhongHuanLastThreeMonthByPhoneNoRes implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String status;
	String errorDetails;
	List<ZhongHuanFydhDetails> fydhList;
	public String getErrorDetails() {
		return errorDetails;
	}
	public void setErrorDetails(String errorDetails) {
		this.errorDetails = errorDetails;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<ZhongHuanFydhDetails> getFydhList() {
		if(fydhList == null) {
			fydhList = new ArrayList<>();
		}
		return fydhList;
	}
	public void setFydhList(List<ZhongHuanFydhDetails> fydhList) {
		this.fydhList = fydhList;
	}
	@Override
	public String toString() {
		return "QueryZhongHuanLastThreeMonthByPhoneNoRes [status=" + status + ", errorDetails=" + errorDetails
				+ ", fydhList=" + ToStringBuilder.reflectionToString(fydhList) + "]";
	}
	
	
}
