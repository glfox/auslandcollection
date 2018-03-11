package com.ausland.weixin.model.reqres;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class GongZhongHaoUserInfoRes  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int subscribe;
	private String openid;
	private int sex;   //0: unknown 1: man 2: woman
	private String city;
	private String country;
	private String province;
	private String language;
	private String headimagurl;
	private String subscribe_time;
	private String unionid;
	private String remark;
	private int groupid;
	private int[] tagid_list;
	private String nickname;
	private String subscribe_scene;
	private int qr_scene;
	private String qr_scene_str;
	
	public int getSubscribe() {
		return subscribe;
	}
	public void setSubscribe(int subscribe) {
		this.subscribe = subscribe;
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
	public int getGroupid() {
		return groupid;
	}
	public void setGroupid(int groupid) {
		this.groupid = groupid;
	}
	public int[] getTagid_list() {
		return tagid_list;
	}
	public void setTagid_list(int[] tagid_list) {
		this.tagid_list = tagid_list;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getSubscribe_scene() {
		return subscribe_scene;
	}
	public void setSubscribe_scene(String subscribe_scene) {
		this.subscribe_scene = subscribe_scene;
	}
	public int getQr_scene() {
		return qr_scene;
	}
	public void setQr_scene(int qr_scene) {
		this.qr_scene = qr_scene;
	}
	public String getQr_scene_str() {
		return qr_scene_str;
	}
	public void setQr_scene_str(String qr_scene_str) {
		this.qr_scene_str = qr_scene_str;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
}
