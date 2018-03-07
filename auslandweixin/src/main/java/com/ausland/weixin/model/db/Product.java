package com.ausland.weixin.model.db;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="product")
public class Product {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    Integer id;
    
    @Column(name="productId", length = 128)	
    String productId;
    
    @Column(name="productName", length = 512)	
    String productName;
    
    @Column(length = 64)	
    String brand;
    
    @Column(length = 256)
    String productMainImageUrl;
    
    @Column(length = 256)
    String productImageUrl1;
    
    @Column(length = 256)
    String productImageUrl2;
    
    @Column(length = 256)
    String productImageUrl3;
    
    @Column(length = 256)
    String productImageUrl4;
    
    @Column(length = 256)
    String productImageUrl5;
    
    @Column(length = 256)
    String productMainVideoUrl;
    
    @Column(length = 64)	
    String productCategory;
    
	@Column(name="weight", length = 64)	
    String productWeight;
    
    @Column(name="createdby", length = 64)	
    String createdBy;
    
    @Column(name="createdtime", nullable = false)	
    @Temporal(TemporalType.TIMESTAMP)
    Date   createdDateTime;
    
    @Column(name="createdsrc", length = 64)	
    String createdSrc;
    
    @Column(name="comments", length = 1024)
    String comments;
    
    @Column(length = 64)
    String status;
        
    @Column(name="updatedby", length = 64)	
    String updatedBy;
    
    @Column(name="updatedtime")	
    @Temporal(TemporalType.TIMESTAMP)
    Date   lastupdatedDateTime;
    
    @Column(length = 32)	
    String size;
    
	@Column(length = 32)
    String color;
	
	@Column(length = 64)
    String feature1;
	
	@Column(length = 64)
    String feature2;
	
	@Column(length = 32)
    String stockStatus;
	
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

	public String getProductImageUrl1() {
		return productImageUrl1;
	}

	public void setProductImageUrl1(String productImageUrl1) {
		this.productImageUrl1 = productImageUrl1;
	}

	public String getProductImageUrl2() {
		return productImageUrl2;
	}

	public void setProductImageUrl2(String productImageUrl2) {
		this.productImageUrl2 = productImageUrl2;
	}

	public String getProductImageUrl3() {
		return productImageUrl3;
	}

	public void setProductImageUrl3(String productImageUrl3) {
		this.productImageUrl3 = productImageUrl3;
	}

	public String getProductImageUrl4() {
		return productImageUrl4;
	}

	public void setProductImageUrl4(String productImageUrl4) {
		this.productImageUrl4 = productImageUrl4;
	}

	public String getProductImageUrl5() {
		return productImageUrl5;
	}

	public void setProductImageUrl5(String productImageUrl5) {
		this.productImageUrl5 = productImageUrl5;
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}


	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
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

	public String getStockStatus() {
		return stockStatus;
	}

	public void setStockStatus(String stockStatus) {
		this.stockStatus = stockStatus;
	}

}
