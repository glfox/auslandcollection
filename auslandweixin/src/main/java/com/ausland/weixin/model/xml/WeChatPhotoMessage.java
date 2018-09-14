package com.ausland.weixin.model.xml;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.NONE)
public class WeChatPhotoMessage implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "ToUserName")
	private String toUserName;
	@XmlElement(name = "FromUserName")
	private String fromUserName;
	@XmlElement(name = "CreateTime")
	private long createTime;
	@XmlElement(name = "MsgType")
	private String msgType;
	@XmlElement(name = "MediaId")
	private String MediaId;
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
	public String getMediaId() {
		return MediaId;
	}
	public void setMediaId(String mediaId) {
		MediaId = mediaId;
	}
	@Override
	public String toString() {
		return "WeChatPhtoMessage [toUserName=" + toUserName + ", fromUserName=" + fromUserName + ", createTime="
				+ createTime + ", msgType=" + msgType + ", MediaId=" + MediaId + "]";
	}
	 
	
}
