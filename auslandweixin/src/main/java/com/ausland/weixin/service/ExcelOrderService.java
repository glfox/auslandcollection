package com.ausland.weixin.service;

import org.springframework.web.multipart.MultipartFile;

import com.ausland.weixin.model.reqres.QueryZhongHuanLastThreeMonthByPhoneNoRes;
import com.ausland.weixin.model.reqres.UploadZhonghanCourierExcelRes;
 
public interface ExcelOrderService {
   
	QueryZhongHuanLastThreeMonthByPhoneNoRes getOrderListFromExcel(String userNameOrPhoneNo);
	UploadZhonghanCourierExcelRes uploadOrderExcel(MultipartFile excelFile, String formatType);
}
