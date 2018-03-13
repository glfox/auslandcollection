package com.ausland.weixin.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import com.ausland.weixin.model.reqres.QueryProductRes;
import com.ausland.weixin.model.reqres.UploadProductReq;
import com.ausland.weixin.model.reqres.UploadProductRes;
import com.ausland.weixin.model.reqres.UploadZhonghanCourierExcelRes;
import com.ausland.weixin.service.QueryProductService;
import com.ausland.weixin.service.ProductService;

@RestController
@RequestMapping(value = "/query/product")
public class QueryProductController {
	
private static final Logger logger = LoggerFactory.getLogger(QueryProductController.class);
    
	@Autowired
	private QueryProductService queryProductService; 
	
	@RequestMapping(value = "/getbyproductIds", method = RequestMethod.GET)
	public QueryProductRes queryByProductIds(@RequestParam(name="productIds", required = true) String productIds,
			                                 HttpServletRequest httpServletRequest) throws IOException 
	{
		logger.debug("entered queryByProductIds with proudctIds:"+productIds);
		List<String> productIdList = new ArrayList<String>();
		String[] productArray = productIds.split(",");
		for(String s:productArray)
		{
			if(!StringUtils.isEmpty(s))
				productIdList.add(s);
		}
		if(productIdList.size() < 1)
		{
			QueryProductRes res = new QueryProductRes();
			res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
			res.setErrorDetails("没有有效的商品id。");
			return res;
		}
		if(productIdList.size() == 1)
			return queryProductService.queryByProductId(productIdList.get(0));
		return queryProductService.queryByProductIds(productIdList);
	}
	 
	@RequestMapping(value = "/getbybrandname", method = RequestMethod.GET)
	public QueryProductRes queryByBrandName(@RequestParam(name="brand", required = true) String brandName,
			                                 HttpServletRequest httpServletRequest) throws IOException 
	{
		logger.debug("entered queryByBrandName with brandName:"+brandName); 
		return queryProductService.queryByBrandName(brandName);
	}
	
	@RequestMapping(value = "/getbymatchingstr", method = RequestMethod.GET)
	public QueryProductRes queryByMatchingStr(@RequestParam(name="matching", required = true) String mathingStr,
			                                  HttpServletRequest httpServletRequest) throws IOException 
	{
		logger.debug("entered queryByMatchingStr with mathingStr:"+mathingStr); 
		return queryProductService.queryByProductIdOrProductNameMatchingLike(mathingStr);
	}
	
	@RequestMapping(value = "/getAll", method = RequestMethod.GET)
	public QueryProductRes queryAll(@RequestParam(name="pageSize", required = true) Integer pageSize,
		                                	@RequestParam(name="pageNo", required = true) Integer pageNo,
			                                HttpServletRequest httpServletRequest) throws IOException 
	{
		logger.debug("entered queryAll with pageNo:"+pageNo+";pageSize="+pageSize); 
		return queryProductService.queryAll(pageNo, pageSize);
	}
	
}
