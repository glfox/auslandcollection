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
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ausland.weixin.config.AuslandApplicationConstants;
import com.ausland.weixin.config.AuslandweixinConfig;
import com.ausland.weixin.dao.PackageDao;
import com.ausland.weixin.model.db.LogisticPackage;
import com.ausland.weixin.model.reqres.QueryUploadLogisticPackageRes;
import com.ausland.weixin.model.reqres.UploadZhonghanCourierExcelRes;
import com.ausland.weixin.service.UploadZhonghuanCourierExcelService;
import com.ausland.weixin.util.ValidationUtil;

@Service
public class UploadZhonghuanCourierExcelServiceImpl implements UploadZhonghuanCourierExcelService{

	@Autowired
	private ValidationUtil validationUtil;
	
	@Autowired
	private PackageDao packageDao;
	
	@Value("${upload.courier.excel.server.directory}")
	private String excelDirectory;
	
	private static final Logger logger = LoggerFactory.getLogger(UploadZhonghuanCourierExcelServiceImpl.class);
    
	
	@Override
	public UploadZhonghanCourierExcelRes uploadZhonghuanCourierExcel(MultipartFile excelFile) {
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
        List<LogisticPackage> records = new ArrayList<LogisticPackage>();
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
        packageDao.saveRecordsInDb(records);
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
	
	
	@Override
	public QueryUploadLogisticPackageRes queryZhonghuanUploadedRecords(String excelFileName,
			String fromDate, String toDate, String receiverPhone) {

        logger.debug("entered queryZhonghuanUploadedRecords with excelFileName:"+excelFileName+"; fromDate:"+fromDate+"; toDate:"+toDate+";receiverPhone:"+receiverPhone);
        QueryUploadLogisticPackageRes res = new QueryUploadLogisticPackageRes();
        if(StringUtils.isEmpty(excelFileName) && (StringUtils.isEmpty(fromDate) || StringUtils.isEmpty(toDate))  && StringUtils.isEmpty(receiverPhone))
        {
        	res.setErrorDetails("query has to provide either excelFileName or fromDate/toDate, or receiverPhone");
        	return res;
        }
        List<LogisticPackage> list = packageDao.queryLogisticPackage(excelFileName, fromDate, toDate, receiverPhone);
        if(list == null || list.size() <= 0)
        {
        	res.setErrorDetails("did not find any matching results by excelFileName:"+excelFileName+"; fromDate:"+fromDate+"; toDate:"+toDate+";receiverPhone:"+receiverPhone);
        	res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
        	return res;
        }
        res.setRecords(list);
        res.setStatus(AuslandApplicationConstants.STATUS_OK);
		return res;
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

	private String validateExcelFile(MultipartFile excelFile, List<LogisticPackage> records)
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
        			if(isValidHeader(currentRow, AuslandweixinConfig.logisticPackageHeaders) == false)
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
        				LogisticPackage record = provisionOneRow(fileName, currentRow);
        				records.add(record);
        			}
        			catch(Exception e)
        			{
        				logger.info("caught exception :"+e.getMessage());
        				int line = i +2;
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
	
	private LogisticPackage provisionOneRow(String fileName, Row currentRow)
	{
		LogisticPackage record = new LogisticPackage();
		StringBuffer strB = new StringBuffer();
		if(currentRow == null)
		{
			return null;
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
	        		strB.append("没有包裹重量");
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
	        		strB.append("没有货运单号");
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
	        		strB.append("没有品名");
	        		record.setStatus("warning");
	        	}
	        	else
	        	{
	        		record.setProductItems(cell);
	        	}
	        	
	        }
	        else if(i == 4)
	        {
	        	//收件人姓名
	        	if(StringUtils.isEmpty(cell))
	        	{
	        		strB.append("没有收件人姓名");
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
	        		strB.append("没有收件人电话");
	        		record.setStatus("error");
	        	}
	        	else
	        	{
	        		cell = validationUtil.trimPhoneNo(cell);
	        		if(validationUtil.isValidChinaMobileNo(cell))
	        		{
	        			strB.append("收件人电话不对");
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
	        		strB.append("没有收件人地址");
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
        record.setCreatedSrc(fileName);
        
        record.setCreatedDateTime(validationUtil.getCurrentDate());
        if(StringUtils.isEmpty(strB.toString()))
        {
        	record.setStatus("ok");
        }
        else
        {
        	record.setValidationErrors(strB.toString());
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
