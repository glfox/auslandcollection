package com.ausland.weixin.model.db;

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
    
	@Column(name="weight", length = 64)	
    String packageWeight;
	
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
    String   createdDateTime;
    
    @Column(name="createdsrc", length = 64)	
    String createdSrc;
    
    @Column(name="linkedno", length = 64)	
    String linkedOrderNo;
    
    @Column(name="logisticcompany", length = 64)	
    String logisticCompany;
    
    @Column(name="comments", length = 1024)
    String comments;
    
    @Column(name="validationerrors", length = 2048)
    String validationErrors;
    
    @Column(length = 64)
    String status;
    
    @Column(name="updatedby", length = 64)	
    String updatedBy;
    
    @Column(name="updatedtime")	
    String   lastupdatedDateTime;
    
    
	public String getValidationErrors() {
		return validationErrors;
	}
	public void setValidationErrors(String validationErrors) {
		this.validationErrors = validationErrors;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	 
	public String getPackageWeight() {
		return packageWeight;
	}
	public void setPackageWeight(String packageWeight) {
		this.packageWeight = packageWeight;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public String getLastupdatedDateTime() {
		return lastupdatedDateTime;
	}
	public void setLastupdatedDateTime(String lastupdatedDateTime) {
		this.lastupdatedDateTime = lastupdatedDateTime;
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
	public String getCreatedDateTime() {
		return createdDateTime;
	}
	public void setCreatedDateTime(String createdDateTime) {
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
		return "LogisticPackage [logisticTrackingNo=" + logisticTrackingNo + ", packageWeight=" + packageWeight
				+ ", ProductItems=" + ProductItems + ", receiverName=" + receiverName + ", receiverPhone="
				+ receiverPhone + ", receiverBackupPhone=" + receiverBackupPhone + ", receiverAddress="
				+ receiverAddress + ", senderName=" + senderName + ", senderPhone=" + senderPhone + ", senderAddress="
				+ senderAddress + ", createdBy=" + createdBy + ", createdDateTime=" + createdDateTime + ", createdSrc="
				+ createdSrc + ", linkedOrderNo=" + linkedOrderNo + ", logisticCompany=" + logisticCompany
				+ ", comments=" + comments + ", status=" + status + ", getStatus()=" + getStatus()
				+ ", getPackageWeight()=" + getPackageWeight() + ", getLogisticTrackingNo()=" + getLogisticTrackingNo()
				+ ", getProductItems()=" + getProductItems() + ", getReceiverName()=" + getReceiverName()
				+ ", getReceiverPhone()=" + getReceiverPhone() + ", getReceiverBackupPhone()="
				+ getReceiverBackupPhone() + ", getReceiverAddress()=" + getReceiverAddress() + ", getSenderName()="
				+ getSenderName() + ", getSenderPhone()=" + getSenderPhone() + ", getSenderAddress()="
				+ getSenderAddress() + ", getCreatedBy()=" + getCreatedBy() + ", getCreatedDateTime()="
				+ getCreatedDateTime() + ", getCreatedSrc()=" + getCreatedSrc() + ", getLinkedOrderNo()="
				+ getLinkedOrderNo() + ", getLogisticCompany()=" + getLogisticCompany() + ", getComments()="
				+ getComments() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}
 
}
