package com.ausland.weixin.model.reqres;

 
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class QueryStockRes {

	String errorDetails;
	
	List<StockInfo> stockInfoList;
	
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
