package com.ausland.weixin.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MessageContent {
   private String content;

public String getContent() {
	return content;
}

public void setContent(String content) {
	this.content = content;
}

@Override
public String toString() {
	return "MessageContent [content=" + content + "]";
}
   
}
