
package com.ausland.weixin.model.zhonghuan.wsdl;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.ausland.weixin.model.zhonghuan.wsdl package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetLogisticsInformation_QNAME = new QName("http://zh.au.service.LogisticsServiceI.com", "getLogisticsInformation");
    private final static QName _GetLogisticsInformationResponse_QNAME = new QName("http://zh.au.service.LogisticsServiceI.com", "getLogisticsInformationResponse");
    private final static QName _GetTeltoFydh_QNAME = new QName("http://zh.au.service.LogisticsServiceI.com", "getTeltoFydh");
    private final static QName _GetTeltoFydhResponse_QNAME = new QName("http://zh.au.service.LogisticsServiceI.com", "getTeltoFydhResponse");
    private final static QName _GetYtLoggistics_QNAME = new QName("http://zh.au.service.LogisticsServiceI.com", "getYtLoggistics");
    private final static QName _GetYtLoggisticsResponse_QNAME = new QName("http://zh.au.service.LogisticsServiceI.com", "getYtLoggisticsResponse");
    private final static QName _InvocationTargetException_QNAME = new QName("http://zh.au.service.LogisticsServiceI.com", "InvocationTargetException");
    private final static QName _IllegalAccessException_QNAME = new QName("http://zh.au.service.LogisticsServiceI.com", "IllegalAccessException");
    private final static QName _NoSuchMethodException_QNAME = new QName("http://zh.au.service.LogisticsServiceI.com", "NoSuchMethodException");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.ausland.weixin.model.zhonghuan.wsdl
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetLogisticsInformation }
     * 
     */
    public GetLogisticsInformation createGetLogisticsInformation() {
        return new GetLogisticsInformation();
    }

    /**
     * Create an instance of {@link GetLogisticsInformationResponse }
     * 
     */
    public GetLogisticsInformationResponse createGetLogisticsInformationResponse() {
        return new GetLogisticsInformationResponse();
    }

    /**
     * Create an instance of {@link GetTeltoFydh }
     * 
     */
    public GetTeltoFydh createGetTeltoFydh() {
        return new GetTeltoFydh();
    }

    /**
     * Create an instance of {@link GetTeltoFydhResponse }
     * 
     */
    public GetTeltoFydhResponse createGetTeltoFydhResponse() {
        return new GetTeltoFydhResponse();
    }

    /**
     * Create an instance of {@link GetYtLoggistics }
     * 
     */
    public GetYtLoggistics createGetYtLoggistics() {
        return new GetYtLoggistics();
    }

    /**
     * Create an instance of {@link GetYtLoggisticsResponse }
     * 
     */
    public GetYtLoggisticsResponse createGetYtLoggisticsResponse() {
        return new GetYtLoggisticsResponse();
    }

    /**
     * Create an instance of {@link InvocationTargetException }
     * 
     */
    public InvocationTargetException createInvocationTargetException() {
        return new InvocationTargetException();
    }

    /**
     * Create an instance of {@link IllegalAccessException }
     * 
     */
    public IllegalAccessException createIllegalAccessException() {
        return new IllegalAccessException();
    }

    /**
     * Create an instance of {@link NoSuchMethodException }
     * 
     */
    public NoSuchMethodException createNoSuchMethodException() {
        return new NoSuchMethodException();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetLogisticsInformation }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://zh.au.service.LogisticsServiceI.com", name = "getLogisticsInformation")
    public JAXBElement<GetLogisticsInformation> createGetLogisticsInformation(GetLogisticsInformation value) {
        return new JAXBElement<GetLogisticsInformation>(_GetLogisticsInformation_QNAME, GetLogisticsInformation.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetLogisticsInformationResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://zh.au.service.LogisticsServiceI.com", name = "getLogisticsInformationResponse")
    public JAXBElement<GetLogisticsInformationResponse> createGetLogisticsInformationResponse(GetLogisticsInformationResponse value) {
        return new JAXBElement<GetLogisticsInformationResponse>(_GetLogisticsInformationResponse_QNAME, GetLogisticsInformationResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetTeltoFydh }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://zh.au.service.LogisticsServiceI.com", name = "getTeltoFydh")
    public JAXBElement<GetTeltoFydh> createGetTeltoFydh(GetTeltoFydh value) {
        return new JAXBElement<GetTeltoFydh>(_GetTeltoFydh_QNAME, GetTeltoFydh.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetTeltoFydhResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://zh.au.service.LogisticsServiceI.com", name = "getTeltoFydhResponse")
    public JAXBElement<GetTeltoFydhResponse> createGetTeltoFydhResponse(GetTeltoFydhResponse value) {
        return new JAXBElement<GetTeltoFydhResponse>(_GetTeltoFydhResponse_QNAME, GetTeltoFydhResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetYtLoggistics }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://zh.au.service.LogisticsServiceI.com", name = "getYtLoggistics")
    public JAXBElement<GetYtLoggistics> createGetYtLoggistics(GetYtLoggistics value) {
        return new JAXBElement<GetYtLoggistics>(_GetYtLoggistics_QNAME, GetYtLoggistics.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetYtLoggisticsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://zh.au.service.LogisticsServiceI.com", name = "getYtLoggisticsResponse")
    public JAXBElement<GetYtLoggisticsResponse> createGetYtLoggisticsResponse(GetYtLoggisticsResponse value) {
        return new JAXBElement<GetYtLoggisticsResponse>(_GetYtLoggisticsResponse_QNAME, GetYtLoggisticsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InvocationTargetException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://zh.au.service.LogisticsServiceI.com", name = "InvocationTargetException")
    public JAXBElement<InvocationTargetException> createInvocationTargetException(InvocationTargetException value) {
        return new JAXBElement<InvocationTargetException>(_InvocationTargetException_QNAME, InvocationTargetException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IllegalAccessException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://zh.au.service.LogisticsServiceI.com", name = "IllegalAccessException")
    public JAXBElement<IllegalAccessException> createIllegalAccessException(IllegalAccessException value) {
        return new JAXBElement<IllegalAccessException>(_IllegalAccessException_QNAME, IllegalAccessException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NoSuchMethodException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://zh.au.service.LogisticsServiceI.com", name = "NoSuchMethodException")
    public JAXBElement<NoSuchMethodException> createNoSuchMethodException(NoSuchMethodException value) {
        return new JAXBElement<NoSuchMethodException>(_NoSuchMethodException_QNAME, NoSuchMethodException.class, null, value);
    }

}
