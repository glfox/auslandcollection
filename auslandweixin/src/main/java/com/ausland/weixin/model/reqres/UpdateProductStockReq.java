package com.ausland.weixin.model.reqres;

 
import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UpdateProductStockReq implements Serializable
{
	private static final long serialVersionUID = 1L;
	String productId;
   	List<StockInfo> stock;
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public List<StockInfo> getStock() {
		return stock;
	}
	public void setStock(List<StockInfo> stock) {
		this.stock = stock;
	}
	
	@Override
	public String toString() {
		return "UpdateProductStockReq [productId=" + productId + ", stock=" + stock + "]";
	}
	
  
}
