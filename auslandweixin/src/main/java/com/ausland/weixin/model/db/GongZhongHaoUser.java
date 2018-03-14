package com.ausland.weixin.model.db;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="gongzhonghaosubscriberuser")
public class GongZhongHaoUser {

	@Column 
	private Integer subscribe; //0: not subscribed, 1: subscribed
	
	@Id
	@Column(name="openid", length=64)
	private String openid;
	
	@Column 
	private int sex;   //0: unknown 1: man 2: woman
	
	@Column(length=64)
	private String city;
	
	@Column(length=64)
	private String country;
	
	@Column(length=64)
	private String province;
	
	@Column(length=64)
	private String language;
	
	@Column(length=512)
	private String headimagurl;
	
	@Column(length=64)
	private String subscribe_time;
	
	@Column(length=256)
	private String unionid;
	
	@Column(length=256)
	private String remark;
	
	@Column(length=256)
	private String groupid;
	
	@Column(length=256)
	private String tagid_list;
	
	@Column 
	@Temporal(TemporalType.TIMESTAMP)
	private Date   createdDateTime;
	
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date   lastupdatedDateTime;
	
	@Column(length=64)
	private String reserved;
	 
	
	public Integer getSubscribe() {
		return subscribe;
	}
	public void setSubscribe(Integer subscribe) {
		this.subscribe = subscribe;
	}
	public Date getCreatedDateTime() {
		return createdDateTime;
	}
	public void setCreatedDateTime(Date createdDateTime) {
		this.createdDateTime = createdDateTime;
	}
	public Date getLastupdatedDateTime() {
		return lastupdatedDateTime;
	}
	public void setLastupdatedDateTime(Date lastupdatedDateTime) {
		this.lastupdatedDateTime = lastupdatedDateTime;
	}
	public String getReserved() {
		return reserved;
	}
	public void setReserved(String reserved) {
		this.reserved = reserved;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getHeadimagurl() {
		return headimagurl;
	}
	public void setHeadimagurl(String headimagurl) {
		this.headimagurl = headimagurl;
	}
	public String getSubscribe_time() {
		return subscribe_time;
	}
	public void setSubscribe_time(String subscribe_time) {
		this.subscribe_time = subscribe_time;
	}
	public String getUnionid() {
		return unionid;
	}
	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getGroupid() {
		return groupid;
	}
	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}
	public String getTagid_list() {
		return tagid_list;
	}
	public void setTagid_list(String tagid_list) {
		this.tagid_list = tagid_list;
	}
	
	
}
