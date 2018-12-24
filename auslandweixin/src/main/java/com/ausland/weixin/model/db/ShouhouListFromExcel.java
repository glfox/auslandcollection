package com.ausland.weixin.model.db;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="excelshouhou")
public class ShouhouListFromExcel implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @Column(name="id")	
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    Long Id;
	
	@Column(name="brandname", length = 64)	
	String branName;
	
    @Column(name="comments", length = 64)	
    String comments;
    
    @Column(name="problem", length = 128)	
    String problem;
    
    @Column(name="progress", length = 128)	
    String progress;

	@Column(name="brandlogisticno", length = 64)	
    String brandLogisticNo;
	
    @Column(name="customerlogisticno", length = 64)	
    String customerLogisticNo;

    @Column(name="items", length = 128)	
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
    @Temporal(TemporalType.DATE)
    Date   createdDateTime;

 

	@Override
	public String toString() {
		return "ShouhouListFromExcel [Id=" + Id + ", branName=" + branName + ", comments=" + comments + ", problem="
				+ problem + ", progress=" + progress + ", brandLogisticNo=" + brandLogisticNo + ", customerLogisticNo="
				+ customerLogisticNo + ", ProductItems=" + ProductItems + ", receiverName=" + receiverName
				+ ", receiverPhone=" + receiverPhone + ", status=" + status + ", errorMsg=" + errorMsg
				+ ", lastupdatedDateTime=" + lastupdatedDateTime + ", createdDateTime=" + createdDateTime + "]";
	}

	public String getBranName() {
		return branName;
	}

	public void setBranName(String branName) {
		this.branName = branName;
	}

	public String getCustomerLogisticNo() {
		return customerLogisticNo;
	}

	public void setCustomerLogisticNo(String customerLogisticNo) {
		this.customerLogisticNo = customerLogisticNo;
	}

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getProblem() {
		return problem;
	}

	public void setProblem(String problem) {
		this.problem = problem;
	}

	public String getProgress() {
		return progress;
	}

	public void setProgress(String progress) {
		this.progress = progress;
	}

	public String getBrandLogisticNo() {
		return brandLogisticNo;
	}

	public void setBrandLogisticNo(String brandLogisticNo) {
		this.brandLogisticNo = brandLogisticNo;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public Date getLastupdatedDateTime() {
		return lastupdatedDateTime;
	}

	public void setLastupdatedDateTime(Date lastupdatedDateTime) {
		this.lastupdatedDateTime = lastupdatedDateTime;
	}

	public Date getCreatedDateTime() {
		return createdDateTime;
	}

	public void setCreatedDateTime(Date createdDateTime) {
		this.createdDateTime = createdDateTime;
	}
    
	

}
