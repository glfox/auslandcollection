package com.ausland.weixin.util;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ausland.weixin.config.AuslandApplicationConstants;
 
@Service
public class ImageUtil {
 
	private static final Logger logger = LoggerFactory.getLogger(ImageUtil.class);
 
	public static void main(String[] args) {
		 
	}
	
	public String getResizedImage(MultipartFile file, int height, int width)
	{
		BufferedImage bufferedImage = null;
		BufferedImage newImage = null;
		ByteArrayOutputStream output = null;
	    
		try {
			String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
	        if(fileExtension == null)
	        { return null;}
	        String imgType = "";
	        if(fileExtension.equalsIgnoreCase("png"))
	        {
	        	imgType = "PNG";
	        }
	        else if(fileExtension.equalsIgnoreCase("jpg"))
	        {
	        	imgType = "JPEG";
	        }
	        if(StringUtils.isEmpty(imgType))
	        	return null;
	        
			bufferedImage = ImageIO.read(file.getInputStream());
		    newImage = new BufferedImage(width, height, bufferedImage.getType());
	        Graphics g = newImage.getGraphics();
            g.drawImage(bufferedImage, 0, 0, width, height, null);
            g.dispose();
            output = new ByteArrayOutputStream(); 
	        ImageIO.write(newImage, imgType, output);
	       
	        String imageStr = output.toString("utf-8");
	        logger.debug("got the length of image "+imageStr.length());
	        return AuslandApplicationConstants.IMAGE_HEADER+imageStr;
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
