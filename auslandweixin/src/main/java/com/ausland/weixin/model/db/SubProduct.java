package com.ausland.weixin.model.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class SubProduct {
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	Integer id;

	@Column(length = 256)	
	String imageUrl;
	@Column(length = 256)	
	String videoUrl;
	@Column(length = 32)	
    String size;
	@Column(length = 32)
    String coler;
	@Column(length = 64)
    String feature1;
	@Column(length = 64)
    String feature2;
	@Column(length = 32)
    String stockStatus;
	@Column(length = 32)
    String subProductStatus;
	@Column(length = 256)
    String comments;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getVideoUrl() {
		return videoUrl;
	}
	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getColer() {
		return coler;
	}
	public void setColer(String coler) {
		this.coler = coler;
	}
	public String getFeature1() {
		return feature1;
	}
	public void setFeature1(String feature1) {
		this.feature1 = feature1;
	}
	public String getFeature2() {
		return feature2;
	}
	public void setFeature2(String feature2) {
		this.feature2 = feature2;
	}
	public String getStockStatus() {
		return stockStatus;
	}
	public void setStockStatus(String stockStatus) {
		this.stockStatus = stockStatus;
	}
	public String getSubProductStatus() {
		return subProductStatus;
	}
	public void setSubProductStatus(String subProductStatus) {
		this.subProductStatus = subProductStatus;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	
}
