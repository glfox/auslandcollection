package com.ausland.weixin.controller;

import java.io.IOException;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ausland.weixin.service.LogisticPackageOrderUploadService;

@RestController
@MultipartConfig
public class LogisticPackageOrderUploadController {

	private static final Logger logger = LoggerFactory.getLogger(LogisticPackageOrderUploadController.class);
    
	@Autowired
	private LogisticPackageOrderUploadService logisticPackageOrderUploadService; 
	
	@RequestMapping(value = "/provisioning/uploadLogisticPackage", method = RequestMethod.POST, 
			consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public String uploadLogisticPackageOrder(@RequestPart("csvFile") MultipartFile csvFile,
			HttpServletRequest httpServletRequest) throws IOException 
	{
        boolean b = logisticPackageOrderUploadService.isCsvFileValid(csvFile); 
		
		if (b == false) {
			/*logger.error("CSV file content is not valid or empty" + errorMessage);
			throw new Exception(errorMessage);*/
		}
		
		return "";
	}
	
}
