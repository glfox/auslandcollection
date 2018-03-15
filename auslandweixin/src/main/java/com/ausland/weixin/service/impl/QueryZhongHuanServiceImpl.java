package com.ausland.weixin.service.impl;

import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.cache.annotation.CacheResult;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.cxf.interceptor.FaultOutInterceptor;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ausland.weixin.config.AuslandApplicationConstants;
import com.ausland.weixin.model.reqres.QueryZhongHuanDetailsByTrackingNoRes;
import com.ausland.weixin.model.reqres.QueryZhongHuanLastThreeMonthByPhoneNoRes;
import com.ausland.weixin.model.reqres.ZhongHuanFydhDetails;
import com.ausland.weixin.model.zhonghuan.wsdl.LogisticsServiceI;
import com.ausland.weixin.model.zhonghuan.xml.Back;
import com.ausland.weixin.model.zhonghuan.xml.Tel;
import com.ausland.weixin.model.zhonghuan.xml.Tel.Fydh;
import com.ausland.weixin.model.zhonghuan.xml.Tel.Fydh.Njxx;
import com.ausland.weixin.service.QueryZhongHuanService;
import com.ausland.weixin.util.ValidationUtil;
import com.ausland.weixin.util.ZhongHuanFydhDetailsComparator;

 
@Service
public class QueryZhongHuanServiceImpl implements QueryZhongHuanService{

	private static final Logger logger = LoggerFactory.getLogger(QueryZhongHuanServiceImpl.class);
    
	@Value("${ausland.zhonghuan.username}")
	private String zhonghuanusername;
	
	@Value("${ausland.zhonghuan.password}")
	private String zhonghuanpassword;
	
	@Value("${zhonghuan.url}")
	private String zhonghuanUrl;
	
	@Value("${ausland.zhonghuan.no}")
	private String zhonghuanno;
	
	@Autowired
	private ValidationUtil validationUtil;
	
	private JaxWsProxyFactoryBean factory;
	
	private LogisticsServiceI port;
	
	//private Client client;
	
	@PostConstruct
	private void init(){
		logger.debug("entered QueryZhongHuanServiceImpl.init() with zhonghuanUrl:"+zhonghuanUrl);
		Properties properties = System.getProperties();
        properties.put("org.apache.cxf.stax.allowInsecureParser", "1");
        System.setProperties(properties);
        
        this.factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(LogisticsServiceI.class);
        factory.setAddress(zhonghuanUrl);
       
        LoggingInInterceptor logging_in = new LoggingInInterceptor();
        logging_in.setPrettyLogging(true);
        factory.getInInterceptors().add(logging_in);

        LoggingOutInterceptor logging_out = new LoggingOutInterceptor();
        logging_out.setPrettyLogging(true);
        
        FaultOutInterceptor fault_out = new FaultOutInterceptor();

        factory.getOutInterceptors().add(logging_out);
        factory.getOutInterceptors().add(fault_out);

        port = (LogisticsServiceI)factory.create(LogisticsServiceI.class);
        //client = ClientProxy.getClient(port);
        
        logger.debug("exited QueryZhongHuanServiceImpl.init() successfully.");
	}
	
