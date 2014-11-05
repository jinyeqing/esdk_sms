
package com.huawei.esdk.sms.openapi.soap.cxf.gen.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ESGReceive.notifySmsReceptionResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ESGReceive.notifySmsReceptionResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="out_lnotifySmsReceptionResponse" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ESGReceive.notifySmsReceptionResponse", propOrder = {
    "outLnotifySmsReceptionResponse"
})
public class ESGReceiveNotifySmsReceptionResponse {

    @XmlElement(name = "out_lnotifySmsReceptionResponse")
    protected int outLnotifySmsReceptionResponse;

    /**
     * Gets the value of the outLnotifySmsReceptionResponse property.
     * 
     */
    public int getOutLnotifySmsReceptionResponse() {
        return outLnotifySmsReceptionResponse;
    }

    /**
     * Sets the value of the outLnotifySmsReceptionResponse property.
     * 
     */
    public void setOutLnotifySmsReceptionResponse(int value) {
        this.outLnotifySmsReceptionResponse = value;
    }

}
