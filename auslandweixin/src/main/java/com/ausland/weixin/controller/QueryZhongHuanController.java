package com.ausland.weixin.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ausland.weixin.model.reqres.QueryZhongHuanDetailsByTrackingNoRes;
import com.ausland.weixin.model.reqres.QueryZhongHuanLastThreeMonthByPhoneNoRes;
import com.ausland.weixin.service.QueryZhongHuanService;

@RestController
@RequestMapping(value = "/query/zhonghuan")
public class QueryZhongHuanController {

	private static final Logger logger = LoggerFactory.getLogger(QueryZhongHuanController.class);
    
	@Autowired
	private QueryZhongHuanService queryZhongHuanService; 

	@RequestMapping(value = "/lastthreemonth", method = RequestMethod.GET)
	public QueryZhongHuanLastThreeMonthByPhoneNoRes queryZhongHuanLastThreeMonthbyPhoneNo(@RequestParam(name="phone", required = true) String phoneNo, 
			                                                                              HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("entered QueryZhongHuanController.queryZhongHuanLastThreeMonthbyPhoneNo with phoneno:"+phoneNo);
		return queryZhongHuanService.queryZhongHuanLastThreeMonthbyPhoneNo(phoneNo);
	}

	@RequestMapping(value = "/detailsbytrackingno", method = RequestMethod.GET)
	public QueryZhongHuanDetailsByTrackingNoRes queryZhongHuanDetailsByTrackingNo(@RequestParam(name="trackingno", required = true) String trackingNo)
	{
		logger.debug("enetered QueryZhongHuanController.queryZhongHuanDetailsByTrackingNo with trackingNo:"+trackingNo);
		return queryZhongHuanService.queryZhongHuanDetailsByTrackingNo(trackingNo);
	}
	
}
