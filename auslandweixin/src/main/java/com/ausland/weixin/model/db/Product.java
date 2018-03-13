package com.ausland.weixin.model.db;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="product")
public class Product {

    @Id
	@Column(length = 128)	
    String productId;
    
    @Column(length = 128)	
    String productName;
    
    @Column(length = 64)
    @NotNull
    String brand;
    
    @Column(length = 256)
    String productMainImageUrl;
    
    @Column(length = 10240)
    String productSmallImage;
    
    @Column(length = 256)
    String productMainVideoUrl;
    
    @Column(length = 64)	
    @NotNull
    String productCategory;
    
	@Column(length = 64)	
    String productWeight;
    
    @Column(length = 64)	
    String createdBy;
    
    @Column(nullable = false)	
    @Temporal(TemporalType.TIMESTAMP)
    Date   createdDateTime;
    
    @Column(length = 64)	
    String createdSrc;
    
    @Column(length = 1024)
    String comments;
    
    @Column(length = 64)
    String status;
        
    @Column(length = 64)	
    String updatedBy;
    
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    Date   lastupdatedDateTime;

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

	public String getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}

	public String getProductWeight() {
		return productWeight;
	}

	public void setProductWeight(String productWeight) {
		this.productWeight = productWeight;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDateTime() {
		return createdDateTime;
	}

	public void setCreatedDateTime(Date createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

	public String getCreatedSrc() {
		return createdSrc;
	}

	public void setCreatedSrc(String createdSrc) {
		this.createdSrc = createdSrc;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getLastupdatedDateTime() {
		return lastupdatedDateTime;
	}

	public void setLastupdatedDateTime(Date lastupdatedDateTime) {
		this.lastupdatedDateTime = lastupdatedDateTime;
	}

	@Override
	public String toString() {
		return "Product [productId=" + productId + ", productName=" + productName + ", brand=" + brand
				+ ", productMainImageUrl=" + productMainImageUrl 
				+ ", productMainVideoUrl=" + productMainVideoUrl + ", productCategory=" + productCategory
				+ ", productWeight=" + productWeight + ", createdBy=" + createdBy + ", createdDateTime="
				+ createdDateTime + ", createdSrc=" + createdSrc + ", comments=" + comments + ", status=" + status
				+ ", updatedBy=" + updatedBy + ", lastupdatedDateTime=" + lastupdatedDateTime + "]";
	}

}
