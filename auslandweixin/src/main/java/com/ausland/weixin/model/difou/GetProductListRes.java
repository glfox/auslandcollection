package com.ausland.weixin.model.difou;

import java.io.Serializable;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetProductListRes  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty("returncode")	
    String returnCode;  //mandatory  0: successful,  1: failed
	@JsonProperty("returninfo")	
    String returnInfo;  //mandatory  success: return no the products,  failed: return failed reason
	@JsonProperty("datalist")	
    ArrayList<ProductDataInfo> dataInfoList;  //response data
	public String getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	public String getReturnInfo() {
		return returnInfo;
	}
	public void setReturnInfo(String returnInfo) {
		this.returnInfo = returnInfo;
	}
	public ArrayList<ProductDataInfo> getDataInfoList() {
		return dataInfoList;
	}
	public void setDataInfoList(ArrayList<ProductDataInfo> dataInfoList) {
		this.dataInfoList = dataInfoList;
	}
	
}