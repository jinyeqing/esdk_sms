
package com.huawei.esdk.sms.north.royamas20.cxf.gen.server;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="APid" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ApSvcAuthResult" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "aPid",
    "apSvcAuthResult"
})
@XmlRootElement(name = "APSvcAuthenticRsp")
public class APSvcAuthenticRsp {

    @XmlElement(name = "APid", required = true)
    protected String aPid;
    @XmlElement(name = "ApSvcAuthResult", required = true, nillable = true)
    protected String apSvcAuthResult;

    /**
     * Gets the value of the aPid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAPid() {
        return aPid;
    }

    /**
     * Sets the value of the aPid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAPid(String value) {
        this.aPid = value;
    }

    /**
     * Gets the value of the apSvcAuthResult property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApSvcAuthResult() {
        return apSvcAuthResult;
    }

    /**
     * Sets the value of the apSvcAuthResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApSvcAuthResult(String value) {
        this.apSvcAuthResult = value;
    }

}
