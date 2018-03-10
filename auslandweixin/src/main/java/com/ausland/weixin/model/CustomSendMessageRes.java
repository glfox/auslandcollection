package com.ausland.weixin.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CustomSendMessageRes {
	private String errorcode;
	private String errmsg;
	public String getErrorcode() {
		return errorcode;
	}
	public void setErrorcode(String errorcode) {
		this.errorcode = errorcode;
	}
	public String getErrmsg() {
		return errmsg;
	}
	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
	@Override
	public String toString() {
		return "CustomSendMessageRes [errorcode=" + errorcode + ", errmsg=" + errmsg + "]";
	}
	 
}
