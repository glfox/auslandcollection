package com.ausland.weixin.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
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
	private ValidationUtil validationUtil;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ProductStockRepository productStockRepository;
	
	@Autowired
	private BrandRepository brandRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
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
		productRepository.saveAndFlush(product);
		
		String[] sizeArray = req.getSizes().split(",");
		String[] colorArray = req.getColors().split(",");
		List<ProductStock> psList = new ArrayList<ProductStock>();
		for(String color : colorArray)
		{
			if(StringUtils.isEmpty(color))
				continue;
			for(String size : sizeArray)
			{
				if(StringUtils.isEmpty(size))
					continue;
				ProductStock ps = new ProductStock();
				ps.setColor(color);
				ps.setSize(size);
				ps.setProductId(req.getProductId());
				ps.setStockStatus(AuslandApplicationConstants.STOCKTATUS_INSTOCK);
				psList.add(ps);
			}
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
		if(StringUtils.isEmpty(req.getCategory()))
			return "没有商品类型";
		
		if(StringUtils.isEmpty(req.getCategory()))
			return "没有商品品牌";
				
		Product p = productRepository.findByProductId(req.getProductId());
		if(p == null)
		{
			return "商品Id不存在";
		}
		
		Category c = categoryRepository.findByCategoryName(req.getCategory());
		if(c == null || c.getCategoryName() == null)
		    return "没有商品类型";
		
		Brand b = brandRepository.findByBrandName(req.getBrand());
		if(b == null || b.getBrandName() == null)
			return "没有商品品牌";
		
		return null;
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
		}
		Product p = new Product();
	    p.setLastupdatedDateTime(new Date());
	    p.setBrand(req.getBrand());
	    p.setComments(req.getComments());
	    p.setProductCategory(req.getCategory());
	    p.setProductName(req.getProductName());
	    p.setProductSmallImage(req.getSmallImageBase64EncodeString());
	    p.setProductWeight(req.getProductWeight());
	    p.setStatus(req.getStatus());
	    productRepository.saveAndFlush(p);
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
        if(isFileExists(fileNamewithFullPath) == true)
        {
        	res.setErrorDetails("same excel file already exists in "+fileNamewithFullPath);
        	res.setStatus(AuslandApplicationConstants.STATUS_FAILED); 
        	return res;
        }
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
        
        logger.debug("precheck completed start to save the excel file");
        errorMessage = saveExcelFileInServerDirectory(fileNamewithFullPath, excelFile);
        if(!StringUtils.isEmpty(errorMessage))
        {
        	res.setErrorDetails(errorMessage);
        	res.setStatus(AuslandApplicationConstants.STATUS_FAILED); 
        	return res;
        }
        
        String createdSrc = FilenameUtils.getBaseName(excelFile.getOriginalFilename())+"."+FilenameUtils.getExtension(excelFile.getOriginalFilename());
        List<Product> productList = new ArrayList<Product>();
        for(ProductRes pres : records)
        {
        	Product p = new Product();
        	p.setBrand(pres.getBrand());
        	p.setCreatedDateTime(new Date());
        	p.setCreatedSrc(createdSrc);
        	p.setProductCategory(pres.getCategory());
        	p.setProductId(pres.getProductId());
        	p.setProductName(pres.getProductName());
        	p.setProductWeight(pres.getProductWeight());
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
        			ps.setStockStatus(AuslandApplicationConstants.STOCKTATUS_INSTOCK);
        			ps.setColor(si.getColor());
        			ps.setSize(si.getSize());
        			psList.add(ps);
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
        res.setStatus(AuslandApplicationConstants.STATUS_OK);
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
        			    	int line = i + 2;
        			    	errorMessage.append(";parse line: "+ line + "got errormsg:"+errorMsg);
        			    }
        			}
        			catch(Exception e)
        			{
        				logger.info("caught exception :"+e.getMessage());
        				int line = i +2;
        				errorMessage.append(";parse line: "+ line + "got exception:"+e.getMessage());
        			}
        		}
        		i ++;
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
			String productId = null;
			String color = null;
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
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		p.setBrand(cell.trim());
	        	} 
	        }
	        else if(i == 4)
	        {
	        	//产品名称
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		p.setProductName(cell.trim());
	        	}	        	
	        }
	        else if(i == 5)
	        {
	        	//产品颜色
	        	if(StringUtils.isEmpty(cell))
	        	   return "Does not have color field."; 
        		color = cell.trim();
	        }
	        else if(i == 6)
	        {
	        	//产品尺码
	        	if(StringUtils.isEmpty(cell))
	        	    return "Does not have size field.";
	        	String[] sizes = cell.trim().split(",");
	        	if(sizes.length <= 0)
	        		return "Does not contain size field.";
	        	
	        	if(p.getStock() == null)
        		{
        			List<StockInfo> stockInfoList = new ArrayList<StockInfo>();
        			p.setStock(stockInfoList);
        		}
	        	for(String size : sizes)
	        	{
	        		StockInfo stockInfo = new StockInfo();
		        	stockInfo.setColor(color);
		        	stockInfo.setSize(size);
		        	p.getStock().add(stockInfo);  
	        	}
	        }
	        else if(i == 7)
	        {
	        	//产品毛重
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		p.setProductWeight(cell.trim());
	        	}
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
	
	private boolean isFileExists(String fileNamewithFullPath)
	{
		if(fileNamewithFullPath == null)
		{
			return false;
		}
		try
		{
			File f = new File(fileNamewithFullPath); 
			return f.exists();
		}
		catch(Exception e)
		{
			logger.error("file:"+fileNamewithFullPath +" already exists.");
			return false;
		}
		
	}
	/*@Override
	public UploadZhonghanCourierExcelRes uploadProductFromExcel(MultipartFile excelFile) {
		UploadZhonghanCourierExcelRes res = new UploadZhonghanCourierExcelRes();
        if(excelFile == null || excelFile.isEmpty() || excelFile.getOriginalFilename() == null)
        {
        	res.setErrorDetails("empty excel file");
        	res.setUploadResult(AuslandApplicationConstants.STATUS_FAILED);  
        	return res;
        }
        String fileExtension = FilenameUtils.getExtension(excelFile.getOriginalFilename());
        if(fileExtension == null || (!fileExtension.equalsIgnoreCase("xls") && !fileExtension.equalsIgnoreCase("xlsx")))
        {
        	res.setErrorDetails("chosen file extension is not correct:"+fileExtension);
        	res.setUploadResult(AuslandApplicationConstants.STATUS_FAILED); 
        	return res;
        }
        String fileNamewithFullPath = excelDirectory+FilenameUtils.getBaseName(excelFile.getOriginalFilename())+"."+fileExtension;
        if(isFileExists(fileNamewithFullPath) == true)
        {
        	res.setErrorDetails("same excel file already exists in "+fileNamewithFullPath);
        	res.setUploadResult(AuslandApplicationConstants.STATUS_FAILED); 
        	return res;
        }
        List<Product> records = new ArrayList<Product>();
        String errorMessage = validateExcelFile(excelFile, records);
        if(!StringUtils.isEmpty(errorMessage))
        {
        	res.setErrorDetails(errorMessage);
        	res.setUploadResult(AuslandApplicationConstants.STATUS_FAILED); 
        	return res;
        }
        if(records.size() <= 0)
        {
        	res.setErrorDetails("did not get any valid row from excel.");
        	res.setUploadResult(AuslandApplicationConstants.STATUS_FAILED); 
        	return res;
        }
        logger.debug("precheck completed start to save the excel file");
        errorMessage = saveExcelFileInServerDirectory(fileNamewithFullPath, excelFile);
        if(!StringUtils.isEmpty(errorMessage))
        {
        	res.setErrorDetails(errorMessage);
        	res.setUploadResult(AuslandApplicationConstants.STATUS_FAILED); 
        	return res;
        }
        logger.debug("save records in db.");
        if(records.size() <= AuslandApplicationConstants.DB_BATCH_SIZE)
        {
        	productRepository.save(records);
        	productRepository.flush();
        }
        else
        {
        	//split to batch size and loop 
        	int i = 0;
        	while(i < records.size())
        	{
        		int endIndex = Math.min(i + AuslandApplicationConstants.DB_BATCH_SIZE, records.size());
        		List<Product> sublist = records.subList(i, endIndex);
        		productRepository.save(sublist);
        		productRepository.flush();
        		i = i + AuslandApplicationConstants.DB_BATCH_SIZE;
        	}
        	 
        }

        res.setUploadResult(AuslandApplicationConstants.STATUS_OK);
        return res;
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
	
	private boolean isFileExists(String fileNamewithFullPath)
	{
		if(fileNamewithFullPath == null)
		{
			return false;
		}
		try
		{
			File f = new File(fileNamewithFullPath); 
			return f.exists();
		}
		catch(Exception e)
		{
			logger.error("file:"+fileNamewithFullPath +" already exists.");
			return false;
		}
		
	}

	private String validateExcelFile(MultipartFile excelFile, List<Product> records)
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
            String fileName = FilenameUtils.getBaseName(excelFile.getOriginalFilename());
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
        				List<Product> productList = new ArrayList<Product>();
        			    String errorMsg = provisionOneRow(fileName, currentRow, productList);
        			    if(StringUtils.isEmpty(errorMsg))
        			    {
        			    	//records.add(record);
        			    	if(productList.size() > 0)
        			    	{
        			    		records.addAll(productList);
        			    	}
        			    }
        			    else
        			    {
        			    	int line = i + 2;
        			    	errorMessage.append(";parse line: "+ line + "got errormsg:"+errorMsg);
        			    }
        			}
        			catch(Exception e)
        			{
        				logger.info("caught exception :"+e.getMessage());
        				int line = i +2;
        				errorMessage.append(";parse line: "+ line + "got exception:"+e.getMessage());
        			}
        		}
        		i ++;
        	}
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
	
	private String provisionOneRow(String fileName, Row currentRow, List<Product> records)
	{
	   
		if(currentRow == null)
		{
			return "got empty row";
		}
		Iterator<Cell> cellIterator = currentRow.iterator();
		int i = 0;
		Product p = new Product();
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
	        	//产品图片
	        	continue;
	        }
	        else if(i == 2)
	        {
	        	//产品品牌
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		p.setBrand(cell.trim());
	        	} 
	        }
	        else if(i == 3)
	        {
	        	//产品名称
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		p.setProductName(cell.trim());
	        	}	        	
	        }
	        else if(i == 4)
	        {
	        	//产品编号
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		p.setProductId(cell.trim());
	        	}
	        	
	        }
	        else if(i == 5)
	        {
	        	//产品颜色
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		p.setColor(cell.trim());
	        	} 
	        }
	        else if(i == 6)
	        {
	        	//产品尺码
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		p.setSize(cell.trim());
	        	}
	        }
	        else if(i == 7)
	        {
	        	//产品毛重
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		p.setProductWeight(cell.trim());
	        	}
	        	 
		        p.setCreatedSrc(fileName); 
		        p.setCreatedDateTime(validationUtil.getCurrentDate());
		        p.setStockStatus(AuslandApplicationConstants.STOCKTATUS_INSTOCK);
		        
	        	if(!StringUtils.isEmpty(p.getSize()))
	        	{
	        		String[] sizes = p.getSize().split("/");
	        	    if(sizes != null && sizes.length > 0)
	        	    {
	        	    	for(String size : sizes)
	        	    	{
	        	    		if(!StringUtils.isEmpty(size))
	        	    		{
	        	    			Product p1 = new Product();
	        	    			p1.setBrand(p.getBrand());
	        	    			p1.setColor(p.getColor());
	        	    			p1.setCreatedDateTime(p.getCreatedDateTime());
	        	    			p1.setCreatedSrc(p.getCreatedSrc());
	        	    			p1.setProductId(p.getProductId());
	        	    			p1.setProductName(p.getProductName());
	        	    			p1.setProductWeight(p.getProductWeight());
	        	    			p1.setStockStatus(p.getStockStatus());
	        	    			p1.setSize(size);
	        	    			records.add(p1);
	        	    		}
	        	    	}
	        	    }
	        	}
	        	else
	        	{ 
	        		records.add(p);
	        	}
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
    }*/
}
