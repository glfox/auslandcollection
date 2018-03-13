package com.ausland.weixin.controller;

import java.io.IOException;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
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

import com.ausland.weixin.config.AuslandApplicationConstants;
import com.ausland.weixin.model.reqres.CreateProductReq;
import com.ausland.weixin.model.reqres.CreateProductRes;
import com.ausland.weixin.model.reqres.GlobalRes;
import com.ausland.weixin.model.reqres.UpdateProductStockReq; 
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
	
	@RequestMapping(value = "/createbrand", method = RequestMethod.POST)
	public GlobalRes createBrand(@RequestParam(value = "brand", required = true) String brandName)
	{
		return productService.createBrand(brandName);
	}
	
	@RequestMapping(value = "/createcategory", method = RequestMethod.POST)
	public GlobalRes createCategory(@RequestParam(value = "category", required = true) String categoryName)
	{
		return productService.createCategory(categoryName);
	}
	
	@RequestMapping(value = "/deletebrand", method = RequestMethod.DELETE)
	public GlobalRes deleteBrand(@RequestParam(value = "brand", required = true) String brandName)
	{
		return productService.deleteBrand(brandName);
	}
	
	@RequestMapping(value = "/deletecategory", method = RequestMethod.DELETE)
	public GlobalRes deleteCategory(@RequestParam(value = "category", required = true) String categoryName)
	{
		return productService.deletedCategory(categoryName);
	}
	
	@RequestMapping(value = "/deleteproduct", method = RequestMethod.DELETE)
	public GlobalRes deleteProduct(@RequestParam(value = "productId", required = true) String productId)
	{
		return productService.deleteProduct(productId);
	}
	
	@RequestMapping(value = "/updateproduct", method = RequestMethod.POST)
	public GlobalRes updateProduct(@RequestBody(required = true) CreateProductReq req)
	{
		logger.debug("entered updateProduct with CreateProductReq:"+req.toString());
	
		return productService.updateProduct(req);
	}
	
	@RequestMapping(value = "/updateproductstock", method = RequestMethod.POST)
	public GlobalRes updateProductStock(@RequestBody(required = true) UpdateProductStockReq req)
	{
		logger.debug("entered updateProductStock with UpdateProductStockReq:"+req.toString());
	
		return productService.updateProductStock(req);
	}
	
	@RequestMapping(value = "/createproduct", method = RequestMethod.POST)
	public CreateProductRes createProduct(@RequestBody(required = true) CreateProductReq req,
			@RequestPart(required = true)MultipartFile smallImage,
			@RequestParam(required = true) int height, @RequestParam(required = true) int width,
			HttpServletRequest httpServletRequest) throws IOException 
	{
		logger.debug("entered createProduct with CreateProductReq:"+req.toString());
		height = Math.min(200, height);
		width = Math.min(200, width);
		String str = imageUtil.getResizedImage(smallImage, height, width);
		if(StringUtils.isEmpty(str) || !str.startsWith(AuslandApplicationConstants.IMAGE_HEADER))
		{
			CreateProductRes res = new CreateProductRes();
			res.setErrorDetails("产品缩略图生成失败");
			res.setStatus(AuslandApplicationConstants.STATUS_OK);
		}
		req.setSmallImageBase64EncodeString(str);
		return productService.createProduct(req);
	}

	/*@RequestMapping(value = "/testimage", method = RequestMethod.POST)
	public String testImage(@RequestPart(required = true) MultipartFile smallImage,
			                @RequestParam int height, @RequestParam int width)
	{
		return 
	}*/
    
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public GlobalRes uploadProductFromExcel(@RequestPart(required = true)MultipartFile excelFile,
			                                HttpServletRequest httpServletRequest) throws IOException 
	{
		logger.debug("entered uploadLogisticPackageOrder.");
		return productService.uploadProductFromExcel(excelFile);
	} 
}
