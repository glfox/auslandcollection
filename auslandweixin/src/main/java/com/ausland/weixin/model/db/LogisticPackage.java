package com.ausland.weixin.model.db;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="LogisticPackage")
public class LogisticPackage {

    @Id
    @Column(name="trackno", length = 128)	
    String logisticTrackingNo;
    
	@Column(name="weight")	
    double packageWeight;
	
    @Column(name="items", length = 1024)	
    String ProductItems;
    @Column(name="rname", nullable = false, length = 64)	
    String receiverName;
    @Column(name="rphone", nullable =false, length = 64)	
    String receiverPhone;
    @Column(name="rbphone", length = 64)	
    String receiverBackupPhone;
    @Column(name="raddress", nullable = false, length = 1024)	
    String receiverAddress;
    @Column(name="sname", length = 64)	
    String senderName;
    @Column(name="sphone", length = 64)	
    String senderPhone;
    @Column(name="saddress", length = 1024)	
    String senderAddress;
    @Column(name="createdby", length = 64)	
    String createdBy;
    @Column(name="createdtime", nullable = false)	
    Date   createdDateTime;
    @Column(name="createdsrc", length = 64)	
    String createdSrc;
    @Column(name="linkedno", length = 64)	
    String linkedOrderNo;
    @Column(name="logisticcompany", length = 64)	
    String logisticCompany;
    @Column(name="comments", length = 1024)
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
