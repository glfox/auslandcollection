package com.ausland.weixin.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ausland.weixin.config.AuslandApplicationConstants;
import com.ausland.weixin.config.AuslandweixinConfig;
import com.ausland.weixin.dao.BrandRepository;
import com.ausland.weixin.dao.CategoryRepository;
import com.ausland.weixin.dao.ProductRepository;
import com.ausland.weixin.dao.ProductStockRepository;
import com.ausland.weixin.model.db.Brand;
import com.ausland.weixin.model.db.Category;
import com.ausland.weixin.model.db.Product;
import com.ausland.weixin.model.db.ProductStock;
import com.ausland.weixin.model.reqres.CreateProductReq;
import com.ausland.weixin.model.reqres.CreateProductRes;
import com.ausland.weixin.model.reqres.GlobalRes;
import com.ausland.weixin.model.reqres.ProductRes;
import com.ausland.weixin.model.reqres.StockInfo;
import com.ausland.weixin.model.reqres.UpdateProductStockReq;
import com.ausland.weixin.service.ProductService;
import com.ausland.weixin.util.ValidationUtil;

@Service
public class ProductServiceImpl implements ProductService{

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ProductStockRepository productStockRepository;
	
	@Autowired
	private BrandRepository brandRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private AuslandweixinConfig config;
	
	@Autowired
	private ValidationUtil validationUtil;
		
	private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
    
	@Value("${upload.product.excel.server.directory}")
	private String excelDirectory;

	@Override
	public CreateProductRes createProduct(CreateProductReq req) {
		CreateProductRes res = new CreateProductRes();
		String ret = validateCreateProductReq(req);
		if(!StringUtils.isEmpty(ret))
		{
			res.setErrorDetails(ret);
			res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
			return res;
		}
		
		Product product = new Product();
		product.setBrand(req.getBrand());
		product.setComments(req.getComments());
		product.setCreatedDateTime(new Date());
		product.setProductCategory(req.getCategory());
		product.setProductId(req.getProductId());
		product.setProductSmallImage(req.getSmallImageBase64EncodeString());
		product.setProductWeight(req.getProductWeight());
		product.setStatus(req.getStatus());
		product.setSizeCategory(req.getSizeCategory());
		productRepository.saveAndFlush(product);
		
		String[] sizeArray = req.getSizes().split(",");
		String[] colorArray = req.getColors().split(",");
		String[] stockStatusArray = new String[sizeArray.length];
		for(int i = 0; i < sizeArray.length; i ++)
		{
			stockStatusArray[i] = AuslandApplicationConstants.STOCKTATUS_INSTOCK;
		}
		String stockStatusStr = String.join(",", stockStatusArray);
		List<ProductStock> psList = new ArrayList<ProductStock>();
		for(String color : colorArray)
		{
			if(StringUtils.isEmpty(color))
				continue;
			ProductStock ps = new ProductStock();
			ps.setColor(color);
			ps.setSize(req.getSizes());
			ps.setSizeCategory(req.getSizeCategory());
			ps.setProductId(req.getProductId());
			ps.setStockStatus(stockStatusStr);
			psList.add(ps); 
		}
		productStockRepository.save(psList);
		res.setStatus(AuslandApplicationConstants.STATUS_OK);
		
		logger.debug("successfully created  the product:"+req.getProductId());
		return res;
	}
		
	private String validateUpdateProductReq(CreateProductReq req)
	{
		if(req == null || StringUtils.isEmpty(req.getProductId()))
		{
			return "没有商品Id";
		} 
		if( !StringUtils.isEmpty(req.getSizeCategory()) || !StringUtils.isEmpty(req.getColors()) || !StringUtils.isEmpty(req.getSizes()))
		{
			if(!StringUtils.isEmpty(req.getColors()) && !StringUtils.isEmpty(req.getSizes()))
			{
				return null;
			}
		}
		return "商品尺码或者颜色变化必须给出尺码和颜色";
	}
	
