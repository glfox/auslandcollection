package com.ausland.weixin.model.difou;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShouhouInfo  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("name")	
    String receriverName;

	@JsonProperty("brand")	
    String brandName;
	
    @JsonProperty("phone")	
    String receiverPhone;

    @JsonProperty("creationdate")	
    Date creationDate;

    @JsonProperty("product")	
    String product;
    
    @JsonProperty("problem")	
    String problem;

    @JsonProperty("progress")	
    String progress;
 
    @JsonProperty("comments")	
    String comments;

    @JsonProperty("customercourierid")	
    String customerCourierId;
    
    @JsonProperty("brandcourierid")	
    String brandCourierId;

    @JsonProperty("status")	
    String status;

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "ShouhouInfo [receriverName=" + receriverName + ", brandName=" + brandName + ", receiverPhone="
				+ receiverPhone + ", creationDate=" + creationDate + ", product=" + product + ", problem=" + problem
				+ ", progress=" + progress + ", comments=" + comments + ", customerCourierId=" + customerCourierId
				+ ", brandCourierId=" + brandCourierId + "]";
	}

	public String getReceriverName() {
		return receriverName;
	}



	public void setReceriverName(String receriverName) {
		this.receriverName = receriverName;
	}



	public String getReceiverPhone() {
		return receiverPhone;
	}



	public void setReceiverPhone(String receiverPhone) {
		this.receiverPhone = receiverPhone;
	}



	public Date getCreationDate() {
		return creationDate;
	}



	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}



	public String getProduct() {
		return product;
	}



	public void setProduct(String product) {
		this.product = product;
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



	public String getComments() {
		return comments;
	}



	public void setComments(String comments) {
		this.comments = comments;
	}



	public String getCustomerCourierId() {
		return customerCourierId;
	}



	public void setCustomerCourierId(String customerCourierId) {
		this.customerCourierId = customerCourierId;
	}



	public String getBrandCourierId() {
		return brandCourierId;
	}



	public void setBrandCourierId(String brandCourierId) {
		this.brandCourierId = brandCourierId;
	}



	public static long getSerialversionuid() {
		return serialVersionUID;
	}
    
}