	private Tel parseXmlToTel(String xml)
	{
		if(StringUtils.isEmpty(xml)) return null;
		try
		{
			StringReader reader = new StringReader(xml);
			JAXBContext jaxbContext = JAXBContext.newInstance(Tel.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			Tel tel = (Tel)jaxbUnmarshaller.unmarshal(reader);
			return tel;
		}
		catch(Exception e)
		{
			logger.debug("cannot parse xml:"+ xml+" back to Tel object:"+e.getMessage());
		}
		return null;
	}
	
	@Override
	@CacheResult(cacheName="zhonghuanlastthreemonth")
	public QueryZhongHuanLastThreeMonthByPhoneNoRes queryZhongHuanLastThreeMonthbyPhoneNo(String phoneNo) {
		QueryZhongHuanLastThreeMonthByPhoneNoRes res = new QueryZhongHuanLastThreeMonthByPhoneNoRes();
		
		if(StringUtils.isEmpty(phoneNo) || validationUtil.isValidChinaMobileNo(phoneNo) == false)
		{
			res.setErrorDetails("input phone number is invalid.");
			res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
			return res;
		} 
		String trimedPhoneNo = validationUtil.trimPhoneNo(phoneNo);
		logger.debug("entered  queryZhongHuanLastThreeMonthbyPhoneNo with phoneNo:"+trimedPhoneNo);
		try
		{
			String xmlRes = port.getTeltoFydh(trimedPhoneNo, zhonghuanusername, zhonghuanpassword, zhonghuanno);
			//xmlRes = validationUtil.removeCDATA(xmlRes);
			if(StringUtils.isEmpty(xmlRes))
			{
				res.setErrorDetails("got empty response from zhonghuan getTelToFydh service."); 
				res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
				return res;
			}
			 
			logger.debug("start to parse the response:"+xmlRes);
			Tel tel = parseXmlToTel(xmlRes);
			if(tel == null)
			{
				res.setErrorDetails("cannot parse xml back to Tel object");
				res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
				return res;
			}
			if(!"true".equalsIgnoreCase(tel.getIssuccess()))
			{
				res.setErrorDetails("got error message from zhonghuan getTelToFydh services:"+tel.getMessage());
				res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
				return res;
			}
			List<ZhongHuanFydhDetails> list = parseTelToFydhList(tel);
			Collections.sort(list, new ZhongHuanFydhDetailsComparator());
			res.setFydhList(list);
			res.setStatus(AuslandApplicationConstants.STATUS_OK);
		    return res;
		}
		catch(Exception e)
		{
			logger.error("got exception calling zhonghuan getTelToFydh :"+e.getMessage());
			res.setErrorDetails("got exception duing zhonghuan getTelToFydh:"+e.getMessage());
		}
	     
		return res;
	}
	
	private List<ZhongHuanFydhDetails> parseTelToFydhList(Tel tel)
	{
		List<ZhongHuanFydhDetails> list = new ArrayList<ZhongHuanFydhDetails>();
		if(tel == null || tel.getFydhlist() == null || tel.getFydhlist().size()<= 0)
			return list;
		for(Fydh fydh : tel.getFydhlist())
		{
			ZhongHuanFydhDetails details = new ZhongHuanFydhDetails();
			details.setCourierChinaNumber(fydh.getChrbgdh());
			details.setCourierCompany(fydh.getCkdhm());
			if(!StringUtils.isEmpty(fydh.getChrlrsj()))
			{
				SimpleDateFormat df = new SimpleDateFormat(AuslandApplicationConstants.DATE_STRING_FORMAT);
				try {
					Date d = df.parse(fydh.getChrlrsj());
					details.setCourierCreatedDateTime(df.format(d));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			details.setCourierNumber(fydh.getChrfydh());
			if("true".equalsIgnoreCase(fydh.getChrshzt()))
			{
				details.setCustomStatus("已寄出");
			}
			else if("false".equalsIgnoreCase(fydh.getChrshzt()))
			{
				details.setCustomStatus("未寄出");
			}
			else
			{
				details.setCustomStatus("状态未知");
			}
			
			details.setReceiverAddress(fydh.getChrsjrdz());
			if(!StringUtils.isEmpty(fydh.getChrsjr()))
			{
				details.setReceiverName(fydh.getChrsjr().replaceAll("([\\u4e00-\\u9fa5]{1})(.*)", "*" + "$2"));
			}
			details.setReceiverPhone(fydh.getChrsjrdh());
			details.setWeight(fydh.getChrzl());
			if(fydh.getNjxx() != null && fydh.getNjxx().size() > 0)
			{
				StringBuffer strb = new StringBuffer();
				for(Njxx njxx:fydh.getNjxx())
				{
					if(!StringUtils.isEmpty(njxx.getChrpm()))
					{
						strb.append(njxx.getChrpm()).append(" ");
					}
					if(!StringUtils.isEmpty(njxx.getChrpp()))
					{
						strb.append(njxx.getChrpp()).append(" ");
					}
					if(!StringUtils.isEmpty(njxx.getChrggxh()))
					{
						strb.append(njxx.getChrggxh()).append(" ");
					}
					if(!StringUtils.isEmpty(njxx.getChrjs()))
					{
						strb.append(njxx.getChrjs()).append(" ");
					}
					strb.append(",");
				}
				String productItems = strb.toString();
				if(productItems.endsWith(","))
				{
					productItems = productItems.substring(0, productItems.lastIndexOf(","));
				}
				details.setProductItems(productItems);
			}
			list.add(details);
		}
		logger.debug("returns ZhongHuanFydhDetails list: "+ToStringBuilder.reflectionToString(list));
		return list;
	}
	private Back parseXmlToBack(String xml)
	{
		if(StringUtils.isEmpty(xml)) return null;
		try
		{
			StringReader reader = new StringReader(xml);
			JAXBContext jaxbContext = JAXBContext.newInstance(Back.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			Back back = (Back)jaxbUnmarshaller.unmarshal(reader);
			return back;
		}
		catch(Exception e)
		{
			logger.debug("cannot parse xml:"+ xml+" back to Back object:"+e.getMessage());
		}
		return null;
	}
	
	private QueryZhongHuanDetailsByTrackingNoRes getTrackingDetailsbyTrackingNo(String trackingNo)
	{
		QueryZhongHuanDetailsByTrackingNoRes res = new QueryZhongHuanDetailsByTrackingNoRes();
		if(StringUtils.isEmpty(trackingNo) || validationUtil.isValidZhongHuanTrackNo(trackingNo) == false) 
		{
			res.setErrorDetails("trackingNo:"+ trackingNo +" is not valid.");
			res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
			return res;
		}
		 
		try
		{
			String xmlRes = port.getLogisticsInformation(trackingNo, "au");
			Back back = parseXmlToBack(xmlRes);
			if(back == null)
			{
				res.setErrorDetails("cannot parse xml back to Back object");
				res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
				return res;
			}
			res.setStatus(AuslandApplicationConstants.STATUS_OK);
			res.setBack(back);
			return res;
		}
		catch(Exception e)
		{
			logger.error("caught exception during getLogisticInformation from zhonghuan:"+e.getMessage());
			res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
			res.setErrorDetails("caught exception during getLogisticInformation from zhonghuan:" + e.getMessage());
		}
		return  res;
	}

	@CacheResult(cacheName="zhonghuandetails")
	@Override
	public QueryZhongHuanDetailsByTrackingNoRes queryZhongHuanDetailsByTrackingNo(String trackingNo) {
		try
		{
			QueryZhongHuanDetailsByTrackingNoRes backres = getTrackingDetailsbyTrackingNo(trackingNo);
			return backres;
		}
        catch(Exception e)
		{
        	logger.error("caught exception during queryZhongHuanDetailsByTrackingNo:"+trackingNo+", exceptionmessage:"+e.getMessage());
		}
		return null;
	}
	
	

	
}
