package com.ausland.weixin.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
	
	private String createAsterisk(int length) {  
	    StringBuffer stringBuffer = new StringBuffer();  
	    for (int i = 0; i < length; i++) {  
	        stringBuffer.append("*");  
	    }  
	    return stringBuffer.toString();  
	}  
//	
//	private static String getName(String receiverInfo) {
//		char[] chars = receiverInfo.toCharArray();
//		boolean isFirstChineseCharacter = false;
//		int start = 0;
//		int end = 0;
//		for(int i = 0; i < chars.length; i ++)
//		{
//			if(!isFirstChineseCharacter && chars[i] >= 0x4E00 && chars[i] <= 0x9FA5 ) {
//				isFirstChineseCharacter = true;
//				start = i;
//			}
//		 
//			if(isFirstChineseCharacter && (Character.isDigit(chars[i]) || chars[i] <  0x4E00 || chars[i] > 0x9FA5)) {
//				end = i;
//				return receiverInfo.substring(start, end);
//			}
//		}
//		return "";
//	}
//	
//	private static String getTel(String receiverInfo) {
//		char[] chars = receiverInfo.toCharArray();
//		for(int i = 0; i < chars.length; i ++)
//		{
//			if(Character.isDigit(chars[i]) && (i + 11) < chars.length) {
//				String tel = receiverInfo.substring(i, i + 11);
//				return tel; 
//			} 
//		}
//		return "";
//	}
	
//	private static Date stringToDate(String str, String format) {
//	    Date date = null;
//	    SimpleDateFormat formatter = new SimpleDateFormat(format);
//	    try {
//	       date = formatter.parse(str);
//	    } catch (ParseException e) {
//	        logger.error("caught exception during parsing date string"+e.getMessage());
//	    }
//	    return date;
//	}
	
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
//		String str = "爱新觉罗";
//		try {
//			System.out.println(str.getBytes("utf-8").length);
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println("爱新".length());
//		System.out.println("爱".length());
//		System.out.println("爱新角".length());
//		//System.out.println("爱新觉罗".replaceAll("([\\u4e00-\\u9fa5]{1})(.*)", "$1" + createAsterisk(str.length() - 1)));
//		System.out.println("爱新觉luo".replaceAll("([\\u4e00-\\u9fa5]{1})(.*)", "*" + "$2"));
//		System.out.println("爱新觉".replaceAll("([\\u4e00-\\u9fa5]{1})(.*)", "*" + "$2"));
//		System.out.println("爱新".replaceAll("([\\u4e00-\\u9fa5]{1})(.*)", "*" + "$2"));
		//System.out.println(Float.parseFloat("1.0"));
		/*// TODO Auto-generated method stub
       System.out.println(u.isValidChinaMobileNo("15618983927"));
       System.out.println(u.trimPhoneNo("156 18983927 "));
       System.out.println(u.removeCDATA("<tel><message>成功</message>"));
       System.out.print(u.removeCDATA("<![CDATA[<?xml  version=\"1.0\" encoding=\"utf-8\" standalone=\"no\" ?><tel><message>成功</message><issuccess>true</issuccess><fydhlist><chrfydh>970000197505</chrfydh> <chrdysj>2018-03-03 06:33:37</chrdysj> </fydhlist> </tel>]]>"));
	  */
//		ArrayList<String> list = new ArrayList<String>();
//		list.add("13676861358");
//		list.add("18611328280");
//		list.add("17600406075");
//		list.add("13883860243");
//		list.add("14727638117");
//		String[] list = {"2018/5/30 11:11:22", 
//				"2018/5/29"};
//		for(String s: list) {
//			Date d = stringToDate(s,"yyyy/M/d"); 
//			System.out.println(d.toString());
//		}
//		 for(String s: list)
//		 System.out.println(isValidChineseName(s));
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
	
	public Date getThreeMonthEarlyDate() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -3);
		Date result = cal.getTime();
		return result;
	}
	
	public Date getCurrentDate()
	{
		Date date = new Date();
		return date;
	}
	
	public boolean isValidLooseChineseName(String name){
		if(StringUtils.isEmpty(name) || name.length() > 64 || name.length() < 2)
		{
			return false;
		}

		return true;
	}
	
	public boolean isValidChineseName(String name){
		if(StringUtils.isEmpty(name))
		{
			return false;
		}
		
		try {
			int chineseNo = name.getBytes("utf-8").length;
			if(chineseNo == name.length() || name.length() > 5 || name.length() < 2)
				return false;
		} catch (UnsupportedEncodingException e) {
			return false;
		}
		
		char[] chars = name.toCharArray();
		for(char ch : chars)
		{
			if(Character.isDigit(ch) || ch <  0x4E00 || ch > 0x9FA5)
				return false;
		}
		return true;
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
		if(phoneNo.matches("^((13[0-9])|(14[5-9])|(15([0-3]|[5-9]))|(16[0-9])|(17[0-9])|(18[0-9])|(19[0-9]))\\d{8}$"))
		//if(phoneNo.matches(cmcc) || phoneNo.matches(cucc) || phoneNo.matches(cnc))
		{
			return true;
		}
		return false;
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
