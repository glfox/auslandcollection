package com.ausland.weixin.service;

import org.springframework.web.multipart.MultipartFile;

public interface LogisticPackageOrderUploadService {

	boolean isCsvFileValid(MultipartFile csvFile); 
	
}
