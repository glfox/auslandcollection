package com.ausland.weixin.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ausland.weixin.dao.ProductDao;
import com.ausland.weixin.model.reqres.QueryStockRes;
import com.ausland.weixin.model.reqres.StockInfo;
import com.ausland.weixin.service.QueryStockService;
 

@Service
public class QueryStockServiceImpl implements QueryStockService{

	/*@Autowired
	private ValidationUtil validationUtil;*/
	
	@Autowired
	private ProductDao productdao;
	
    private static final Logger logger = LoggerFactory.getLogger(QueryStockServiceImpl.class);
    
	@Override
	public QueryStockRes queryStockByProductIds(List<String> productIds,String matchingStockStatus) {

		logger.debug("entered queryStockByProductIds with productIds:"+ToStringBuilder.reflectionToString(productIds));
		QueryStockRes res = new QueryStockRes();
		Set<String> finalProductIdSet = new HashSet<String>();
		for(String productId : productIds)
		{
			List<String> pIds = productdao.getMatchingProductIds(productId);
			if(pIds == null || pIds.size() <= 0)
				continue;
			finalProductIdSet.addAll(pIds);
		}
		if(finalProductIdSet.size() <= 0)
		{
			res.setErrorDetails("cannot find the matching productId in the db.");
			return res;
		}
		List<String> pids = new ArrayList<String>(finalProductIdSet);
		
		List<StockInfo> resp = productdao.getStockInfoByProductionIds(pids, matchingStockStatus);
		if(resp == null || resp.size() <= 0)
		{
			res.setErrorDetails("cannot find matching productId in the db.");
			return res;
		}
	    
		res.setStockInfoList(resp);
		return res;
	}

	@Override
	public QueryStockRes queryAllStockInfo(String matchingStockStatus) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QueryStockRes queryStockByBrandName(String brandName,String matchingStockStatus) {
		logger.debug("entered queryStockByBrandName with brandName:"+brandName);
		QueryStockRes res = new QueryStockRes();
		if(StringUtils.isEmpty(brandName))
		{
			res.setErrorDetails("empty brand name.");
			return res;
		}
		List<String> brandNames = productdao.getMatchingBrandNames(brandName.trim());
		if(brandNames == null || brandNames.size()<= 0)
		{
			res.setErrorDetails("cannot find matching brandname:"+brandName+" in db.");
			return res;
		}
		List<StockInfo> resp = null;
		if(brandNames.size() == 1)
		{
			resp = productdao.getStockInfoByBrandName(brandNames.get(0),matchingStockStatus);
		}
		else
		{
			resp = productdao.getStockInfoByBrandNames(brandNames,matchingStockStatus);
		}
		if(resp == null || resp.size() <= 0)
		{
			res.setErrorDetails("cannot find matching stockinfo for brandname:"+brandName);
			return res;
		}
		res.setStockInfoList(resp);
		return res;
	}
	

}
