package com.ausland.weixin.model.db;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class ProductFeatureIdentity implements Serializable{

	@NotNull
	@Column(length = 128)
	private String productId;

	@Column(length = 64)
	private String color;
	
	@Column(length = 64)
	private String size;
	
	@Column(name="additional", length = 256)
	private String additionalFeature;

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

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

	public String getAdditionalFeature() {
		return additionalFeature;
	}

	public void setAdditionalFetaure(String additionalFeature) {
		this.additionalFeature = additionalFeature;
	}

	@Override
	public String toString() {
		return "ProductFeatureIdentity [productId=" + productId + ", color=" + color + ", size=" + size
				+ ", additionalFeaure=" + additionalFeature + "]";
	}
	
}
