package com.ausland.weixin.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
 
@Service
public class ValidationUtil {

	
	@Value("${zhonghuan.trackno.length}")
	private Integer zhonghuantracknolength;
	
	@Value("${zhonghuan.trackno.startswith}")
	private String zhonghuantracknoStartwith;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	private static final SimpleDateFormat simpleDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
 
	private static final Logger logger = LoggerFactory.getLogger(ValidationUtil.class);

	public static void main(String[] args) {
		ValidationUtil u = new ValidationUtil();
		// TODO Auto-generated method stub
       System.out.println(u.isValidChinaMobileNo("15618983927"));
       System.out.println(u.trimPhoneNo("156 18983927 "));
       System.out.println(u.removeCDATA("<tel><message>成功</message>"));
       System.out.print(u.removeCDATA("<![CDATA[<?xml  version=\"1.0\" encoding=\"utf-8\" standalone=\"no\" ?><tel><message>成功</message><issuccess>true</issuccess><fydhlist><chrfydh>970000197505</chrfydh> <chrdysj>2018-03-03 06:33:37</chrdysj> </fydhlist> </tel>]]>"));
	}
	
	public String getCurrentDateString()
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
