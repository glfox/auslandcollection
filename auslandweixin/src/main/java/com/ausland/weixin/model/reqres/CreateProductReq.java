package com.ausland.weixin.model.reqres;

 
import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

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
	String status;
	String comments;
	String sizes;
	String colors;
	
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
				+ ", category=" + category + ", productWeight=" + productWeight + ", status=" + status + ", comments="
				+ comments + ", sizes=" + sizes + ", colors=" + colors + "]";
	}
	
  
}
