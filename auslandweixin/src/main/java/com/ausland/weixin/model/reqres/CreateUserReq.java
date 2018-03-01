package com.ausland.weixin.model.reqres;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CreateUserReq {
    
    @JsonProperty("username")	
	String userName;
    @JsonProperty("password")
	String password;
    @JsonProperty("phone")
	String phoneNumber;
    @JsonProperty("src") 
	String createdSrc;
	String role;	 
	String email; 
	String status;
	@Column(name="wechatid")
	String wechatId;
	@Column(name="wechatopenid")
	String wechatOpenId;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getCreatedSrc() {
		return createdSrc;
	}
	public void setCreatedSrc(String createdSrc) {
		this.createdSrc = createdSrc;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getWechatId() {
		return wechatId;
	}
	public void setWechatId(String wechatId) {
		this.wechatId = wechatId;
	}
	public String getWechatOpenId() {
		return wechatOpenId;
	}
	public void setWechatOpenId(String wechatOpenId) {
		this.wechatOpenId = wechatOpenId;
	}
 
	
}
