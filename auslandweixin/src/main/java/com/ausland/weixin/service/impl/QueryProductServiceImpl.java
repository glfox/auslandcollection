package com.ausland.weixin.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ausland.weixin.config.AuslandApplicationConstants;
import com.ausland.weixin.dao.ProductRepository;
import com.ausland.weixin.dao.ProductStockRepository;
import com.ausland.weixin.model.db.Product;
import com.ausland.weixin.model.db.ProductStock;
import com.ausland.weixin.model.reqres.ProductRes;
import com.ausland.weixin.model.reqres.QueryProductRes;
import com.ausland.weixin.model.reqres.StockInfo;
import com.ausland.weixin.service.QueryProductService;
 

@Service
public class QueryProductServiceImpl implements QueryProductService{

	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ProductStockRepository productStockRepository;
	
    private static final Logger logger = LoggerFactory.getLogger(QueryProductServiceImpl.class);

	@Override
	public QueryProductRes queryByProductIds(List<String> productIds) {
		logger.debug("entered queryByProductIds with productIds:" +  ToStringBuilder.reflectionToString(productIds));
		QueryProductRes res = new QueryProductRes();
		List<Product> prodList  = productRepository.findByProductIdIn(productIds);
		if(prodList != null && prodList.size() > 0)
		{
			List<ProductRes> products = new ArrayList<ProductRes>();
			for(Product prod: prodList)
			{
				List<ProductStock> productStockList = productStockRepository.findByProductId(prod.getProductId());
				ProductRes pres = convertFromProdAndProdStockList(prod, productStockList);
				products.add(pres);
			}
			res.setProducts(products);
		}
		res.setStatus(AuslandApplicationConstants.STATUS_OK);
		return res;
	}

	@Override
	public QueryProductRes queryByProductId(String productId) {
		logger.debug("entered queryByProductIds with productId:" + productId);
		QueryProductRes res = new QueryProductRes();
		Product prod   = productRepository.findByProductId(productId);
		List<ProductStock> productStockList = productStockRepository.findByProductId(prod.getProductId());
		ProductRes pres = convertFromProdAndProdStockList(prod, productStockList);
		List<ProductRes> proudcts = new ArrayList<ProductRes>();
		proudcts.add(pres);
		res.setProducts(proudcts);
		res.setStatus(AuslandApplicationConstants.STATUS_OK);
		return res;
	}

	@Override
	public QueryProductRes queryByBrandName(String brandName) {
		logger.debug("entered queryByProductIds with brandName:" +  brandName);
		QueryProductRes res = new QueryProductRes();
		List<Product> prodList  = productRepository.findByBrand(brandName);
		if(prodList != null && prodList.size() > 0)
		{
			List<ProductRes> products = new ArrayList<ProductRes>();
			for(Product prod: prodList)
			{
				List<ProductStock> productStockList = productStockRepository.findByProductId(prod.getProductId());
				ProductRes pres = convertFromProdAndProdStockList(prod, productStockList);
				products.add(pres);
			}
			res.setProducts(products);
		}
		res.setStatus(AuslandApplicationConstants.STATUS_OK);
		return res;
	}

	@Override
	public QueryProductRes queryByProductIdOrProductNameMatchingLike(String matchingString) {
		logger.debug("entered queryByProductIds with matchingString:" +  matchingString);
		QueryProductRes res = new QueryProductRes();
		List<Product> prodList  = productRepository.findByMatchingString(matchingString);
		if(prodList != null && prodList.size() > 0)
		{
			List<ProductRes> products = new ArrayList<ProductRes>();
			for(Product prod: prodList)
			{
				List<ProductStock> productStockList = productStockRepository.findByProductId(prod.getProductId());
				ProductRes pres = convertFromProdAndProdStockList(prod, productStockList);
				products.add(pres);
			}
			res.setProducts(products);
		}
		res.setStatus(AuslandApplicationConstants.STATUS_OK);
		return res;
	}

	@Override
	public QueryProductRes queryAll(Integer pageNo, Integer pageSize) {
		logger.debug("entered queryAll with pageNo:"+pageNo+"; pageSize:"+pageSize);
		QueryProductRes res = new QueryProductRes();
		Pageable pagable = new PageRequest(pageNo, pageSize);
		Page<Product> p = productRepository.findAll(pagable);
		List<Product> prodList = p.getContent();
		if(prodList != null && prodList.size() > 0)
		{
			List<ProductRes> products = new ArrayList<ProductRes>();
			for(Product prod: prodList)
			{
				List<ProductStock> productStockList = productStockRepository.findByProductId(prod.getProductId());
				ProductRes pres = convertFromProdAndProdStockList(prod, productStockList);
				products.add(pres);
			}
			res.setProducts(products);
		}
		res.setStatus(AuslandApplicationConstants.STATUS_OK);
		return res;
	}

	private ProductRes convertFromProdAndProdStockList(Product product, List<ProductStock> productStockList)
	{
		ProductRes res = new ProductRes();
		res.setBrand(product.getBrand());
		res.setCategory(product.getProductCategory());
		res.setComments(product.getComments());
		res.setProductId(product.getProductId());
		res.setProductMainImageUrl(product.getProductMainImageUrl());
		res.setProductMainVideoUrl(product.getProductMainVideoUrl());
		res.setProductName(product.getProductName());
		res.setProductSmallImage(product.getProductSmallImage());
		res.setProductWeight(product.getProductWeight());
		res.setStatus(product.getStatus());
		if(productStockList != null && productStockList.size() > 0)
		{
			List<StockInfo> stockList = new ArrayList<StockInfo>();
			for(ProductStock ps: productStockList)
			{
				StockInfo sinfo = new StockInfo();
				sinfo.setColor(ps.getColor());
				sinfo.setSize(ps.getSize());
				sinfo.setStockStatus(ps.getStockStatus());
				stockList.add(sinfo);
			}
			res.setStock(stockList);
		}
		return res;
	}

}
