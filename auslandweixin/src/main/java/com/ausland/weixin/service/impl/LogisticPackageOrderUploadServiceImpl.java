package com.ausland.weixin.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ausland.weixin.config.AuslandweixinConfig;
import com.ausland.weixin.controller.LogisticPackageOrderUploadController;
import com.ausland.weixin.model.reqres.LogisticPackageRecord;
import com.ausland.weixin.model.reqres.UploadLogisticPackageRes;
import com.ausland.weixin.service.LogisticPackageOrderUploadService;
import com.ausland.weixin.util.ValidationUtil;
 

@Service
public class LogisticPackageOrderUploadServiceImpl implements LogisticPackageOrderUploadService{

	private static final Logger logger = LoggerFactory.getLogger(LogisticPackageOrderUploadController.class);
    
	@Override
	public UploadLogisticPackageRes uploadLogisticPackageOrder(MultipartFile csvFile) 
	{
		logger.debug("entered uploadLogisticPackageOrder.");
		UploadLogisticPackageRes uploadLogisticPackageRes = new UploadLogisticPackageRes();
		StringBuffer strb = new StringBuffer();
		if(csvFile == null || csvFile.isEmpty())
		{
			uploadLogisticPackageRes.setErrorDetails("excel file is empty.");
			return uploadLogisticPackageRes;
		}
        logger.debug("entered excel file validation for the file:"+csvFile.getOriginalFilename());
        List<LogisticPackageRecord> recordList = new ArrayList<LogisticPackageRecord>();
        
        Workbook workbook = null;
        InputStream  inputStream = null;
        try
        {
        	inputStream = csvFile.getInputStream();
        	workbook = new XSSFWorkbook(csvFile.getInputStream());
        	Sheet datatypeSheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = datatypeSheet.iterator();
            int i = 0;
        	while(iterator.hasNext())
        	{
        		Row currentRow = iterator.next();
        		if(i == 0)
        		{
        			if(isValidHeader(currentRow, AuslandweixinConfig.logisticPackageHeaders) == false)
        			{
        				uploadLogisticPackageRes.setErrorDetails("excel file header is not valid.");
        				logger.debug("validate header returns false.");
        				return uploadLogisticPackageRes;
        			}
        		}
        		else
        		{
        			try
        			{
        				LogisticPackageRecord record = provisionOneRow(currentRow);
            			recordList.add(record);
        			}
        			catch(Exception e)
        			{
        				logger.info("caught exception :"+e.getMessage());
        				int line = i +2;
        				strb.append("parse line: "+ line + "got exception:"+e.getMessage());
        			}
        		}
        		i ++;
        	}
        }
        catch(Exception e)
        {
        	logger.error("got exception:"+e.getMessage());
            strb.append("got exception:"+e.getMessage());
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
        uploadLogisticPackageRes.setRecords(recordList);
        uploadLogisticPackageRes.setErrorDetails(strb.toString());
		return uploadLogisticPackageRes;
	}
	
	private LogisticPackageRecord provisionOneRow(Row currentRow)
	{
		LogisticPackageRecord record = new LogisticPackageRecord();
		List<String> errors = new ArrayList<String>();
		record.setErrors(errors);
		if(currentRow == null)
		{
			errors.add("empty row");
			return record;
		}
		Iterator<Cell> cellIterator = currentRow.iterator();
		int i = 0;
		while(cellIterator.hasNext())
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
	        	//包裹重量
	        	if(StringUtils.isEmpty(cell))
	        	{
	        		errors.add("没有包裹重量");
	        		record.setStatus("warning");
	        	}
	        	else
	            {
	        		record.setPackageWeight(cell.trim());
	        	}
	        }
	        else if(i == 2)
	        {
	        	//货运单号
	        	if(StringUtils.isEmpty(cell))
	        	{
	        		errors.add("没有货运单号");
	        		record.setStatus("warning");
	        	}
	        	else
	        	{
	        		record.setLogisticTrackingNo(cell.trim());
	        	}
	        }
	        else if(i == 3)
	        {
	        	//品名
	        	String[] items = cell.split(",");
	        	ArrayList<String> itemList = new ArrayList<String>();
	        	for(String item : items)
	        	{
	        		if(StringUtils.isEmpty(item))
	        			continue;
	        		itemList.add(item.trim());
	        	}
	        	if(itemList.size()<= 0)
	        	{
	        		errors.add("没有品名");
	        		record.setStatus("warning");
	        	}
	        	else
	        	{
	        		record.setProductItems(itemList);
	        	}
	        	
	        }
	        else if(i == 4)
	        {
	        	//收件人姓名
	        	if(StringUtils.isEmpty(cell))
	        	{
	        		errors.add("没有收件人姓名");
	        		record.setStatus("error");
	        	}
	        	else
	        	{
	        		record.setReceiverName(cell.trim());
	        	}
	        }
	        else if(i == 5)
	        {
	        	//收件人电话
	        	if(StringUtils.isEmpty(cell))
	        	{
	        		errors.add("没有收件人电话");
	        		record.setStatus("error");
	        	}
	        	else
	        	{
	        		cell = ValidationUtil.trimPhoneNo(cell);
	        		if(ValidationUtil.isValidChinaMobileNo(cell))
	        		{
	        			errors.add("收件人电话不对");
	        			record.setStatus("error"); 
	        		}
	        		record.setReceiverPhone(cell);
	        	}
	        }
	        else if(i == 6)
	        {
	        	//收件人地址
	        	if(StringUtils.isEmpty(cell))
	        	{
	        		errors.add("没有收件人地址");
	        		record.setStatus("error");
	        	}
	        	else
	        	{
	        		record.setReceiverAddress(cell.trim());
	        	}
	        }
	        else if(i == 7)
	        {
	        	//寄件人姓名
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		record.setSenderName(cell.trim());
	        	}
	        }
	        else if(i == 8)
	        {
	        	//寄件人电话
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		record.setSenderPhone(cell.trim());
	        	}
	        }
	        else if(i == 9)
	        {
	        	//寄件人地址
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		record.setSenderAddress(cell.trim());
	        	}
	        }
		}
        record.setCreatedSrc("bulkUpload");
        record.setCreatedDateTime(new Date());
        if(record.getErrors().size() <= 0)
        {
        	record.setStatus("ok");
        }
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
