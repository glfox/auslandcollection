package com.ausland.weixin.controller;

import java.io.File;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ausland.weixin.config.AuslandApplicationConstants;
import com.ausland.weixin.model.reqres.UploadPackingPhotoRes;
import com.ausland.weixin.model.reqres.UploadZhonghanCourierExcelRes;
import com.ausland.weixin.service.ExcelOrderService;

@RestController
@RequestMapping(value = "/upload")
public class UploadController {

	private static final Logger logger = LoggerFactory.getLogger(UploadController.class);
    
	@Autowired
	private ExcelOrderService excelOrderService; 
	@Value("${upload.packing.photo.server.directory}")
	private String packingPhotoDirectory;
	
	@PostMapping(value = "/order/excel/ozlana")
	public UploadZhonghanCourierExcelRes uploadOzlanaFormatOrderExcel(@RequestParam("excelFile") MultipartFile file)
	{
		logger.debug("entered uploadOzlanaFormatOrderExcel");
		return excelOrderService.uploadOrderExcel(file, "ozlana");
	}
	
	@PostMapping(value = "/order/excel/difou")
	public UploadZhonghanCourierExcelRes uploadDifouFormatOrderExcel(@RequestParam("excelFile") MultipartFile file)
	{
		logger.debug("entered uploadDifouFormatOrderExcel");
		return excelOrderService.uploadOrderExcel(file, "mmc");
	}

	@PostMapping(value = "/order/excel/vitamin")
	public UploadZhonghanCourierExcelRes uploadVitaminFormatOrderExcel(@RequestParam("excelFile") MultipartFile file)
	{
		logger.debug("entered uploadVitaminFormatOrderExcel");
		return excelOrderService.uploadOrderExcel(file, "vitamin");
	}
	
	@PostMapping(value = "/packing/photo")
	public UploadPackingPhotoRes uploadPackingPhoto(@RequestParam(name="file", required=false) MultipartFile file)
	{
		logger.debug("entered uploadPackingPhoto");
		UploadPackingPhotoRes res = new UploadPackingPhotoRes();
		if(file == null || file.isEmpty()) {
			logger.debug("here is the empty multipartfile.");
		}
	    res = excelOrderService.uploadPackingPhoto(file);
		
		logger.debug("exit uploadPackingPhoto with res:"+res.toString());
		String toProcessDir = packingPhotoDirectory+"toprocess/";
		String failedDir = packingPhotoDirectory+"failed/";
		File toDir = new File(toProcessDir);
		if(toDir.exists()&& toDir.isDirectory()) {
			String[] files = toDir.list();
			if(files != null && files.length > 0) {
				File dstDir = new File(failedDir);
				for(String fileName: files) {
					File f = new File(toProcessDir, fileName);
					f.renameTo(new File(failedDir,fileName));
				}
			}
		}
		return res;
	}
}
