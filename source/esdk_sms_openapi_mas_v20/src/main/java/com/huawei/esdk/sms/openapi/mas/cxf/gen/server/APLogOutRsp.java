
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
 *         &lt;element name="LogoutResult" type="{http://www.csapi.org/schema/ap}APLogoutResult"/>
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
    "logoutResult"
})
@XmlRootElement(name = "APLogOutRsp")
public class APLogOutRsp {

    @XmlElement(name = "LogoutResult", required = true, nillable = true)
    protected APLogoutResult logoutResult;

    /**
     * Gets the value of the logoutResult property.
     * 
     * @return
     *     possible object is
     *     {@link APLogoutResult }
     *     
     */
    public APLogoutResult getLogoutResult() {
        return logoutResult;
    }

    /**
     * Sets the value of the logoutResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link APLogoutResult }
     *     
     */
    public void setLogoutResult(APLogoutResult value) {
        this.logoutResult = value;
    }

}
