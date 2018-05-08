package com.ausland.weixin.model.reqres;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProductIdsReq  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	List<String> productIds;
	public List<String> getProductIds() {
		return productIds;
	}
	public void setProductIds(List<String> productIds) {
		this.productIds = productIds;
	}
	
}
