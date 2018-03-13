package com.ausland.weixin.service;

import com.ausland.weixin.model.reqres.CreateProductReq;
import com.ausland.weixin.model.reqres.CreateProductRes;

public interface ProductService {

	
	CreateProductRes createProduct(CreateProductReq req);
	
	/*UploadZhonghanCourierExcelRes uploadProductFromExcel(MultipartFile excelFile);*/
}
