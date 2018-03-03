/**
 * LogisticsServiceImplServiceSoapBindingSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ausland.weixin.model.wsdl.zhonghuan;

public class LogisticsServiceImplServiceSoapBindingSkeleton implements com.ausland.weixin.model.wsdl.zhonghuan.LogisticsServiceI, org.apache.axis.wsdl.Skeleton {
    private com.ausland.weixin.model.wsdl.zhonghuan.LogisticsServiceI impl;
    private static java.util.Map _myOperations = new java.util.Hashtable();
    private static java.util.Collection _myOperationsList = new java.util.ArrayList();

    /**
    * Returns List of OperationDesc objects with this name
    */
    public static java.util.List getOperationDescByName(java.lang.String methodName) {
        return (java.util.List)_myOperations.get(methodName);
    }

    /**
    * Returns Collection of OperationDescs
    */
    public static java.util.Collection getOperationDescs() {
        return _myOperationsList;
    }

    static {
        org.apache.axis.description.OperationDesc _oper;
        org.apache.axis.description.FaultDesc _fault;
        org.apache.axis.description.ParameterDesc [] _params;
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "fydh"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "countrytype"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getLogisticsInformation", _params, new javax.xml.namespace.QName("", "return"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://zh.au.service.LogisticsServiceI.com", "getLogisticsInformation"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getLogisticsInformation") == null) {
            _myOperations.put("getLogisticsInformation", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getLogisticsInformation")).add(_oper);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("InvocationTargetException");
        _fault.setQName(new javax.xml.namespace.QName("http://zh.au.service.LogisticsServiceI.com", "InvocationTargetException"));
        _fault.setClassName("com.LogisticsServiceI.service.au.zh.InvocationTargetException");
        _fault.setXmlType(new javax.xml.namespace.QName("http://zh.au.service.LogisticsServiceI.com", "InvocationTargetException"));
        _oper.addFault(_fault);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("NoSuchMethodException");
        _fault.setQName(new javax.xml.namespace.QName("http://zh.au.service.LogisticsServiceI.com", "NoSuchMethodException"));
        _fault.setClassName("com.LogisticsServiceI.service.au.zh.NoSuchMethodException");
        _fault.setXmlType(new javax.xml.namespace.QName("http://zh.au.service.LogisticsServiceI.com", "NoSuchMethodException"));
        _oper.addFault(_fault);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("IllegalAccessException");
        _fault.setQName(new javax.xml.namespace.QName("http://zh.au.service.LogisticsServiceI.com", "IllegalAccessException"));
        _fault.setClassName("com.LogisticsServiceI.service.au.zh.IllegalAccessException");
        _fault.setXmlType(new javax.xml.namespace.QName("http://zh.au.service.LogisticsServiceI.com", "IllegalAccessException"));
        _oper.addFault(_fault);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "chrsjrdh"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "username"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "password"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "didbh"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getTeltoFydh", _params, new javax.xml.namespace.QName("", "return"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://zh.au.service.LogisticsServiceI.com", "getTeltoFydh"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getTeltoFydh") == null) {
            _myOperations.put("getTeltoFydh", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getTeltoFydh")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "fydh"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getYtLoggistics", _params, new javax.xml.namespace.QName("", "return"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://zh.au.service.LogisticsServiceI.com", "getYtLoggistics"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getYtLoggistics") == null) {
            _myOperations.put("getYtLoggistics", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getYtLoggistics")).add(_oper);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("InvocationTargetException");
        _fault.setQName(new javax.xml.namespace.QName("http://zh.au.service.LogisticsServiceI.com", "InvocationTargetException"));
        _fault.setClassName("com.LogisticsServiceI.service.au.zh.InvocationTargetException");
        _fault.setXmlType(new javax.xml.namespace.QName("http://zh.au.service.LogisticsServiceI.com", "InvocationTargetException"));
        _oper.addFault(_fault);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("NoSuchMethodException");
        _fault.setQName(new javax.xml.namespace.QName("http://zh.au.service.LogisticsServiceI.com", "NoSuchMethodException"));
        _fault.setClassName("com.LogisticsServiceI.service.au.zh.NoSuchMethodException");
        _fault.setXmlType(new javax.xml.namespace.QName("http://zh.au.service.LogisticsServiceI.com", "NoSuchMethodException"));
        _oper.addFault(_fault);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("IllegalAccessException");
        _fault.setQName(new javax.xml.namespace.QName("http://zh.au.service.LogisticsServiceI.com", "IllegalAccessException"));
        _fault.setClassName("com.LogisticsServiceI.service.au.zh.IllegalAccessException");
        _fault.setXmlType(new javax.xml.namespace.QName("http://zh.au.service.LogisticsServiceI.com", "IllegalAccessException"));
        _oper.addFault(_fault);
    }

    public LogisticsServiceImplServiceSoapBindingSkeleton() {
        this.impl = new com.ausland.weixin.model.wsdl.zhonghuan.LogisticsServiceImplServiceSoapBindingImpl();
    }

    public LogisticsServiceImplServiceSoapBindingSkeleton(com.ausland.weixin.model.wsdl.zhonghuan.LogisticsServiceI impl) {
        this.impl = impl;
    }
    public java.lang.String getLogisticsInformation(java.lang.String fydh, java.lang.String countrytype) throws java.rmi.RemoteException, com.ausland.weixin.model.wsdl.zhonghuan.InvocationTargetException, com.ausland.weixin.model.wsdl.zhonghuan.NoSuchMethodException, com.ausland.weixin.model.wsdl.zhonghuan.IllegalAccessException
    {
        java.lang.String ret = impl.getLogisticsInformation(fydh, countrytype);
        return ret;
    }

    public java.lang.String getTeltoFydh(java.lang.String chrsjrdh, java.lang.String username, java.lang.String password, java.lang.String didbh) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.getTeltoFydh(chrsjrdh, username, password, didbh);
        return ret;
    }

    public java.lang.String getYtLoggistics(java.lang.String fydh) throws java.rmi.RemoteException, com.ausland.weixin.model.wsdl.zhonghuan.InvocationTargetException, com.ausland.weixin.model.wsdl.zhonghuan.NoSuchMethodException, com.ausland.weixin.model.wsdl.zhonghuan.IllegalAccessException
    {
        java.lang.String ret = impl.getYtLoggistics(fydh);
        return ret;
    }

}
