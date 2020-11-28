package com.ausland.weixin;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuslandweixinApplicationTests {

	@Test
	public void contextLoads() {
		
		Date date = null;
	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	    try {
	       date = formatter.parse("2020-11-04");
	    } catch (ParseException e) {
	        System.out.println("caught exception during parsing date string"+e.getMessage());
	    }
	    System.out.println("date: "+date.toString());
	}

}
