package com.ausland.weixin.service.impl;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
 
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.ausland.weixin.config.AuslandApplicationConstants;
import com.ausland.weixin.model.CustomSendMessage;
import com.ausland.weixin.model.reqres.QueryZhongHuanDetailsByTrackingNoRes;
import com.ausland.weixin.model.reqres.QueryZhongHuanLastThreeMonthByPhoneNoRes;
import com.ausland.weixin.model.xml.WeChatMessage;
import com.ausland.weixin.model.zhonghuan.xml.Back;
import com.ausland.weixin.model.zhonghuan.xml.Back.Logisticsback;
import com.ausland.weixin.service.CoreService;
import com.ausland.weixin.service.QueryZhongHuanService;
import com.ausland.weixin.service.WeChatMessageService;
import com.ausland.weixin.util.ValidationUtil;

@Service
public class CoreServiceImpl implements CoreService {

	private static final Logger logger = LoggerFactory.getLogger(CoreServiceImpl.class);
	
	@Value("${message.send.url}")
	private String messageSendUrl;

	/*@Autowired
	private WeChatMessageService weChatMessageService;*/
	
	@Autowired
	private QueryZhongHuanService queryZhongHuanService; 
	
	@Autowired
	private ValidationUtil validationUtil;

	@Override
	public String processRequest(WeChatMessage message, HttpServletResponse response) {
		String serverName = message.getToUserName();
		String userName = message.getFromUserName();
		CustomSendMessage newMsg = new CustomSendMessage();
	    if(!AuslandApplicationConstants.WEIXIN_MSG_TYPE_TEXT.equalsIgnoreCase(message.getMsgType()) || validationUtil.isValidZhongHuanTrackNo(message.getContent())== false)
	    {
	    	newMsg.setContent(AuslandApplicationConstants.ZHONGHUAN_COURIER_SEARCH_PROMPT);
	    }
	    else
	    {
	    	QueryZhongHuanDetailsByTrackingNoRes res = queryZhongHuanService.queryZhongHuanDetailsByTrackingNo(message.getContent().trim());
	    	if(res == null)
	    	{
	    		newMsg.setContent(AuslandApplicationConstants.ZHONGHUAN_COURIER_SEARCH_PROMPT_SERVERERROR);
	    	}
	    	else
	    	{
	    		if(StringUtils.isEmpty(res.getErrorDetails()))
		    	{
		    		Back back = res.getBack();
		    		if(back == null || back.getLogisticsback() == null || back.getLogisticsback().size()<= 0)
		    		{
		    			newMsg.setContent(AuslandApplicationConstants.ZHONGHUAN_COURIER_SEARCH_PROMPT_NOCOURIERINFO);
		    		}
		    		else
		    		{
		    			StringBuffer strb = new StringBuffer();
		    			for(Back.Logisticsback  lb : back.getLogisticsback())
		    			{
		    				strb.append(lb.getTime()).append(":").append(lb.getZtai()).append("\n");
		    			}
		    			newMsg.setContent(strb.toString());
		    		}
		    	}
		    	else
		    	{
		    		newMsg.setContent(res.getErrorDetails());
		    	}
	    	}
	    	
	    }
		newMsg.setFromUserName(serverName);
		newMsg.setToUserName(userName);
		newMsg.setMsgType("text");
		newMsg.setCreateTime(new Date().getTime());
		
		logger.debug("reply message:"+newMsg.toXMLString());
		try {
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(newMsg.toXMLString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		//weChatMessageService.sendMessage(newMsg.toString());
		return "success";
	}

	@Override
	@Async
	public void asyncProcessRequest(WeChatMessage message) {
		String serverName = message.getToUserName();
		String userName = message.getFromUserName();
		CustomSendMessage newMsg = new CustomSendMessage();
		if(AuslandApplicationConstants.WEIXIN_MSG_TYPE_TEXT.equalsIgnoreCase(message.getMsgType()))
		{
			if(validationUtil.isValidChinaMobileNo(message.getContent()) == true)
	    	{
				QueryZhongHuanLastThreeMonthByPhoneNoRes res =  queryZhongHuanService.queryZhongHuanLastThreeMonthbyPhoneNo(message.getContent());
				if(res == null)
		    	{
		    		newMsg.setContent(AuslandApplicationConstants.ZHONGHUAN_COURIER_SEARCH_PROMPT_SERVERERROR);
		    	}
		    	else
		    	{
		    		if(AuslandApplicationConstants.STATUS_OK.equalsIgnoreCase(res.getStatus()))
			    	{
			    		if(res.getFydhList() == null  || res.getFydhList().size()<= 0)
			    		{
			    			newMsg.setContent(AuslandApplicationConstants.ZHONGHUAN_COURIER_SEARCH_PROMPT_NOORDERINFO);
			    		}
			    		else
			    		{
			    			newMsg.setContent(res.getFydhList().toString());
			    		}
			    	}
			    	else
			    	{
			    		newMsg.setContent(res.getErrorDetails());
			    	}
		    	}
	    	}
	    	else if(validationUtil.isValidZhongHuanTrackNo(message.getContent()) == true)
	    	{
	    		QueryZhongHuanDetailsByTrackingNoRes res = queryZhongHuanService.queryZhongHuanDetailsByTrackingNo(message.getContent().trim());
	    	}
	    	else
	    	{
	    		//
	    	}
		}
		
	    if(!AuslandApplicationConstants.WEIXIN_MSG_TYPE_TEXT.equalsIgnoreCase(message.getMsgType()) ||
	       (validationUtil.isValidZhongHuanTrackNo(message.getContent())== false  && validationUtil.isValidChinaMobileNo(message.getContent()) == false ) )
	    {
	    	newMsg.setContent(AuslandApplicationConstants.ZHONGHUAN_COURIER_SEARCH_PROMPT);
	    }
	    else
	    {
	    	
	    	QueryZhongHuanDetailsByTrackingNoRes res = queryZhongHuanService.queryZhongHuanDetailsByTrackingNo(message.getContent().trim());
	    	if(res == null)
	    	{
	    		newMsg.setContent(AuslandApplicationConstants.ZHONGHUAN_COURIER_SEARCH_PROMPT_SERVERERROR);
	    	}
	    	else
	    	{
	    		if(StringUtils.isEmpty(res.getErrorDetails()))
		    	{
		    		Back back = res.getBack();
		    		if(back == null || back.getLogisticsback() == null || back.getLogisticsback().size()<= 0)
		    		{
		    			newMsg.setContent(AuslandApplicationConstants.ZHONGHUAN_COURIER_SEARCH_PROMPT_NOCOURIERINFO);
		    		}
		    		else
		    		{
		    			StringBuffer strb = new StringBuffer();
		    			for(Back.Logisticsback  lb : back.getLogisticsback())
		    			{
		    				strb.append(lb.getTime()).append(":").append(lb.getZtai()).append("\n");
		    			}
		    			newMsg.setContent(strb.toString());
		    		}
		    	}
		    	else
		    	{
		    		newMsg.setContent(res.getErrorDetails());
		    	}
	    	}
	    	
	    }
		newMsg.setFromUserName(serverName);
		newMsg.setToUserName(userName);
		newMsg.setMsgType("text");
		newMsg.setCreateTime(new Date().getTime());
		
		logger.debug("reply message:"+newMsg.toXMLString());
	}
}
