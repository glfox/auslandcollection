package com.ausland.weixin.model.db;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


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
	@Temporal(TemporalType.TIMESTAMP)
	Date createdDateTime;
	
	@Column(name="updatedtime")
	@Temporal(TemporalType.TIMESTAMP)
	Date lastUpdatedDateTime;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id")
	List<ContactInfo> contactInfo;
    	
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
	
	public Date getCreatedDateTime() {
		return createdDateTime;
	}
	
	public void setCreatedDateTime(Date createdDateTime) {
		this.createdDateTime = createdDateTime;
	}
	
	public Date getLastUpdatedDateTime() {
		return lastUpdatedDateTime;
	}
	
	public void setLastUpdatedDateTime(Date lastUpdatedDateTime) {
		this.lastUpdatedDateTime = lastUpdatedDateTime;
	}
	 
	
}