	private String validateCreateProductReq(CreateProductReq req)
	{
		if(req == null || StringUtils.isEmpty(req.getProductId()))
		{
			return "没有商品Id";
		}
		if(StringUtils.isEmpty(req.getSizes()) || StringUtils.isEmpty(req.getColors()))
		{
			return "没有商品尺码和颜色";
		}
		if(StringUtils.isEmpty(req.getSizeCategory()) || !config.supportedSizeCategoryList.contains(req.getSizeCategory()))
		{
			return "没有商品尺码类型";
		}
		String[] sizes = req.getSizes().split(",");
		for(String size : sizes)
		{
	        if(!config.getSupportedSizeCategoryMap().get(req.getSizeCategory()).contains(size))
	        {
	        	return "尺码"+size+" 不属于尺码类型："+req.getSizeCategory();
	        }
		}
		if(StringUtils.isEmpty(req.getCategory()))
			return "没有商品类型";
		
		if(StringUtils.isEmpty(req.getCategory()))
			return "没有商品品牌";
				
		Product p = productRepository.findByProductId(req.getProductId());
		if(p != null)
		{
			return "商品Id已经存在";
		}
		
		Category c = categoryRepository.findByCategoryName(req.getCategory());
		if(c == null || c.getCategoryName() == null)
		    return "没有商品类型";
		
		Brand b = brandRepository.findByBrandName(req.getBrand());
		if(b == null || b.getBrandName() == null)
			return "没有商品品牌";
		
		return null;
	}

	@Override
	public GlobalRes createBrand(String brandName) {
		GlobalRes res = new GlobalRes();
		Brand b = brandRepository.findByBrandName(brandName);
		if(b != null)
		{
			res.setErrorDetails("品牌已经存在");
			res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
			return res;
		}
		b = new Brand();
		b.setBrandName(brandName);
		brandRepository.save(b);
		res.setStatus(AuslandApplicationConstants.STATUS_OK);
		return res;
	}

	@Override
	public GlobalRes createCategory(String category) {
		GlobalRes res = new GlobalRes();
		Category c = categoryRepository.findByCategoryName(category);
		if(c != null)
		{
			res.setErrorDetails("类型已经存在");
			res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
			return res;
		}
		c  = new Category();
		c.setCategoryName(category);
		categoryRepository.save(c);
		res.setStatus(AuslandApplicationConstants.STATUS_OK);
		return res;
	}

	@Override
	public GlobalRes deleteBrand(String brand) {
		GlobalRes res = new GlobalRes();
		Brand b = brandRepository.findByBrandName(brand);
		if(b == null)
		{
			res.setErrorDetails("品牌不存在");
			res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
			return res;
		}
		 
		brandRepository.delete(b);
		res.setStatus(AuslandApplicationConstants.STATUS_OK);
		return res;
	}

	@Override
	public GlobalRes deletedCategory(String category) {
		GlobalRes res = new GlobalRes();
		Category c = categoryRepository.findByCategoryName(category);
		if(c == null)
		{
			res.setErrorDetails("类型不存在");
			res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
			return res;
		}
		 
		categoryRepository.delete(c);
		res.setStatus(AuslandApplicationConstants.STATUS_OK);
		return res;
	}

	@Override
	public GlobalRes deleteProduct(String productId) {
		GlobalRes res = new GlobalRes();
		Product p = productRepository.findByProductId(productId);
		if(p == null)
		{
			res.setErrorDetails("商品不存在");
			res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
			return res;
		}
		 
		productRepository.delete(p);
		List<ProductStock> psList = productStockRepository.findByProductId(productId);
		if(psList != null && psList.size() > 0)
		{
			productStockRepository.delete(psList);
		}
		res.setStatus(AuslandApplicationConstants.STATUS_OK);
		return res;
	}

	@Override
	public GlobalRes updateProductStock(UpdateProductStockReq req) {
		GlobalRes res = new GlobalRes();
		if(req == null || StringUtils.isEmpty(req.getProductId()) || req.getStock() == null || req.getStock().size() < 0)
		{
			res.setErrorDetails("没有库存需要修改");
			res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
			return res;
		}
		Product p = productRepository.findByProductId(req.getProductId());
		if(p == null)
		{
			res.setErrorDetails("数据库中没有找到该商品");
			res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
			return res;
		}
		productStockRepository.deleteByProductId(req.getProductId());
		List<ProductStock> list = new ArrayList<ProductStock>();
		for(StockInfo s: req.getStock())
		{
			ProductStock ps = new ProductStock();
			ps.setColor(s.getColor());
			ps.setProductId(req.getProductId()); 
			ps.setSize(s.getSize());
		    ps.setStockStatus(s.getStockStatus());
			list.add(ps);
		}
		productStockRepository.save(list);
		res.setStatus(AuslandApplicationConstants.STATUS_OK);
		return res;
	}

