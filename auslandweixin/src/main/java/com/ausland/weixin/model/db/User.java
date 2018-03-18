package com.ausland.weixin.model.db;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.ausland.weixin.config.AuslandApplicationConstants;


@Entity
@Table(name="user")
public class User {

	@Id
	@Column(name="id")	
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	Integer id;
	
	@Column(name="username", unique = true, nullable = false, length = 64)	
	String username;
	
	@Column(name="password", nullable = false, length = 256)
	String password;
	
	@Column(length = 16)	
	String phoneNumber;
	
	@Column(length = 32)
	String createdSrc;
	
	@Column(length = 32)
	String role = AuslandApplicationConstants.STANDARD_USER_ROLE;
	
	@Column(length = 64)
	String email;
	
	@Column(length = 32)
	String status;
	
	@Column(length = 256)
	String wechatId;
	
	@Column(length = 256)
	String wechatOpenId;
	
	@Column()
	@Temporal(TemporalType.TIMESTAMP)
	Date createdDateTime;
	
	@Column(name="updatedtime")
	@Temporal(TemporalType.TIMESTAMP)
	Date lastUpdatedDateTime;
	
	@Column(length = 256)
	String defaultReceiverAddress;
	
	@Column(length = 64)
	String defaultReceiverName;
	
	@Column(length = 16)
	String deafaultReceiverPhone;
	
	@Column(precision=7, scale=2)
	BigDecimal accountBalance = new BigDecimal(0.0);
  
	public String getDefaultReceiverAddress() {
		return defaultReceiverAddress;
	}
	public void setDefaultReceiverAddress(String defaultReceiverAddress) {
		this.defaultReceiverAddress = defaultReceiverAddress;
	}
	public String getDefaultReceiverName() {
		return defaultReceiverName;
	}
	public void setDefaultReceiverName(String defaultReceiverName) {
		this.defaultReceiverName = defaultReceiverName;
	}
	public String getDeafaultReceiverPhone() {
		return deafaultReceiverPhone;
	}
	public void setDeafaultReceiverPhone(String deafaultReceiverPhone) {
		this.deafaultReceiverPhone = deafaultReceiverPhone;
	}
	public BigDecimal getAccountBalance() {
		return accountBalance;
	}
	public void setAccountBalance(BigDecimal accountBalance) {
		this.accountBalance = accountBalance;
	}
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
