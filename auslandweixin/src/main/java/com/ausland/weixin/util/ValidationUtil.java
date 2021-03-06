package com.ausland.weixin.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ausland.weixin.config.AuslandApplicationConstants;
 
@Service
public class ValidationUtil {

	@Value("${zhonghuan.trackno.length}")
	private Integer zhonghuantracknolength;
	
	@Value("${zhonghuan.trackno.startswith}")
	private String zhonghuantracknoStartwith;
	
	private static final SimpleDateFormat simpleDateTimeFormat = new SimpleDateFormat(AuslandApplicationConstants.DATETIME_STRING_FORMAT);
 
	private static final Logger logger = LoggerFactory.getLogger(ValidationUtil.class);

	private static  String createAsterisk(int length) {  
	    StringBuffer stringBuffer = new StringBuffer();  
	    for (int i = 0; i < length; i++) {  
	        stringBuffer.append("*");  
	    }  
	    return stringBuffer.toString();  
	}  
	public static void main(String[] args) {
		/*ValidationUtil u = new ValidationUtil();
		SimpleDateFormat df = new SimpleDateFormat(AuslandApplicationConstants.DATE_STRING_FORMAT);
		try {
			Date d = df.parse("2018-03-04 23:01:02");
			System.out.println(df.format(d));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		String str = "爱新觉罗";
		try {
			System.out.println(str.getBytes("utf-8").length);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("爱新".length());
		System.out.println("爱".length());
		System.out.println("爱新角".length());
		//System.out.println("爱新觉罗".replaceAll("([\\u4e00-\\u9fa5]{1})(.*)", "$1" + createAsterisk(str.length() - 1)));
		System.out.println("爱新觉luo".replaceAll("([\\u4e00-\\u9fa5]{1})(.*)", "*" + "$2"));
		System.out.println("爱新觉".replaceAll("([\\u4e00-\\u9fa5]{1})(.*)", "*" + "$2"));
		System.out.println("爱新".replaceAll("([\\u4e00-\\u9fa5]{1})(.*)", "*" + "$2"));
		/*// TODO Auto-generated method stub
       System.out.println(u.isValidChinaMobileNo("15618983927"));
       System.out.println(u.trimPhoneNo("156 18983927 "));
       System.out.println(u.removeCDATA("<tel><message>成功</message>"));
       System.out.print(u.removeCDATA("<![CDATA[<?xml  version=\"1.0\" encoding=\"utf-8\" standalone=\"no\" ?><tel><message>成功</message><issuccess>true</issuccess><fydhlist><chrfydh>970000197505</chrfydh> <chrdysj>2018-03-03 06:33:37</chrdysj> </fydhlist> </tel>]]>"));
	  */
	}
		
	private void readExcel(File file)
	{

		try {
			InputStream inputStream = new FileInputStream(file);
			Workbook workbook = WorkbookFactory.create(inputStream);
			Sheet datatypeSheet = workbook.getSheetAt(0);
	        Iterator<Row> iterator = datatypeSheet.iterator();
	        int i = 0;
	        while(iterator.hasNext())
	        {
	        	i ++;
	        	Row currentRow = iterator.next();
	        	if(i == 1) continue;
	        	if(i >= 10) break;
	        	Iterator<Cell> cellIterator = currentRow.iterator();
	        	while(cellIterator.hasNext())
	        	{
	        		Cell cell = cellIterator.next();
	        		System.out.println(cell.getAddress());
	        		
	        		//System.out.println(cell.getCellTypeEnum().toString());
	        		if(cell.getCellTypeEnum() == CellType.STRING)
	        		{
	        			System.out.println("cell type string,  cellvalue: "+cell.getStringCellValue());
	        		}
	        		else if(cell.getCellTypeEnum() == CellType.NUMERIC)
	        		{
	        			System.out.println("cell type numeric, cellvalue: "+cell.getNumericCellValue());
	        		}
	        		else
	        		{
	        			System.out.println("unknow celltype:"+cell.getCellTypeEnum().toString());
	        		}
	        	}
	        }
		} catch (EncryptedDocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// new XSSFWorkbook(excelFile.getInputStream());
    	
	}
	
	public String getCurrentDateTimeString()
	{
		Date date = new Date();
		return simpleDateTimeFormat.format(date);
	}
	
	public Date getCurrentDate()
	{
		Date date = new Date();
		return date;
	}
	
	public boolean isValidZhongHuanTrackNo(String trackingNo)
	{
		if(StringUtils.isEmpty(trackingNo))
		{
			return false;
		}
		
		String no = trackingNo.trim();
		if(!no.startsWith(zhonghuantracknoStartwith) || no.length() != zhonghuantracknolength)
		{
			return false;
		}
		
		char[] chars = no.toCharArray();
		for(char ch : chars)
		{
			if(!Character.isDigit(ch))
				return false;
		}
		return true;
	}
	public boolean isValidChinaMobileNo(String phoneNo)
	{
		if(phoneNo == null || phoneNo.length() != 11) return false;
		char[] chars = phoneNo.toCharArray();
		for(char ch : chars)
		{
			if(!Character.isDigit(ch))
				return false;
		}
		return true;
	}
	
	public String removeCDATA(String text) {
		if(StringUtils.isEmpty(text))
			return "";
	    String resultString = "";
	    Pattern regex = Pattern.compile("(?<!(<!\\[CDATA\\[))|((.*)\\w+\\W)");
	    Matcher regexMatcher = regex.matcher(text);
	    while (regexMatcher.find()) {
	        resultString += regexMatcher.group();
	    }
	    return resultString;
	}
	
	public String trimPhoneNo(String phoneNo)
	{
		if(StringUtils.isEmpty(phoneNo)) return "";
		List<Character> chars = new ArrayList<Character>();
		char[] chs = phoneNo.trim().toCharArray();
		for(char ch : chs)
		{
			if(Character.isDigit(ch))
			{
				chars.add(ch);
			}
		}
		char[] CS = new char[chars.size()];
		for(int i = 0; i < CS.length; i ++)
		{
			CS[i] = chars.get(i);
		}
		return new String(CS);
	}
	
	
}
