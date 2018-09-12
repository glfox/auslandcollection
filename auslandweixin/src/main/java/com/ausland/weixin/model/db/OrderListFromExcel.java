package com.ausland.weixin.model.db;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="excelorder")
public class OrderListFromExcel implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @Column(name="orderid", length = 64)	
    String Id;
    
    @Column(name="orderno", length = 32)	
    String orderNo;

	@Column(name="logisticno", length = 32)	
    String logisticNo;
	
    @Column(name="logisticcompany", length = 64)	
    String logisticCompany;

    @Column(name="items", length = 1024)	
    String ProductItems;
    
    @Column(name="rname", length = 64)	
    String receiverName;
    
    @Column(name="rphone", length = 64)	
    String receiverPhone;  

    @Column(name="status", length = 64)
    String status;
    
    @Column(name="errormsg", length = 64)
    String errorMsg;
    
    @Column(name="updatedtime")	
    @Temporal(TemporalType.TIMESTAMP)
    Date   lastupdatedDateTime;

    @Column(name="createdtime")	
    String   createdDateTime;
    
	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getLogisticNo() {
		return logisticNo;
	}

	public void setLogisticNo(String logisticNo) {
		this.logisticNo = logisticNo;
	}

	public String getProductItems() {
		return ProductItems;
	}

	public void setProductItems(String productItems) {
		ProductItems = productItems;
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

	public String getCreatedDateTime() {
		return createdDateTime;
	}

	public void setCreatedDateTime(String createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

	public String getLogisticCompany() {
		return logisticCompany;
	}

	public void setLogisticCompany(String logisticCompany) {
		this.logisticCompany = logisticCompany;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getLastupdatedDateTime() {
		return lastupdatedDateTime;
	}

	public void setLastupdatedDateTime(Date lastupdatedDateTime) {
		this.lastupdatedDateTime = lastupdatedDateTime;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	@Override
	public String toString() {
		return "OrderListFromExcel [Id=" + Id + ", orderNo=" + orderNo + ", logisticNo=" + logisticNo
				+ ", logisticCompany=" + logisticCompany + ", ProductItems=" + ProductItems + ", receiverName="
				+ receiverName + ", receiverPhone=" + receiverPhone + ", status=" + status + ", errorMsg=" + errorMsg
				+ ", lastupdatedDateTime=" + lastupdatedDateTime + ", createdDateTime=" + createdDateTime + "]";
	}

}
