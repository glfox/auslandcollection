package com.ausland.weixin.model.reqres;

 
import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class QueryStockRes  implements Serializable{

	String status;
	List<StockInfo> stockInfoList;
	String errorDetails;
	
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
	public List<StockInfo> getStockInfoList() {
		return stockInfoList;
	}
	public void setStockInfoList(List<StockInfo> stockInfoList) {
		this.stockInfoList = stockInfoList;
	}
	 
	 
	
}
