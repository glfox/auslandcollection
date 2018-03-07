package com.ausland.weixin.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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
import com.ausland.weixin.dao.LogisticPackageRepository;
import com.ausland.weixin.dao.ProductRepository;
import com.ausland.weixin.model.db.LogisticPackage;
import com.ausland.weixin.model.db.Product;
import com.ausland.weixin.model.reqres.UploadProductReq;
import com.ausland.weixin.model.reqres.UploadProductRes;
import com.ausland.weixin.model.reqres.UploadZhonghanCourierExcelRes;
import com.ausland.weixin.service.UploadProductService;
import com.ausland.weixin.util.ValidationUtil;

@Service
public class UploadProductServiceImpl implements UploadProductService{

	@Autowired
	private ValidationUtil validationUtil;
	
	@Autowired
	private ProductRepository productRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(UploadProductServiceImpl.class);
    
	@Value("${upload.product.excel.server.directory}")
	private String excelDirectory;
	
	
	@Override
	public UploadProductRes uploadProduct(UploadProductReq req) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
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
    }
}
