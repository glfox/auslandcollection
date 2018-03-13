package com.ausland.weixin.model.reqres;

 
import java.io.Serializable;
import java.util.Date;
import java.util.List;
 
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class LogisticPackageRecord  implements Serializable{

    	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String logisticTrackingNo;
 
    String packageWeight;
    	
    String ProductItems;
    	
    String receiverName;
   	
    String receiverPhone;
     
    String receiverBackupPhone;
     
    String receiverAddress;
     
    String senderName;
    
    String senderPhone;
     
    String senderAddress;
     	
    String createdBy;
     	
    Date   createdDateTime;
    
    String lastUpdatedBy;
 	
    Date   lastUpdatedDateTime;
     
    String createdSrc;
     
    String linkedOrderNo;
   	
    String logisticCompany;
     
    String comments;
    
    String status;
    
    List<String> errors;
    
	public List<String> getErrors() {
		return errors;
	}
	public void setErrors(List<String> errors) {
		this.errors = errors;
	}
	public String getPackageWeight() {
		return packageWeight;
	}
	public void setPackageWeight(String packageWeight) {
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
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	public Date getLastUpdatedDateTime() {
		return lastUpdatedDateTime;
	}
	public void setLastUpdatedDateTime(Date lastUpdatedDateTime) {
		this.lastUpdatedDateTime = lastUpdatedDateTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
		return "LogisticPackageRecord [logisticTrackingNo=" + logisticTrackingNo + ", packageWeight=" + packageWeight
				+ ", ProductItems=" + ProductItems + ", receiverName=" + receiverName + ", receiverPhone="
				+ receiverPhone + ", receiverBackupPhone=" + receiverBackupPhone + ", receiverAddress="
				+ receiverAddress + ", senderName=" + senderName + ", senderPhone=" + senderPhone + ", senderAddress="
				+ senderAddress + ", createdBy=" + createdBy + ", createdDateTime=" + createdDateTime
				+ ", lastUpdatedBy=" + lastUpdatedBy + ", lastUpdatedDateTime=" + lastUpdatedDateTime + ", createdSrc="
				+ createdSrc + ", linkedOrderNo=" + linkedOrderNo + ", logisticCompany=" + logisticCompany
				+ ", comments=" + comments + ", status=" + status + "]";
	}
	 
}
