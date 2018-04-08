package com.ausland.weixin.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ausland.weixin.config.AuslandApplicationConstants;
import com.ausland.weixin.model.reqres.GlobalListRes;
import com.ausland.weixin.model.reqres.ProductIdsReq;
import com.ausland.weixin.model.reqres.QueryProductRes;
import com.ausland.weixin.service.QueryProductService;

@RestController
@RequestMapping(value = "/query/product")
public class QueryProductController {
	
private static final Logger logger = LoggerFactory.getLogger(QueryProductController.class);
    
	@Autowired
	private QueryProductService queryProductService; 
	
	@RequestMapping(value = "/getproductby", method = RequestMethod.POST)
	public QueryProductRes queryProductBy(@RequestParam(name="pageSize", required = true) Integer pageSize,
									      @RequestParam(name="pageNo", required = true) Integer pageNo,
									      @RequestParam(name="matchingstr", required = false) String mathingStr,
			                              @RequestParam(name="brands", required = false) String brandNames,
			                              @RequestBody(required = false)ProductIdsReq productIds,
			                              HttpServletRequest httpServletRequest) throws IOException 
	{
		logger.debug("entered queryByMatchingStr with mathingStr:"+mathingStr+";brandnames:"+brandNames+";productIds:"+productIds+";pageSize:"+pageSize+";pageNo:"+pageNo); 
		
		return queryProductService.queryProductBy(pageNo, pageSize, brandNames, mathingStr, productIds);
	}
	
	@RequestMapping(value = "/getproductbyid", method = RequestMethod.GET)
	public QueryProductRes queryProductByProductId(@RequestParam(name="productId", required = true) String productId,
			                                   HttpServletRequest httpServletRequest) throws IOException 
	{
		logger.debug("entered queryProductByProductId "); 
		return queryProductService.queryByProductId(productId);
	}
	
	
	@RequestMapping(value = "/getallbrand", method = RequestMethod.GET)
	public GlobalListRes getAllBrand(HttpServletRequest httpServletRequest) throws IOException 
	{
		logger.debug("entered getAllBrand ");
		GlobalListRes res = new GlobalListRes();
		List<String> list = queryProductService.getAllBrand();
		if(list != null && list.size() > 0)
		{
			res.setList(list);
		}
		res.setStatus(AuslandApplicationConstants.STATUS_OK);
		return res;
	}
	
	@RequestMapping(value = "/getallcategory", method = RequestMethod.GET)
	public GlobalListRes getAllCategory(HttpServletRequest httpServletRequest) throws IOException 
	{
		logger.debug("entered getAllCategory "); 
		GlobalListRes res = new GlobalListRes();
		List<String> list = queryProductService.getAllCategory();
		if(list != null && list.size() > 0)
		{
			res.setList(list);
		}
		res.setStatus(AuslandApplicationConstants.STATUS_OK);
		return res;
	}
	
	@RequestMapping(value = "/getproductidlistby", method = RequestMethod.GET)
	public GlobalListRes getProductIdListBy(@RequestParam(name="brands", required = false) String brandNames,
			                               @RequestParam(name="matchingstr", required = false) String matchingStr,
	                                       HttpServletRequest httpServletRequest) throws IOException 
	{
		logger.debug("entered getAllCategory "); 
		GlobalListRes res = new GlobalListRes();
		List<String> list = queryProductService.getProductIdListBy(brandNames, matchingStr);
		if(list != null && list.size() > 0)
		{
			res.setList(list);
		}
		res.setStatus(AuslandApplicationConstants.STATUS_OK);
		return res;
	}
	
	 
}
