package com.ausland.weixin.model.difou;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StockDataInfo implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty("goodsid")
	String goodsId;
	
	@JsonProperty("goodsno")	
	String goodsNo; //货品编号 
	
	@JsonProperty("specid")
	String specId;
	
	@JsonProperty("specname")
	String specName;
	
	@JsonProperty("stock")	
    String stock;
	
	public String getGoodsId() {
		return goodsId;
	}
	public String getSpecName() {
		return specName;
	}
	public void setSpecName(String specName) {
		this.specName = specName;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public String getGoodsNo() {
		return goodsNo;
	}
	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}
	public String getSpecId() {
		return specId;
	}
	public void setSpecId(String specId) {
		this.specId = specId;
	}
	public String getStock() {
		return stock;
	}
	public void setStock(String stock) {
		this.stock = stock;
	}
	
}
