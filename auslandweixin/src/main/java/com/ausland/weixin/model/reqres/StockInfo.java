package com.ausland.weixin.model.reqres;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class StockInfo  implements Serializable{

	private static final long serialVersionUID = 1L;
    String color;
    String size;
    String stockStatus;
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getStockStatus() {
		return stockStatus;
	}
	public void setStockStatus(String stockStatus) {
		this.stockStatus = stockStatus;
	}
	@Override
	public String toString() {
		return "StockInfo [color=" + color + ", size=" + size + ", stockStatus=" + stockStatus + "]";
	}
	
}
