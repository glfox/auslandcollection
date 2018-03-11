package com.ausland.weixin.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;

import com.ausland.weixin.config.AuslandApplicationConstants;
import com.ausland.weixin.model.reqres.ZhongHuanFydhDetails;

public class ZhongHuanFydhDetailsComparator implements Comparator<ZhongHuanFydhDetails>{

	@Override
	public int compare(ZhongHuanFydhDetails o1, ZhongHuanFydhDetails o2) {
		DateFormat f = new SimpleDateFormat(AuslandApplicationConstants.DATE_STRING_FORMAT);
		if(o1 == null || o1.getCourierCreatedDateTime() == null || o2 == null || o2.getCourierCreatedDateTime() == null)
		    return 0;
		try {
			return f.parse(o2.getCourierCreatedDateTime()).compareTo(f.parse(o1.getCourierCreatedDateTime()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	

}
