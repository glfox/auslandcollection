package com.ausland.weixin.model.reqres;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class StockInfo  implements Serializable{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String productId;
    String productName;
    String brandName;
    String color;
    String size;
    String feature1;
    String feature2;
    String stockStatus;
 
	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getFeature1() {
		return feature1;
	}

	public void setFeature1(String feature1) {
		this.feature1 = feature1;
	}

	public String getFeature2() {
		return feature2;
	}

	public void setFeature2(String feature2) {
		this.feature2 = feature2;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	 
	public String getStockStatus() {
		return stockStatus;
	}

	public void setStockStatus(String stockStatus) {
		this.stockStatus = stockStatus;
	}
 
    
}
