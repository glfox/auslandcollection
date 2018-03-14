package com.ausland.weixin.model.reqres;

 
import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class QueryProductRes  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String errorDetails;
	String status; 
	List<ProductRes> products;
	Integer totalPages;
	Integer totalElements;
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
	public List<ProductRes> getProducts() {
		return products;
	}
	public void setProducts(List<ProductRes> products) {
		this.products = products;
	}
	
	public Integer getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}
	public Integer getTotalElements() {
		return totalElements;
	}
	public void setTotalElements(Integer totalElements) {
		this.totalElements = totalElements;
	}
	@Override
	public String toString() {
		return "QueryProductRes [errorDetails=" + errorDetails + ", status=" + status + ", products=" + products
				+ ", totalPages=" + totalPages + ", totalElements=" + totalElements + "]";
	}
	

}