	@Override
	public GlobalRes updateProduct(CreateProductReq req) {
		GlobalRes res = new GlobalRes();
	
		String str = validateUpdateProductReq(req);
		if(!StringUtils.isEmpty(str))
		{
			res.setErrorDetails(str);
			res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
			return res;
		}
		Product p = productRepository.findByProductId(req.getProductId());
		if(p == null)
		{
			res.setErrorDetails("没有在数据库中找到商品型号"+req.getProductId());
			res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
			return res;
		}
	    p.setLastupdatedDateTime(new Date());
	    if(!StringUtils.isEmpty(req.getBrand()))
	        p.setBrand(req.getBrand());
	    if(!StringUtils.isEmpty(req.getComments()))
	        p.setComments(req.getComments());
	    if(!StringUtils.isEmpty(req.getCategory()))
	        p.setProductCategory(req.getCategory());
	    if(!StringUtils.isEmpty(req.getProductName()))
	        p.setProductName(req.getProductName());
	    if(!StringUtils.isEmpty(req.getSmallImageBase64EncodeString()))
	        p.setProductSmallImage(req.getSmallImageBase64EncodeString());
	    if(!StringUtils.isEmpty(req.getProductWeight()))
	        p.setProductWeight(req.getProductWeight());
	    if(!StringUtils.isEmpty(req.getStatus()))
	        p.setStatus(req.getStatus());
	    boolean deleteProductStockFlag = false;
	    if(!StringUtils.isEmpty(req.getSizeCategory()) || !StringUtils.isEmpty(req.getColors()) || !StringUtils.isEmpty(req.getSizes()))
	    {
	    	p.setSizeCategory(req.getSizeCategory());
		    deleteProductStockFlag = true;
	    }
	    productRepository.saveAndFlush(p);
	    //List<ProductStock> existingProductStockList = productStockRepository.findByProductId(req.getProductId());
	    //productStockRepository.deleteByProductId(req.getProductId());
	    if(deleteProductStockFlag == true)
	    {
	        productStockRepository.deleteByProductId(req.getProductId());
	    	String[] sizeArray = req.getSizes().split(",");
	    	String[] colorArray = req.getColors().split(",");
			String[] stockStatusArray = new String[sizeArray.length];
			for(int i = 0; i < sizeArray.length; i ++)
			{
				stockStatusArray[i] = AuslandApplicationConstants.STOCKTATUS_INSTOCK;
			}
			String stockStatusStr = String.join(",", stockStatusArray);
			List<ProductStock> psList = new ArrayList<ProductStock>();
			for(String color : colorArray)
			{
				if(StringUtils.isEmpty(color))
					continue;
				ProductStock ps = new ProductStock();
				ps.setColor(color);
				ps.setSize(req.getSizes());
				ps.setProductId(req.getProductId());
				ps.setSizeCategory(req.getSizeCategory());
				ps.setStockStatus(stockStatusStr);
				psList.add(ps);
				 
			}
			productStockRepository.save(psList);
	    }
	    res.setStatus(AuslandApplicationConstants.STATUS_OK);
	    return res;
	}
	
