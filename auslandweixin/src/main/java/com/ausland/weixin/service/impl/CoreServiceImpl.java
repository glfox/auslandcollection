package com.ausland.weixin.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.ausland.weixin.config.AuslandApplicationConstants;
import com.ausland.weixin.model.CustomSendMessage;
import com.ausland.weixin.model.MessageContent;
import com.ausland.weixin.model.difou.GetStockListRes;
import com.ausland.weixin.model.difou.StockDataInfo;
import com.ausland.weixin.model.reqres.GongZhongHaoUserInfoRes;
import com.ausland.weixin.model.reqres.QueryZhongHuanDetailsByTrackingNoRes;
import com.ausland.weixin.model.reqres.QueryZhongHuanLastThreeMonthByPhoneNoRes;
import com.ausland.weixin.model.reqres.ZhongHuanFydhDetails;
import com.ausland.weixin.model.xml.WeChatMessage;
import com.ausland.weixin.model.zhonghuan.xml.Back.Logisticsback;
import com.ausland.weixin.service.CoreService;
import com.ausland.weixin.service.DifouProductStockService;
import com.ausland.weixin.service.DifouService;
import com.ausland.weixin.service.GongZhongHaoSubscriberUserService;
import com.ausland.weixin.service.QueryZhongHuanService;
import com.ausland.weixin.service.WeChatMessageService;
import com.ausland.weixin.util.ValidationUtil;
import com.ausland.weixin.util.ZhongHuanFydhDetailsComparator;

@Service
public class CoreServiceImpl implements CoreService {

	private static final Logger logger = LoggerFactory.getLogger(CoreServiceImpl.class);
	
	@Value("${message.send.url}")
	private String messageSendUrl;

	 @Autowired
	private WeChatMessageService weChatMessageService; 
	
	@Autowired
	private QueryZhongHuanService queryZhongHuanService; 
	
	@Autowired
	GongZhongHaoSubscriberUserService gongZhongHaoSubscriberUserService;
	
	@Autowired
	private ValidationUtil validationUtil;
	
	@Autowired
	private DifouProductStockService difouProductStockService;
	
	@Autowired
	private DifouService difouService;

	@Value("${ausland.server.hosturl}")
	private String auslandHostUrl;
	
	@Value("${ausland.queryzhonghuan.trackdetails.url}")
	private String queryZhonghuanTrackingNoDetailsUrl;
	
	@Value("${ausland.text.usehref}")
	private boolean userRef;
	
	
	@Value("${ausland.text.max.split}")
	private Integer maxSplitMessage;
	
	@Override
	@Async
	public void asyncProcessRequest(WeChatMessage message) {
		//String userName = message.getFromUserName();
		CustomSendMessage newMsg = new CustomSendMessage();
		MessageContent mContent = new MessageContent();
		if(AuslandApplicationConstants.WEIXIN_MSG_TYPE_TEXT.equalsIgnoreCase(message.getMsgType()))
		{
			logger.debug("got a text message:"+message.getContent());
			if(validationUtil.isValidChinaMobileNo(message.getContent()) == true || validationUtil.isValidChineseName(message.getContent()) == true)
	    	{
				sendQueryZhongHuanLastThreeMonthByPhoneMessage(message.getFromUserName(), message.getContent().trim());
				return;
	    	}
	    	else if(validationUtil.isValidZhongHuanTrackNo(message.getContent()) == true)
	    	{
	    		sendQueryZhongHuanDetailsByTrackingNoMessage(message.getFromUserName(), message.getContent().trim());
	    		return;
	    	}
	    	else
	    	{
	    		logger.debug("got text message which is not valid phoneno nor the valid trackingno:"+message.getContent());
	    		List<String> possibleIdList = difouProductStockService.isValidProductId(message.getContent());
	    		if(possibleIdList == null || possibleIdList.size() <= 0)
	    		{
					mContent.setContent(AuslandApplicationConstants.ZHONGHUAN_COURIER_SEARCH_PROMPT);
	    		}
	    		else if (possibleIdList.size() > 1)
	    		{
					mContent.setContent(AuslandApplicationConstants.REFINED_SEARCH_PROMPT+String.join("  ", possibleIdList)+"\n 请输入具体的商品编号查询库存信息:"); 
	    		}
	    		else
	    		{
	    			try {
						sendStockInfo(message.getFromUserName(), possibleIdList.get(0));
						return;
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace(); 
						mContent.setContent(AuslandApplicationConstants.ZHONGHUAN_COURIER_SEARCH_PROMPT_SERVERERROR);
						 
					}
	    		}
	    	}
		}
		else if(AuslandApplicationConstants.WEIXIN_MSG_TYPE_EVENT.equalsIgnoreCase(message.getMsgType()))
		{
			// this is the user subscribe event
			logger.debug("got a event message:"+message.getContent());
			//GongZhongHaoUserInfoRes res = gongZhongHaoSubscriberUserService.getWeChatUserInfo(message.getFromUserName());
			return;
		}
		
		newMsg.setContent(mContent);
		newMsg.setToUserName(message.getFromUserName());
		newMsg.setMsgType(AuslandApplicationConstants.WEIXIN_MSG_TYPE_TEXT);
		logger.debug("send message:"+newMsg.toString());
		weChatMessageService.sendMessage(newMsg);
		logger.debug("after send.");
	}
	
