package com.ausland.weixin.model.reqres;

 
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OrderDetailsRes implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	String orderId;
	
	String createdBy;
	
    Date createdDateTime;
	
	String status;
	
	String orderTotalPrice;
	
	List<PurchaseItemRes> pucharseItemList;
	
	String comments;
 
	Address receiver;
	
	Address sender;

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public List<PurchaseItemRes> getPucharseItemList() {
		return pucharseItemList;
	}

	public void setPucharseItemList(List<PurchaseItemRes> pucharseItemList) {
		this.pucharseItemList = pucharseItemList;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Address getReceiver() {
		return receiver;
	}

	public void setReceiver(Address receiver) {
		this.receiver = receiver;
	}

	public Address getSender() {
		return sender;
	}

	public void setSender(Address sender) {
		this.sender = sender;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Date getCreatedDateTime() {
		return createdDateTime;
	}

	public void setCreatedDateTime(Date createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOrderTotalPrice() {
		return orderTotalPrice;
	}

	public void setOrderTotalPrice(String orderTotalPrice) {
		this.orderTotalPrice = orderTotalPrice;
	}

	@Override
	public String toString() {
		return "CreateOrderReq [createdBy=" + createdBy + ", pucharseItemList=" + pucharseItemList + ", comments="
				+ comments + ", receiver=" + receiver + ", sender=" + sender + "]";
	}
	
  
}