	@Override
	public GlobalRes uploadProductFromExcel(MultipartFile excelFile) 
	{
		GlobalRes res = new GlobalRes();
        if(excelFile == null || excelFile.isEmpty() || excelFile.getOriginalFilename() == null)
        {
        	res.setErrorDetails("empty excel file");
        	res.setStatus(AuslandApplicationConstants.STATUS_FAILED);  
        	return res;
        }
        String fileExtension = FilenameUtils.getExtension(excelFile.getOriginalFilename());
        if(fileExtension == null || (!fileExtension.equalsIgnoreCase("xls") && !fileExtension.equalsIgnoreCase("xlsx")))
        {
        	res.setErrorDetails("chosen file extension is not correct:"+fileExtension);
        	res.setStatus(AuslandApplicationConstants.STATUS_FAILED); 
        	return res;
        }
        String fileNamewithFullPath = excelDirectory+FilenameUtils.getBaseName(excelFile.getOriginalFilename())+"."+fileExtension;

        List<ProductRes> records = new ArrayList<ProductRes>();
        String errorMessage = validateExcelFile(excelFile, records);
        if(!StringUtils.isEmpty(errorMessage))
        {
        	res.setErrorDetails(errorMessage);
        	res.setStatus(AuslandApplicationConstants.STATUS_FAILED); 
        	return res;
        }
        if(records.size() <= 0)
        {
        	res.setErrorDetails("did not get any valid row from excel.");
        	res.setStatus(AuslandApplicationConstants.STATUS_FAILED); 
        	return res;
        }
        
        try
        {
        	String createdSrc = FilenameUtils.getBaseName(excelFile.getOriginalFilename())+"."+FilenameUtils.getExtension(excelFile.getOriginalFilename());
            List<Product> productList = new ArrayList<Product>();
            logger.debug("start to save in db.");
            Set<String> brandSet = new HashSet<String>();
            Set<String> categorySet = new HashSet<String>();
            for(ProductRes pres : records)
            {
            	logger.debug("productRes: "+pres.toString());
            	Product p = new Product();
            	p.setBrand(pres.getBrand());
            	brandSet.add(pres.getBrand());
            	categorySet.add(pres.getCategory());
            	p.setCreatedDateTime(new Date());
            	p.setCreatedSrc(createdSrc);
            	p.setSizeCategory(pres.getSizeCategory());
            	p.setProductCategory(pres.getCategory());
            	p.setProductId(pres.getProductId());
            	p.setProductName(pres.getProductName());
            	p.setProductWeight(pres.getProductWeight());
            	p.setProductUniPrice(new BigDecimal(pres.getProductUnitPrice()));
            	p.setProvider(AuslandApplicationConstants.PRODUCT_PROVIDER_AUSLAND);
            	productList.add(p);
            	List<StockInfo> l = pres.getStock();
            	if(l != null && l.size() > 0)
            	{
            		productStockRepository.deleteByProductId(pres.getProductId());
            		List<ProductStock> psList = new ArrayList<ProductStock>();
            		for(StockInfo si : l)
            		{
            			ProductStock ps = new ProductStock();
            			ps.setProductId(pres.getProductId());
            			ps.setStockStatus(si.getStockStatus());
            			ps.setColor(si.getColor());
            			ps.setSize(si.getSize());
            			ps.setSizeCategory(pres.getSizeCategory());
            			psList.add(ps);
            			logger.debug(ps.toString());
            		}
            		productStockRepository.save(psList);
            		productStockRepository.flush();
            	}
            }
            if(productList.size() <= AuslandApplicationConstants.DB_BATCH_SIZE)
            {
            	productRepository.save(productList);
            	productRepository.flush();
            }
            else
            {
            	//split to batch size and loop 
            	int i = 0;
            	while(i < productList.size())
            	{
            		int endIndex = Math.min(i + AuslandApplicationConstants.DB_BATCH_SIZE, records.size());
            		List<Product> sublist = productList.subList(i, endIndex);
            		productRepository.save(sublist);
            		productRepository.flush();
            		i = i + AuslandApplicationConstants.DB_BATCH_SIZE;
            	}
            }
            
            List<Brand> bList = new ArrayList<Brand>();
            for(String brandName : brandSet)
            {
            	Brand b = brandRepository.findByBrandName(brandName);
            	if(b != null)
            		continue;
            	b = new Brand();
            	b.setBrandName(brandName);
            	bList.add(b);
            }
            if(bList.size() > 0)
               brandRepository.save(bList);
            
            List<Category> cList = new ArrayList<Category>();
            for(String categoryName : categorySet)
            {
            	Category c = categoryRepository.findByCategoryName(categoryName);
            	if(c != null)
            		continue;
            	c = new Category();
            	c.setCategoryName(categoryName);
            	cList.add(c);
            }
            if(cList.size() > 0)
            	categoryRepository.save(cList);
            
            logger.debug("save in db completed, start to save the excel file....");
            saveExcelFileInServerDirectory(fileNamewithFullPath+validationUtil.getCurrentDateTimeString(), excelFile);
            
            res.setStatus(AuslandApplicationConstants.STATUS_OK);
            return res;
        }
        catch(Exception e)
        {
        	res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
        	res.setErrorDetails("got exception during save excel file in db:"+e.getMessage());
        }
        return res;
	}
	
