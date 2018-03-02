package com.ausland.weixin.service;

import java.util.List;

public interface QueryZhongHuanService {

	List<String> queryZhongHuanLastThreeMonthbyPhoneNo(String phoneNo);
	String queryZhongHuanDetailsByTrackingNo(String trackingNo);
}
