package com.ausland.weixin.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ausland.weixin.model.db.ProductFeature;
import com.ausland.weixin.model.reqres.StockInfo;

@Repository
public class ProductDaoImpl implements ProductDao{
	
	private static final Logger logger = LoggerFactory.getLogger(ProductDaoImpl.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private NamedParameterJdbcTemplate namedJdbcTemplate;
	
	@Override
	public List<String> getMatchingProductIds(String productId)
	{
		if(StringUtils.isEmpty(productId)) return null;
		String pId = productId.trim();
		List<String> response = new ArrayList<String>(); 
		try
		{
			String getMatchingProductIdsQuery = "select productId from product where productId like '%"+pId+"'%";
			logger.debug("getMatchingProductIdsQuery:"+getMatchingProductIdsQuery);
			response = jdbcTemplate.queryForList(getMatchingProductIdsQuery, String.class);
			return response;
		}
		catch(Exception e)
		{
			logger.error("got exception during getMatchingProductIds:"+productId +"; exception message:"+e.getMessage());
		}
		return null;
	}
	
	@Override
	public List<StockInfo> getStockInfoByProductionId(String productId, String matchingStockStatus)
	{
		if(StringUtils.isEmpty(productId)) return null;
		//String pId = productId.trim();
		//List<StockInfo> productFeature = new ArrayList<StockInfo>();
		String getpfbyProductIdQuery = "";
		try
		{
			if(StringUtils.isEmpty(matchingStockStatus))
			    getpfbyProductIdQuery = "select p.productId, p.productName, p.brand, pf.stockStatus, pf.color, pf.size, pf.additional from productfeature pf, product p where pf.productId = p.productId and pf.productId = ?";
			else
				getpfbyProductIdQuery = "select p.productId, p.productName, p.brand, pf.stockStatus, pf.color, pf.size, pf.additional from productfeature pf, product p where pf.productId = p.productId and pf.productId = ? and pf.stockStatus = ?";

			logger.debug("getpfbyProductIdQuery:"+getpfbyProductIdQuery);
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(getpfbyProductIdQuery, productId.trim(), matchingStockStatus);
			if(rows == null || rows.size() <= 0)
				return null;
			logger.debug("got the rows:"+rows.size());
			List<StockInfo> response = new ArrayList<StockInfo>();
			for(Map row : rows)
			{
				StockInfo stockInfo = new StockInfo();
				stockInfo.setProductId((String)row.get("productId"));
				stockInfo.setAdditonal((String)row.get("additional"));
				stockInfo.setBrandName((String)row.get("brand"));
				stockInfo.setColor((String)row.get("color"));
				stockInfo.setProductName((String)row.get("productName"));
				stockInfo.setSize((String)row.get("size"));
				stockInfo.setStockStatus((String)row.get("stockStatus"));
				response.add(stockInfo);
			}
			return response;
		}
		catch(Exception e)
		{
			logger.error("got exception during sql query:"+getpfbyProductIdQuery+"; exception message:"+e.getMessage()); 
		}
		return null;
	}

	@Override
	public List<StockInfo> getStockInfoByProductionIds(List<String> productIdList, String matchingStockStatus) {
		if(productIdList == null || productIdList.size() <= 0) return null;
		 
		List<String> filteredPIdList = new ArrayList<String>();
		for(String pid : productIdList)
		{
			if(StringUtils.isEmpty(pid))
				continue;
			filteredPIdList.add(pid.trim());
		}
		String ids = String.join(",", filteredPIdList);
		//List<ProductFeature> productFeature = new ArrayList<ProductFeature>();
		String getpfbyProductIdsQuery = "";
		try
		{
			if(StringUtils.isEmpty(matchingStockStatus))
				getpfbyProductIdsQuery = "select p.productId, p.productName, p.brand, pf.stockStatus, pf.color, pf.size, pf.additional from productfeature pf, product p where pf.productId = p.productId and pf.productId in ("+ids+")";
			else
				getpfbyProductIdsQuery = "select p.productId, p.productName, p.brand, pf.stockStatus, pf.color, pf.size, pf.additional from productfeature pf, product p where pf.productId = p.productId and pf.productId in ("+ids+") and pf.stockStatus = ? ";
			logger.debug("getpfbyProductIdsQuery:"+getpfbyProductIdsQuery);
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(getpfbyProductIdsQuery, matchingStockStatus);
			if(rows == null || rows.size() <= 0)
				return null;
			logger.debug("got the rows:"+rows.size());
			List<StockInfo> response = new ArrayList<StockInfo>();
			for(Map row : rows)
			{
				StockInfo stockInfo = new StockInfo();
				stockInfo.setProductId((String)row.get("productId"));
				stockInfo.setAdditonal((String)row.get("additional"));
				stockInfo.setBrandName((String)row.get("brand"));
				stockInfo.setColor((String)row.get("color"));
				stockInfo.setProductName((String)row.get("productName"));
				stockInfo.setSize((String)row.get("size"));
				stockInfo.setStockStatus((String)row.get("stockStatus"));
				response.add(stockInfo);
			}
			return response;
		}
		catch(Exception e)
		{
			logger.error("got exception during sql query:"+getpfbyProductIdsQuery+"; exception message:"+e.getMessage()); 
		}
		return null;
	}

	@Override
	public List<String> getMatchingBrandNames(String brandName) {
		if(StringUtils.isEmpty(brandName)) return null;
		String bId = brandName.trim();
		List<String> response = new ArrayList<String>(); 
		try
		{
			String getMatchingBrandNames = "select brand from product where brand like '%"+bId+"'%";
			logger.debug("getMatchingBrandNames:"+getMatchingBrandNames);
			response = jdbcTemplate.queryForList(getMatchingBrandNames, String.class);
			return response;
		}
		catch(Exception e)
		{
			logger.error("got exception during getMatchingBrandNames:"+bId +"; exception message:"+e.getMessage());
		}
		return null;
	}

	@Override
	public List<StockInfo> getStockInfoByBrandNames(List<String> brandNameList,String matchingStockStatus) {
		if(brandNameList == null || brandNameList.size() <= 0) return null;
		 
		List<String> filteredBrandNameList = new ArrayList<String>();
		for(String pid : brandNameList)
		{
			if(StringUtils.isEmpty(pid))
				continue;
			filteredBrandNameList.add(pid.trim());
		}
		String ids = String.join(",", filteredBrandNameList);
		//List<ProductFeature> productFeature = new ArrayList<ProductFeature>();
		String getStockInfoByBrandNamesQuery = "";
		try
		{
			if(StringUtils.isEmpty(matchingStockStatus))
				getStockInfoByBrandNamesQuery = "select p.productId, p.productName, p.brand, pf.stockStatus, pf.color, pf.size, pf.additional from productfeature pf, product p where pf.productId = p.productId and p.brand in ("+ids+")";
			else 
				getStockInfoByBrandNamesQuery = "select p.productId, p.productName, p.brand, pf.stockStatus, pf.color, pf.size, pf.additional from productfeature pf, product p where pf.productId = p.productId and p.brand in ("+ids+") and pf.stockStatus = ?";
			logger.debug("getStockInfoByBrandNamesQuery:"+getStockInfoByBrandNamesQuery);
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(getStockInfoByBrandNamesQuery, matchingStockStatus);
			if(rows == null || rows.size() <= 0)
				return null;
			logger.debug("got the rows:"+rows.size());
			List<StockInfo> response = new ArrayList<StockInfo>();
			for(Map row : rows)
			{
				StockInfo stockInfo = new StockInfo();
				stockInfo.setProductId((String)row.get("productId"));
				stockInfo.setAdditonal((String)row.get("additional"));
				stockInfo.setBrandName((String)row.get("brand"));
				stockInfo.setColor((String)row.get("color"));
				stockInfo.setProductName((String)row.get("productName"));
				stockInfo.setSize((String)row.get("size"));
				stockInfo.setStockStatus((String)row.get("stockStatus"));
				response.add(stockInfo);
			}
			return response;
		}
		catch(Exception e)
		{
			logger.error("got exception during sql query:"+getStockInfoByBrandNamesQuery+"; exception message:"+e.getMessage()); 
		}
		return null;
	}

	@Override
	public List<StockInfo> getStockInfoByBrandName(String brandName, String matchingStockStatus) {
		if(StringUtils.isEmpty(brandName)) return null;
		//String bId = branName.trim();
		//List<StockInfo> productFeature = new ArrayList<StockInfo>();
		String getStockInfoByBrandNameQuery = "";
		try
		{
			if(StringUtils.isEmpty(matchingStockStatus))
				getStockInfoByBrandNameQuery = "select p.productId, p.productName, p.brand, pf.stockStatus, pf.color, pf.size, pf.additional from productfeature pf, product p where pf.productId = p.productId and p.brand = ?";
			else
				getStockInfoByBrandNameQuery = "select p.productId, p.productName, p.brand, pf.stockStatus, pf.color, pf.size, pf.additional from productfeature pf, product p where pf.productId = p.productId and p.brand = ? and pf.stockStatus = ?";				
			logger.debug("getStockInfoByBrandNameQuery:"+getStockInfoByBrandNameQuery);
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(getStockInfoByBrandNameQuery, brandName.trim(), matchingStockStatus);
			if(rows == null || rows.size() <= 0)
				return null;
			logger.debug("got the rows:"+rows.size());
			List<StockInfo> response = new ArrayList<StockInfo>();
			for(Map row : rows)
			{
				StockInfo stockInfo = new StockInfo();
				stockInfo.setProductId((String)row.get("productId"));
				stockInfo.setAdditonal((String)row.get("additional"));
				stockInfo.setBrandName((String)row.get("brand"));
				stockInfo.setColor((String)row.get("color"));
				stockInfo.setProductName((String)row.get("productName"));
				stockInfo.setSize((String)row.get("size"));
				stockInfo.setStockStatus((String)row.get("stockStatus"));
				response.add(stockInfo);
			}
			return response;
		}
		catch(Exception e)
		{
			logger.error("got exception during sql query:"+getStockInfoByBrandNameQuery+"; exception message:"+e.getMessage()); 
		}
		return null;
	}

}
