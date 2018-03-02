package com.ausland.weixin.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class ValidationUtil {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
       System.out.println(isValidChinaMobileNo("15618983927"));
       System.out.println(trimPhoneNo("156 18983927 "));
	}
	
	public static boolean isValidChinaMobileNo(String phoneNo)
	{
		if(phoneNo == null || phoneNo.length() != 11) return false;
		String regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";  
        Pattern p = Pattern.compile(regExp);  
        Matcher m = p.matcher(phoneNo);  
        return m.matches();  
	}
	
	public static String trimPhoneNo(String phoneNo)
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
