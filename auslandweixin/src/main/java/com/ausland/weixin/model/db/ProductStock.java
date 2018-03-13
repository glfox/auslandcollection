package com.ausland.weixin.model.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="productstock")
public class ProductStock {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    Integer id;
    
    @Column(length = 128)	
    String productId;
    
    @Column(length = 128)	
    String productName;

    
    @Column(length = 64)
    String size;
        
    @Column(length = 64)	
    String color;
    
    @Column(length = 64)
    String stockStatus;
        
    @Column(length = 64)	
    String stockDetails;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getStockStatus() {
		return stockStatus;
	}

	public void setStockStatus(String stockStatus) {
		this.stockStatus = stockStatus;
	}

	public String getStockDetails() {
		return stockDetails;
	}

	public void setStockDetails(String stockDetails) {
		this.stockDetails = stockDetails;
	}

	@Override
	public String toString() {
		return "ProductStock [id=" + id + ", productId=" + productId + ", productName=" + productName + ", size=" + size
				+ ", color=" + color + ", stockStatus=" + stockStatus + ", stockDetails=" + stockDetails + "]";
	}
   

}
