package com.ausland.weixin.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CustomSendImageMessage {
	@JsonProperty("touser")
	private String toUserName;
 
	@JsonProperty("msgtype")
	private String msgType;
	
	@JsonProperty("image")
	private WeChatImage image;

	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public WeChatImage getImage() {
		return image;
	}

	public void setImage(WeChatImage image) {
		this.image = image;
	}

	@Override
	public String toString() {
		return "CustomSendImageMessage [toUserName=" + toUserName + ", msgType=" + msgType + ", image=" + image + "]";
	}
 
	 
}
