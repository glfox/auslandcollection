package com.ausland.weixin.model.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="contact")
public class ContactInfo {

	@Id
	@Column(name="id")	
	Integer contactInfoId;
    
	@Column(name="userid", nullable = false)
	Integer userId;
	
	@Column(name="address",length = 1024)	
	String address;
	
	@Column(length = 64)
    String name;
    
	@Column(name="phoneno",length = 64)	
	String phoneNumber;
	
	@Column(name="backupphoneno",length = 64)	
	String backupPhoneNumber;
 
	public Integer getContactInfoId() {
		return contactInfoId;
	}

	public void setContactInfoId(Integer contactInfoId) {
		this.contactInfoId = contactInfoId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getBackupPhoneNumber() {
		return backupPhoneNumber;
	}

	public void setBackupPhoneNumber(String backupPhoneNumber) {
		this.backupPhoneNumber = backupPhoneNumber;
	}

	@Override
	public String toString() {
		return "ContactInfo [id=" + id + ", address=" + address + ", name=" + name + ", phoneNumber=" + phoneNumber
				+ ", backupPhoneNumber=" + backupPhoneNumber + "]";
	}
	
}
