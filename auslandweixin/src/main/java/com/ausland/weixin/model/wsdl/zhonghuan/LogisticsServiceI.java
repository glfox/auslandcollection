/**
 * LogisticsServiceI.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ausland.weixin.model.wsdl.zhonghuan;

public interface LogisticsServiceI extends java.rmi.Remote {
    public java.lang.String getLogisticsInformation(java.lang.String fydh, java.lang.String countrytype) throws java.rmi.RemoteException, com.ausland.weixin.model.wsdl.zhonghuan.InvocationTargetException, com.ausland.weixin.model.wsdl.zhonghuan.NoSuchMethodException, com.ausland.weixin.model.wsdl.zhonghuan.IllegalAccessException;
    public java.lang.String getYtLoggistics(java.lang.String fydh) throws java.rmi.RemoteException, com.ausland.weixin.model.wsdl.zhonghuan.InvocationTargetException, com.ausland.weixin.model.wsdl.zhonghuan.NoSuchMethodException, com.ausland.weixin.model.wsdl.zhonghuan.IllegalAccessException;
    public java.lang.String getTeltoFydh(java.lang.String chrsjrdh, java.lang.String username, java.lang.String password, java.lang.String didbh) throws java.rmi.RemoteException;
}
