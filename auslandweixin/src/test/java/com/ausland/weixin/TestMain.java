package com.ausland.weixin;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.TimeZone;

public class TestMain {

	public static void main(String[] args) {
		Date date = null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		formatter.setTimeZone(TimeZone.getTimeZone("CTT"));
		try {
			date = formatter.parse("2020-11-04 10:00:00");

		} catch (ParseException e) {
			System.out.println("caught exception during parsing date string" + e.getMessage());
		}
		System.out.println("date: " + date.toString());
	}

}
