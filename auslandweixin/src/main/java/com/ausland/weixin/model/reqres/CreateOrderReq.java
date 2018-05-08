package com.ausland.weixin.model.reqres;

 
import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CreateOrderReq implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	String createdBy;
	
	List<PurchaseItemReq> pucharseItemList;
	
	String comments;
 
	Address receiver;
	
	Address sender;

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public List<PurchaseItemReq> getPucharseItemList() {
		return pucharseItemList;
	}

	public void setPucharseItemList(List<PurchaseItemReq> pucharseItemList) {
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

	@Override
	public String toString() {
		return "CreateOrderReq [createdBy=" + createdBy + ", pucharseItemList=" + pucharseItemList + ", comments="
				+ comments + ", receiver=" + receiver + ", sender=" + sender + "]";
	}
	
  
}
