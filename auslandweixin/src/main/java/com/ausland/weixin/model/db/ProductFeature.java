package com.ausland.weixin.model.db;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="productfeature")
public class ProductFeature {

    @EmbeddedId	
    ProductFeatureIdentity productFeatureIdentity;
    
    @Column(name="comments", length = 1024)
    String comments;
    
    @Column(length = 64)
    String status;
 
    @Column(length = 64)
    String stockStatus;

	public ProductFeatureIdentity getProductFeatureIdentity() {
		return productFeatureIdentity;
	}

	public void setProductFeatureIdentity(ProductFeatureIdentity productFeatureIdentity) {
		this.productFeatureIdentity = productFeatureIdentity;
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

	public String getStockStatus() {
		return stockStatus;
	}

	public void setStockStatus(String stockStatus) {
		this.stockStatus = stockStatus;
	}
    
    
}
