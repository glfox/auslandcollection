package com.ausland.weixin.service.impl;

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
import com.ausland.weixin.dao.OrderListFromExcelRepository;
import com.ausland.weixin.model.db.OrderListFromExcel;
import com.ausland.weixin.model.reqres.QueryZhongHuanLastThreeMonthByPhoneNoRes;
import com.ausland.weixin.model.reqres.UploadPackingPhotoRes;
import com.ausland.weixin.model.reqres.UploadZhonghanCourierExcelRes;
import com.ausland.weixin.model.reqres.ZhongHuanFydhDetails;
import com.ausland.weixin.service.ExcelOrderService;
import com.ausland.weixin.util.ValidationUtil;

@Service
public class ExcelOrderServiceImpl implements ExcelOrderService {
	
	@Autowired
	private ValidationUtil validationUtil;
	
	@Autowired
	private OrderListFromExcelRepository orderListFromExcelRepository;
	
	@Value("${upload.order.excel.server.directory}")
	private String excelDirectory;
	
	@Value("${multipart.file.per.size}")
	private int fileMaxSize;
	
	private static final Logger logger = LoggerFactory.getLogger(ExcelOrderServiceImpl.class);

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
					//logger.debug("got order details from db:"+o.toString());
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
					z.setCourierCreatedDateTime(o.getCreatedDateTime().toString());
					list.add(z);
				}
				res.setFydhList(list);
				
			}
			res.setStatus(AuslandApplicationConstants.STATUS_OK);
		}catch(Exception e) {
			res.setErrorDetails("查找时遇到异常："+e.getMessage());
			res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
		}
		return res;
	}

	private boolean isValidFormatType(String formatType) {
		if(StringUtils.isEmpty(formatType)) {
			return false;
		}
		if("mmc".equalsIgnoreCase(formatType) || "ozlana".equalsIgnoreCase(formatType) || "vitamin".equalsIgnoreCase(formatType)) {
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
        	 List<OrderListFromExcel> records = new ArrayList<OrderListFromExcel>();
             String errorMessage = validateExcelFile(excelFile, records, formatType);
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
             String fileNamewithFullPath = excelDirectory+FilenameUtils.getBaseName(excelFile.getOriginalFilename())+"_"+validationUtil.getCurrentDateTimeString()+ "."+fileExtension;
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
             	orderListFromExcelRepository.save(records);
             	orderListFromExcelRepository.flush();
             }
             else
             {
             	//split to batch size and loop 
             	int i = 0;
             	while(i < records.size())
             	{
             		int endIndex = Math.min(i + AuslandApplicationConstants.DB_BATCH_SIZE, records.size());
             		List<OrderListFromExcel> sublist = records.subList(i, endIndex);
             		orderListFromExcelRepository.save(sublist);
             		orderListFromExcelRepository.flush();
             		i = i + AuslandApplicationConstants.DB_BATCH_SIZE;
             	}
             	 
             }
             res.setUploadResult(AuslandApplicationConstants.STATUS_OK);
        }
        catch(Exception e) {
        	logger.error("got exception during uploadOrderExcel"+e.getMessage());
        	res.setErrorDetails(e.getMessage());
        	res.setUploadResult(AuslandApplicationConstants.STATUS_FAILED);
        }
       
        return res;
	}
	
	 private String validateExcelFile(MultipartFile excelFile, List<OrderListFromExcel> records, String formatType){
		if(StringUtils.isEmpty(formatType)) {
        	return "excel file format type is empty.";
        }
		 if("mmc".equalsIgnoreCase(formatType)) {
			 return validateMmcExcelFile(excelFile, records);
		 }
		 if("ozlana".equalsIgnoreCase(formatType)) {
			 return validateOzlanaExcelFile(excelFile, records);
		 }
		 if("vitamin".equalsIgnoreCase(formatType)) {
			 return validateVitaminExcelFile(excelFile, records);
		 }
		 return "excel file format type is not supported"+formatType;
	 }
	 
	 private String validateVitaminExcelFile(MultipartFile excelFile, List<OrderListFromExcel> records){
	        logger.debug("entered validateVitaminExcelFile with excelFile");
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
	        		else
	        		{
	        			try
	        			{
							OrderListFromExcel record = provisionOneRowForVitamin(fileName, currentRow);
							if(record != null && !StringUtils.isEmpty(record.getId()) && StringUtils.isEmpty(record.getErrorMsg())) {
								logger.debug("provisionOneRowForvitamin: add record="+record.toString());
								records.add(record);
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
	        return errorMessage.toString();
		}
    private String validateOzlanaExcelFile(MultipartFile excelFile, List<OrderListFromExcel> records){
        logger.debug("entered validateOzlanaExcelFile with excelFile");
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
						OrderListFromExcel record = provisionOneRowForOzlana(fileName, currentRow);
						if(record != null && !StringUtils.isEmpty(record.getId()) && StringUtils.isEmpty(record.getErrorMsg())) {
							logger.debug("provisionOneRowForOzlana: add record="+record.toString());
							records.add(record);
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
        return errorMessage.toString();
	}

	private String validateMmcExcelFile(MultipartFile excelFile, List<OrderListFromExcel> records){
        logger.debug("entered validateMmcExcelFile with excelFile");
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
						OrderListFromExcel record = provisionOneRowForMmc(fileName, currentRow);
						records.add(record);
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
	
	private OrderListFromExcel provisionOneRowForOzlana(String fileName, Row currentRow)
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
					IdBuf.append(cell.trim());
	        	}
	        }
	        else if(i == 1) {
	        	// 下单日期
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		record.setCreatedDateTime(stringToDate(cell.trim(), "yyyy-MM-dd"));
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
					IdBuf.append("-").append(cell.trim());
	        	}       	
	        }
	        else if(i == 6)
	        {
	        	//尺码
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		String p = record.getProductItems() + "-" + cell.trim();
	        		record.setProductItems(getSubStringByLength(p,128));
					IdBuf.append("-").append(cell.trim());
	        	} 
	        }
	        else if(i == 7)
	        {
	        	//颜色
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		String p = record.getProductItems() + "-" + cell.trim();
	        		record.setProductItems(getSubStringByLength(p,128));
	        	}
	        }
	        else if(i == 8)
	        {
	        	//数量
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		String p = record.getProductItems() + "-" + cell.trim();
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
	
	private OrderListFromExcel provisionOneRowForVitamin(String fileName, Row currentRow)
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
	        if(i == 0)
	        {
	        	//运单号
	        	if(StringUtils.isEmpty(cell))
	        	{
	        		strB.append("没有运单号");
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
	        		record.setCreatedDateTime(stringToDate(cell.trim(), "yyyy/M/d"));
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
	        		record.setReceiverName(getSubStringByLength(getName(cell.trim()),64));
	        		record.setReceiverPhone(getSubStringByLength(getTel(cell.trim()),64));
	        		logger.debug("from cell:" + cell.trim()+ " got receivername:"+record.getReceiverName()+"receiverphone:"+record.getReceiverPhone());
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
	private OrderListFromExcel provisionOneRowForMmc(String fileName, Row currentRow)
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
	        		IdBuf.append(cell.trim());
	        	}
	        }
	        else if(i == 1) {
	        	//交易时间
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		record.setCreatedDateTime(stringToDate(cell.trim(), "yyyy/M/d"));
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
	        		IdBuf.append("-").append(cell.trim());
	        	}
	        }
	        else if(i == 6)
	        {
	        	//规格
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		String p = record.getProductItems()+"-"+cell.trim();
	        		record.setProductItems(getSubStringByLength(p,128));
	        		IdBuf.append("-").append(cell.trim());
	        	}
	        } 
	        else if(i == 7)
	        {
	        	//数量
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		String p = record.getProductItems()+"-"+cell.trim();
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
				if(StringUtils.isEmpty(currentCell.getStringCellValue()) || !currentCell.getStringCellValue().contains(templateHeaders.get(i)))
				{
					logger.debug("got cell value:"+currentCell.getStringCellValue()+"; tempalte header cell value:"+templateHeaders.get(i));
					return false;
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

	@Override
	public UploadPackingPhotoRes uploadPackingPhoto() {
		// TODO Auto-generated method stub
		return null;
	}


}
