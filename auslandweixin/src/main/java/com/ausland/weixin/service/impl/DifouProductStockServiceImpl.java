package com.ausland.weixin.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.cache.annotation.CacheResult;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ausland.weixin.service.DifouProductStockService;
import com.ausland.weixin.service.DifouService;

@Service
public class DifouProductStockServiceImpl implements DifouProductStockService {
	
	@Autowired
	private DifouService difouService;
	
	@Value("${difou.apiurl}")
	private String difouUrl;
	
	private static final Logger logger = LoggerFactory.getLogger(DifouProductStockServiceImpl.class);



	@Override
	@CacheResult(cacheName="difouIsValidProductId")
	public List<String> isValidProductId(String productId){
		List<String> possibleIdList = new ArrayList<String>();
		if(StringUtils.isEmpty(productId) || productId.trim().length() < 3)
		{
			return possibleIdList;
		}
		logger.debug("entered isValidProductId with productId:", productId);
		List<String> productIdsList = difouService.getAllProductIds();
        if(productIdsList == null || productIdsList.size() <= 0)
        {
        	return possibleIdList;
        }
        
        String productId_UpperCase = productId.toUpperCase();
    	if(productIdsList.contains(productId_UpperCase))
    	{
    		possibleIdList.add(productId);
    		return possibleIdList;
    	}
    	else
        {
        	if(productId.trim().length() < 3)
        	{
        		return possibleIdList;
        	}
        	for(String s:productIdsList)
        	{
        		if(s.contains(productId_UpperCase)){
        			possibleIdList.add(s);
        		}
        	}
        }
        return possibleIdList;
	}


}
