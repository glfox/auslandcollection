package com.ausland.weixin.model.reqres;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ZhongHuanFydhDetails  implements Serializable{

	  String courierCreatedDateTime;//电子下单时间chrlrsj
      String courierNumber;  //分运单号chrfydh
      String courierChinaNumber; //国内单号
      String weight; //重量
      String customStatus;//审核状态
      String courierCompany;//快递公司
      String receiverName;//收件人
      String receiverAddress;//收件人地址
      String receiverPhone;//收件人电话
      String productItems; //商品
	public String getCourierCreatedDateTime() {
		return courierCreatedDateTime;
	}
	public void setCourierCreatedDateTime(String courierCreatedDateTime) {
		this.courierCreatedDateTime = courierCreatedDateTime;
	}
	public String getCourierNumber() {
		return courierNumber;
	}
	public void setCourierNumber(String courierNumber) {
		this.courierNumber = courierNumber;
	}
	public String getCourierChinaNumber() {
		return courierChinaNumber;
	}
	public void setCourierChinaNumber(String courierChinaNumber) {
		this.courierChinaNumber = courierChinaNumber;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getCustomStatus() {
		return customStatus;
	}
	public void setCustomStatus(String customStatus) {
		this.customStatus = customStatus;
	}
	public String getCourierCompany() {
		return courierCompany;
	}
	public void setCourierCompany(String courierCompany) {
		this.courierCompany = courierCompany;
	}
	public String getReceiverName() {
		return receiverName;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	public String getReceiverAddress() {
		return receiverAddress;
	}
	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}
	public String getReceiverPhone() {
		return receiverPhone;
	}
	public void setReceiverPhone(String receiverPhone) {
		this.receiverPhone = receiverPhone;
	}
	public String getProductItems() {
		return productItems;
	}
	public void setProductItems(String productItems) {
		this.productItems = productItems;
	}
	@Override
	public String toString() {
		return "ZhongHuanFydhDetails [courierCreatedDateTime=" + courierCreatedDateTime + ", courierNumber="
				+ courierNumber + ", courierChinaNumber=" + courierChinaNumber + ", weight=" + weight
				+ ", customStatus=" + customStatus + ", courierCompany=" + courierCompany + ", receiverName="
				+ receiverName + ", receiverAddress=" + receiverAddress + ", receiverPhone=" + receiverPhone
				+ ", productItems=" + productItems + "]";
	}
	
      
}
  