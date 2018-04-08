package com.ausland.weixin.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ausland.weixin.config.AuslandApplicationConstants;
import com.ausland.weixin.dao.BrandRepository;
import com.ausland.weixin.dao.CategoryRepository;
import com.ausland.weixin.dao.ProductRepository;
import com.ausland.weixin.dao.ProductStockRepository;
import com.ausland.weixin.model.db.Brand;
import com.ausland.weixin.model.db.Category;
import com.ausland.weixin.model.db.Product;
import com.ausland.weixin.model.db.ProductStock;
import com.ausland.weixin.model.reqres.ProductIdsReq;
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
	
	@Autowired
	private BrandRepository brandRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
    private static final Logger logger = LoggerFactory.getLogger(QueryProductServiceImpl.class);

	@Override
	public QueryProductRes queryByProductId(String productId) {
		logger.debug("entered queryByProductIds with productId:" + productId);
		QueryProductRes res = new QueryProductRes();
		try
		{
			Product prod   = productRepository.findByProductId(productId);
			if(prod == null)
			{
				res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
				res.setErrorDetails("在数据库中没有找到该商品型号。");
				return res;
			}
			List<ProductStock> productStockList = productStockRepository.findByProductId(prod.getProductId());
			if(productStockList == null || productStockList.size() <= 0)
			{
				res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
				res.setErrorDetails("在数据库中没有找到该商品的颜色尺码等库存信息。");
				return res;
			}
			ProductRes pres = convertFromProdAndProdStockList(prod, productStockList);
			List<ProductRes> proudcts = new ArrayList<ProductRes>();
			proudcts.add(pres);
			res.setProducts(proudcts);
			res.setTotalElements(1);
			res.setTotalPages(1);
			res.setStatus(AuslandApplicationConstants.STATUS_OK);
			return res;
		}
		catch(Exception e)
		{
			res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
			res.setErrorDetails("在数据库中查找商品型号失败："+e.getMessage());
		}
		
		return res;
	}

	private QueryProductRes genQueryProductRes(Page<Product> p)
	{
		QueryProductRes res = new QueryProductRes();
	 
		logger.debug("p.getTotalElements()="+p.getTotalElements());
		logger.debug("p.getTotalPages()="+p.getTotalPages());
		res.setTotalElements((int)p.getTotalElements());
		res.setTotalPages(p.getTotalPages());
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
	
	@Override
	public QueryProductRes queryProductBy(Integer pageNo, Integer pageSize, String brandNames, String matchingString, ProductIdsReq productIds) {
		logger.debug("entered queryProductBy with matchingString:" +  matchingString +" brandNames:"+brandNames+" productIds:"+productIds);
		Pageable pagable = new PageRequest(pageNo, pageSize);
		
		if(productIds != null && productIds.getProductIds() != null && productIds.getProductIds().size() > 0)
		{
			List<String> productIdList = productIds.getProductIds();
			if(productIdList.size() == 1)
			{
			    logger.debug("there is only 1 productId detected.");
				return queryByProductId(productIdList.get(0));
			}
			else
			{
			    logger.debug("there is multiple productIds detected so will only use user selected productIds to search.");
				Page<Product> p  = productRepository.findByProductIdIn(pagable, productIdList);
				return genQueryProductRes(p);
			}
		}
		logger.debug("there is no productId detected, will use the brandnames and matchingstring to search.");
		List<String> brandList = new ArrayList<String>();
		if(!StringUtils.isEmpty(brandNames))
		{
			String[] brands = brandNames.split(",");
			for(String b : brands)
			{
				if(!StringUtils.isEmpty(b))
					brandList.add(b);
			}
		}
		
		if(brandList.size() <= 0 && StringUtils.isEmpty(matchingString))
		{
			Page<Product> p = productRepository.findAll(pagable);
			return genQueryProductRes(p);
		}
		
		if(brandList.size() <= 0)
		{
			Page<Product> p = productRepository.findByProductIdLikeOrProductNameLike(pagable, "%"+matchingString+"%", "%"+matchingString+"%");
			return genQueryProductRes(p);
		}
		
		if(StringUtils.isEmpty(matchingString))
		{
			Page<Product> p = productRepository.findByBrandIn(pagable, brandList);
			return genQueryProductRes(p);
		}
		
		Page<Product> p = productRepository.findByProductIdLikeOrProductNameLikeAndBrandIn(pagable, "%"+matchingString+"%", "%"+matchingString+"%", brandList);
		return genQueryProductRes(p);
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

	@Override
	public List<String> getAllBrand() {
		List<Brand> list = brandRepository.findAll();
		List<String> brandList = new ArrayList<String>();
		for(Brand b : list)
		{
			if(b != null && !StringUtils.isEmpty(b.getBrandName()))
			{
				brandList.add(b.getBrandName());
			}
		}
		return brandList;   
	}

	@Override
	public List<String> getAllCategory() {
		 List<Category> list = categoryRepository.findAll();
		 List<String> categoryList = new ArrayList<String>();
			for(Category b : list)
			{
				if(b != null && !StringUtils.isEmpty(b.getCategoryName()))
				{
					categoryList.add(b.getCategoryName());
				}
			}
			return categoryList;   
	}

	@Override
	public List<String> getProductIdListBy(String  brandNames, String matchingString) {
		List<String> brandList = new ArrayList<String>();
		if(!StringUtils.isEmpty(brandNames))
		{
			String[] brands = brandNames.split(",");
			for(String b : brands)
			{
				if(!StringUtils.isEmpty(b))
					brandList.add(b);
			}
		}
		if(brandList.size() <= 0 && StringUtils.isEmpty(matchingString))
		{
			return productRepository.findProductIdsAll();
		}
        if(brandList.size() <= 0)
           return productRepository.findProductIdsByMatchingString(matchingString);
        if(StringUtils.isEmpty(matchingString))
        	return productRepository.findProductIdsByBrands(brandList);
        return productRepository.findProductIdsByMatchingStringAndBrands(matchingString, brandList);  		
	}

}
