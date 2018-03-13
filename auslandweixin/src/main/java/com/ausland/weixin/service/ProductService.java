package com.ausland.weixin.service;

import org.springframework.web.multipart.MultipartFile;

import com.ausland.weixin.model.reqres.CreateProductReq;
import com.ausland.weixin.model.reqres.CreateProductRes;
import com.ausland.weixin.model.reqres.GlobalRes;
import com.ausland.weixin.model.reqres.UpdateProductStockReq;

public interface ProductService {

	GlobalRes createBrand(String brandName);
	GlobalRes createCategory(String category);
	GlobalRes deleteBrand(String brand);
	GlobalRes deletedCategory(String category);
	
	GlobalRes deleteProduct(String productId);
	CreateProductRes createProduct(CreateProductReq req);
	GlobalRes updateProduct(CreateProductReq req);
	GlobalRes updateProductStock(UpdateProductStockReq req);
	
	GlobalRes uploadProductFromExcel(MultipartFile excelFile);
	
}
