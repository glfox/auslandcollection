package com.ausland.weixin.model.difou;

import java.io.Serializable;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderDataInfo implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty("regtime")
	String regTime;
	
	@JsonProperty("sndto")	
	String sendTo; 
	
	@JsonProperty("phone")
	String tel;
	
	@JsonProperty("postid")
	String courierCompany;
	
	@JsonProperty("goodslist")	
	ArrayList<GoodsInfo> goodsList;

	public String getRegTime() {
		return regTime;
	}

	public void setRegTime(String regTime) {
		this.regTime = regTime;
	}

	public String getSendTo() {
		return sendTo;
	}

	public void setSendTo(String sendTo) {
		this.sendTo = sendTo;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getCourierCompany() {
		return courierCompany;
	}

	public void setCourierCompany(String courierCompany) {
		this.courierCompany = courierCompany;
	}

	public ArrayList<GoodsInfo> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(ArrayList<GoodsInfo> goodsList) {
		this.goodsList = goodsList;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
