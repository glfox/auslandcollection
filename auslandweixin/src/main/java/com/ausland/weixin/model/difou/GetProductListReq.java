package com.ausland.weixin.model.difou;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetProductListReq  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty("pageno")	
    String pageNo;  //mandatory start from 1 
	@JsonProperty("pagesize")	
    String pageSize;  //mandatory 1-100
	@JsonProperty("searchno")	
    String productId;  //optional
	public String getPageNo() {
		return pageNo;
	}
	public void setPageNo(String pageNo) {
		this.pageNo = pageNo;
	}
	public String getPageSize() {
		return pageSize;
	}
	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	
}
