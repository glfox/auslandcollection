package com.ausland.weixin.util;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ausland.weixin.config.AuslandApplicationConstants;
 
@Service
public class ImageUtil {
 
	private static final Logger logger = LoggerFactory.getLogger(ImageUtil.class);
 
	public static void main(String[] args) {
//		 ImageUtil util = new ImageUtil();
//		 util.getResizedImage("/Users/lluo/javaproject/order/photo/toProcess/4.jpg", 1100, 600);
		String packingPhotoDirectory = "/Users/lluo/javaproject/order/photo/";
		 String toProcessDir = packingPhotoDirectory+"toprocess/";
			String failedDir = packingPhotoDirectory+"failed/";
			File toDir = new File(toProcessDir);
			if(toDir.exists()&& toDir.isDirectory()) {
				String[] files = toDir.list();
				if(files != null && files.length > 0) {
					File dstDir = new File(failedDir);
					for(String fileName: files) {
						System.out.println(fileName);
						File f = new File(toProcessDir, fileName);
						f.renameTo(new File(failedDir,fileName));
					}
				}
			}
			
	}
	
	public String validateAndGetImageType(String imagePath) {
		if(StringUtils.isEmpty(imagePath)){
			return "";
		}
		File f = new File(imagePath);
		String fileExtension = FilenameUtils.getExtension(f.getName());
        if(fileExtension == null)
        { return "";}
       
        if(fileExtension.equalsIgnoreCase("png"))
        {
        	return "PNG";
        }
        else if(fileExtension.equalsIgnoreCase("jpg"))
        {
        	return "JPEG";
        }
        return "";
	}
	
	public String getResizedImage(String file, int height, int width)
	{
		BufferedImage bufferedImage = null;
		BufferedImage newImage = null;
		FileOutputStream output = null;
	    
		try {
            String imageType = validateAndGetImageType(file);
	        if(StringUtils.isEmpty(imageType)) {
	        	return "";
	        }
			bufferedImage = ImageIO.read(new File(file));
		    newImage = new BufferedImage(width, height, bufferedImage.getType());
	        Graphics g = newImage.getGraphics();
            g.drawImage(bufferedImage, 0, 0, width, height, null);
            g.dispose();
            output = new FileOutputStream("/Users/lluo/javaproject/order/photo/toProcess/4-small.jpg"); 
	        ImageIO.write(newImage, imageType, output);
	         
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
 
			if(output != null)
			{
				try {
					output.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return "";
	}
	
	 
}
