package com.ausland.weixin.model.reqres;

 
import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProductRes implements Serializable
{
	private static final long serialVersionUID = 1L;
	String productId;
	String productName;
	String brand;
	String category;
	String productMainImageUrl;
	String productSmallImage;
	String productMainVideoUrl;
	String productWeight;
	String status;
	String comments;
	String sizeCategory;
	List<StockInfo> stock;
	
	public String getSizeCategory() {
		return sizeCategory;
	}
	public void setSizeCategory(String sizeCategory) {
		this.sizeCategory = sizeCategory;
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
	public String getProductMainImageUrl() {
		return productMainImageUrl;
	}
	public void setProductMainImageUrl(String productMainImageUrl) {
		this.productMainImageUrl = productMainImageUrl;
	}
	public String getProductSmallImage() {
		return productSmallImage;
	}
	public void setProductSmallImage(String productSmallImage) {
		this.productSmallImage = productSmallImage;
	}
	public String getProductMainVideoUrl() {
		return productMainVideoUrl;
	}
	public void setProductMainVideoUrl(String productMainVideoUrl) {
		this.productMainVideoUrl = productMainVideoUrl;
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
	public List<StockInfo> getStock() {
		return stock;
	}
	public void setStock(List<StockInfo> stock) {
		this.stock = stock;
	}
	
	@Override
	public String toString() {
		return "ProductRes [productId=" + productId + ", productName=" + productName + ", brand=" + brand
				+ ", category=" + category + ", productMainImageUrl=" + productMainImageUrl + ", productSmallImage="
				+ productSmallImage + ", productMainVideoUrl=" + productMainVideoUrl + ", productWeight="
				+ productWeight + ", status=" + status + ", comments=" + comments + ", sizeCategory=" + sizeCategory
				+ ", stock=" + ToStringBuilder.reflectionToString(stock) + "]";
	}
 
	
}
