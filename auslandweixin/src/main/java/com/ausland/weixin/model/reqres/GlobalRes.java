package com.ausland.weixin.model.reqres;

 
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class GlobalRes  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String errorDetails;
	String status; 
	 
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
	 
	@Override
	public String toString() {
		return "GolbalRes [errorDetails=" + errorDetails + ", status=" + status + "]";
	}

}
