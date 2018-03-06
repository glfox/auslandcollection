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
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "product_id")
    List<SubProduct> SubProduct;
    
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
	public void setLastupdatedDateTime(Date lastupdatedDateTime) {
		this.lastupdatedDateTime = lastupdatedDateTime;
	}
	@Override
	public String toString() {
		return "Product [productId=" + productId + ", productName=" + productName + ", productWeight=" + productWeight
				+ ", createdBy=" + createdBy + ", createdDateTime=" + createdDateTime + ", createdSrc=" + createdSrc
				+ ", comments=" + comments + ", status=" + status + ", updatedBy=" + updatedBy
				+ ", lastupdatedDateTime=" + lastupdatedDateTime + "]";
	}

 
}
