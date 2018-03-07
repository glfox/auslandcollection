package com.ausland.weixin.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ausland.weixin.config.AuslandApplicationConstants;
import com.ausland.weixin.dao.ProductRepository;
import com.ausland.weixin.model.db.Product;

import com.ausland.weixin.model.reqres.QueryStockRes;
import com.ausland.weixin.model.reqres.StockInfo;
import com.ausland.weixin.service.QueryStockService;
 

@Service
public class QueryStockServiceImpl implements QueryStockService{

	
	@Autowired
	private ProductRepository productRepository;
	
    private static final Logger logger = LoggerFactory.getLogger(QueryStockServiceImpl.class);
      
    private List<StockInfo> convert(List<Product> productList)
    {
    	List<StockInfo> stockList = new ArrayList<StockInfo>();
    	for(Product p: productList)
    	{
    		StockInfo stock = new StockInfo();
    		stock.setBrandName(p.getBrand());
    		stock.setColor(p.getColor());
    		stock.setSize(p.getSize());
    		stock.setProductId(p.getProductId());
    		stock.setProductName(p.getProductName());
    		stock.setFeature1(p.getFeature1());
            stock.setFeature2(p.getFeature2());
            stockList.add(stock);
    	}
    	return stockList;
    }
    
	@Override
	public QueryStockRes queryStockByProductIds(List<String> productIds,String matchingStockStatus) {

		logger.debug("entered queryStockByProductIds with productIds:"+ToStringBuilder.reflectionToString(productIds));
		QueryStockRes res = new QueryStockRes();
		try
		{
			List<Product> productList = null;
			if(StringUtils.isEmpty(matchingStockStatus))
			{
				productList = productRepository.findByProductIdIn(productIds);
			}
			else
			{
				productList = productRepository.findByProductIdInAndStockStatus(productIds, matchingStockStatus);
			} 
			if(productList == null || productList.size() <= 0)
			{
				res.setStatus(AuslandApplicationConstants.STATUS_OK);
				return res;
			}
			res.setStatus(AuslandApplicationConstants.STATUS_OK);
			res.setStockInfoList(convert(productList));
			return res;
		}
		catch(Exception e)
		{
			res.setErrorDetails("failed in db:"+e.getMessage());
			res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
		    return res;
		}
		
	}

	@Override
	public QueryStockRes queryAllStockInfo(String matchingStockStatus) {
		QueryStockRes res = new QueryStockRes();
		try
		{
			List<Product> productList = null;
			if(StringUtils.isEmpty(matchingStockStatus))
			{
				productList = productRepository.findAll();
			}
			else
			{
				productList = productRepository.findByStockStatus(matchingStockStatus);
			} 
			if(productList == null || productList.size() <= 0)
			{
				res.setStatus(AuslandApplicationConstants.STATUS_OK);
				return res;
			}
			res.setStatus(AuslandApplicationConstants.STATUS_OK);
			res.setStockInfoList(convert(productList));
			return res;
		}
		catch(Exception e)
		{
			res.setErrorDetails("failed in db:"+e.getMessage());
			res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
		    return res;
		}
	}

	@Override
	public QueryStockRes queryStockByBrandName(String brandName,String matchingStockStatus) {
		logger.debug("entered queryStockByBrandName with brandName:"+brandName);
		QueryStockRes res = new QueryStockRes();
		try
		{
			List<Product> productList = null;
			if(StringUtils.isEmpty(matchingStockStatus))
			{
				productList = productRepository.findByBrandLike(brandName);
			}
			else
			{
				productList = productRepository.findByBrandLikeAndStockStatus(brandName, matchingStockStatus);
			} 
			if(productList == null || productList.size() <= 0)
			{
				res.setStatus(AuslandApplicationConstants.STATUS_OK);
				return res;
			}
			res.setStatus(AuslandApplicationConstants.STATUS_OK);
			res.setStockInfoList(convert(productList));
			return res;
		}
		catch(Exception e)
		{
			res.setErrorDetails("failed in db:"+e.getMessage());
			res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
		    return res;
		}
	}
	

}
