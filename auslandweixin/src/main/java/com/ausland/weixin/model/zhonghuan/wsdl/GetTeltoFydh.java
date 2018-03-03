
package com.ausland.weixin.model.zhonghuan.wsdl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getTeltoFydh complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getTeltoFydh"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="chrsjrdh" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="username" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="password" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="didbh" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getTeltoFydh", propOrder = {
    "chrsjrdh",
    "username",
    "password",
    "didbh"
})
public class GetTeltoFydh {

    protected String chrsjrdh;
    protected String username;
    protected String password;
    protected String didbh;

    /**
     * Gets the value of the chrsjrdh property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChrsjrdh() {
        return chrsjrdh;
    }

    /**
     * Sets the value of the chrsjrdh property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChrsjrdh(String value) {
        this.chrsjrdh = value;
    }

    /**
     * Gets the value of the username property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the value of the username property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsername(String value) {
        this.username = value;
    }

    /**
     * Gets the value of the password property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the value of the password property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPassword(String value) {
        this.password = value;
    }

    /**
     * Gets the value of the didbh property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDidbh() {
        return didbh;
    }

    /**
     * Sets the value of the didbh property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDidbh(String value) {
        this.didbh = value;
    }

}
