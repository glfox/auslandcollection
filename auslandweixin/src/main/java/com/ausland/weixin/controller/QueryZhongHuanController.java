package com.ausland.weixin.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ausland.weixin.config.AuslandApplicationConstants;
import com.ausland.weixin.model.reqres.QueryZhongHuanDetailsByTrackingNoRes;
import com.ausland.weixin.model.reqres.QueryZhongHuanLastThreeMonthByPhoneNoRes;
import com.ausland.weixin.service.ExcelOrderService;
import com.ausland.weixin.service.QueryZhongHuanService;
import com.ausland.weixin.model.reqres.ZhongHuanFydhDetails;

@RestController
@RequestMapping(value = "/query/zhonghuan")
public class QueryZhongHuanController {

	private static final Logger logger = LoggerFactory.getLogger(QueryZhongHuanController.class);
    
	@Autowired
	private QueryZhongHuanService queryZhongHuanService; 
	
	@Autowired
	private ExcelOrderService excelOrderService; 
	
	@RequestMapping(value = "/lastthreemonth", method = RequestMethod.GET)
	public QueryZhongHuanLastThreeMonthByPhoneNoRes queryZhongHuanLastThreeMonthbyPhoneNo(@RequestParam(name="phone", required = true) String phoneNo, 
			                                                                              HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("entered QueryZhongHuanController.queryZhongHuanLastThreeMonthbyPhoneNo with phoneno:"+phoneNo);
		
		QueryZhongHuanLastThreeMonthByPhoneNoRes res1 = queryZhongHuanService.queryZhongHuanLastThreeMonthbyPhoneNo(phoneNo);
		
		List<ZhongHuanFydhDetails> fydhList = new ArrayList<ZhongHuanFydhDetails>();
		
		if(res1.getFydhList() != null && res1.getFydhList().size() > 0) {
			fydhList.addAll(res1.getFydhList());
		}
		
		logger.debug("the size of the fydhlist:" + fydhList.size());
		
		QueryZhongHuanLastThreeMonthByPhoneNoRes res2 = excelOrderService.getOrderListFromExcel(phoneNo);
		if(res2.getFydhList() != null && res2.getFydhList().size() > 0) {
			fydhList.addAll(res2.getFydhList());
		}
		
		QueryZhongHuanLastThreeMonthByPhoneNoRes res = new QueryZhongHuanLastThreeMonthByPhoneNoRes();

        res.getFydhList().addAll(fydhList);
        if(res.getFydhList().size() > 0)
        {
        	res.setStatus(AuslandApplicationConstants.STATUS_OK);
        }
        logger.debug("the total size of the fydhlist:" + res.getFydhList().size());
		return res;
		
	}

	@RequestMapping(value = "/detailsbytrackingno", method = RequestMethod.GET)
	public QueryZhongHuanDetailsByTrackingNoRes queryZhongHuanDetailsByTrackingNo(@RequestParam(name="trackingno", required = true) String trackingNo)
	{
		logger.debug("enetered QueryZhongHuanController.queryZhongHuanDetailsByTrackingNo with trackingNo:"+trackingNo);
		return queryZhongHuanService.queryZhongHuanDetailsByTrackingNo(trackingNo);
	}
	
}
