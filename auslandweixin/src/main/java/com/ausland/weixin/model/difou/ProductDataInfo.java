package com.ausland.weixin.model.difou;

import java.io.Serializable;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductDataInfo implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty("goodsid")
	String goodsId;
	@JsonProperty("goodsno")	
	String goodsNo; //货品编号
	@JsonProperty("pricedetail")	
    String priceDetail;
	@JsonProperty("pricemember")	
    String priceMember;
	@JsonProperty("barcode")	
    String barCode;
	@JsonProperty("brand")
	String brand;
	@JsonProperty("bmultispec")	
    Boolean bMultiSpec;
	@JsonProperty("specinfo")	
    ArrayList<ProductSpecInfo> specInfo;
	public String getGoodsId() {
		return goodsId;
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
	public String getPriceDetail() {
		return priceDetail;
	}
	public void setPriceDetail(String priceDetail) {
		this.priceDetail = priceDetail;
	}
	public String getPriceMember() {
		return priceMember;
	}
	public void setPriceMember(String priceMember) {
		this.priceMember = priceMember;
	}
	public String getBarCode() {
		return barCode;
	}
	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public Boolean getbMultiSpec() {
		return bMultiSpec;
	}
	public void setbMultiSpec(Boolean bMultiSpec) {
		this.bMultiSpec = bMultiSpec;
	}
	public ArrayList<ProductSpecInfo> getSpecInfo() {
		return specInfo;
	}
	public void setSpecInfo(ArrayList<ProductSpecInfo> specInfo) {
		this.specInfo = specInfo;
	}
	
}
