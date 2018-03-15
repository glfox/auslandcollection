package com.ausland.weixin.model.reqres;

 
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CreateProductReq implements Serializable
{
	private static final long serialVersionUID = 1L;
	String productId;
	String productName;
	String brand;
	String category;
	String productWeight;
	String smallImageBase64EncodeString;
	String status;
	String comments;
	String sizeCategory;
	String sizes;
	String colors;
	
	public String getSizeCategory() {
		return sizeCategory;
	}
	public void setSizeCategory(String sizeCategory) {
		this.sizeCategory = sizeCategory;
	}
	public String getSmallImageBase64EncodeString() {
		return smallImageBase64EncodeString;
	}
	public void setSmallImageBase64EncodeString(String smallImageBase64EncodeString) {
		this.smallImageBase64EncodeString = smallImageBase64EncodeString;
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
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	
	public String getProductWeight() {
		return productWeight;
	}
	public void setProductWeight(String productWeight) {
		this.productWeight = productWeight;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getSizes() {
		return sizes;
	}
	public void setSizes(String sizes) {
		this.sizes = sizes;
	}
	public String getColors() {
		return colors;
	}
	public void setColors(String colors) {
		this.colors = colors;
	}
	@Override
	public String toString() {
		return "CreateProductReq [productId=" + productId + ", productName=" + productName + ", brand=" + brand
				+ ", category=" + category + ", productWeight=" + productWeight + ", smallImageBase64EncodeString="
				+ smallImageBase64EncodeString + ", status=" + status + ", comments=" + comments + ", sizeCategory="
				+ sizeCategory + ", sizes=" + sizes + ", colors=" + colors + "]";
	}	
  
}
