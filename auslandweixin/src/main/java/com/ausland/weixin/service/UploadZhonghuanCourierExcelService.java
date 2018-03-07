package com.ausland.weixin.service;

import org.springframework.web.multipart.MultipartFile;

import com.ausland.weixin.model.reqres.QueryUploadLogisticPackageRes;
import com.ausland.weixin.model.reqres.UploadZhonghanCourierExcelRes;

public interface UploadZhonghuanCourierExcelService {

	
	UploadZhonghanCourierExcelRes uploadZhonghuanCourierExcel(MultipartFile excelFile);
	
	QueryUploadLogisticPackageRes queryZhonghuanUploadedRecordsByUploadedExcelName(String excelFileName);
	QueryUploadLogisticPackageRes queryZhonghuanUploadedRecordsByDateRange(String fromDate, String toDate);
	QueryUploadLogisticPackageRes queryZhonghuanUploadedRecordsByPhoneAndDateRange(String receiverPhone, String fromDate, String toDate);

}
