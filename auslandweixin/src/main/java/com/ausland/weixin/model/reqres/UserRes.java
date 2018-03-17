package com.ausland.weixin.model.reqres;

import java.io.Serializable;
import java.util.List;

import com.ausland.weixin.model.db.ContactInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserRes  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Integer userId;
	
	@JsonProperty("username")	
	String userName;

    @JsonProperty("phone")
	String phoneNumber;
    
	String role;	
	
	String email; 
	
	String status;

    List<ContactInfo> contacts;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
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

	public List<ContactInfo> getContacts() {
		return contacts;
	}

	public void setContacts(List<ContactInfo> contacts) {
		this.contacts = contacts;
	}

	@Override
	public String toString() {
		return "UserRes [userId=" + userId + ", userName=" + userName + ", phoneNumber=" + phoneNumber + ", role="
				+ role + ", email=" + email + ", status=" + status + ", contacts=" + contacts + "]";
	}
    
}