	private String validateExcelFile(MultipartFile excelFile,  List<ProductRes> records)
	{
		StringBuffer errorMessage = new StringBuffer();
		//Workbook[] wbs = new Workbook[] { new HSSFWorkbook(), new XSSFWorkbook() };
		Workbook workbook = null;
        InputStream  inputStream = null;
        try
        {
        	inputStream = excelFile.getInputStream();
        	workbook = WorkbookFactory.create(inputStream);// new XSSFWorkbook(excelFile.getInputStream());
        	Sheet datatypeSheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = datatypeSheet.iterator();
            int i = 0;
            HashMap<String, ProductRes> map = new HashMap<String, ProductRes>();
        	while(iterator.hasNext())
        	{
        		Row currentRow = iterator.next();
        		if(i == 0)
        		{
        			if(isValidHeader(currentRow, AuslandweixinConfig.productUploadExcelHeaders) == false)
        			{
        				logger.debug("validate header returns false.");
        				errorMessage.append("validate header failed for excel file:"+excelFile.getOriginalFilename());
        				return errorMessage.toString();
        			}
        		}
        		else
        		{
        			try
        			{
        			    String errorMsg = provisionOneRow(currentRow, map);
        			    if(!StringUtils.isEmpty(errorMsg))
        			    {
        			    	int line = i + 1;
        			    	errorMessage.append(";parse line: "+ line + " got errormsg:"+errorMsg);
        			    }
        			}
        			catch(Exception e)
        			{
        				logger.info("caught exception :"+e.getMessage());
        				int line = i + 1;
        				errorMessage.append(";parse line: "+ line + " got exception:"+e.getMessage());
        			}
        		}
        		i ++;
        	}
        	Set<String> productSet = map.keySet();
        	List<String> proList = productRepository.findProductIdByProductIdIn(productSet);
        	if(proList != null && proList.size() > 0)
        	{
        		logger.debug("found already existing productId in the db:"+ ToStringBuilder.reflectionToString(proList));
        		errorMessage.append("found already existing productId in the db:"+ ToStringBuilder.reflectionToString(proList));
        	}
        	records.addAll(map.values());
        	logger.debug("added "+ records.size()+" products to the result list.");
        }
        catch(Exception e)
        {
        	logger.error("got exception:"+e.getMessage());
        	errorMessage.append("got exception:"+e.getMessage());
        }
        finally
        {
        	if(inputStream != null)
				try {
					inputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	if(workbook != null)
				try {
					workbook.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        }
        return errorMessage.toString();
	}
	
	private String provisionOneRow(Row currentRow, HashMap<String, ProductRes> recordsMap)
	{
	   
		if(currentRow == null)
		{
			return "got empty row";
		}
		Iterator<Cell> cellIterator = currentRow.iterator();
		int i = 0;
		ProductRes p = null;
		String productId = null;
		String color = null;
		while(cellIterator.hasNext() && i <= AuslandweixinConfig.productUploadExcelHeaders.size())
		{
	        i ++;
	        Cell currentCell = cellIterator.next();
	        String cell = "";
			if(currentCell.getCellTypeEnum() == CellType.STRING)
			{
				cell = currentCell.getStringCellValue();
				logger.debug("cell "+i +":"+cell);
			}
			else if(currentCell.getCellTypeEnum() == CellType.NUMERIC)
			{
				cell = currentCell.getNumericCellValue() +"";
				logger.debug("cell "+i +":"+cell);
			}
	        if(i == 1)
	        {
	        	//产品编号
	        	if(StringUtils.isEmpty(cell))
	        		return "Does not contain productId";
	        	productId = cell.trim();
	        	if(recordsMap.containsKey(productId))
	        	{
	        		p = recordsMap.get(productId);
	        	}
	        	else
	        	{
	        		p = new ProductRes();
	        		p.setProductId(productId);
	        		recordsMap.put(productId, p);
	        	}
	        }
	        else if(i == 2)
	        {
	        	//产品图片
	        	continue;	        	
	        }
	        else if(i == 3)
	        {
	        	//产品品牌
	        	if(StringUtils.isEmpty(cell))
	        	   return "Does not have product brand.";
	        	p.setBrand(cell.trim());
	        	  
	        }
	        else if(i == 4)
	        {
	        	//产品category
	        	if(StringUtils.isEmpty(cell))
	        	    return "Does not have product Category.";
	        	p.setCategory(cell.trim());
	        }
	        else if(i == 5)
	        {
	        	//产品名称
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		p.setProductName(cell.trim());
	        	}	        	
	        }
	        else if(i == 6)
	        {
	        	//产品颜色
	        	if(StringUtils.isEmpty(cell))
	        	   return "Does not have color field."; 
        		color = cell.trim();
	        }
	        else if(i == 7)
	        {
	        	//产品尺码
	        	if(StringUtils.isEmpty(cell))
	        	    return "Does not have size field.";
	        	String[] sizes = cell.trim().split("/");
	        	if(sizes.length <= 0)
	        		return "Does not contain size field.";
	        	String[] stockStatusArray = new String[sizes.length];
	        	p.setSizeCategory(config.getFromSizeToSizeCategoryMap().get(sizes[0]));
	        	for(int j = 0; j < sizes.length; j ++)
	        	{
	        		stockStatusArray[j] = AuslandApplicationConstants.STOCKTATUS_INSTOCK;
	        	}
	        	if(p.getStock() == null)
        		{
        			List<StockInfo> stockInfoList = new ArrayList<StockInfo>();
        			p.setStock(stockInfoList);
        		}
	        	StockInfo stockInfo = new StockInfo();
	        	stockInfo.setColor(color);
	        	stockInfo.setSize(String.join(",", sizes));
	        	stockInfo.setStockStatus(String.join(",", stockStatusArray));
	        	p.getStock().add(stockInfo);
	        }
	        else if(i == 8)
	        {
	        	//产品毛重
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		p.setProductWeight(cell.trim());
	        	}
	        	
	        }
	        else if(i == 9)
	        {
	        	try
	        	{
	        		BigDecimal price = new BigDecimal(cell.trim());
	        		
	        	}
	        	catch(Exception e)
	        	{
	        		return "价格："+cell+"格式不正确";
	        	}
	        	 
	        	p.setProductUnitPrice(cell.trim());
	         
	        	return null;
	        }
		}
        return null;
	}
	