	private void sendStockInfo(String toUserName, String productId) throws UnsupportedEncodingException{
		CustomSendMessage newMsg = new CustomSendMessage(); 
		MessageContent mContent = new MessageContent();
		GetStockListRes res = difouService.getStockList(productId);
		if(res == null || !"0".equals(res.getReturnCode()))
		{
			mContent.setContent(AuslandApplicationConstants.ZHONGHUAN_COURIER_SEARCH_PROMPT_SERVERERROR);
		}
		else {
			if(res.getDataInfoList() != null && res.getDataInfoList().size() > 0)
			{ 
				List<String> contentTextList = new ArrayList<String>();
				StringBuffer strb = new StringBuffer();
				strb.append("商品编号：【"+productId+"】的库存信息如下：").append("\n");
				for(StockDataInfo sdi: res.getDataInfoList())
				{
					if(StringUtils.isEmpty(sdi.getSpecName()) || StringUtils.isEmpty(sdi.getStock()))
						continue;
					if(strb.toString().getBytes("utf-8").length > AuslandApplicationConstants.WEIXIN_MSG_TYPE_TEXT_MAXLENGTH){
						contentTextList.add(strb.toString());
						strb = new StringBuffer();
					}
					strb.append("型号：").append(sdi.getSpecName()).append("  库存：").append(sdi.getStock()).append("\n");
				}
                contentTextList.add(strb.toString());
                for(String s: contentTextList)
                {
                	CustomSendMessage mesg = new CustomSendMessage();
					MessageContent mContent1 = new MessageContent();
					mContent1.setContent(s);
					mesg.setContent(mContent1);
					mesg.setToUserName(toUserName);
					mesg.setMsgType(AuslandApplicationConstants.WEIXIN_MSG_TYPE_TEXT);
					weChatMessageService.sendMessage(mesg);
                }
                return;
			}
			else
			{
				mContent.setContent(AuslandApplicationConstants.NO_MATCH_PROMPT);
			}
		}
		newMsg.setContent(mContent);
		newMsg.setToUserName(toUserName);
		newMsg.setMsgType(AuslandApplicationConstants.WEIXIN_MSG_TYPE_TEXT);
		logger.debug("send message:"+newMsg.toString());
		weChatMessageService.sendMessage(newMsg);
		logger.debug("after send.");
	}
	
	private List<String> splitFydh(List<ZhongHuanFydhDetails> detailsList) throws UnsupportedEncodingException
	{
		List<String> list = new ArrayList<String>();
		StringBuffer strb = new StringBuffer();
		int i = 1;
	    
		for(ZhongHuanFydhDetails details: detailsList)
		{
			if(details == null || StringUtils.isEmpty(details.getCourierNumber()))
				continue;
			String productItems = details.getProductItems();
			if(productItems.length() > 32)
				productItems = productItems.substring(0, 32);
			String address = details.getReceiverAddress();
			if(address.length() > 32)
			{
				address = address.substring(0, 32);
			}
			String preString = strb.toString();
			strb.append("【").append(i).append("】:");
			if(userRef == true)
			{
				strb.append("<a href=\"").append(auslandHostUrl).append(queryZhonghuanTrackingNoDetailsUrl).append("?trackingno=").append(details.getCourierNumber()).append("\">");
			}
			strb.append(details.getCourierNumber());
			if(userRef == true)
			{
				strb.append("</a>");
			}
			strb.append("\n时间：").append(details.getCourierCreatedDateTime()).append("\n状态：").append(details.getCustomStatus()).append("\n收件人：").append(details.getReceiverName()).append("\n商品：").append(productItems).append("\n");
            if(strb.toString().getBytes("utf-8").length > AuslandApplicationConstants.WEIXIN_MSG_TYPE_TEXT_MAXLENGTH)
			{
				logger.debug("added the string:"+preString);
				list.add(preString);
				strb = new StringBuffer();
				strb.append("【").append(i).append("】:");
				if(userRef == true)
				{
					strb.append("<a href=\"").append(auslandHostUrl).append(queryZhonghuanTrackingNoDetailsUrl).append("?trackingno=").append(details.getCourierNumber()).append("\">");
				}
				strb.append(details.getCourierNumber());
				if(userRef == true)
				{
					strb.append("</a>");
				}
				strb.append("\n时间：").append(details.getCourierCreatedDateTime()).append("\n状态：").append(details.getCustomStatus()).append("\n收件人：").append(details.getReceiverName()).append("\n商品：").append(productItems).append("\n");
				//strb.append("【").append(i).append("】:").append("<a href=\"").append(auslandHostUrl).append(queryZhonghuanTrackingNoDetailsUrl).append("?trackingno=").append(details.getCourierNumber()).append("\">").append(details.getCourierNumber()).append("</a>").append("\n时间：").append(details.getCourierCreatedDateTime()).append("\n状态：").append(details.getCustomStatus()).append("\n收件人：").append(details.getReceiverName()).append("\n商品：").append(productItems).append("\n收件人地址：").append(address).append("\n");
			}
			i ++;
			if(i > maxSplitMessage)
				break;
		}
		if(strb.toString().length() > 0)
		{
			logger.debug("added the last string:"+strb.toString());
			list.add(strb.toString());
		}
		return list;
	}
	
