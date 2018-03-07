package com.ausland.weixin.controller;

import java.io.IOException;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ausland.weixin.model.reqres.UploadProductReq;
import com.ausland.weixin.model.reqres.UploadProductRes;
import com.ausland.weixin.model.reqres.UploadZhonghanCourierExcelRes;
import com.ausland.weixin.service.UploadProductService;

@RestController
@MultipartConfig
@RequestMapping(value = "/provisioning/product")
public class UploadProductController {
	
private static final Logger logger = LoggerFactory.getLogger(UploadProductController.class);
    
	@Autowired
	private UploadProductService uploadProductService; 
	
	@RequestMapping(value = "/createproduct", method = RequestMethod.POST)
	public UploadProductRes createProduct(@RequestBody(required = true) UploadProductReq uploadProductRequest,
			HttpServletRequest httpServletRequest) throws IOException 
	{
		logger.debug("entered uploadLogisticPackageOrder.");
		return uploadProductService.uploadProduct(uploadProductRequest);
	}
	
    
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public UploadZhonghanCourierExcelRes uploadProductFromExcel(@RequestPart(required = true)MultipartFile excelFile,
			                                           HttpServletRequest httpServletRequest) throws IOException 
	{
		logger.debug("entered uploadLogisticPackageOrder.");
		return uploadProductService.uploadProductFromExcel(excelFile);
	}
}
