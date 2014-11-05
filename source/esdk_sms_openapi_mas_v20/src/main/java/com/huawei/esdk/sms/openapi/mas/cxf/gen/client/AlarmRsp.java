
package com.huawei.esdk.sms.openapi.mas.cxf.gen.client;

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
 *         &lt;element name="recode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="reMsg" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "recode",
    "reMsg"
})
@XmlRootElement(name = "AlarmRsp")
public class AlarmRsp {

    @XmlElement(required = true)
    protected String recode;
    @XmlElement(required = true)
    protected String reMsg;

    /**
     * Gets the value of the recode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRecode() {
        return recode;
    }

    /**
     * Sets the value of the recode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRecode(String value) {
        this.recode = value;
    }

    /**
     * Gets the value of the reMsg property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReMsg() {
        return reMsg;
    }

    /**
     * Sets the value of the reMsg property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReMsg(String value) {
        this.reMsg = value;
    }

}
