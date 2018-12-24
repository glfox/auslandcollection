package com.ausland.weixin.controller;

import java.io.IOException;
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

import com.ausland.weixin.model.difou.GetShouhouListRes;
import com.ausland.weixin.model.difou.GetStockListRes;
import com.ausland.weixin.service.DifouProductStockService;
import com.ausland.weixin.service.DifouService;
import com.ausland.weixin.service.ExcelOrderService;

@RestController
public class DifouController {
	private static final Logger logger = LoggerFactory.getLogger(DifouController.class);
    
	@Autowired
    private DifouService difouService;
	
	@Autowired
	private ExcelOrderService excelOrderService; 
	
	@Autowired
    private DifouProductStockService difouProductStockService;
	

	@RequestMapping(value = "/shouhou/getShouhouList", method = RequestMethod.POST)
	public GetShouhouListRes getShouhouList(HttpServletRequest request, HttpServletResponse response,
			  @RequestParam(name="user", required = true) String userNameOrPhoneNo) throws IOException 
	{
		return excelOrderService.getShouhouListByUserNameOrPhoneNo(userNameOrPhoneNo);
	}
	
	@RequestMapping(value = "/difou/getAllProductIdList", method = RequestMethod.POST)
	public List<String> difouGetAllProductIdList(HttpServletRequest request, HttpServletResponse response) throws IOException 
	{
		return difouService.getAllProductIds();
	}
	
	@RequestMapping(value = "/difou/getProductStockList", method = RequestMethod.POST)
	public GetStockListRes difouGetProductStockList(HttpServletRequest request, HttpServletResponse response, 
			@RequestParam(name="productId", required = true) String productId) throws IOException 
	{
		GetStockListRes res = new GetStockListRes();  
		List<String> possibleIds = difouProductStockService.isValidProductId(productId);
		if(possibleIds == null || possibleIds.size() <= 0){
			res.setReturnCode("1");
			res.setReturnInfo("cannot find any match for the productId.");
			return res;
		}else if (possibleIds.size() > 1)
		{
            res.setReturnCode("0");
            res.setReturnInfo(""+possibleIds.size());
			res.setPossibleProductIds(String.join(";", possibleIds));
			return res;
		}
		return difouService.getStockList(possibleIds.get(0));
	}

}
