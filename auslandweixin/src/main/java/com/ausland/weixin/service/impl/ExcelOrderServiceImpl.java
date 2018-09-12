package com.ausland.weixin.service.impl;

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
import com.ausland.weixin.dao.OrderListFromExcelRepository;
import com.ausland.weixin.model.db.OrderListFromExcel;
import com.ausland.weixin.model.reqres.QueryZhongHuanLastThreeMonthByPhoneNoRes;
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
			else {
				logger.debug("this is not the phone number, query by username");
				ret = orderListFromExcelRepository.findByReceiverName(userNameOrPhoneNo);
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
					z.setCourierNumber(o.getLogisticNo());
					z.setCourierChinaNumber(o.getOrderNo());
					z.setReceiverName(o.getReceiverName());
					z.setProductItems(o.getProductItems());
					z.setCustomStatus(o.getStatus());
					z.setCourierCreatedDateTime(o.getCreatedDateTime());
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

	@Override
	public UploadZhonghanCourierExcelRes uploadOzlanaFormatOrderExcel(MultipartFile excelFile) {
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
        try
        {
             logger.debug("start to process excel file, call validateOzlanaExcelFile()"); 
        	 List<OrderListFromExcel> records = new ArrayList<OrderListFromExcel>();
             String errorMessage = validateOzlanaExcelFile(excelFile, records);
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

	private OrderListFromExcel provisionOneRowForOzlana(String fileName, Row currentRow)
	{
		if(currentRow == null)
		{
			return null;
		}
		logger.debug("entered provisionOneRowForOzlana with filename:"+fileName);
		OrderListFromExcel record = new OrderListFromExcel();
		StringBuffer strB = new StringBuffer();
		
		Iterator<Cell> cellIterator = currentRow.iterator();
		int i = 0;
		StringBuffer IdBuf = new StringBuffer();
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
	        	//订单号
	        	if(StringUtils.isEmpty(cell))
	        	{
	        		strB.append("没有订单号");
	        		record.setErrorMsg(strB.toString());
	        		return record;
	        	}
	        	else
	            {
	        		record.setOrderNo(cell.trim());
					IdBuf.append(cell.trim());
	        	}
	        }
	        else if(i == 2) {
	        	// 下单日期
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		record.setCreatedDateTime(cell.trim());
	        	} 
	        }
	        else if(i == 3) {
	        	// 代理名称
	        }
	        else if(i == 4)
	        {
	        	//收件人
	        	if(StringUtils.isEmpty(cell))
	        	{
	        		strB.append("没有收件人");
	        	}
	        	else
	        	{
	        		record.setReceiverName(cell.trim());
	        	}
	        }
	        else if(i == 5) {
	        	// 电话号码
	        	if(StringUtils.isEmpty(cell))
	        	{
	        		strB.append("没有电话号码");
	        	}
	        	else
	        	{
	        		record.setReceiverPhone(cell.trim());
	        	}
	        }
	        else if(i == 6)
	        {
	        	//产品编号
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		record.setProductItems(cell.trim());
					IdBuf.append("-").append(cell.trim());
	        	}       	
	        }
	        else if(i == 7)
	        {
	        	//尺码
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		String p = record.getProductItems() + "-" + cell.trim();
	        		record.setProductItems(p);
					IdBuf.append("-").append(cell.trim());
	        	} 
	        }
	        else if(i == 8)
	        {
	        	//颜色
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		String p = record.getProductItems() + "-" + cell.trim();
	        		record.setProductItems(p);
	        	}
	        }
	        else if(i == 9)
	        {
	        	//数量
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		String p = record.getProductItems() + "-" + cell.trim();
	        		record.setProductItems(p);
	        	}
	        }
	        else if(i == 10) {
	        	// 快递名称
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		record.setLogisticCompany(cell.trim());
	        	}
	        }
	        else if(i == 11)
	        {
	        	//快递单号
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		record.setLogisticNo(cell.trim());
	        	}
	        } 
		}

        record.setLastupdatedDateTime(validationUtil.getCurrentDate());
        if(IdBuf.toString().length() < 64) {
        	record.setId(IdBuf.toString());
        }else {
        	record.setId(IdBuf.toString().substring(0,64));
        }
		
		if(!StringUtils.isEmpty(strB.toString())) {
			logger.debug("warning record:"+strB.toString());
		}
        logger.debug("provisionOneRowForOzlana returns with record:"+record.toString());
		return record;
	}
	
	private OrderListFromExcel provisionOneRowForMmc(String fileName, Row currentRow)
	{
		OrderListFromExcel record = new OrderListFromExcel();
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
	        	//订单编号
	        	if(StringUtils.isEmpty(cell))
	        	{
	        		strB.append("没有订单编号");
	        		record.setErrorMsg(strB.toString());
	        		break;
	        	}
	        	else
	            {
	        		record.setOrderNo(cell.trim());
	        	}
	        }
	        else if(i == 2) {
	        	//交易时间
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		record.setCreatedDateTime(cell.trim());
	        	}
	        }
	        else if(i == 3)
	        {
	        	//收货人
	        	if(StringUtils.isEmpty(cell))
	        	{
	        		strB.append("没有收件人");
	        	}
	        	else
	        	{
	        		record.setReceiverName(cell.trim());
	        	}
	        }
	        else if(i == 4) {
	        	// 物流方式
	        	if(!StringUtils.isEmpty(cell)) {
	        		record.setLogisticCompany(cell.trim());
	        	}
	        }
	        else if(i == 5)
	        {
	        	//物流单号
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		record.setLogisticNo(cell.trim());
	        	}
	        }
	        else if(i == 6)
	        {
	        	//品名
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		record.setProductItems(cell.trim());
	        	}
	        }
	        else if(i == 7)
	        {
	        	//规格
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		String p = record.getProductItems()+"-"+cell.trim();
	        		record.setProductItems(p);
	        	}
	        } 
	        else if(i == 8)
	        {
	        	//数量
	        	if(!StringUtils.isEmpty(cell))
	        	{
	        		String p = record.getProductItems()+"-"+cell.trim();
	        		record.setProductItems(p);
	        	}
	        }
		}

        record.setLastupdatedDateTime(validationUtil.getCurrentDate());
        logger.debug("warning record:", strB.toString());
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
	public UploadZhonghanCourierExcelRes uploadMmcFormatOrderExcel(MultipartFile excelFile) {
		// TODO Auto-generated method stub
		return null;
	}
}
