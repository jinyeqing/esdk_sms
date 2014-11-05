
package com.huawei.esdk.sms.openapi.mas.cxf.gen.server;

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
 *         &lt;element name="RegResult" type="{http://www.csapi.org/schema/ap}APRegResult"/>
 *         &lt;element name="NextInterval" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
    "regResult",
    "nextInterval"
})
@XmlRootElement(name = "APRegistrationRsp")
public class APRegistrationRsp {

    @XmlElement(name = "RegResult", required = true, nillable = true)
    protected APRegResult regResult;
    @XmlElement(name = "NextInterval")
    protected int nextInterval;

    /**
     * Gets the value of the regResult property.
     * 
     * @return
     *     possible object is
     *     {@link APRegResult }
     *     
     */
    public APRegResult getRegResult() {
        return regResult;
    }

    /**
     * Sets the value of the regResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link APRegResult }
     *     
     */
    public void setRegResult(APRegResult value) {
        this.regResult = value;
    }

    /**
     * Gets the value of the nextInterval property.
     * 
     */
    public int getNextInterval() {
        return nextInterval;
    }

    /**
     * Sets the value of the nextInterval property.
     * 
     */
    public void setNextInterval(int value) {
        this.nextInterval = value;
    }

}
