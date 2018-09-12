package com.ausland.weixin.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ausland.weixin.config.AuslandApplicationConstants;
import com.ausland.weixin.model.reqres.QueryZhongHuanDetailsByTrackingNoRes;
import com.ausland.weixin.model.reqres.QueryZhongHuanLastThreeMonthByPhoneNoRes;
import com.ausland.weixin.model.reqres.UploadZhonghanCourierExcelRes;
import com.ausland.weixin.service.ExcelOrderService;
import com.ausland.weixin.service.QueryZhongHuanService;
import com.ausland.weixin.model.reqres.ZhongHuanFydhDetails;

@RestController
@RequestMapping(value = "/upload")
public class UploadController {

	private static final Logger logger = LoggerFactory.getLogger(UploadController.class);
    
	@Autowired
	private ExcelOrderService excelOrderService; 
	
	@Value("${multipart.file.per.size}")
	private long maxFileSize;
	
	
	@PostMapping(value = "/order/excel/ozlana")
	public UploadZhonghanCourierExcelRes uploadOzlanaFormatOrderExcel(@RequestParam("file") MultipartFile file)
	{
		logger.debug("entered uploadOzlanaFormatOrderExcel");
		if(file.getSize() > maxFileSize) {
			UploadZhonghanCourierExcelRes res = new UploadZhonghanCourierExcelRes();
			res.setErrorDetails("上传文件不能超过"+maxFileSize+"字节");
			res.setUploadResult(AuslandApplicationConstants.STATUS_FAILED);
			return res;
		}
		return excelOrderService.uploadOrderExcel(file, "ozlana");
	}
	
	@PostMapping(value = "/order/excel/mmc")
	public UploadZhonghanCourierExcelRes uploadMmcFormatOrderExcel(@RequestParam("file") MultipartFile file)
	{
		logger.debug("entered uploadMmcFormatOrderExcel");
		if(file.getSize() > maxFileSize) {
			UploadZhonghanCourierExcelRes res = new UploadZhonghanCourierExcelRes();
			res.setErrorDetails("上传文件不能超过"+maxFileSize+"字节");
			res.setUploadResult(AuslandApplicationConstants.STATUS_FAILED);
			return res;
		}
		return excelOrderService.uploadOrderExcel(file, "mmc");
	}

}
