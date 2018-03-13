package com.ausland.weixin.model.reqres;

 
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CreateProductRes  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String errorDetails;
	String status; 
	ProductRes product;
	public String getErrorDetails() {
		return errorDetails;
	}
	public void setErrorDetails(String errorDetails) {
		this.errorDetails = errorDetails;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public ProductRes getProduct() {
		return product;
	}
	public void setProduct(ProductRes product) {
		this.product = product;
	}
	@Override
	public String toString() {
		return "CreateProductRes [errorDetails=" + errorDetails + ", status=" + status + ", product=" + product + "]";
	}

}
