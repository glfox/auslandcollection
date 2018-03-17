package com.ausland.weixin.model.reqres;

 
import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class GlobalListRes  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String errorDetails;
	String status; 
	List<String> list;
	 
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
	public List<String> getList() {
		return list;
	}
	public void setList(List<String> list) {
		this.list = list;
	}
	@Override
	public String toString() {
		return "GlobalListRes [errorDetails=" + errorDetails + ", status=" + status + ", list=" + ToStringBuilder.reflectionToString(list) + "]";
	}
	 

}
