package com.ausland.weixin.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CustomSendMessage {
	@JsonProperty("ToUserName")
	private String toUserName;
	@JsonProperty("FromUserName")
	private String fromUserName;
	@JsonProperty("CreateTime")
	private long createTime;
	@JsonProperty("MsgType")
	private String msgType;
	@JsonProperty("Content")
	private String content;
	public String getToUserName() {
		return toUserName;
	}
	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}
	public String getFromUserName() {
		return fromUserName;
	}
	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	@Override
	public String toString() {
		return String.format("<xml><ToUserName><![CDATA[%s]]></ToUserName> <FromUserName><![CDATA[%s]]></FromUserName><CreateTime>%s</CreateTime><MsgType><![CDATA[%s]]></MsgType><Content><![CDATA[%s]]></Content></xml>", 
				toUserName, fromUserName, createTime, msgType, content);
	}
	
}
