package com.ausland.weixin.controller;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
	
	@PostMapping(value = "/order/excel/ever")
	public UploadZhonghanCourierExcelRes uploadEverFormatOrderExcel(@RequestParam("excelFile") MultipartFile file)
	{
		logger.debug("entered uploadEverFormatOrderExcel");
		return excelOrderService.uploadOrderExcel(file, "ever");
	}

    @PostMapping(value = "/order/excel/luxury")
	public UploadZhonghanCourierExcelRes uploadLuxuryFormatOrderExcel(@RequestParam("excelFile") MultipartFile file)
	{
		logger.debug("entered uploadLuxuryFormatOrderExcel");
		return excelOrderService.uploadOrderExcel(file, "luxury");
	}
    
    @PostMapping(value = "/order/excel/fruit")
   	public UploadZhonghanCourierExcelRes uploadFruitFormatOrderExcel(@RequestParam("excelFile") MultipartFile file)
   	{
   		logger.debug("entered uploadFruitFormatOrderExcel");
   		return excelOrderService.uploadOrderExcel(file, "fruit");
   	}

	 @PostMapping(value = "/order/excel/tasman")
	 public UploadZhonghanCourierExcelRes uploadTasmanFormatOrderExcel(@RequestParam("excelFile") MultipartFile file)
	 {
	 	logger.debug("entered uploadTasmanFormatOrderExcel");
	 	return excelOrderService.uploadOrderExcel(file, "tasman");
	 }

	@PostMapping(value = "/order/excel/ozlana")
	public UploadZhonghanCourierExcelRes uploadOzlanaFormatOrderExcel(@RequestParam("excelFile") MultipartFile file)
	{
		logger.debug("entered uploadOzlanaFormatOrderExcel");
		return excelOrderService.uploadOrderExcel(file, "ozlana");
	}
	
	@PostMapping(value = "/order/excel/macymccoy")
	public UploadZhonghanCourierExcelRes uploadMmcFormatOrderExcel(@RequestParam("excelFile") MultipartFile file)
	{
		logger.debug("entered uploadMmcFormatOrderExcel");
		return excelOrderService.uploadOrderExcel(file, "mmc");
	}

	@PostMapping(value = "/order/excel/someothers")
	public UploadZhonghanCourierExcelRes uploadSomeOthersFormatOrderExcel(@RequestParam("excelFile") MultipartFile file)
	{
		logger.debug("entered uploadSomeOthersFormatOrderExcel");
		return excelOrderService.uploadOrderExcel(file, "cch");
	}
	
	@PostMapping(value = "/order/excel/cch")
	public UploadZhonghanCourierExcelRes uploadCchFormatOrderExcel(@RequestParam("excelFile") MultipartFile file)
	{
		logger.debug("entered uploadCchFormatOrderExcel");
		return excelOrderService.uploadOrderExcel(file, "cch");
	}
	
	@PostMapping(value = "/order/excel/dk")
	public UploadZhonghanCourierExcelRes uploadDkFormatOrderExcel(@RequestParam("excelFile") MultipartFile file)
	{
		logger.debug("entered uploadDkFormatOrderExcel");
		return excelOrderService.uploadOrderExcel(file, "dk");
	}

	@PostMapping(value = "/order/excel/vitamin")
	public UploadZhonghanCourierExcelRes uploadVitaminFormatOrderExcel(@RequestParam("excelFile") MultipartFile file)
	{
		logger.debug("entered uploadVitaminFormatOrderExcel");
		return excelOrderService.uploadOrderExcel(file, "vitamin");
	}

	@PostMapping(value = "/order/excel/ozwear")
	public UploadZhonghanCourierExcelRes uploadOzwearFormatOrderExcel(@RequestParam("excelFile") MultipartFile file)
	{
		logger.debug("entered uploadOzwearFormatOrderExcel");
		return excelOrderService.uploadOrderExcel(file, "ozwear");
	}

    @PostMapping(value = "/order/excel/auspecial")
	public UploadZhonghanCourierExcelRes uploadAuSpecialFormatOrderExcel(@RequestParam("excelFile") MultipartFile file)
	{
		logger.debug("entered uploadAuSpecialFormatOrderExcel");
		return excelOrderService.uploadOrderExcel(file, "auspecial");
	}
	
	@PostMapping(value = "/shouhou/excel/shouhou")
	public UploadZhonghanCourierExcelRes uploadShouhouExcel(@RequestParam("excelFile") MultipartFile file)
	{
		logger.debug("entered uploadShouhouExcel");
		return excelOrderService.uploadOrderExcel(file, "shouhou");
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
