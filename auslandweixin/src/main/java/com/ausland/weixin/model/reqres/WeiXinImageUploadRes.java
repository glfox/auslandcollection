package com.ausland.weixin.model.reqres;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class WeiXinImageUploadRes  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	@JsonProperty("type")	
	String type;

    @JsonProperty("media_id")
	String mediaId;
    
    @JsonProperty("created_at")
	String createdAt;	
	
    @JsonProperty("errorcode")
	String errorCode; 
    
    @JsonProperty("errmsg")
	String errMsg;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	@Override
	public String toString() {
		return "WeiXinImageUploadRes [type=" + type + ", mediaId=" + mediaId + ", createdAt=" + createdAt
				+ ", errorCode=" + errorCode + ", errMsg=" + errMsg + "]";
	}
    
}
