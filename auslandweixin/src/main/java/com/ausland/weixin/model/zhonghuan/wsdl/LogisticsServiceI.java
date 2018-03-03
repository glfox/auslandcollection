package com.ausland.weixin.model.zhonghuan.wsdl;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 3.1.11
 * 2018-03-03T08:40:32.765-08:00
 * Generated source version: 3.1.11
 * 
 */
@WebService(targetNamespace = "http://zh.au.service.LogisticsServiceI.com", name = "LogisticsServiceI")
@XmlSeeAlso({ObjectFactory.class})
public interface LogisticsServiceI {

    @WebMethod
    @RequestWrapper(localName = "getYtLoggistics", targetNamespace = "http://zh.au.service.LogisticsServiceI.com", className = "com.ausland.weixin.model.zhonghuan.wsdl.GetYtLoggistics")
    @ResponseWrapper(localName = "getYtLoggisticsResponse", targetNamespace = "http://zh.au.service.LogisticsServiceI.com", className = "com.ausland.weixin.model.zhonghuan.wsdl.GetYtLoggisticsResponse")
    @WebResult(name = "return", targetNamespace = "")
    public java.lang.String getYtLoggistics(
        @WebParam(name = "fydh", targetNamespace = "")
        java.lang.String fydh
    ) throws NoSuchMethodException_Exception, IllegalAccessException_Exception, InvocationTargetException_Exception;

    @WebMethod
    @RequestWrapper(localName = "getTeltoFydh", targetNamespace = "http://zh.au.service.LogisticsServiceI.com", className = "com.ausland.weixin.model.zhonghuan.wsdl.GetTeltoFydh")
    @ResponseWrapper(localName = "getTeltoFydhResponse", targetNamespace = "http://zh.au.service.LogisticsServiceI.com", className = "com.ausland.weixin.model.zhonghuan.wsdl.GetTeltoFydhResponse")
    @WebResult(name = "return", targetNamespace = "")
    public java.lang.String getTeltoFydh(
        @WebParam(name = "chrsjrdh", targetNamespace = "")
        java.lang.String chrsjrdh,
        @WebParam(name = "username", targetNamespace = "")
        java.lang.String username,
        @WebParam(name = "password", targetNamespace = "")
        java.lang.String password,
        @WebParam(name = "didbh", targetNamespace = "")
        java.lang.String didbh
    );

    @WebMethod
    @RequestWrapper(localName = "getLogisticsInformation", targetNamespace = "http://zh.au.service.LogisticsServiceI.com", className = "com.ausland.weixin.model.zhonghuan.wsdl.GetLogisticsInformation")
    @ResponseWrapper(localName = "getLogisticsInformationResponse", targetNamespace = "http://zh.au.service.LogisticsServiceI.com", className = "com.ausland.weixin.model.zhonghuan.wsdl.GetLogisticsInformationResponse")
    @WebResult(name = "return", targetNamespace = "")
    public java.lang.String getLogisticsInformation(
        @WebParam(name = "fydh", targetNamespace = "")
        java.lang.String fydh,
        @WebParam(name = "countrytype", targetNamespace = "")
        java.lang.String countrytype
    ) throws NoSuchMethodException_Exception, IllegalAccessException_Exception, InvocationTargetException_Exception;
}