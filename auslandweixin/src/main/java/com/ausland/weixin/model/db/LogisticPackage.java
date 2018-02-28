package com.ausland.weixin.model.db;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="LogisticPackage")
public class LogisticPackage {
	@Column(name="weight")	
    double packageWeight;
    @Id
    @Column(name="trackno")	
    String logisticTrackingNo;
    @Column(name="items")	
    String ProductItems;
    @Column(name="rname")	
    String receiverName;
    @Column(name="rphone")	
    String receiverPhone;
    @Column(name="rbphone")	
    String receiverBackupPhone;
    @Column(name="raddress")	
    String receiverAddress;
    @Column(name="sname")	
    String senderName;
    @Column(name="sphone")	
    String senderPhone;
    @Column(name="saddress")	
    String senderAddress;
    @Column(name="createdby")	
    String createdBy;
    @Column(name="createdtime")	
    Date   createdDateTime;
    @Column(name="createdsrc")	
    String createdSrc;
    @Column(name="linkedno")	
    String linkedOrderNo;
    @Column(name="logisticcompany")	
    String logisticCompany;
    @Column(name="comments")
    String comments;
	public double getPackageWeight() {
		return packageWeight;
	}
	public void setPackageWeight(double packageWeight) {
		this.packageWeight = packageWeight;
	}
	public String getLogisticTrackingNo() {
		return logisticTrackingNo;
	}
	public void setLogisticTrackingNo(String logisticTrackingNo) {
		this.logisticTrackingNo = logisticTrackingNo;
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
	public String getReceiverBackupPhone() {
		return receiverBackupPhone;
	}
	public void setReceiverBackupPhone(String receiverBackupPhone) {
		this.receiverBackupPhone = receiverBackupPhone;
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
	public String getLinkedOrderNo() {
		return linkedOrderNo;
	}
	public void setLinkedOrderNo(String linkedOrderNo) {
		this.linkedOrderNo = linkedOrderNo;
	}
	public String getLogisticCompany() {
		return logisticCompany;
	}
	public void setLogisticCompany(String logisticCompany) {
		this.logisticCompany = logisticCompany;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	@Override
	public String toString() {
		return "LogisticPackage [packageWeight=" + packageWeight + ", logisticTrackingNo=" + logisticTrackingNo
				+ ", ProductItems=" + ProductItems + ", receiverName=" + receiverName + ", receiverPhone="
				+ receiverPhone + ", receiverBackupPhone=" + receiverBackupPhone + ", receiverAddress="
				+ receiverAddress + ", senderName=" + senderName + ", senderPhone=" + senderPhone + ", senderAddress="
				+ senderAddress + ", createdBy=" + createdBy + ", createdDateTime=" + createdDateTime + ", createdSrc="
				+ createdSrc + ", linkedOrderNo=" + linkedOrderNo + ", logisticCompany=" + logisticCompany
				+ ", comments=" + comments + "]";
	}
    
}
