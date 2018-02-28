package com.ausland.weixin.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ausland.weixin.service.LogisticPackageOrderUploadService;

@Service
public class LogisticPackageOrderUploadServiceImpl implements LogisticPackageOrderUploadService{

	@Override
	public boolean isCsvFileValid(MultipartFile csvFile) {
		// TODO Auto-generated method stub
		return false;
	}


}
