package com.ausland.weixin.model.reqres;

 
import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class QueryOrderDetailsRes implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	String status;
	String errorDetails;
	List<OrderDetailsRes> orderDetailsList;
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
	public List<OrderDetailsRes> getOrderDetails() {
		return orderDetailsList;
	}
	public void setOrderDetails(List<OrderDetailsRes> orderDetailsList) {
		this.orderDetailsList = orderDetailsList;
	}
  
}
