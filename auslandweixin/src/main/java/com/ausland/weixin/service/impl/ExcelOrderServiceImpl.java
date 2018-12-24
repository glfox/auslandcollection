package com.ausland.weixin.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ausland.weixin.config.AuslandApplicationConstants;
import com.ausland.weixin.config.AuslandweixinConfig;
import com.ausland.weixin.dao.OrderListFromExcelRepository;
import com.ausland.weixin.dao.ShouhouListFromExcelRepository;
import com.ausland.weixin.model.db.OrderListFromExcel;
import com.ausland.weixin.model.db.ShouhouListFromExcel;
import com.ausland.weixin.model.difou.GetShouhouListRes;
import com.ausland.weixin.model.difou.ShouhouInfo;
import com.ausland.weixin.model.reqres.QueryZhongHuanLastThreeMonthByPhoneNoRes;
import com.ausland.weixin.model.reqres.UploadPackingPhotoRes;
import com.ausland.weixin.model.reqres.UploadZhonghanCourierExcelRes;
import com.ausland.weixin.model.reqres.ZhongHuanFydhDetails;
import com.ausland.weixin.service.ExcelOrderService;
import com.ausland.weixin.util.CodeReader;
import com.ausland.weixin.util.ValidationUtil;

@Service
public class ExcelOrderServiceImpl implements ExcelOrderService {
	
	@Autowired
	private ValidationUtil validationUtil;
	
	@Autowired
	private CodeReader photoReader;
	
	@Autowired
	private OrderListFromExcelRepository orderListFromExcelRepository;
	@Autowired
	private ShouhouListFromExcelRepository shouhouListFromExcelRepository;
	
	
	@Value("${upload.order.excel.server.directory}")
	private String excelDirectory;
	
	@Value("${upload.packing.photo.server.directory}")
	private String packingPhotoDirectory;

	@Value("${multipart.file.per.size}")
	private int fileMaxSize;
	
	private DataFormatter objDefaultFormat = new DataFormatter();
	
	private static final Logger logger = LoggerFactory.getLogger(ExcelOrderServiceImpl.class);
	
	@Override
	public GetShouhouListRes getShouhouListByUserNameOrPhoneNo(String userNameOrPhoneNo) {
		logger.debug("entered getShouhouListByUserNameOrPhoneNo with username or phoneno:"+userNameOrPhoneNo);
		GetShouhouListRes res = new GetShouhouListRes();
		List<ShouhouListFromExcel> list = null;
		try {
			if(validationUtil.isValidChinaMobileNo(userNameOrPhoneNo)) {
				list = shouhouListFromExcelRepository.findByReceiverPhone(userNameOrPhoneNo);
			}else {
				list = shouhouListFromExcelRepository.findByReceiverName(userNameOrPhoneNo);
			}
			if(list == null || list.size() <= 0) {
				res.setReturnCode("1");
				res.setReturnInfo("没有找到对应的售后信息");
				return res;
			}
			ArrayList<ShouhouInfo> dataInfoList = new ArrayList<ShouhouInfo>();
			for(int i = 0; i < list.size(); i ++) {
				ShouhouInfo data = new ShouhouInfo();
				data.setBrandName(list.get(i).getBranName());
				data.setCreationDate(list.get(i).getCreatedDateTime());
				if(StringUtils.isEmpty(list.get(i).getReceiverName())) {
					data.setReceriverName("-");
				}else {
					data.setReceriverName(list.get(i).getReceiverName().replaceAll("([\\u4e00-\\u9fa5]{1})(.*)", "*" + "$2"));
				}
				
				if(StringUtils.isEmpty(list.get(i).getProductItems())) {
					data.setProduct("-");
				}else {
					data.setProduct(list.get(i).getProductItems());
				}
				
				if(StringUtils.isEmpty(list.get(i).getProblem())) {
					data.setProblem("-");
				}else {
					
				}
				
				if(StringUtils.isEmpty(list.get(i).getProgress())) {
					data.setProgress("-");
				}else {
					data.setProgress(list.get(i).getProgress());
				}
				
				if(StringUtils.isEmpty(list.get(i).getComments())) {
					data.setComments("-");
				}else {
					data.setComments(list.get(i).getComments());
				}
				
				if(StringUtils.isEmpty(list.get(i).getCustomerLogisticNo())) {
					data.setCustomerCourierId("-");
				}else {
					data.setCustomerCourierId(list.get(i).getCustomerLogisticNo());
				}
				
				if(StringUtils.isEmpty(list.get(i).getBrandLogisticNo())) {
					data.setBrandCourierId("-");
				}else {
					data.setBrandCourierId(list.get(i).getBrandLogisticNo());
				}
				
				if(StringUtils.isEmpty(list.get(i).getStatus())) {
					data.setStatus("-");
				}else {
					data.setStatus(list.get(i).getStatus());
				}
				dataInfoList.add(data);
			}
			res.setDataInfoList(dataInfoList);
			res.setReturnCode("0");
		}catch(Exception e) {
			res.setReturnCode("1");
			res.setReturnInfo("查找对应售后信息时遇到系统错误");
		}
		return res;
	}
	
	@Override
	public QueryZhongHuanLastThreeMonthByPhoneNoRes getOrderListFromExcel(String userNameOrPhoneNo) {
		// TODO Auto-generated method stub
		logger.debug("entered getOrderListFromExcel with username or phoneno:"+userNameOrPhoneNo);
		
		QueryZhongHuanLastThreeMonthByPhoneNoRes res = new QueryZhongHuanLastThreeMonthByPhoneNoRes();
		try {
			List<OrderListFromExcel> ret = null;
			if(validationUtil.isValidChinaMobileNo(userNameOrPhoneNo)) {
				//query by phone no
				logger.debug("this is the phone number, query by phoneno");
				ret = orderListFromExcelRepository.findByReceiverPhone(userNameOrPhoneNo);
			}
			else if(validationUtil.isValidLooseChineseName(userNameOrPhoneNo)){
				logger.debug("this is not the phone number, query by username");
				ret = orderListFromExcelRepository.findByReceiverName(userNameOrPhoneNo);
			}
			else {
				res.setErrorDetails("用户输入的不是有效的用户名或手机号码");
				res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
				return res;
			}

			if(ret == null || ret.size() <= 0) {
				logger.debug("did not get any orderlist from db with userNameOrPhoneNo:"+userNameOrPhoneNo);
			}else {
				List<ZhongHuanFydhDetails> list = new ArrayList<ZhongHuanFydhDetails>();
				for(int i =0; i < ret.size(); i ++) {
					OrderListFromExcel o = ret.get(i);
					logger.debug("got order details from db:"+o.toString());
					ZhongHuanFydhDetails z = new ZhongHuanFydhDetails();
					z.setCourierCompany(o.getLogisticCompany());
					if(StringUtils.isEmpty(o.getLogisticNo())) {
						z.setCourierNumber(" - ");
					}else {
						z.setCourierNumber(o.getLogisticNo());
					}
					z.setCourierChinaNumber(o.getOrderNo());
					if(!StringUtils.isEmpty(o.getReceiverName())) {
						z.setReceiverName(o.getReceiverName().replaceAll("([\\u4e00-\\u9fa5]{1})(.*)", "*" + "$2"));
					}
					
					z.setProductItems(o.getProductItems());
					z.setCustomStatus(o.getStatus());
					if(o.getCreatedDateTime() != null) {
						z.setCourierCreatedDateTime(o.getCreatedDateTime().toString());
					}
					
					list.add(z);
				}
				logger.debug("got the size of fydh list:"+list.size());
				res.setFydhList(list);
				
			}
			res.setStatus(AuslandApplicationConstants.STATUS_OK);
		}catch(Exception e) {
			logger.error("caught exception during here.");
			res.setErrorDetails("查找时遇到异常："+e.getMessage());
			res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
		}
		
		return res;
	}

	private boolean isValidFormatType(String formatType) {
		if(StringUtils.isEmpty(formatType)) {
			return false;
		}
		if("mmc".equalsIgnoreCase(formatType) || "ozlana".equalsIgnoreCase(formatType) || "vitamin".equalsIgnoreCase(formatType)
		   || "luxury".equalsIgnoreCase(formatType) || "ever".equalsIgnoreCase(formatType) || "tasman".equalsIgnoreCase(formatType)
		   || "fruit".equalsIgnoreCase(formatType) || "shouhou".equalsIgnoreCase(formatType)) {
			return true;
		}
		return false;
	}
	
