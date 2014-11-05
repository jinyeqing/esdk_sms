
package com.huawei.esdk.sms.openapi.soap.cxf.gen.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ESGReceive.SmsMessage complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ESGReceive.SmsMessage">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="strMessage" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="strSmsServiceActivationNumber" type="{SMStoESG.wsdl}ESGReceive.SmsServiceActivationNumber"/>
 *         &lt;element name="strSenderAddress" type="{SMStoESG.wsdl}ESGReceive.SenderAddress"/>
 *         &lt;element name="enMessageFormat" type="{SMStoESG.wsdl}ESGReceive.MessageFormat"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ESGReceive.SmsMessage", propOrder = {
    "strMessage",
    "strSmsServiceActivationNumber",
    "strSenderAddress",
    "enMessageFormat"
})
public class ESGReceiveSmsMessage {

    @XmlElement(required = true)
    protected String strMessage;
    @XmlElement(required = true)
    protected ESGReceiveSmsServiceActivationNumber strSmsServiceActivationNumber;
    @XmlElement(required = true)
    protected ESGReceiveSenderAddress strSenderAddress;
    @XmlElement(required = true)
    protected ESGReceiveMessageFormat enMessageFormat;

    /**
     * Gets the value of the strMessage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrMessage() {
        return strMessage;
    }

    /**
     * Sets the value of the strMessage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrMessage(String value) {
        this.strMessage = value;
    }

    /**
     * Gets the value of the strSmsServiceActivationNumber property.
     * 
     * @return
     *     possible object is
     *     {@link ESGReceiveSmsServiceActivationNumber }
     *     
     */
    public ESGReceiveSmsServiceActivationNumber getStrSmsServiceActivationNumber() {
        return strSmsServiceActivationNumber;
    }

    /**
     * Sets the value of the strSmsServiceActivationNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link ESGReceiveSmsServiceActivationNumber }
     *     
     */
    public void setStrSmsServiceActivationNumber(ESGReceiveSmsServiceActivationNumber value) {
        this.strSmsServiceActivationNumber = value;
    }

    /**
     * Gets the value of the strSenderAddress property.
     * 
     * @return
     *     possible object is
     *     {@link ESGReceiveSenderAddress }
     *     
     */
    public ESGReceiveSenderAddress getStrSenderAddress() {
        return strSenderAddress;
    }

    /**
     * Sets the value of the strSenderAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link ESGReceiveSenderAddress }
     *     
     */
    public void setStrSenderAddress(ESGReceiveSenderAddress value) {
        this.strSenderAddress = value;
    }

    /**
     * Gets the value of the enMessageFormat property.
     * 
     * @return
     *     possible object is
     *     {@link ESGReceiveMessageFormat }
     *     
     */
    public ESGReceiveMessageFormat getEnMessageFormat() {
        return enMessageFormat;
    }

    /**
     * Sets the value of the enMessageFormat property.
     * 
     * @param value
     *     allowed object is
     *     {@link ESGReceiveMessageFormat }
     *     
     */
    public void setEnMessageFormat(ESGReceiveMessageFormat value) {
        this.enMessageFormat = value;
    }

}
