package com.ausland.weixin.controller;

import java.io.IOException;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ausland.weixin.model.reqres.QueryUploadLogisticPackageRes;
import com.ausland.weixin.model.reqres.UploadZhonghanCourierExcelRes;
import com.ausland.weixin.service.UploadZhonghuanCourierExcelService;

@RestController
@MultipartConfig
@RequestMapping(value = "/provisioning/zhonghuan/courierexcel")
public class UploadZhonghuanCourierExcelController {
	
private static final Logger logger = LoggerFactory.getLogger(UploadZhonghuanCourierExcelController.class);
    
	@Autowired
	private UploadZhonghuanCourierExcelService uploadZhonghuanCourierExcelService; 
	
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public UploadZhonghanCourierExcelRes uploadZhonghuanCourierExcel(@RequestPart(required = true)MultipartFile excelFile,
			                                           HttpServletRequest httpServletRequest) throws IOException 
	{
		logger.debug("entered uploadLogisticPackageOrder.");
		return uploadZhonghuanCourierExcelService.uploadZhonghuanCourierExcel(excelFile);
	}
	
	@RequestMapping(value = "/querybyuploadedexcelname", method = RequestMethod.GET)
	public QueryUploadLogisticPackageRes queryByFileName(@RequestParam(name = "filename", required = true)String excelFileName,
			                                             HttpServletRequest httpServletRequest) throws IOException 
	{
		logger.debug("entered queryByFileName.");
		return uploadZhonghuanCourierExcelService.queryZhonghuanUploadedRecordsByUploadedExcelName(excelFileName);
	}
	
	
	@RequestMapping(value = "/querybyuploadeddaterange", method = RequestMethod.GET)
	public QueryUploadLogisticPackageRes queryByDateRange(@RequestParam(name = "fromDate", required = true)String fromDate,
			                                             @RequestParam(name = "toDate", required = true)String toDate,
			                                           HttpServletRequest httpServletRequest) throws IOException 
	{
		logger.debug("entered queryByDateRange.");
		return uploadZhonghuanCourierExcelService.queryZhonghuanUploadedRecordsByDateRange(fromDate, toDate);
	}
	
	@RequestMapping(value = "/query", method = RequestMethod.GET)
	public QueryUploadLogisticPackageRes queryByReceiverPhoneAndDateRange(@RequestParam(name = "receiverPhone", required = true)String receiverPhone,
			@RequestParam(name = "fromDate", required = true)String fromDate,
            @RequestParam(name = "toDate", required = true)String toDate,
			                                           HttpServletRequest httpServletRequest) throws IOException 
	{
		logger.debug("entered queryByReceiverPhoneAndDateRange.");
		return uploadZhonghuanCourierExcelService.queryZhonghuanUploadedRecordsByPhoneAndDateRange(receiverPhone, fromDate, toDate);
	}
	
	
}
