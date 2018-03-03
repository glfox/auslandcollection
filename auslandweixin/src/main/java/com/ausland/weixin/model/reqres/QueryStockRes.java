package com.ausland.weixin.model.reqres;

 
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class QueryStockRes {

	String errorDetails;
	
}
