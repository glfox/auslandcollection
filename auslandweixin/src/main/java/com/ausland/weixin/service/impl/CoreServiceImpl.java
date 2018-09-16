package com.ausland.weixin.service.impl;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.ausland.weixin.config.AuslandApplicationConstants;
import com.ausland.weixin.model.CustomSendImageMessage;
import com.ausland.weixin.model.CustomSendMessage;
import com.ausland.weixin.model.MessageContent;
import com.ausland.weixin.model.WeChatImage;
import com.ausland.weixin.model.difou.GetStockListRes;
import com.ausland.weixin.model.difou.StockDataInfo;
import com.ausland.weixin.model.reqres.GongZhongHaoUserInfoRes;
import com.ausland.weixin.model.reqres.QueryZhongHuanDetailsByTrackingNoRes;
import com.ausland.weixin.model.reqres.QueryZhongHuanLastThreeMonthByPhoneNoRes;
import com.ausland.weixin.model.reqres.WeiXinImageUploadRes;
import com.ausland.weixin.model.reqres.ZhongHuanFydhDetails;
 
import com.ausland.weixin.model.xml.WeChatMessage;
 
import com.ausland.weixin.model.zhonghuan.xml.Back.Logisticsback;
import com.ausland.weixin.service.AuthService;
import com.ausland.weixin.service.CoreService;
import com.ausland.weixin.service.DifouProductStockService;
import com.ausland.weixin.service.DifouService;
import com.ausland.weixin.service.GongZhongHaoSubscriberUserService;
import com.ausland.weixin.service.QueryZhongHuanService;
import com.ausland.weixin.service.WeChatMessageService;
import com.ausland.weixin.util.ValidationUtil;
import com.ausland.weixin.util.ZhongHuanFydhDetailsComparator;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CoreServiceImpl implements CoreService {

	private static final Logger logger = LoggerFactory.getLogger(CoreServiceImpl.class);
	
	@Value("${message.send.url}")
	private String messageSendUrl;
	
	@Value("${photo.send.url}")
	private String photoSendUrl;

	 @Autowired
	private WeChatMessageService weChatMessageService; 
	 
    @Autowired
	private AuthService authService;
    
    @Autowired
	private RestTemplate restTemplate;
    
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
	
	@Value("${upload.packing.photo.server.directory}")
	private String packingPhotoDirectory;
	
	@Override
	@Async
	public void asyncProcessRequest(WeChatMessage message) {
		//String userName = message.getFromUserName();
		CustomSendMessage newMsg = new CustomSendMessage();
		MessageContent mContent = new MessageContent();
		try
		{
			if(AuslandApplicationConstants.WEIXIN_MSG_TYPE_TEXT.equalsIgnoreCase(message.getMsgType()))
			{
				 logger.debug("got a text message:"+message.getContent());
				 String courierNo = message.getContent().trim();
				 if(!validationUtil.isValidZhongHuanTrackNo(courierNo)) {
					 mContent.setContent("您输入的不是一个有效的重庆中环运单号，请重新输入"); 
				 }
				 else {
					 File dir = new File(packingPhotoDirectory+"processed/");
					 if(dir == null || !dir.isDirectory() ) {
						 mContent.setContent("不能找到上传图片的目录，请联系客服获取打包图片");
					 }
					 else
					 {
						String[] filenames = dir.list();
						boolean found = false;
						if(filenames != null && filenames.length > 0) {
							for(String filename: filenames) {
								String baseName = FilenameUtils.getBaseName(filename);
								if(courierNo.equalsIgnoreCase(baseName)) {
									//find the packing photo
									WeiXinImageUploadRes res = uploadToWeiXin(packingPhotoDirectory+"processed/"+filename);
									if(res == null || !StringUtils.isEmpty(res.getErrMsg()) || StringUtils.isEmpty(res.getMediaId())) {
										mContent.setContent("不能上传打包图片到微信服务器端，请联系客服获取打包图片");
									}
									else {
										CustomSendImageMessage photoMessage = new CustomSendImageMessage();
										WeChatImage image = new WeChatImage();
										image.setMediaId(res.getMediaId());
										photoMessage.setImage(image);
										photoMessage.setMsgType(AuslandApplicationConstants.WEIXIN_MSG_TYPE_PHOTO);
										photoMessage.setToUserName(message.getFromUserName());
										logger.debug("send message:"+photoMessage.toString());
										weChatMessageService.sendMessage(photoMessage);
										logger.debug("after send.");
										return;
									}
								}
							}
							if(!found) {
								mContent.setContent("不能找到打包图片，目前仅提供澳洲发货的重庆中环类产品的打包图片");
							}
						}
					 }
				 }
				
				logger.debug("got a text message:"+message.getContent());
		
			}
			else if(AuslandApplicationConstants.WEIXIN_MSG_TYPE_EVENT.equalsIgnoreCase(message.getMsgType()))
			{
				// this is the user subscribe event
				logger.debug("got a event message:"+message.getContent());
				//GongZhongHaoUserInfoRes res = gongZhongHaoSubscriberUserService.getWeChatUserInfo(message.getFromUserName());
				return;
			}
			else if("file".equalsIgnoreCase(message.getMsgType())){
	            logger.debug("got a file type message:"+message.getContent());
				return;
			}
			else{
				// ignore
				logger.debug("got unknown message type:"+message.getMsgType());
				return;
			}
		}catch(Exception e)
		{
			logger.error("caught exception during asyncProcessRequest:"+e.getMessage());
			e.printStackTrace();
			mContent.setContent("服务器异常，请联系客服获取打包图片");
		}

		newMsg.setContent(mContent);
		newMsg.setToUserName(message.getFromUserName());
		newMsg.setMsgType(AuslandApplicationConstants.WEIXIN_MSG_TYPE_TEXT);
		logger.debug("send message:"+newMsg.toString());
		weChatMessageService.sendMessage(newMsg);
		logger.debug("after send.");
		return;
	}
	
	public WeiXinImageUploadRes uploadphoto(String fileUrl,String accesstoken,String type) throws IOException{

		//创建一个文件file

		File file = new File(fileUrl);

		//判断file文件是否为空

		if(file==null)   throw new IOException("文件不存在");
        StringBuffer url = new StringBuffer("https://api.weixin.qq.com/cgi-bin/media/upload?access_token=");
		url.append(accesstoken).append("&type=image");
        logger.debug("post url:"+url.toString());
		URL urlobj = new URL(url.toString());

		urlobj.openStream();

		//httpURLConnection实例的作用是用来做一个请求但潜在网络连接到HTTP服务器

		HttpURLConnection urlconnection = (HttpURLConnection) urlobj.openConnection();

		//进行urconnection对象设置

		urlconnection.setRequestMethod("POST");

		urlconnection.setDoInput(true);

		urlconnection.setDoOutput(true);

		urlconnection.setUseCaches(false);

		//设置请求头信息

		urlconnection.setRequestProperty("Connection", "Keep-Alive");

		urlconnection.setRequestProperty("Charset", "UTF-8");

		//设置边界

		//currentTimeMillis方法获取当前时间信息

		String BOUNDARY = "-----------"+System.currentTimeMillis();

		//Content-Type，内容类型一般是指网页中存在的Content-Type，用于定义网络文件的类型和网页的编码

		//multipart/from-data请求文件上传类型

		urlconnection.setRequestProperty("Content-Type","multipart/form-data;boundary="+BOUNDARY);

		StringBuilder sb = new StringBuilder();

		sb.append("--");

		sb.append(BOUNDARY);

		sb.append("\r\n");

		//Content-Disposition就是当用户请求所得内容存为一个文件的提供一个默认的文件名

		sb.append("Content-Disposition:form-data;name=\"file\";filename=\""+file.getName()+"\"\r\n");

		//application.octet-stream 只能提交二进制，而且提交一个二进制，如果提交文件的话，只能提交一个文件

		//后台接收参数只能有一个，而且还只能是流或者是字节码

		sb.append("Content-Type:application/octet-stream\r\n\r\n");

		//创建一个byte数组

		       //sb对象数据转换成字节码

		byte[] head = sb.toString().getBytes("utf-8");

		//获取输出流   getoutputStream作用就是返回使用此连接的流

		//OutputStream 该抽象类是所有类的字节输出流的父类

		//DataOutputStream 创建一个新的数据输出流，以便将数据写入指定的基础输出流,返回为零

		OutputStream output = new DataOutputStream(urlconnection.getOutputStream());

		//在将字节码数据转入到流对象中

		output.write(head);

		//文件正文部分

		//把文件一流文件的方式 推入url中

		//DateinputStream的作用就是file目录的文件以流的方式输入进来
        logger.debug("checkpoint1");
		DataInputStream in = new DataInputStream(new FileInputStream(file));

		int bytes = 0;

		byte [] b = new byte[1024];

		while((bytes=in.read(b))!=-1){

		output.write(b, 0, bytes);

		}
		logger.debug("checkpoint2");
		//关闭输入流

		in.close();

		//结尾部分

		byte []foot = ("\r\n--"+BOUNDARY+"--\r\n").getBytes("utf-8"); //定义最后数据分割线

		//把定义最后的数据分割线字节码数据转入流对象中

		output.write(foot);

		//刷新

		output.flush();

		//关闭

		output.close();
		logger.debug("checkpoint3");
		StringBuffer buffer = new StringBuffer();

		BufferedReader reader = null;

		String result = null;

		try {

		//定义一个BufferRader输入流来读取url的响应

		reader = new BufferedReader(new InputStreamReader(urlconnection.getInputStream()));

		System.out.println(urlconnection.getInputStream());

		String line = null;

		//while循环读取文字

		while((line=reader.readLine())!=null){

		buffer.append(line);

		}

		if(result == null){

		result = buffer.toString();

		}

		} catch (IOException e) {

		   e.printStackTrace();

		}finally{

		//关闭流

		            if(reader!=null){

		            reader.close();

		            logger.debug("关闭");

		            }

		}
		logger.debug("checkpoint4");
		ObjectMapper objectMapper = new ObjectMapper();
		WeiXinImageUploadRes resObject = objectMapper.readValue(result, WeiXinImageUploadRes.class);
		logger.debug("parse json string to resObject:"+resObject.toString());

		return resObject;

	}
	
	private WeiXinImageUploadRes uploadToWeiXin(String imagePath) throws IOException {
		logger.debug("uploadToWeiXin entered with imagePath:" + imagePath);
		String accessToken = authService.getAccessToken();
		logger.debug("authService got accessToken:" + accessToken);
		
		return uploadphoto(imagePath, accessToken, "image");
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
