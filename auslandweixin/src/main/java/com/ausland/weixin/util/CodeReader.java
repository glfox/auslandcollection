package com.ausland.weixin.util;

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;  
  
@Service
public class CodeReader {  
  
    /** 
     * @param args 
     * @throws ReaderException  
     * @throws InterruptedException  
     */  
	public static String decode(String imgPath) {  
        BufferedImage image = null;  
        Result result = null;  
        try {  
            image = ImageIO.read(new File(imgPath));  
            if (image == null) {  
                System.out.println("the decode image may be not exit.");  
            }  
            
            LuminanceSource source = new BufferedImageLuminanceSource(image);  
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));  
            
            Map<DecodeHintType, Object> hints = new HashMap<DecodeHintType, Object>();  
            hints.put(DecodeHintType.CHARACTER_SET, "GBK");  
            hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
            result = new MultiFormatReader().decode(bitmap, hints);  
            return result.getText();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return null;  
    }  
	
    public static void main(String[] args) {  
        String imgPath = "/Users/lluo/javaproject/order/photo/toProcess/1.jpg"; 
        cropImage(imgPath);
//        String decodeContent = decode(imgPath);  
//        System.out.println("解码内容如下：");  
//        System.out.println(decodeContent);  
      
    }
    
  
    public static BufferedImage cropImage(String imgPath) { 
    	 BufferedImage img = null;  

         Result result = null;  
         try {  
             img = ImageIO.read(new File(imgPath));  
             if (img == null) {  
                 System.out.println("the decode image may be not exit.");  
             }  
             img = img.getSubimage(img.getWidth()/2,0,img.getWidth()/2, img.getHeight()/2); //fill in the corners of the desired crop location here
             BufferedImage copyOfImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
             AffineTransform identity = new AffineTransform();
             AffineTransform trans = new AffineTransform();
             Graphics2D g = copyOfImage.createGraphics();
             trans.setTransform(identity);
             trans.rotate( Math.toRadians(90) );
             
             g.drawImage(img, trans, null);
             File outputfile = new File("/Users/lluo/javaproject/order/photo/toProcess/image.jpg");
             ImageIO.write(copyOfImage, "jpg", outputfile); 
             return copyOfImage; //or use it however you want
            
         } catch (Exception e) {  
             e.printStackTrace();  
         }  
            
        return null;  
    }  
  
} 