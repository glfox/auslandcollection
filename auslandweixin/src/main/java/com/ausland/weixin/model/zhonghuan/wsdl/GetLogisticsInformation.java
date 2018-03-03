
package com.ausland.weixin.model.zhonghuan.wsdl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getLogisticsInformation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getLogisticsInformation"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="fydh" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="countrytype" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getLogisticsInformation", propOrder = {
    "fydh",
    "countrytype"
})
public class GetLogisticsInformation {

    protected String fydh;
    protected String countrytype;

    /**
     * Gets the value of the fydh property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFydh() {
        return fydh;
    }

    /**
     * Sets the value of the fydh property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFydh(String value) {
        this.fydh = value;
    }

    /**
     * Gets the value of the countrytype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCountrytype() {
        return countrytype;
    }

    /**
     * Sets the value of the countrytype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCountrytype(String value) {
        this.countrytype = value;
    }

}