	private void sendQueryZhongHuanLastThreeMonthByPhoneMessage(String toUserName, String phoneNo)
	{
		CustomSendMessage newMsg = new CustomSendMessage(); 
		QueryZhongHuanLastThreeMonthByPhoneNoRes res =  queryZhongHuanService.queryZhongHuanLastThreeMonthbyPhoneNo(phoneNo);
		if(res == null)
    	{
			MessageContent mContent = new MessageContent();
			mContent.setContent(AuslandApplicationConstants.ZHONGHUAN_COURIER_SEARCH_PROMPT_SERVERERROR);
			newMsg.setContent(mContent);
    	}
    	else
    	{
    		if(AuslandApplicationConstants.STATUS_OK.equalsIgnoreCase(res.getStatus()))
	    	{
	    		if(res.getFydhList() == null  || res.getFydhList().size()<= 0)
	    		{
	    			MessageContent mContent = new MessageContent();
					mContent.setContent(AuslandApplicationConstants.ZHONGHUAN_COURIER_SEARCH_PROMPT_NOORDERINFO);
	    			newMsg.setContent(mContent);
	    		}
	    		else
	    		{
	    			try
	    			{
	    				List<String> contentList = splitFydh(res.getFydhList());
	    				logger.debug("will send out "+contentList.size()+" messages to user:"+toUserName);
	    				for(String s:contentList)
	    				{
	    					CustomSendMessage mesg = new CustomSendMessage();
	    					MessageContent mContent = new MessageContent();
							mContent.setContent(s);
							mesg.setContent(mContent);
							mesg.setToUserName(toUserName);
							mesg.setMsgType(AuslandApplicationConstants.WEIXIN_MSG_TYPE_TEXT);
							weChatMessageService.sendMessage(mesg);
	    				}
	    				logger.debug("completed...");
	    				return;
	    			}
	    			catch(Exception e)
	    			{
	    				MessageContent mContent = new MessageContent();
						mContent.setContent(AuslandApplicationConstants.ZHONGHUAN_COURIER_SEARCH_PROMPT_SERVERERROR);
	    				newMsg.setContent(mContent);
	    			}
	    		}
	    	}
	    	else
	    	{
	    		MessageContent mContent = new MessageContent();
				mContent.setContent(res.getErrorDetails());
    			newMsg.setContent(mContent);
	    	}
    	}
		newMsg.setToUserName(toUserName);
		newMsg.setMsgType(AuslandApplicationConstants.WEIXIN_MSG_TYPE_TEXT);
		logger.debug("send message:"+newMsg.toString());
		weChatMessageService.sendMessage(newMsg);
		logger.debug("after send.");
	}
	
	
	private void sendQueryZhongHuanDetailsByTrackingNoMessage(String toUserName, String trackingNo)
	{
		CustomSendMessage newMsg = new CustomSendMessage(); 
		QueryZhongHuanDetailsByTrackingNoRes res = queryZhongHuanService.queryZhongHuanDetailsByTrackingNo(trackingNo);
		if(res == null)
    	{
			MessageContent mContent = new MessageContent();
			mContent.setContent(AuslandApplicationConstants.ZHONGHUAN_COURIER_SEARCH_PROMPT_SERVERERROR);
			newMsg.setContent(mContent);
    	}
    	else
    	{
    		if(AuslandApplicationConstants.STATUS_OK.equalsIgnoreCase(res.getStatus()))
	    	{
    			if(res.getBack() == null  || res.getBack().getLogisticsback() == null || res.getBack().getLogisticsback().size() <= 0)
	    		{
	    			MessageContent mContent = new MessageContent();
					mContent.setContent(AuslandApplicationConstants.ZHONGHUAN_COURIER_SEARCH_PROMPT_NOCOURIERINFO);
	    			newMsg.setContent(mContent);
	    		}
	    		else
	    		{
	    			MessageContent mContent = new MessageContent();
	    			StringBuffer strb = new StringBuffer();
	    			for(Logisticsback lb : res.getBack().getLogisticsback())
	    			{
	    				if(StringUtils.isEmpty(lb.getZtai()))
	    					continue;
	    				strb.append(lb.getTime()).append(" ").append(lb.getZtai()).append("\n");
	    			}
					mContent.setContent(strb.toString());
	    			newMsg.setContent(mContent);
	    		}
	    	}
    		else
	    	{
	    		MessageContent mContent = new MessageContent();
				mContent.setContent(res.getErrorDetails());
    			newMsg.setContent(mContent);
	    	}
    	}
		newMsg.setToUserName(toUserName);
		newMsg.setMsgType(AuslandApplicationConstants.WEIXIN_MSG_TYPE_TEXT);
		
		logger.debug("send message:"+newMsg.toString());
		weChatMessageService.sendMessage(newMsg);
		logger.debug("after send.");
	}
}
