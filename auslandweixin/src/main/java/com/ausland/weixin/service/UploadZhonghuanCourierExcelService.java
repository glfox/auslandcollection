package com.ausland.weixin.service;

import org.springframework.web.multipart.MultipartFile;

import com.ausland.weixin.model.reqres.QueryUploadLogisticPackageRes;
import com.ausland.weixin.model.reqres.UploadZhonghanCourierExcelRes;

public interface UploadZhonghuanCourierExcelService {

	
	UploadZhonghanCourierExcelRes uploadZhonghuanCourierExcel(MultipartFile excelFile);
	
	QueryUploadLogisticPackageRes queryZhonghuanUploadedRecords(String excelFileName, String fromDate, String toDate, String receiverPhone);

}
