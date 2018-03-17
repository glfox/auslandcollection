package com.ausland.weixin.model.reqres;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class QueryUserRes  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    
	String errorDetails;
	String status; 
	 
	List<UserRes> userList;

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

	public List<UserRes> getUserList() {
		return userList;
	}

	public void setUserList(List<UserRes> userList) {
		this.userList = userList;
	}
	
}