	@Override
	public UploadZhonghanCourierExcelRes uploadOrderExcel(MultipartFile excelFile, String formatType) {
		UploadZhonghanCourierExcelRes res = new UploadZhonghanCourierExcelRes();
        if(excelFile == null || excelFile.isEmpty() || excelFile.getOriginalFilename() == null)
        {
        	res.setErrorDetails("empty excel file");
        	res.setUploadResult(AuslandApplicationConstants.STATUS_FAILED);  
        	return res;
        }
        if(!isValidFormatType(formatType)) {
        	res.setErrorDetails("excel file format type" + formatType + " is not correct");
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
        try
        {
             logger.debug("start to process excel file, call validateExcelFile()"); 
        	 
             String errorMessage = validateExcelFile(excelFile, formatType);
             if(!StringUtils.isEmpty(errorMessage))
             {
             	res.setErrorDetails(errorMessage);
             	res.setUploadResult(AuslandApplicationConstants.STATUS_FAILED); 
             	return res;
             }
             
             logger.debug("save to db completed start to save the excel file");
             String fileNamewithFullPath = excelDirectory+FilenameUtils.getBaseName(excelFile.getOriginalFilename())+"_"+validationUtil.getCurrentDateTimeString()+ "."+fileExtension;
             errorMessage = saveExcelFileInServerDirectory(fileNamewithFullPath, excelFile);
             if(!StringUtils.isEmpty(errorMessage))
             {
             	res.setErrorDetails(errorMessage);
             	res.setUploadResult(AuslandApplicationConstants.STATUS_FAILED); 
             	return res;
             }
             if ("shouhou".equalsIgnoreCase(formatType)) {
            	 logger.debug("don't delete old records for shouhou");
             }else {
            	 logger.debug("delete old records in db.");
                 orderListFromExcelRepository.deleteByCreatedDateTimeBefore(validationUtil.getThreeMonthEarlyDate());
             }
             
             res.setUploadResult(AuslandApplicationConstants.STATUS_OK);
        }
        catch(Exception e) {
        	logger.error("got exception during uploadOrderExcel"+e.getMessage());
        	e.printStackTrace();
        	res.setErrorDetails(e.getMessage());
        	res.setUploadResult(AuslandApplicationConstants.STATUS_FAILED);
        }
       
        return res;
	}
	
	 private String validateExcelFile(MultipartFile excelFile, String formatType){
		if(StringUtils.isEmpty(formatType)) {
        	return "excel file format type is empty.";
        }
		 if("mmc".equalsIgnoreCase(formatType)) {
			 return validateMmcExcelFile(excelFile);
		 }
		 if("ozlana".equalsIgnoreCase(formatType)) {
			 return validateOzlanaExcelFile(excelFile);
		 }
		 if("vitamin".equalsIgnoreCase(formatType)) {
			 return validateVitaminExcelFile(excelFile);
		 }
		 if("luxury".equalsIgnoreCase(formatType)) {
			 return validateLuxuryExcelFile(excelFile);
		 }
		 if("tasman".equalsIgnoreCase(formatType)) {
			 return validateTasmanExcelFile(excelFile);
		 }
		 if("ever".equalsIgnoreCase(formatType)) {
			 return validateEverExcelFile(excelFile);
		 }
		 if("fruit".equalsIgnoreCase(formatType)) {
			 return validateFruitExcelFile(excelFile);
		 }
		 if("shouhou".equalsIgnoreCase(formatType)) {
			 return validateShouhouExcelFile(excelFile);
		 }
		 return "excel file format type is not supported "+formatType;
	 }
	 
	    private String validateFruitExcelFile(MultipartFile excelFile){
	        logger.debug("entered validateFruitExcelFile with excelFile");
	        List<OrderListFromExcel> records = new ArrayList<OrderListFromExcel>();
			StringBuffer errorMessage = new StringBuffer();
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
	            FormulaEvaluator objFormulaEvaluator = null;
	            
	            try {
	            	if(workbook instanceof HSSFWorkbook) {
	            		objFormulaEvaluator = new HSSFFormulaEvaluator((HSSFWorkbook) workbook);
	            	}
	            	else if(workbook instanceof XSSFWorkbook){
	            		objFormulaEvaluator = new XSSFFormulaEvaluator((XSSFWorkbook) workbook);
	            	}
	            	else {
	            		logger.debug("invalidate excel format");
						errorMessage.append("invalid excel format:"+excelFile.getOriginalFilename());
						return errorMessage.toString();
	            	}
	            }
	            catch(Exception e1) {
	            	logger.debug("validate header returns false, not mmc format");
					errorMessage.append("validate header failed for excel file:"+excelFile.getOriginalFilename());
					return errorMessage.toString();
	            }
				boolean foundHeader = false;
	        	while(iterator.hasNext())
	        	{
	        		Row currentRow = iterator.next();
					if (foundHeader != true){
	                    if(i > 4) {
							logger.debug("validate header returns false, not luxury format");
							errorMessage.append("validate header failed for excel file:"+excelFile.getOriginalFilename());
							return errorMessage.toString();
						}
						if (isValidHeader(currentRow,AuslandweixinConfig.fruitOrderHeaders) == true){
							foundHeader = true;
						}
					}
	        		else
	        		{
	        			try
	        			{
							OrderListFromExcel record = provisionOneRowForFruit(fileName, currentRow, objFormulaEvaluator);
							if(record != null && !StringUtils.isEmpty(record.getId()) && StringUtils.isEmpty(record.getErrorMsg())) {
								logger.debug("provisionOneRowForFruit: add record="+record.toString());
								records.add(record);
								if(records.size() >= AuslandApplicationConstants.DB_BATCH_SIZE)
								{
									orderListFromExcelRepository.save(records);
									orderListFromExcelRepository.flush();
									records.clear();
								}
							}else {
								logger.debug("provisionOneRowForFruit: skip this record");
							}
	        			}
	        			catch(Exception e)
	        			{
	        				logger.info("caught exception :"+e.getMessage());
	        				int line = i + 2;
	        				errorMessage.append("parse line: "+ line + "got exception:"+e.getMessage());
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
	        if(records.size() > 0)
			{
				orderListFromExcelRepository.save(records);
				orderListFromExcelRepository.flush();
				records.clear();
			}
	        
	        return errorMessage.toString();
		}
	    
	    private String validateEverExcelFile(MultipartFile excelFile){
	        logger.debug("entered validateEverExcelFile with excelFile");
	        List<OrderListFromExcel> records = new ArrayList<OrderListFromExcel>();
			StringBuffer errorMessage = new StringBuffer();
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
	            FormulaEvaluator objFormulaEvaluator = null;
	            
	            try {
	            	if(workbook instanceof HSSFWorkbook) {
	            		objFormulaEvaluator = new HSSFFormulaEvaluator((HSSFWorkbook) workbook);
	            	}
	            	else if(workbook instanceof XSSFWorkbook){
	            		objFormulaEvaluator = new XSSFFormulaEvaluator((XSSFWorkbook) workbook);
	            	}
	            	else {
	            		logger.debug("invalidate excel format");
						errorMessage.append("invalid excel format:"+excelFile.getOriginalFilename());
						return errorMessage.toString();
	            	}
	            }
	            catch(Exception e1) {
	            	logger.debug("validate header returns false, not mmc format");
					errorMessage.append("validate header failed for excel file:"+excelFile.getOriginalFilename());
					return errorMessage.toString();
	            }
				boolean foundHeader = false;
	        	while(iterator.hasNext())
	        	{
	        		Row currentRow = iterator.next();
					if (foundHeader != true){
	                    if(i > 4) {
							logger.debug("validate header returns false, not luxury format");
							errorMessage.append("validate header failed for excel file:"+excelFile.getOriginalFilename());
							return errorMessage.toString();
						}
						if (isValidHeader(currentRow,AuslandweixinConfig.everOrderHeaders) == true){
							foundHeader = true;
						}
					}
	        		else
	        		{
	        			try
	        			{
							OrderListFromExcel record = provisionOneRowForEver(fileName, currentRow, objFormulaEvaluator);
							if(record != null && !StringUtils.isEmpty(record.getId()) && StringUtils.isEmpty(record.getErrorMsg())) {
								logger.debug("provisionOneRowForEver: add record="+record.toString());
								records.add(record);
								if(records.size() >= AuslandApplicationConstants.DB_BATCH_SIZE)
								{
									orderListFromExcelRepository.save(records);
									orderListFromExcelRepository.flush();
									records.clear();
								}
							}else {
								logger.debug("provisionOneRowForEver: skip this record");
							}
	        			}
	        			catch(Exception e)
	        			{
	        				logger.info("caught exception :"+e.getMessage());
	        				int line = i + 2;
	        				errorMessage.append("parse line: "+ line + "got exception:"+e.getMessage());
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
	        if(records.size() > 0)
			{
				orderListFromExcelRepository.save(records);
				orderListFromExcelRepository.flush();
				records.clear();
			}
	        
	        return errorMessage.toString();
		}
    

	
	    private String validateTasmanExcelFile(MultipartFile excelFile){
	        logger.debug("entered validateTasmanExcelFile with excelFile");
	        List<OrderListFromExcel> records = new ArrayList<OrderListFromExcel>();
			StringBuffer errorMessage = new StringBuffer();
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
	            FormulaEvaluator objFormulaEvaluator = null;
	            
	            try {
	            	if(workbook instanceof HSSFWorkbook) {
	            		objFormulaEvaluator = new HSSFFormulaEvaluator((HSSFWorkbook) workbook);
	            	}
	            	else if(workbook instanceof XSSFWorkbook){
	            		objFormulaEvaluator = new XSSFFormulaEvaluator((XSSFWorkbook) workbook);
	            	}
	            	else {
	            		logger.debug("invalidate excel format");
						errorMessage.append("invalid excel format:"+excelFile.getOriginalFilename());
						return errorMessage.toString();
	            	}
	            }
	            catch(Exception e1) {
	            	logger.debug("validate header returns false, not mmc format");
					errorMessage.append("validate header failed for excel file:"+excelFile.getOriginalFilename());
					return errorMessage.toString();
	            }
				boolean foundHeader = false;
	        	while(iterator.hasNext())
	        	{
	        		Row currentRow = iterator.next();
					if (foundHeader != true){
	                    if(i > 4) {
							logger.debug("validate header returns false, not luxury format");
							errorMessage.append("validate header failed for excel file:"+excelFile.getOriginalFilename());
							return errorMessage.toString();
						}
						if (isValidHeader(currentRow,AuslandweixinConfig.tasmanOrderHeaders) == true){
							foundHeader = true;
						}
					}
	        		else
	        		{
	        			try
	        			{
							OrderListFromExcel record = provisionOneRowForTasman(fileName, currentRow, objFormulaEvaluator);
							if(record != null && !StringUtils.isEmpty(record.getId()) && StringUtils.isEmpty(record.getErrorMsg())) {
								logger.debug("provisionOneRowForTasman: add record="+record.toString());
								records.add(record);
								if(records.size() >= AuslandApplicationConstants.DB_BATCH_SIZE)
								{
									orderListFromExcelRepository.save(records);
									orderListFromExcelRepository.flush();
									records.clear();
								}
							}else {
								logger.debug("provisionOneRowForTasman: skip this record");
							}
	        			}
	        			catch(Exception e)
	        			{
	        				logger.info("caught exception :"+e.getMessage());
	        				int line = i + 2;
	        				errorMessage.append("parse line: "+ line + "got exception:"+e.getMessage());
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
	        if(records.size() > 0)
			{
				orderListFromExcelRepository.save(records);
				orderListFromExcelRepository.flush();
				records.clear();
			}
	        
	        return errorMessage.toString();
		}
	
	

	private String validateLuxuryExcelFile(MultipartFile excelFile){
	        logger.debug("entered validateLuxuryExcelFile with excelFile");
	        List<OrderListFromExcel> records = new ArrayList<OrderListFromExcel>();
			StringBuffer errorMessage = new StringBuffer();
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
	            FormulaEvaluator objFormulaEvaluator = null;
	            
	            try {
	            	if(workbook instanceof HSSFWorkbook) {
	            		objFormulaEvaluator = new HSSFFormulaEvaluator((HSSFWorkbook) workbook);
	            	}
	            	else if(workbook instanceof XSSFWorkbook){
	            		objFormulaEvaluator = new XSSFFormulaEvaluator((XSSFWorkbook) workbook);
	            	}
	            	else {
	            		logger.debug("invalidate excel format");
						errorMessage.append("invalid excel format:"+excelFile.getOriginalFilename());
						return errorMessage.toString();
	            	}
	            }
	            catch(Exception e1) {
	            	logger.debug("validate header returns false, not mmc format");
					errorMessage.append("validate header failed for excel file:"+excelFile.getOriginalFilename());
					return errorMessage.toString();
	            }
				boolean foundHeader = false;
	        	while(iterator.hasNext())
	        	{
	        		Row currentRow = iterator.next();
					if (foundHeader != true){
	                    if(i > 4){
							logger.debug("validate header returns false, not luxury format");
							errorMessage.append("validate header failed for excel file:"+excelFile.getOriginalFilename());
							return errorMessage.toString();
						}
						if (isValidHeader(currentRow,AuslandweixinConfig.luxuryOrderHeaders) == true){
							foundHeader = true;
						}
					}
	        		else
	        		{
	        			try
	        			{
							OrderListFromExcel record = provisionOneRowForLuxury(fileName, currentRow, objFormulaEvaluator);
							if(record != null && !StringUtils.isEmpty(record.getId()) && StringUtils.isEmpty(record.getErrorMsg())) {
								logger.debug("provisionOneRowForLuxury: add record="+record.toString());
								records.add(record);
								if(records.size() >= AuslandApplicationConstants.DB_BATCH_SIZE)
								{
									orderListFromExcelRepository.save(records);
									orderListFromExcelRepository.flush();
									records.clear();
								}
							}else {
								logger.debug("provisionOneRowForLuxury: skip this record");
							}
	        			}
	        			catch(Exception e)
	        			{
	        				logger.info("caught exception :"+e.getMessage());
	        				int line = i + 2;
	        				errorMessage.append("parse line: "+ line + "got exception:"+e.getMessage());
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
	        if(records.size() > 0)
			{
				orderListFromExcelRepository.save(records);
				orderListFromExcelRepository.flush();
				records.clear();
			}
	        
	        return errorMessage.toString();
		}
    

	
	
	
	private String validateVitaminExcelFile(MultipartFile excelFile){
	        logger.debug("entered validateVitaminExcelFile with excelFile");
	        List<OrderListFromExcel> records = new ArrayList<OrderListFromExcel>();
			StringBuffer errorMessage = new StringBuffer();
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
	            FormulaEvaluator objFormulaEvaluator = null;
	            
	            try {
	            	if(workbook instanceof HSSFWorkbook) {
	            		objFormulaEvaluator = new HSSFFormulaEvaluator((HSSFWorkbook) workbook);
	            	}
	            	else if(workbook instanceof XSSFWorkbook){
	            		objFormulaEvaluator = new XSSFFormulaEvaluator((XSSFWorkbook) workbook);
	            	}
	            	else {
	            		logger.debug("invalidate excel format");
						errorMessage.append("invalid excel format:"+excelFile.getOriginalFilename());
						return errorMessage.toString();
	            	}
	            }
	            catch(Exception e1) {
	            	logger.debug("validate header returns false, not mmc format");
					errorMessage.append("validate header failed for excel file:"+excelFile.getOriginalFilename());
					return errorMessage.toString();
	            }
	        	while(iterator.hasNext())
	        	{
	        		Row currentRow = iterator.next();
	        	    if(i == 0) {
						if(isValidHeader(currentRow,AuslandweixinConfig.vitaminOrderHeaders) == false) {
							logger.debug("validate header returns false, not vitamin format");
							errorMessage.append("validate header failed for excel file:"+excelFile.getOriginalFilename());
							return errorMessage.toString();
						}
						logger.debug("this is the vitamin format excel order file");
					}
						//else if(i < 2676) {
//
//        			}
	        		else
	        		{
	        			try
	        			{
							OrderListFromExcel record = provisionOneRowForVitamin(fileName, currentRow, objFormulaEvaluator);
							if(record != null && !StringUtils.isEmpty(record.getId()) && StringUtils.isEmpty(record.getErrorMsg())) {
								logger.debug("provisionOneRowForvitamin: add record="+record.toString());
								records.add(record);
								if(records.size() >= AuslandApplicationConstants.DB_BATCH_SIZE)
								{
									orderListFromExcelRepository.save(records);
									orderListFromExcelRepository.flush();
									records.clear();
								}
							}else {
								logger.debug("provisionOneRowForvitamin: skip this record");
							}
	        			}
	        			catch(Exception e)
	        			{
	        				logger.info("caught exception :"+e.getMessage());
	        				int line = i + 2;
	        				errorMessage.append("parse line: "+ line + "got exception:"+e.getMessage());
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
	        if(records.size() > 0)
			{
				orderListFromExcelRepository.save(records);
				orderListFromExcelRepository.flush();
				records.clear();
			}
	        
	        return errorMessage.toString();
		}
    private String validateOzlanaExcelFile(MultipartFile excelFile){
        logger.debug("entered validateOzlanaExcelFile with excelFile");
        List<OrderListFromExcel> records = new ArrayList<OrderListFromExcel>();
		StringBuffer errorMessage = new StringBuffer();
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
            FormulaEvaluator objFormulaEvaluator = null;
            
            try {
            	if(workbook instanceof HSSFWorkbook) {
            		objFormulaEvaluator = new HSSFFormulaEvaluator((HSSFWorkbook) workbook);
            	}
            	else if(workbook instanceof XSSFWorkbook){
            		objFormulaEvaluator = new XSSFFormulaEvaluator((XSSFWorkbook) workbook);
            	}
            	else {
            		logger.debug("invalidate excel format");
					errorMessage.append("invalid excel format:"+excelFile.getOriginalFilename());
					return errorMessage.toString();
            	}
            }
            catch(Exception e1) {
            	logger.debug("validate header returns false, not mmc format");
				errorMessage.append("validate header failed for excel file:"+excelFile.getOriginalFilename());
				return errorMessage.toString();
            }
        	while(iterator.hasNext())
        	{
        		Row currentRow = iterator.next();
        		if(i == 0)
        		{
                   //do nothing
        		}else if(i == 1) {
					if(isValidHeader(currentRow,AuslandweixinConfig.ozlanaOrderHeaders) == false) {
						logger.debug("validate header returns false, not ozlana format");
						errorMessage.append("validate header failed for excel file:"+excelFile.getOriginalFilename());
						return errorMessage.toString();
					}
					logger.debug("this is the ozlana format excel order file");
				}
        		else
        		{
        			try
        			{
						OrderListFromExcel record = provisionOneRowForOzlana(fileName, currentRow, objFormulaEvaluator);
						if(record != null && !StringUtils.isEmpty(record.getId()) && StringUtils.isEmpty(record.getErrorMsg())) {
							logger.debug("provisionOneRowForOzlana: add record="+record.toString());
							records.add(record);
							if(records.size() >= AuslandApplicationConstants.DB_BATCH_SIZE)
							{
								orderListFromExcelRepository.save(records);
								orderListFromExcelRepository.flush();
								records.clear();
							}
						}else {
							logger.debug("provisionOneRowForOzlana: skip this record");
						}
        			}
        			catch(Exception e)
        			{
        				logger.info("caught exception :"+e.getMessage());
        				int line = i + 2;
        				errorMessage.append("parse line: "+ line + "got exception:"+e.getMessage());
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
        if(records.size() > 0)
		{
			orderListFromExcelRepository.save(records);
			orderListFromExcelRepository.flush();
			records.clear();
		}
        return errorMessage.toString();
	}

    
    private String validateShouhouExcelFile(MultipartFile excelFile){
        logger.debug("entered validateShouhouExcelFile with excelFile");
		StringBuffer errorMessage = new StringBuffer();
		Workbook workbook = null;
        InputStream  inputStream = null;
        try
        {
        	inputStream = excelFile.getInputStream();
        	workbook = WorkbookFactory.create(inputStream);// new XSSFWorkbook(excelFile.getInputStream());
            FormulaEvaluator objFormulaEvaluator = null;
            try {
            	if(workbook instanceof HSSFWorkbook) {
            		objFormulaEvaluator = new HSSFFormulaEvaluator((HSSFWorkbook) workbook);
            	}
            	else if(workbook instanceof XSSFWorkbook){
            		objFormulaEvaluator = new XSSFFormulaEvaluator((XSSFWorkbook) workbook);
            	}
            	else {
            		logger.debug("invalidate excel format");
					errorMessage.append("invalid excel format:"+excelFile.getOriginalFilename());
					return errorMessage.toString();
            	}
            }
            catch(Exception e1) {
            	logger.debug("validate header returns false, not shouhou format");
				errorMessage.append("validate header failed for excel file:"+excelFile.getOriginalFilename());
				return errorMessage.toString();
            }
            
        	for(int j = 0; j < workbook.getNumberOfSheets(); j ++) {
        		Sheet datatypeSheet = workbook.getSheetAt(j);
        		try {
        			List<ShouhouListFromExcel> records = getRecordsFromSheet(datatypeSheet, objFormulaEvaluator);
        			if(records != null && records.size() > 0) {
        				shouhouListFromExcelRepository.deleteByBranName(datatypeSheet.getSheetName());
        				shouhouListFromExcelRepository.flush();
        				shouhouListFromExcelRepository.save(records);
        				shouhouListFromExcelRepository.flush();
        			}
        		}catch(Exception e3) {
        			logger.error("caught exception during getRecordsFromSheet."+e3.getMessage());
        			return "caught exception during parse sheet.";
        		}
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
    
    private List<ShouhouListFromExcel> getRecordsFromSheet(Sheet datatypeSheet,FormulaEvaluator objFormulaEvaluator){
    	String sheetName = datatypeSheet.getSheetName();
    	logger.debug("entered sheetName=",sheetName);
        Iterator<Row> iterator = datatypeSheet.iterator();
        int i = 0;
        List<ShouhouListFromExcel> records = new ArrayList<ShouhouListFromExcel>();
    	while(iterator.hasNext())
    	{
    		Row currentRow = iterator.next();
    		ShouhouListFromExcel record = new ShouhouListFromExcel();
    		if(i == 0) {
				if(isValidHeader(currentRow,AuslandweixinConfig.shouhouOrderHeaders) == false) {
					logger.error("validate header returns false, not shouhou format for sheet "+sheetName); 
					return null;
				}
				logger.debug("this is the shouhou format excel order file");
			}
    		else
    		{
    			try
    			{   
    				String dateStr = getCellStringByIndex(0,currentRow, objFormulaEvaluator);
					if(!StringUtils.isEmpty(dateStr)) {
						record.setCreatedDateTime(stringToDate(getCellStringByIndex(0,currentRow, objFormulaEvaluator), "yyyyMMdd"));
					}else {
						record.setCreatedDateTime(validationUtil.getCurrentDate());
					}
					record.setBranName(sheetName);
					record.setReceiverName(getCellStringByIndex(1,currentRow, objFormulaEvaluator));
					record.setReceiverPhone(getCellStringByIndex(2,currentRow, objFormulaEvaluator));
					record.setProductItems(getSubStringByLength(getCellStringByIndex(3,currentRow, objFormulaEvaluator),128));
					record.setProblem(getSubStringByLength(getCellStringByIndex(4,currentRow, objFormulaEvaluator),128));
					record.setProgress(getSubStringByLength(getCellStringByIndex(5,currentRow, objFormulaEvaluator),128));
					record.setComments(getCellStringByIndex(7,currentRow, objFormulaEvaluator));
					record.setCustomerLogisticNo(getCellStringByIndex(8,currentRow, objFormulaEvaluator));
					record.setBrandLogisticNo(getCellStringByIndex(9,currentRow, objFormulaEvaluator));
					record.setStatus(getCellStringByIndex(10,currentRow, objFormulaEvaluator));
					if(StringUtils.isEmpty(record.getReceiverName()) && StringUtils.isEmpty(record.getReceiverPhone()) && StringUtils.isEmpty(record.getProductItems())) {
						break;
					}
					records.add(record);
    			}
    			catch(Exception e)
    			{
    				logger.info("caught exception :"+e.getMessage());
    				continue;
    			}
    		}
    		i ++;
    	}
    	return records;
    }
    
    private String getCellStringByIndex(int i, Row currentRow, FormulaEvaluator objFormulaEvaluator) {
    	Cell currentCell = currentRow.getCell(i,AuslandApplicationConstants.xRow.CREATE_NULL_AS_BLANK);
        objFormulaEvaluator.evaluate(currentCell); // This will evaluate the cell, And any type of cell will return string value
        return objDefaultFormat.formatCellValue(currentCell,objFormulaEvaluator);
    }
    
	private String validateMmcExcelFile(MultipartFile excelFile){
        logger.debug("entered validateMmcExcelFile with excelFile");
        List<OrderListFromExcel> records = new ArrayList<OrderListFromExcel>();
		StringBuffer errorMessage = new StringBuffer();
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
            FormulaEvaluator objFormulaEvaluator = null;
           
            try {
            	if(workbook instanceof HSSFWorkbook) {
            		objFormulaEvaluator = new HSSFFormulaEvaluator((HSSFWorkbook) workbook);
            	}
            	else if(workbook instanceof XSSFWorkbook){
            		objFormulaEvaluator = new XSSFFormulaEvaluator((XSSFWorkbook) workbook);
            	}
            	else {
            		logger.debug("invalidate excel format");
					errorMessage.append("invalid excel format:"+excelFile.getOriginalFilename());
					return errorMessage.toString();
            	}
            }
            catch(Exception e1) {
            	logger.debug("validate header returns false, not mmc format");
				errorMessage.append("validate header failed for excel file:"+excelFile.getOriginalFilename());
				return errorMessage.toString();
            }

        	while(iterator.hasNext())
        	{
        		Row currentRow = iterator.next();
        		if(i == 0)
        		{
                   if(isValidHeader(currentRow,AuslandweixinConfig.mmcOrderHeaders) == false) {
						logger.debug("validate header returns false, not mmc format");
						errorMessage.append("validate header failed for excel file:"+excelFile.getOriginalFilename());
						return errorMessage.toString();
					}
					logger.debug("this is the mmc format excel order file");
        		}else{
        			try
        			{
						OrderListFromExcel record = provisionOneRowForMmc(fileName, currentRow, objFormulaEvaluator);
						records.add(record);
						if(records.size() >= AuslandApplicationConstants.DB_BATCH_SIZE)
						{
							orderListFromExcelRepository.save(records);
							orderListFromExcelRepository.flush();
							records.clear();
						}
        			}
        			catch(Exception e)
        			{
        				logger.info("caught exception :"+e.getMessage());
        				int line = i + 1;
        				errorMessage.append("parse line: "+ line + "got exception:"+e.getMessage());
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
        if(records.size() > 0)
		{
			orderListFromExcelRepository.save(records);
			orderListFromExcelRepository.flush();
			records.clear();
		}
        return errorMessage.toString();
	}

	private Date stringToDate(String str, String format) {
	    Date date = null;
	    SimpleDateFormat formatter = new SimpleDateFormat(format);
	    try {
	       date = formatter.parse(str);
	    } catch (ParseException e) {
	        logger.error("caught exception during parsing date string"+e.getMessage());
	    }
	    return date;
	}
	private OrderListFromExcel provisionOneRowForFruit(String fileName, Row currentRow, FormulaEvaluator objFormulaEvaluator)
		{
			if(currentRow == null)
			{
				return null;
			}
			logger.debug("entered provisionOneRowForFruit with filename:"+fileName);
			OrderListFromExcel record = new OrderListFromExcel();
			StringBuffer strB = new StringBuffer();


			for(int i = 0; i < currentRow.getLastCellNum(); i++)
			{
		        Cell currentCell = currentRow.getCell(i,AuslandApplicationConstants.xRow.CREATE_NULL_AS_BLANK);
		        objFormulaEvaluator.evaluate(currentCell); // This will evaluate the cell, And any type of cell will return string value
		        String cell = objDefaultFormat.formatCellValue(currentCell,objFormulaEvaluator);
	            //logger.debug("got cell value:"+cell);
		        if(i == 1)
		        {
		        	//订单编号
		        	if(StringUtils.isEmpty(cell))
		        	{
		        		logger.debug("没有订单编号,skip: "+cell);
		        		continue;
		        	}
		        	else
		            {
		        		record.setOrderNo(getSubStringByLength(cell,64));
						record.setId(getSubStringByLength(cell,64));
		        	}
		        }
		        else if(i == 2) {
		        	// 收货人姓名
		        	if(!StringUtils.isEmpty(cell))
		        	{
		        		record.setReceiverName(getSubStringByLength(cell,64));
		        	} 
		        }
		        else if(i == 4)
		        {
		        	//收货人电话
		        	if(!StringUtils.isEmpty(cell)) 
		        	{
		        		record.setReceiverPhone(getSubStringByLength(cell,64));
		        	}
		        }
		        else if(i == 6) {
					//goodsno
		        	if(!StringUtils.isEmpty(cell))
		        	{
		        		record.setProductItems(getSubStringByLength(cell,16)); 
		        	} 
		        }
			
				else if(i == 7) {
					//sizedesc
		        	if(!StringUtils.isEmpty(cell))
		        	{
		        		record.setProductItems(record.getProductItems() + "-" + getSubStringByLength(cell,16));
		        	} 
		        }
	            else if(i == 8) {
					//quantity
		        	if(!StringUtils.isEmpty(cell))
		        	{
		        		record.setProductItems(record.getProductItems() + "-" + getSubStringByLength(cell,8)); 
		        	} 
		        }
	            else if(i == 10) {
		        	// 日期
		        	if(!StringUtils.isEmpty(cell))
		        	{
		        		record.setCreatedDateTime(stringToDate(cell, "yyyyMMdd"));
		        	} 
		        }
				else if(i == 11) {
					//traffic_company
		        	if(!StringUtils.isEmpty(cell))
		        	{
		        		record.setLogisticCompany(getSubStringByLength(cell,64)); 
		        	} 
		        }
				else if(i == 12) {
					//traffic_no
		        	if(!StringUtils.isEmpty(cell))
		        	{
		        		record.setLogisticNo(getSubStringByLength(cell,64));
		        	} 
					break;
		        }
			}

	        record.setLastupdatedDateTime(validationUtil.getCurrentDate());
			if(!StringUtils.isEmpty(strB.toString())) {
				logger.debug("warning record:"+strB.toString());
			}
	        logger.debug("provisionOneRowForFruit returns with record:"+record.toString());
			return record;
		}	
	
	private OrderListFromExcel provisionOneRowForOzlana(String fileName, Row currentRow, FormulaEvaluator objFormulaEvaluator)
	{
		if(currentRow == null)
		{
			return null;
		}
		logger.debug("entered provisionOneRowForOzlana with filename:"+fileName);
		OrderListFromExcel record = new OrderListFromExcel();
		StringBuffer strB = new StringBuffer();
 
		StringBuffer IdBuf = new StringBuffer();
		for(int i = 0; i < currentRow.getLastCellNum(); i++)
		{
	        Cell currentCell = currentRow.getCell(i, AuslandApplicationConstants.xRow.CREATE_NULL_AS_BLANK);
	        objFormulaEvaluator.evaluate(currentCell); // This will evaluate the cell, And any type of cell will return string value
	        String cell = objDefaultFormat.formatCellValue(currentCell,objFormulaEvaluator);
           //logger.debug("got cell value:"+cell);
	        if(i == 0)
	        {
	        	//订单号
	        	if(StringUtils.isEmpty(cell))
	        	{
	        		strB.append("没有订单号");
	        		record.setErrorMsg(strB.toString());
	        		return record;
	        	}
	        	else
	            {
	        		record.setOrderNo(getSubStringByLength(cell,64));
					IdBuf.append(cell);
	        	}
	        }
	        else if(i == 1) {
	        	// 下单日期
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		record.setCreatedDateTime(stringToDate(cell, "yyyy-MM-dd"));
	        	} 
	        }
	        else if(i == 2) {
	        	// 代理名称
	        }
	        else if(i == 3)
	        {
	        	//收件人
	        	if(StringUtils.isEmpty(cell))
	        	{
	        		strB.append("没有收件人");
	        	}
	        	else
	        	{
	        		record.setReceiverName(getSubStringByLength(cell,64));
	        	}
	        }
	        else if(i == 4) {
	        	// 电话号码
	        	if(StringUtils.isEmpty(cell))
	        	{
	        		strB.append("没有电话号码");
	        	}
	        	else
	        	{
	        		record.setReceiverPhone(getSubStringByLength(cell,64));
	        	}
	        }
	        else if(i == 5)
	        {
	        	//产品编号
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		record.setProductItems(getSubStringByLength(cell,128));
					IdBuf.append("-").append(cell);
	        	}       	
	        }
	        else if(i == 6)
	        {
	        	//尺码
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		String p = record.getProductItems() + "-" + cell;
	        		record.setProductItems(getSubStringByLength(p,128));
					IdBuf.append("-").append(cell);
	        	} 
	        }
	        else if(i == 7)
	        {
	        	//颜色
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		String p = record.getProductItems() + "-" + cell;
	        		record.setProductItems(getSubStringByLength(p,128));
	        	}
	        }
	        else if(i == 8)
	        {
	        	//数量
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		String p = record.getProductItems() + "-" + cell;
	        		record.setProductItems(getSubStringByLength(p,128));
	        	}
	        }
	        else if(i == 9) {
	        	// 快递名称
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		record.setLogisticCompany(getSubStringByLength(cell,64));
	        	}
	        }
	        else if(i == 10)
	        {
	        	//快递单号
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		record.setLogisticNo(getSubStringByLength(cell,64));
	        	}
	        	break;
	        } 
		}

        record.setLastupdatedDateTime(validationUtil.getCurrentDate());
        record.setId(getSubStringByLength(IdBuf.toString(),64));

		if(!StringUtils.isEmpty(strB.toString())) {
			logger.debug("warning record:"+strB.toString());
		}
        logger.debug("provisionOneRowForOzlana returns with record:"+record.toString());
		return record;
	}
	
	private String getSubStringByLength(String str, int maxLength) {
		if(StringUtils.isEmpty(str)) {
			return str;
		}
		if(str.trim().length() > maxLength) {
			return str.trim().substring(0, maxLength);
		}
		return str.trim();
	}
    private OrderListFromExcel provisionOneRowForTasman(String fileName, Row currentRow, FormulaEvaluator objFormulaEvaluator)
	{
		if(currentRow == null)
		{
			return null;
		}
		logger.debug("entered provisionOneRowForTasman with filename:"+fileName);
		OrderListFromExcel record = new OrderListFromExcel();
		StringBuffer strB = new StringBuffer();
		record.setProductItems("");
		record.setLogisticNo("");
		for(int i = 0; i < currentRow.getLastCellNum(); i++)
		{
	        Cell currentCell = currentRow.getCell(i,AuslandApplicationConstants.xRow.CREATE_NULL_AS_BLANK);
	        objFormulaEvaluator.evaluate(currentCell); // This will evaluate the cell, And any type of cell will return string value
	        String cell = objDefaultFormat.formatCellValue(currentCell,objFormulaEvaluator);
            //logger.debug("got cell value:"+cell);
	        if(i == 1)
	        {
	        	//款式
	        	if(!StringUtils.isEmpty(cell))
	            {
	        		record.setProductItems(getSubStringByLength(cell,16));
	        	}
	        	else {
	        		strB.append("没有订单号");
	        		record.setErrorMsg(strB.toString());
	        		return record;
	        	}
	        }
	        else if(i == 3) {
	        	// 名称
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		record.setProductItems(record.getProductItems()+"-"+getSubStringByLength(cell,8));
	        	} 
	        }
	        else if(i == 4) {
	        	// color
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		record.setProductItems(record.getProductItems()+"-"+getSubStringByLength(cell,8));
	        		//record.setCreatedDateTime(stringToDate(cell, "M/d/yy"));
	        	} 
	        }
	        else if(i == 5) {
	        	// size 
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		record.setProductItems(record.getProductItems()+"-"+getSubStringByLength(cell,4));
	        	} 
	        }
	        else if(i == 6)
	        {
	        	// quantity
	        	if(!StringUtils.isEmpty(cell)) 
	        	{
	        		record.setProductItems(record.getProductItems()+"-"+getSubStringByLength(cell,4));
	        	}
	        }
	        else if(i == 8) {
				//receivername
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		record.setReceiverName(getSubStringByLength(cell,16)); 
	        	} 
	        }
	        else if(i == 10) {
				//receiver phone
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		record.setReceiverPhone(getSubStringByLength(cell,16)); 
	        	} 
	        }
	        else if(i == 11) {
				//logistic no
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		record.setLogisticNo(getSubStringByLength(cell,32));  
	        	} 
	        }
			else if(i == 12) {
				//logistic company
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		record.setLogisticCompany(getSubStringByLength(cell,32)); 
	        	} 
	        	break;
	        }
		}
        record.setId(getSubStringByLength(record.getLogisticNo()+"-"+record.getProductItems(),64));
        record.setLastupdatedDateTime(validationUtil.getCurrentDate());
        record.setCreatedDateTime(validationUtil.getCurrentDate());
		if(!StringUtils.isEmpty(strB.toString())) {
			logger.debug("warning record:"+strB.toString());
		}
        logger.debug("provisionOneRowForTasman returns with record:"+record.toString());
		return record;
	}

    private OrderListFromExcel provisionOneRowForEver(String fileName, Row currentRow, FormulaEvaluator objFormulaEvaluator)
	{
		if(currentRow == null)
		{
			return null;
		}
		logger.debug("entered provisionOneRowForEver with filename:"+fileName);
		OrderListFromExcel record = new OrderListFromExcel();
		StringBuffer strB = new StringBuffer();

		for(int i = 0; i < currentRow.getLastCellNum(); i++)
		{
	        Cell currentCell = currentRow.getCell(i,AuslandApplicationConstants.xRow.CREATE_NULL_AS_BLANK);
	        objFormulaEvaluator.evaluate(currentCell); // This will evaluate the cell, And any type of cell will return string value
	        String cell = objDefaultFormat.formatCellValue(currentCell,objFormulaEvaluator);
            //logger.debug("got cell value:"+cell);
	        if(i == 0)
	        {
	        	//订单编号
	        	if(StringUtils.isEmpty(cell))
	        	{
	        		strB.append("没有订单编号");
	        		record.setErrorMsg(strB.toString());
	        		return record;
	        	}
	        	else
	            {
	        		record.setOrderNo(getSubStringByLength(cell,64));
					record.setId(getSubStringByLength(cell,19));
	        	}
	        }
	        else if(i == 1) {
	        	// 日期
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		record.setCreatedDateTime(stringToDate(cell, "M/d/yy"));
	        	} 
	        }
	        else if(i == 4) {
	        	// 收货人姓名
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		record.setReceiverName(getSubStringByLength(cell,64));
	        	} 
	        }
	        else if(i == 5)
	        {
	        	//收货人电话
	        	if(!StringUtils.isEmpty(cell)) 
	        	{
	        		record.setReceiverPhone(getSubStringByLength(cell,64));
	        	}
	        }
	        else if(i == 9) {
				//goodsno
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		record.setProductItems(getSubStringByLength(cell,16)); 
	        	} 
	        }
			else if(i == 10) {
				//goodsname
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		record.setProductItems(record.getProductItems() + "-" + getSubStringByLength(cell,32)); 
	        	} 
	        }
			else if(i == 11) {
				//colordesc
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		record.setProductItems(record.getProductItems() + "-" + getSubStringByLength(cell,16));
	        	} 
	        }
			else if(i == 12) {
				//sizedesc
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		record.setProductItems(record.getProductItems() + "-" + getSubStringByLength(cell,16));
	        	} 
	        }
            else if(i == 14) {
				//quantity
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		record.setProductItems(record.getProductItems() + "-" + getSubStringByLength(cell,8));
	        	} 
	        }
			else if(i == 16) {
				//traffic_company
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		record.setLogisticCompany(getSubStringByLength(cell,64)); 
	        	} 
	        }
			else if(i == 17) {
				//traffic_no
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		record.setLogisticNo(getSubStringByLength(cell,64));
	        	} 
				break;
	        }
		}
		record.setId(getSubStringByLength(record.getId()+"-"+record.getLogisticNo()+"-"+record.getProductItems(),64));
        record.setLastupdatedDateTime(validationUtil.getCurrentDate());
		if(!StringUtils.isEmpty(strB.toString())) {
			logger.debug("warning record:"+strB.toString());
		}
        logger.debug("provisionOneRowForEver returns with record:"+record.toString());
		return record;
	}	

	private OrderListFromExcel provisionOneRowForLuxury(String fileName, Row currentRow, FormulaEvaluator objFormulaEvaluator)
	{
		if(currentRow == null)
		{
			return null;
		}
		logger.debug("entered provisionOneRowForLuxury with filename:"+fileName);
		OrderListFromExcel record = new OrderListFromExcel();
		StringBuffer strB = new StringBuffer();


		for(int i = 0; i < currentRow.getLastCellNum(); i++)
		{
	        Cell currentCell = currentRow.getCell(i,AuslandApplicationConstants.xRow.CREATE_NULL_AS_BLANK);
	        objFormulaEvaluator.evaluate(currentCell); // This will evaluate the cell, And any type of cell will return string value
	        String cell = objDefaultFormat.formatCellValue(currentCell,objFormulaEvaluator);
            //logger.debug("got cell value:"+cell);
	        if(i == 0)
	        {
	        	//订单编号
	        	if(StringUtils.isEmpty(cell))
	        	{
	        		strB.append("没有订单编号");
	        		record.setErrorMsg(strB.toString());
	        		return record;
	        	}
	        	else
	            {
	        		record.setOrderNo(getSubStringByLength(cell,64));
					record.setId(record.getOrderNo());
	        	}
	        }
	        else if(i == 1) {
	        	// 日期
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		record.setCreatedDateTime(stringToDate(cell, "yyyy-MM-dd"));
	        	} 
	        }
	        else if(i == 3) {
	        	// 收货人姓名
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		record.setReceiverName(getSubStringByLength(cell,64));
	        	} 
	        }
	        else if(i == 4)
	        {
	        	//收货人电话
	        	if(!StringUtils.isEmpty(cell)) 
	        	{
	        		record.setReceiverPhone(getSubStringByLength(cell,64));
	        	}
	        }
	        else if(i == 12) {
				//快递单号
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		record.setLogisticNo(getSubStringByLength(cell,64)); 
	        	} 
	        }
			else if(i == 15) {
				//商品名称
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		record.setProductItems(getSubStringByLength(cell,64)); 
	        	} 
	        }
			else if(i == 16) {
				//商品规格
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		record.setProductItems(record.getProductItems() + "-" + getSubStringByLength(cell,40)); 
	        	} 
	        }else if(i == 18) {
				//商品shuliang
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		record.setProductItems(record.getProductItems() + "-" + getSubStringByLength(cell,10)); 
	        	} 
	        	break;
	        }
		}

        record.setLastupdatedDateTime(validationUtil.getCurrentDate());
		if(!StringUtils.isEmpty(strB.toString())) {
			logger.debug("warning record:"+strB.toString());
		}
        logger.debug("provisionOneRowForLuxury returns with new record:"+record.toString());
		return record;
	}

	private OrderListFromExcel provisionOneRowForVitamin(String fileName, Row currentRow, FormulaEvaluator objFormulaEvaluator)
	{
		if(currentRow == null)
		{
			return null;
		}
		logger.debug("entered provisionOneRowForVitamin with filename:"+fileName);
		OrderListFromExcel record = new OrderListFromExcel();
		StringBuffer strB = new StringBuffer();


		for(int i = 0; i < currentRow.getLastCellNum(); i++)
		{
	        Cell currentCell = currentRow.getCell(i,AuslandApplicationConstants.xRow.CREATE_NULL_AS_BLANK);
	        objFormulaEvaluator.evaluate(currentCell); // This will evaluate the cell, And any type of cell will return string value
	        String cell = objDefaultFormat.formatCellValue(currentCell,objFormulaEvaluator);
            //logger.debug("got cell value:"+cell);
	        if(i == 0)
	        {
	        	//运单号
	        	if(StringUtils.isEmpty(cell))
	        	{
	        		strB.append("没有物流单号");
	        		record.setErrorMsg(strB.toString());
	        		return record;
	        	}
	        	else
	            {
	        		record.setLogisticNo(getSubStringByLength(cell,64));
					record.setId(record.getLogisticNo());
	        	}
	        }
	        else if(i == 1) {
	        	// 下单日期
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		if(cell.contains("/")){
	        			return null;
	        		}
	        		try {
	        			record.setCreatedDateTime(stringToDate(cell, "yyyyMMdd"));
	        		}catch(Exception e) {
	        			return null;
	        		}
	        	} 
	        }
	        else if(i == 2) {
	        	// 下单编号
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		record.setOrderNo(getSubStringByLength(cell,64));
	        	} 
	        }
	        else if(i == 3)
	        {
	        	//产品信息
	        	if(!StringUtils.isEmpty(cell)) 
	        	{
	        		record.setProductItems(getSubStringByLength(cell,128));
	        	}
	        }
	        else if(i == 4) {
	        	//ignore
	        }
	        else if(i == 5)
	        {
	        	//收件人信息
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		record.setReceiverName(getSubStringByLength(getName(cell),64));
	        		record.setReceiverPhone(getSubStringByLength(getTel(cell),64));
	        		//logger.debug("from cell:" + cell+ " got receivername:"+record.getReceiverName()+"receiverphone:"+record.getReceiverPhone());
	        	} 
	        }
	        else if(i == 6) {
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		record.setLogisticCompany(getSubStringByLength(cell,64)); 
	        		//logger.debug("from cell:" + cell+ );
	        	} 
	        	break;
	        }
		}

        record.setLastupdatedDateTime(validationUtil.getCurrentDate());
		if(!StringUtils.isEmpty(strB.toString())) {
			logger.debug("warning record:"+strB.toString());
		}
        logger.debug("provisionOneRowForVitamin returns with record:"+record.toString());
		return record;
	}
	
	private String getName(String receiverInfo) {
		char[] chars = receiverInfo.toCharArray();
		boolean isFirstChineseCharacter = false;
		int start = 0;
		int end = 0;
		for(int i = 0; i < chars.length; i ++)
		{
			if(!isFirstChineseCharacter && chars[i] >= 0x4E00 && chars[i] <= 0x9FA5 ) {
				isFirstChineseCharacter = true;
				start = i;
			}
		 
			if(isFirstChineseCharacter && (Character.isDigit(chars[i]) || chars[i] <  0x4E00 || chars[i] > 0x9FA5)) {
				end = i;
				logger.debug("got name from receiverinfo:"+receiverInfo+";start:"+start+";end:"+end);
				return receiverInfo.substring(start, end);
			}
		}
		logger.warn("can not getName from "+receiverInfo);
		return "";
	}
	
	private String getTel(String receiverInfo) {
		char[] chars = receiverInfo.toCharArray();
		for(int i = 0; i < chars.length; i ++)
		{
			if(Character.isDigit(chars[i]) && (i + 11) <= chars.length) {
				String tel = receiverInfo.substring(i, i + 11);
				if(validationUtil.isValidChinaMobileNo(tel)) {
					return tel;
				}
				logger.warn("not the valid china mobile no, can not getTel from "+receiverInfo);
				return "";
			} 
		}
		logger.warn("can not getTel from "+receiverInfo);
		return "";
	}

	private OrderListFromExcel provisionOneRowForMmc(String fileName, Row currentRow, FormulaEvaluator objFormulaEvaluator)
	{
		OrderListFromExcel record = new OrderListFromExcel();
		StringBuffer strB = new StringBuffer();
		if(currentRow == null)
		{
			return null;
		}
		 
		StringBuffer IdBuf = new StringBuffer();
		for(int i = 0; i < currentRow.getLastCellNum(); i++)
		{

	        Cell currentCell = currentRow.getCell(i,AuslandApplicationConstants.xRow.CREATE_NULL_AS_BLANK);
	        objFormulaEvaluator.evaluate(currentCell); // This will evaluate the cell, And any type of cell will return string value
	        String cell = objDefaultFormat.formatCellValue(currentCell,objFormulaEvaluator);
            //logger.debug("got cell value:"+cell);
	        if(i == 0)
	        {
	        	//订单编号
	        	if(StringUtils.isEmpty(cell))
	        	{
	        		strB.append("没有订单编号");
	        		record.setErrorMsg(strB.toString());
	        		return record;
	        	}
	        	else
	            {
	        		record.setOrderNo(getSubStringByLength(cell,64));
	        		IdBuf.append(cell);
	        	}
	        }
	        else if(i == 1) {
	        	//交易时间
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		record.setCreatedDateTime(stringToDate(cell, "yyyy/M/d"));
	        	}
	        }
	        else if(i == 2)
	        {
	        	//收货人
	        	if(StringUtils.isEmpty(cell))
	        	{
	        		strB.append("没有收件人");
	        	}
	        	else
	        	{
	        		record.setReceiverName(getSubStringByLength(cell,64));
	        	}
	        }
	        else if(i == 3) {
	        	// 物流方式
	        	if(!StringUtils.isEmpty(cell)) {
	        		record.setLogisticCompany(getSubStringByLength(cell,64));
	        	}
	        }
	        else if(i == 4)
	        {
	        	//物流单号
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		record.setLogisticNo(getSubStringByLength(cell,64));
	        	}
	        }
	        else if(i == 5)
	        {
	        	//品名
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        	    record.setProductItems(getSubStringByLength(cell,128));
	        		IdBuf.append("-").append(cell);
	        	}
	        }
	        else if(i == 6)
	        {
	        	//规格
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		String p = record.getProductItems()+"-"+cell;
	        		record.setProductItems(getSubStringByLength(p,128));
	        		IdBuf.append("-").append(cell);
	        	}
	        } 
	        else if(i == 7)
	        {
	        	//数量
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		String p = record.getProductItems()+"-"+cell;
	        		record.setProductItems(getSubStringByLength(p,128));
	        	}
	        	break;
	        }
		}
		record.setId(getSubStringByLength(IdBuf.toString(),64));
         
        record.setLastupdatedDateTime(validationUtil.getCurrentDate());
        if(!StringUtils.isEmpty(strB.toString())) {
			logger.debug("warning record:"+strB.toString());
		}
        logger.debug("provisionOneRowForOzlana returns with record:"+record.toString());
		return record;
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
				if ( !StringUtils.isEmpty(templateHeaders.get(i)) ){
					if(StringUtils.isEmpty(currentCell.getStringCellValue()) || !currentCell.getStringCellValue().contains(templateHeaders.get(i)))
					{
						logger.debug("got cell value:"+currentCell.getStringCellValue()+"; tempalte header cell value:"+templateHeaders.get(i));
						return false;
					}
				}
				i ++; 
			}
			else
			{
				 logger.debug("got wrong cell type:"+currentCell.getCellTypeEnum().name());
				 return false;
			}	 
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
	
	private boolean unzipFile(File file) {
		ZipInputStream zis = null;
		try
		{
			zis = new ZipInputStream(new FileInputStream(file));
	        ZipEntry zipEntry = null;
	        byte[] buffer = new byte[4096];
	        while((zipEntry = zis.getNextEntry()) != null){
	            String fileName = zipEntry.getName();
	            if(zipEntry.isDirectory()) {
	            	logger.error("it is a subdirectory, ignore:"+fileName);
	            	continue;
	            }
	            logger.debug("got the fileName:"+fileName); 
	            if(!isValidPhotoSuffix(fileName)) {
	            	logger.error("it is not a valid photo suffix, ignore:"+fileName);
	            	continue;
	            }
	            if(StringUtils.contains(fileName, "/")) {
	            	logger.error("it is the file under a subdirectory, ignore:"+fileName);
	            	continue;
	            }
	            logger.debug("got the filePath:"+file.getPath()); 
	            File newFile = new File(packingPhotoDirectory+"toprocess/" + fileName);
	            FileOutputStream fos = new FileOutputStream(newFile);
	            int len;
	            while ((len = zis.read(buffer)) >= 0) {
	                fos.write(buffer, 0, len);
	            }
	            fos.flush();
	            fos.close();
	 
	        }
	        zis.closeEntry();
	        file.delete();
	        return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			logger.error("caught exception during unzipFile:"+e.getMessage());
		}
		finally {
			if(zis != null) {
				try {
					zis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return false;
	}
	private boolean saveFile(MultipartFile file, String path) {
		FileOutputStream fos = null;
		InputStream is = null;
		try {
			File convFile = new File(path+file.getOriginalFilename());
		    convFile.createNewFile(); 
		    fos = new FileOutputStream(convFile); 
		    is = file.getInputStream();
		    // Create the byte array to hold the data
	       byte[] bytes = new byte[4096];
	       int numRead = 0;
	       while ( (numRead=is.read(bytes, 0, 4096)) >= 0 ) {
	    	   fos.write(bytes, 0, numRead);
	       }
		}
		catch(Exception e) {
			e.printStackTrace();
			logger.error("caught exception during saveFile:"+e.getMessage());
			return false;
		}
		finally {
			if(fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(is != null) {
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return true;
	}

	@Override
	public UploadPackingPhotoRes uploadPackingPhoto(MultipartFile zipFile) {
		UploadPackingPhotoRes res = new UploadPackingPhotoRes();
		int successCount = 0;
		int failedCount = 0;
        try {
        	//
        	if(zipFile != null && !zipFile.isEmpty()) {
        		logger.debug("there is zip file, so save and unzip it.");
        		boolean bRet = saveFile(zipFile, packingPhotoDirectory+"toprocess/");
            	if(!bRet) {
            		res.setErrorDetails("failed to save the zip file:"+zipFile.getOriginalFilename());
            		res.setUploadResult(AuslandApplicationConstants.STATUS_FAILED);
            		return res;
            	}
            	logger.debug("successfully save zipfile :"+packingPhotoDirectory+"toprocess/"+zipFile.getOriginalFilename());
            	 bRet = unzipFile(new File(packingPhotoDirectory+"toprocess/"+zipFile.getOriginalFilename()));
            	 if(!bRet) {
            	 	res.setErrorDetails("failed to unzip the zip file:"+zipFile.getOriginalFilename());
            	 	res.setUploadResult(AuslandApplicationConstants.STATUS_FAILED);
            	 	return res;
            	 }
        	}else {
        		logger.debug("got empty zip file");
        	}
        	File dir = new File(packingPhotoDirectory+"toprocess/");
        	if(dir == null || !dir.isDirectory()) {
        		res.setErrorDetails("The directory does not exist:"+packingPhotoDirectory);
        		res.setUploadResult(AuslandApplicationConstants.STATUS_FAILED);
        		return res;
        	}
        	 String[] filenames = dir.list();
        	 if(filenames == null || filenames.length <= 0) {
        	 	res.setErrorDetails("There is no file under toprocess directory:"+packingPhotoDirectory);
        	 	res.setUploadResult(AuslandApplicationConstants.STATUS_FAILED);
        	 	return res;
        	 }
        	 for(String filename: filenames) {
        	 	if(processPhoto(packingPhotoDirectory+"toprocess/"+filename)) {
        	 		successCount ++;
        	 	}else {
        	 		failedCount ++;
        	 	}
        	 }
        	res.setUploadResult(AuslandApplicationConstants.STATUS_OK);
        }catch(Exception e) {
        	logger.error("caught exception during uploadPackingPhoto:"+e.getMessage());
        	res.setErrorDetails(e.getMessage());
        	res.setUploadResult(AuslandApplicationConstants.STATUS_FAILED);
        }
        
        res.setSuccessUploadedCount(successCount);
        res.setFailedUploadedCount(failedCount);
		return res;
	}

    private boolean processPhoto(String imagePath) {
    	logger.debug("processPhoto entered with imagePath:"+imagePath);
    	try
    	{
    		if(!isValidPhotoSuffix(imagePath)) {
            	return false;
            }
            File f = new File(imagePath);
            if(!f.exists())
            {
            	return false;
            }
            String suffix = imagePath.substring(imagePath.lastIndexOf("."));
        	BufferedImage image = photoReader.cropImage(imagePath);
        	if(image == null) {
        		return false;
        	}
        	String barCode = photoReader.decode(image); 
        	if(StringUtils.isEmpty(barCode) || !validationUtil.isValidZhongHuanTrackNo(barCode)) {
        		return false;
        	}
        	String dst = packingPhotoDirectory+"processed/"+barCode+suffix;
        	
        	return f.renameTo(new File(dst));
  
    	}catch(Exception e) {
    		logger.error("caught exception during processPhoto:"+e.getMessage());
    	}
		return false;
    }
    
    private boolean isValidPhotoSuffix(String imagePath) {
    	if(StringUtils.isEmpty(imagePath))
    		return false;
    	if(imagePath.endsWith(".jpg") || imagePath.endsWith(".png")) {
    		return true;
    	}
    	return false;
    }
     
}
