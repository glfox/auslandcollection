package com.ausland.weixin.util;

import java.io.Serializable;

public class CustomCookie implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String password;  //here is the encrypted password
	String userName;
	String role;
	 
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
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
		inputs[0] = this.password;
		inputs[1] = this.userName;
		inputs[2] = this.role;
		return String.join(",", inputs);
	}
}
