
package com.huawei.esdk.sms.north.royamas20.cxf.gen.client;

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
 *         &lt;element name="APStatus" type="{http://www.csapi.org/schema/ap}APStatusType"/>
 *         &lt;element name="APPid" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
    "apStatus",
    "apPid"
})
@XmlRootElement(name = "APStatusRepReq")
public class APStatusRepReq {

    @XmlElement(name = "APid", required = true)
    protected String aPid;
    @XmlElement(name = "APStatus", required = true)
    protected APStatusType apStatus;
    @XmlElement(name = "APPid")
    protected int apPid;

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
     * Gets the value of the apStatus property.
     * 
     * @return
     *     possible object is
     *     {@link APStatusType }
     *     
     */
    public APStatusType getAPStatus() {
        return apStatus;
    }

    /**
     * Sets the value of the apStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link APStatusType }
     *     
     */
    public void setAPStatus(APStatusType value) {
        this.apStatus = value;
    }

    /**
     * Gets the value of the apPid property.
     * 
     */
    public int getAPPid() {
        return apPid;
    }

    /**
     * Sets the value of the apPid property.
     * 
     */
    public void setAPPid(int value) {
        this.apPid = value;
    }

}
