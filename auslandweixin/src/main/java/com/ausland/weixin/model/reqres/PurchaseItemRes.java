package com.ausland.weixin.model.reqres;

 
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PurchaseItemRes implements Serializable
{
	private static final long serialVersionUID = 1L;
	
    String productId;
	
	String size;
	
	String color;
	
	Integer quantity;
	
	String unitPrice;
	
	String comments;
	
	String status;
	
	String logisticTrackingNo; 
	
	String requireCheckDetails;

	public String getRequireCheckDetails() {
		return requireCheckDetails;
	}

	public void setRequireCheckDetails(String requireCheckDetails) {
		this.requireCheckDetails = requireCheckDetails;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(String unitPrice) {
		this.unitPrice = unitPrice;
	}

	public String getLogisticTrackingNo() {
		return logisticTrackingNo;
	}

	public void setLogisticTrackingNo(String logisticTrackingNo) {
		this.logisticTrackingNo = logisticTrackingNo;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
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

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
 
  
}
