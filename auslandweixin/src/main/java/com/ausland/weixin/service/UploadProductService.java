package com.ausland.weixin.service;

import org.springframework.web.multipart.MultipartFile;

import com.ausland.weixin.model.reqres.UploadProductReq;
import com.ausland.weixin.model.reqres.UploadProductRes;
import com.ausland.weixin.model.reqres.UploadZhonghanCourierExcelRes;

public interface UploadProductService {

	
	UploadProductRes uploadProduct(UploadProductReq req);
	
	UploadZhonghanCourierExcelRes uploadProductFromExcel(MultipartFile excelFile);
}
