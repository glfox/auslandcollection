package com.ausland.weixin.service;

import com.ausland.weixin.model.reqres.UploadProductReq;
import com.ausland.weixin.model.reqres.UploadProductRes;

public interface UploadProductService {

	
	UploadProductRes uploadProduct(UploadProductReq req);
}
