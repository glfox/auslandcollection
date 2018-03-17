package com.ausland.weixin.util;

import java.io.Serializable;

public class CustomCookie implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Integer userId;
	String userName;
	String role;
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
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}

	public String toString()
	{
		String[] inputs=new String[3];
		inputs[0] = this.userId.toString();
		inputs[1] = this.userName;
		inputs[2] = this.role;
		return String.join(",", inputs);
	}
}
