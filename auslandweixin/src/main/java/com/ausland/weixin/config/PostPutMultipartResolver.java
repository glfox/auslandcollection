package com.ausland.weixin.config;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.commons.CommonsMultipartResolver;

public class PostPutMultipartResolver extends CommonsMultipartResolver {
	
	@Override
	public boolean isMultipart(HttpServletRequest request) {
		String method = request.getMethod().toLowerCase();
		// By default, only POST is allowed. Since this is an 'update' we should
		// accept PUT.
		if (!Arrays.asList("put", "post").contains(method)) {
			return false;
		}
		String contentType = request.getContentType();
		return (contentType != null && contentType.toLowerCase().startsWith("multipart/"));
	}
	@Override
	public void setMaxUploadSize(long maxUploadSize) {
		super.getFileUpload().setSizeMax(maxUploadSize);
	}
	@Override
	public void setMaxUploadSizePerFile(long maxUploadSizePerFile) {
		super.getFileUpload().setFileSizeMax(maxUploadSizePerFile);
	}
}
