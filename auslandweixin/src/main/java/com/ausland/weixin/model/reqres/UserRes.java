package com.ausland.weixin.model.reqres;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

import javax.persistence.Column;

import com.ausland.weixin.model.db.ContactInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserRes  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String id;
	
	@JsonProperty("username")	
	String userName;
	
	@Column(name="createdtime")
	Date createdDateTime;
	
	@Column(name="updatedtime")
	Date lastUpdatedDateTime;
	
    List<ContactInfo> contacts;
    
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
	
	
}
