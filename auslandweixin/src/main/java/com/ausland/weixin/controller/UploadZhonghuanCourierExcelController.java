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
	
	@RequestMapping(value = "/query", method = RequestMethod.GET)
	public QueryUploadLogisticPackageRes queryByFileName(@RequestParam(name = "filename", required = false)String excelFileName,
			                                             @RequestParam(name = "fromDate", required = false)String fromDate,
			                                             @RequestParam(name = "toDate", required = false)String toDate,
			                                             @RequestParam(name = "receiverPhone", required = false)String receiverPhone,
			                                           HttpServletRequest httpServletRequest) throws IOException 
	{
		logger.debug("entered uploadLogisticPackageOrder.");
		return uploadZhonghuanCourierExcelService.queryZhonghuanUploadedRecords(excelFileName, fromDate, toDate, receiverPhone);
	}
	
}
