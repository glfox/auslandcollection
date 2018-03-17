package com.ausland.weixin.model.db;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="contact")
public class ContactInfo {

	@Id
	@Column(name="id")	
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	Integer contactInfoId;
  	
	@Column(name="address",nullable = false, length = 1024)	
	String address;
	
	@Column(nullable = false, length = 64)
    String name;
    
	@Column(nullable = false, name="phoneno",length = 64)	
	String phoneNumber;
	
	@Column(name="backupphoneno",length = 64)	
	String backupPhoneNumber;
	
	@Column(name="createdtime")
	@Temporal(TemporalType.TIMESTAMP)
	Date createdDateTime;
	
	@Column(length = 1024)	
	String comments;
	
	@Column
	Boolean defaultAddress;
 
	public Integer getContactInfoId() {
		return contactInfoId;
	}

	public void setContactInfoId(Integer contactInfoId) {
		this.contactInfoId = contactInfoId;
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
 
}
