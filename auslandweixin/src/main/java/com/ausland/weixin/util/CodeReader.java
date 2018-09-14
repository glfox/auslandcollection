package com.ausland.weixin.util;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
@Service
public class CodeReader {  
	private static final Logger logger = LoggerFactory.getLogger(CodeReader.class);
   
	public String decode(BufferedImage image) {
		if(image == null) 
			return null;
		try {   
		 
            LuminanceSource source = new BufferedImageLuminanceSource(image);  
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));  
            
            Map<DecodeHintType, Object> hints = new HashMap<DecodeHintType, Object>();  
            hints.put(DecodeHintType.CHARACTER_SET, "GBK");  
            hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
            
            Result result = new MultiFormatReader().decode(bitmap, hints); 
            logger.debug("got barcode:"+result.getText());
            return result.getText();  
        } catch (Exception e) {  
            e.printStackTrace();  
            logger.error("caught exception during decode image:"+e.getMessage());
        }  
        return null;  
	}
	
    public static void main(String[] args) {  
        String imgPath = "/Users/lluo/javaproject/order/photo/toProcess/5.jpg"; 
       CodeReader cr = new CodeReader();
       
        String decodeContent = cr.decode(cr.cropImage(imgPath));  
        System.out.println("解码内容如下：");  
        System.out.println(decodeContent);  
      
    }
    
  
    public BufferedImage cropImage(String imgPath) { 
    	 BufferedImage img = null;  
 
         try {  
             img = ImageIO.read(new File(imgPath));  
             if (img == null) {  
                 logger.error("the decode image may  not exist."); 
                 return null;
             }  
             img = img.getSubimage(img.getWidth()*3/4,img.getHeight()/2,img.getWidth()/4, img.getHeight()/2); //fill in the corners of the desired crop location here
             BufferedImage copyOfImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
             AffineTransform identity = new AffineTransform();
             AffineTransform trans = new AffineTransform();
             Graphics2D g = copyOfImage.createGraphics();
             trans.setTransform(identity);
             //trans.rotate( Math.toRadians(270));
             
             g.drawImage(img, trans, null);
//             File outputfile = new File("/Users/lluo/javaproject/order/photo/toProcess/image.jpg");
//             ImageIO.write(copyOfImage, "jpg", outputfile); 
             return copyOfImage; //or use it however you want
            
         } catch (Exception e) {  
             e.printStackTrace(); 
             logger.error("caught exception during cropimage:"+e.getMessage());
         }  
            
        return null;  
    }  
  
} 