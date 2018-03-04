package com.ausland.weixin.model.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="user")
public class User {

	@Id
	@Column(name="id")	
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	Integer id;
	
	@Column(name="username", nullable = false, length = 128)	
	String username;
	@Column(name="password", nullable = false, unique = true, length = 64)
	String password;
	@Column(name="phoneno", nullable = false, unique = true, length = 64)	
	String phoneNumber;
	@Column(name="createdsrc")	
	String createdSrc;
	@Column(name="role",length = 32)
	String role;
	@Column(name="email", length = 64)
	String email;
	@Column(name="status",length = 32)
	String status;
	@Column(name="wechatid",length = 256)
	String wechatId;
	@Column(name="wechatopenid",length = 256)
	String wechatOpenId;
	@Column(name="createdtime")
	String createdDateTime;
	@Column(name="updatedtime")
	String lastUpdatedDateTime;
    	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	 
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
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
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
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
	
	public String getCreatedDateTime() {
		return createdDateTime;
	}
	
	public void setCreatedDateTime(String createdDateTime) {
		this.createdDateTime = createdDateTime;
	}
	
	public String getLastUpdatedDateTime() {
		return lastUpdatedDateTime;
	}
	
	public void setLastUpdatedDateTime(String lastUpdatedDateTime) {
		this.lastUpdatedDateTime = lastUpdatedDateTime;
	}
	 
	
}
