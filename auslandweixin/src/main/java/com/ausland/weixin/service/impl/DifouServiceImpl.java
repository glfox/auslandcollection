package com.ausland.weixin.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.cache.annotation.CacheResult;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ausland.weixin.model.difou.GetDifouOrderListRes;
import com.ausland.weixin.model.difou.GetProductListRes;
import com.ausland.weixin.model.difou.GetStockListRes;
import com.ausland.weixin.model.difou.ProductDataInfo;
import com.ausland.weixin.model.difou.ProductSpecInfo;
import com.ausland.weixin.model.difou.StockDataInfo;
import com.ausland.weixin.model.reqres.QueryZhongHuanLastThreeMonthByPhoneNoRes;
import com.ausland.weixin.service.DifouService;
import com.ausland.weixin.util.DifouUtil;

@Service
public class DifouServiceImpl implements DifouService {

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private DifouUtil difouUtil;
	
	@Value("${difou.apiurl}")
	private String difouUrl;
	
	private static final Logger logger = LoggerFactory.getLogger(DifouServiceImpl.class);
	
	@Override
	@CacheResult(cacheName="difouStock")
	public GetStockListRes getStockList(String productId){
		GetStockListRes res = new GetStockListRes();
		if(StringUtils.isEmpty(productId))
		{
			res.setReturnCode("1");
			res.setReturnInfo("productId is empty");
			return res;
		}
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		String requestStr = difouUtil.getStockReqStr(productId);
		if(StringUtils.isEmpty(requestStr))
		{
			res.setReturnCode("1");
			res.setReturnInfo("cannot get stock request string from the productId.");
			return res;
		}
		HttpEntity<String> entity = new HttpEntity<String>(requestStr, headers);
        ResponseEntity<GetStockListRes> responseEntity = restTemplate.exchange(difouUrl, HttpMethod.POST, entity, GetStockListRes.class);
        if(responseEntity != null && responseEntity.getStatusCode() == HttpStatus.OK)
        {
        	res = responseEntity.getBody(); 
        	if(res.getDataInfoList()!= null && res.getDataInfoList().size() > 0)
        	{
        		for(StockDataInfo sdi : res.getDataInfoList())
        		{
        			if(!StringUtils.isEmpty(sdi.getStock())){
        				float f = Float.parseFloat(sdi.getStock().trim());
        				int st = (int)f;
        				if(st <= 0)
        				{
        					sdi.setStock("缺货");
        				}
        				else if(st <=3)
        				{
        					sdi.setStock("紧张");
        				}
        				else if(st <=10)
        				{
        					sdi.setStock("有货");
        				}
        				else
        				{
        					sdi.setStock("充足");
        				}
        			}
        		}
        	}
        }else{
        	res.setReturnCode("1");
        	res.setReturnInfo("get difou stock failed.");
        }
        return res;
	}


	@Override
	@CacheResult(cacheName="difouAllProductIds")
	public List<String> getAllProductIds() {
		List<String> productIdsList = new ArrayList<String>();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		for(int i = 1; i < 20; i ++)
		{
			String requestStr = difouUtil.getAllProductIdListReqStr(""+i);
			if(StringUtils.isEmpty(requestStr))
				return productIdsList;
			HttpEntity<String> entity = new HttpEntity<String>(requestStr, headers);
	        ResponseEntity<GetProductListRes> responseEntity = restTemplate.exchange(difouUrl, HttpMethod.POST, entity, GetProductListRes.class);
	        if(responseEntity != null && responseEntity.getStatusCode() == HttpStatus.OK)
	        {
	        	GetProductListRes res = responseEntity.getBody();
	        	if(!"0".equalsIgnoreCase(res.getReturnCode()) || res.getDataInfoList() == null || res.getDataInfoList().size() <= 0){
	        		return productIdsList;
	        	}
	        	
	        	productIdsList.addAll(getIdsList(res.getDataInfoList()));
	        	if(res.getDataInfoList().size() < 100)
	        	{
	        		return productIdsList;
	        	}
	        }
		}
		return productIdsList;
	}
	
	private List<String> getIdsList(List<ProductDataInfo> list){
		List<String> productIdsList = new ArrayList<String>();
		if(list == null || list.size() <= 0)
			return productIdsList;
		for(ProductDataInfo p: list)
		{
			 
			productIdsList.add(p.getGoodsNo().toUpperCase());
		}
		return productIdsList;
	}

	@Override
	public QueryZhongHuanLastThreeMonthByPhoneNoRes queryDifouLastThreeMonth(String nameOrPhoneNo) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public GetDifouOrderListRes getLastThreeMonthOrders() {
		// TODO Auto-generated method stub
		return null;
	}

}
