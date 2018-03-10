package com.ausland.weixin.service;

import com.ausland.weixin.model.reqres.QueryZhongHuanDetailsByTrackingNoRes;
import com.ausland.weixin.model.reqres.QueryZhongHuanLastThreeMonthByPhoneNoRes;

public interface QueryZhongHuanService {

	QueryZhongHuanLastThreeMonthByPhoneNoRes queryZhongHuanLastThreeMonthbyPhoneNo(String phoneNo);
	QueryZhongHuanDetailsByTrackingNoRes queryZhongHuanDetailsByTrackingNo(String trackingNo);
}
