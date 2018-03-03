package com.ausland.weixin.service.impl;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.interceptor.FaultOutInterceptor;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ausland.weixin.model.reqres.QueryZhongHuanDetailsByTrackingNoRes;
import com.ausland.weixin.model.reqres.QueryZhongHuanLastThreeMonthByPhoneNoRes;
import com.ausland.weixin.model.zhonghuan.wsdl.LogisticsServiceI;
import com.ausland.weixin.model.zhonghuan.xml.Back;
import com.ausland.weixin.model.zhonghuan.xml.Tel;
import com.ausland.weixin.model.zhonghuan.xml.Tel.Fydh;
import com.ausland.weixin.service.QueryZhongHuanService;
import com.ausland.weixin.util.ValidationUtil;

 
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
	public QueryZhongHuanLastThreeMonthByPhoneNoRes queryZhongHuanLastThreeMonthbyPhoneNo(String phoneNo, Boolean onlyTrackingNo) {
		QueryZhongHuanLastThreeMonthByPhoneNoRes res = new QueryZhongHuanLastThreeMonthByPhoneNoRes();
		
		if(StringUtils.isEmpty(phoneNo) || validationUtil.isValidChinaMobileNo(phoneNo) == false)
		{
			res.setErrorDetails("input phone number is invalid.");
			return res;
		} 
		String trimedPhoneNo = validationUtil.trimPhoneNo(phoneNo);
		logger.debug("entered  queryZhongHuanLastThreeMonthbyPhoneNo with phoneNo:"+trimedPhoneNo);
		try
		{
			String xmlRes = port.getTeltoFydh(trimedPhoneNo, zhonghuanusername, zhonghuanpassword, zhonghuanno);
			xmlRes = validationUtil.removeCDATA(xmlRes);
			if(StringUtils.isEmpty(xmlRes))
			{
				res.setErrorDetails("got empty response from zhonghuan getTelToFydh service."); 
				return res;
			}
			 
			logger.debug("start to parse the response:"+xmlRes);
			Tel tel = parseXmlToTel(xmlRes);
			if(tel == null)
			{
				res.setErrorDetails("cannot parse xml back to Tel object");
				return res;
			}
			res.setTrackingNos(tel);
			if(!"true".equalsIgnoreCase(tel.getIssuccess()))
			{
				res.setErrorDetails("got error message from zhonghuan getTelToFydh services:"+tel.getMessage());
				return res;
			}
			if(onlyTrackingNo == true) return res;
			
			if("true".equalsIgnoreCase(tel.getIssuccess()) && tel.getFydhlist() != null && tel.getFydhlist().size() > 0)
			{
				//get the details from tracking no
				List<QueryZhongHuanDetailsByTrackingNoRes> backresList = new ArrayList<QueryZhongHuanDetailsByTrackingNoRes>();
				for(Fydh fydh : tel.getFydhlist())
				{
					QueryZhongHuanDetailsByTrackingNoRes backres = getTrackingDetailsbyTrackingNo(fydh.getChrfydh());
					backresList.add(backres);
				}
				res.setTrackingDetails(backresList);
				return res;
			}
		 
		}
		catch(Exception e)
		{
			logger.error("got exception calling zhonghuan getTelToFydh :"+e.getMessage());
			res.setErrorDetails("got exception duing zhonghuan getTelToFydh:"+e.getMessage());
		}
	     
		return res;
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
		if(StringUtils.isEmpty(trackingNo) || validationUtil.isValidZhongHuanTrackNo(trackingNo)) 
		{
			res.setErrorDetails("trackingNo:"+ trackingNo +" is not valid.");
			return res;
		}
		 
		try
		{
			String xmlRes = port.getLogisticsInformation(trackingNo, "au");
			Back back = parseXmlToBack(xmlRes);
			if(back == null)
			{
				res.setErrorDetails("cannot parse xml back to Back object");
				return res;
			}
			res.setBack(back);
			return res;
		}
		catch(Exception e)
		{
			logger.error("caught exception during getLogisticInformation from zhonghuan:"+e.getMessage());
			res.setErrorDetails("caught exception during getLogisticInformation from zhonghuan:" + e.getMessage());
		}
		return  res;
	}

	@Override
	public QueryZhongHuanDetailsByTrackingNoRes queryZhongHuanDetailsByTrackingNo(String trackingNo) {
		// TODO Auto-generated method stub
		 
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
