package com.ausland.weixin.model.db;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="purchaseitem")
public class PurchaseItem {

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    int id;
	
	@ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	@JoinColumn(name = "customer_order_id")
    Order customer_order;
    
    @Column(length = 128)	
    @NotNull
    String productId;
    
    @Column(length = 64)
    String productProvider;
    
    @Column(length = 64)
    String color;
    
    @Column(length = 32)
    String size;
    
    @Column(precision=5, scale=2)
    @NotNull
	BigDecimal productUnitPrice = new BigDecimal(0.0);
    
    @Column(name="trackno", length = 128)	
    String logisticTrackingNo;
    
    @Column
    Integer quantity;
    
    @Column(length = 64)	
    String createdSrc;
    
    @Column(length = 1024)
    String comments;
    
    @Column(length = 1024)
    String checkReason;
    
    @Column(length = 64)
    String status;
        
    @Column(length = 64)	
    String updatedBy;
    
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    Date   lastupdatedDateTime;

 /*
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
*/
	public String getCheckReason() {
		return checkReason;
	}

	public void setCheckReason(String checkReason) {
		this.checkReason = checkReason;
	}

	public String getProductProvider() {
		return productProvider;
	}

	public void setProductProvider(String productProvider) {
		this.productProvider = productProvider;
	}

	public String getLogisticTrackingNo() {
		return logisticTrackingNo;
	}

	public void setLogisticTrackingNo(String logisticTrackingNo) {
		this.logisticTrackingNo = logisticTrackingNo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
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

	public BigDecimal getProductUnitPrice() {
		return productUnitPrice;
	}

	public void setProductUnitPrice(BigDecimal productUnitPrice) {
		this.productUnitPrice = productUnitPrice;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
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

	public Order getCustomer_order() {
		return customer_order;
	}

	public void setCustomer_order(Order customer_order) {
		this.customer_order = customer_order;
	}

}