	 private boolean isValidHeader(Row currentRow, List<String> templateHeaders)
	    {
	    	if(currentRow == null || currentRow.iterator() == null || templateHeaders == null || templateHeaders.size() <= 0)
				return false;
			Iterator<Cell> cellIterator = currentRow.iterator();
			int i = 0;
			while(cellIterator.hasNext() && i < templateHeaders.size())
			{
				Cell currentCell = cellIterator.next();
				if(currentCell.getCellTypeEnum() == CellType.STRING)
				{
					if(StringUtils.isEmpty(currentCell.getStringCellValue()) || !currentCell.getStringCellValue().contains(templateHeaders.get(i)))
					{
						logger.debug("got cell value:"+currentCell.getStringCellValue()+"; tempalte header cell value:"+templateHeaders.get(i));
						return false;
					}
					
				}
				else
				{
					 logger.debug("got wrong cell type:"+currentCell.getCellTypeEnum().name());
					 return false;
				}
				i ++;	 
			}
			if(i < templateHeaders.size())
				return false;
			return true;
	    }
	 
	private String saveExcelFileInServerDirectory(String fileName, MultipartFile csvFile)
	{
		try
		{
		    if(csvFile.isEmpty())
		    	return "excel file:"+ csvFile.getOriginalFilename() + " is empty.";
		    byte[] bytes = csvFile.getBytes();
		    Path path = Paths.get(fileName);
		    Files.write(path, bytes);
		    return null;
		}
		catch(Exception e)
		{
			return "save file:"+ csvFile.getOriginalFilename()+" to " + fileName + " got exception:"+e.getMessage();
		}
	} 

}
