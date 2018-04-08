package com.ausland.weixin.model.db;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="customer_order")
public class Order {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
    int id;

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
    
    @Column(precision=5, scale=2)
    @NotNull
	BigDecimal orderTotalPrice = new BigDecimal(0.0);
    
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    Date   lastupdatedDateTime;

    @OneToMany(mappedBy="customer_order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	List<PurchaseItem> purchaseItems;
	
	public BigDecimal getOrderTotalPrice() {
		return orderTotalPrice;
	}

	public void setOrderTotalPrice(BigDecimal orderTotalPrice) {
		this.orderTotalPrice = orderTotalPrice;
	}

	@Column(name="rname", nullable = false, length = 64)	
	String receiverName;
	
	@Column(name="rphone", nullable =false, length = 64)	
	String receiverPhone;
	
	@Column(name="raddress", nullable = false, length = 1024)	
	String receiverAddress;
	
	@Column(name="sname", length = 64)	
	String senderName;
	
	@Column(name="sphone", length = 64)	
	String senderPhone;
	
	@Column(name="saddress", length = 1024)	
	String senderAddress;
	
	 
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public List<PurchaseItem> getPurchaseItems() {
		return purchaseItems;
	}

	public void setPurchaseItems(List<PurchaseItem> purchaseItems) {
		this.purchaseItems = purchaseItems;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getReceiverPhone() {
		return receiverPhone;
	}

	public void setReceiverPhone(String receiverPhone) {
		this.receiverPhone = receiverPhone;
	}

	public String getReceiverAddress() {
		return receiverAddress;
	}

	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getSenderPhone() {
		return senderPhone;
	}

	public void setSenderPhone(String senderPhone) {
		this.senderPhone = senderPhone;
	}

	public String getSenderAddress() {
		return senderAddress;
	}

	public void setSenderAddress(String senderAddress) {
		this.senderAddress = senderAddress;
	}

	
}
