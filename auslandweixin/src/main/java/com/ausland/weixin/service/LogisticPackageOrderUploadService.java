package com.ausland.weixin.service;

import org.springframework.web.multipart.MultipartFile;

import com.ausland.weixin.model.reqres.UploadLogisticPackageRes;

public interface LogisticPackageOrderUploadService {

	UploadLogisticPackageRes uploadLogisticPackageOrder(MultipartFile csvFile);
}
