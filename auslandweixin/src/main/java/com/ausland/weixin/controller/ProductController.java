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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ausland.weixin.model.reqres.CreateProductReq;
import com.ausland.weixin.model.reqres.CreateProductRes;
import com.ausland.weixin.service.ProductService;
import com.ausland.weixin.util.ImageUtil;

@RestController
@MultipartConfig
@RequestMapping(value = "/provisioning/product")
public class ProductController {
	
private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    
	@Autowired
	private ProductService productService; 
	
	@Autowired
	private ImageUtil imageUtil;
	
	@RequestMapping(value = "/createproduct", method = RequestMethod.POST)
	public CreateProductRes createProduct(@RequestBody(required = true) CreateProductReq req,
			@RequestPart(required = true)MultipartFile smallImage,
			HttpServletRequest httpServletRequest) throws IOException 
	{
		logger.debug("entered uploadLogisticPackageOrder.");
		return productService.createProduct(req);
	}
	
	public String testImage(@RequestPart(required = true) MultipartFile smallImage,
			                @RequestParam int height, @RequestParam int width)
	{
		return imageUtil.getResizedImage(smallImage, height, width);
	}
   /* 
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public UploadZhonghanCourierExcelRes uploadProductFromExcel(@RequestPart(required = true)MultipartFile excelFile,
			                                           HttpServletRequest httpServletRequest) throws IOException 
	{
		logger.debug("entered uploadLogisticPackageOrder.");
		return uploadProductService.uploadProductFromExcel(excelFile);
	}*/
}
