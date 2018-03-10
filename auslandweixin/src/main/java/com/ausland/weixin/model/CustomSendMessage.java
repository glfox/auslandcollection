package com.ausland.weixin.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CustomSendMessage {
	@JsonProperty("touser")
	private String toUserName;
 
	@JsonProperty("msgtype")
	private String msgType;
	
	@JsonProperty("text")
	private MessageContent content;

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

	public MessageContent getContent() {
		return content;
	}

	public void setContent(MessageContent content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "CustomSendMessage [toUserName=" + toUserName + ", msgType=" + msgType + ", content=" + content + "]";
	}
	
	 
}
