
package com.huawei.esdk.sms.openapi.soap.cxf.gen.server;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ESGSend.SendSMSRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ESGSend.SendSMSRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ApplicationID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="in_DestinationAddresses" type="{ESGtoSMS.wsdl}ESGSend.DestinationAddresses"/>
 *         &lt;element name="in_sExtendCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="in_sMessage" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="in_enMessageFormat" type="{ESGtoSMS.wsdl}ESGSend.MessageFormat"/>
 *         &lt;element name="in_enSendMethodType" type="{ESGtoSMS.wsdl}ESGSend.SendMethodType"/>
 *         &lt;element name="DeliveryResultRequest" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ESGSend.SendSMSRequest", propOrder = {
    "applicationID",
    "inDestinationAddresses",
    "inSExtendCode",
    "inSMessage",
    "inEnMessageFormat",
    "inEnSendMethodType",
    "deliveryResultRequest"
})
public class ESGSendSendSMSRequest {

    @XmlElement(name = "ApplicationID", required = true)
    protected String applicationID;
    @XmlElement(name = "in_DestinationAddresses", required = true)
    protected ESGSendDestinationAddresses inDestinationAddresses;
    @XmlElement(name = "in_sExtendCode", required = true)
    protected String inSExtendCode;
    @XmlElement(name = "in_sMessage", required = true)
    protected String inSMessage;
    @XmlElement(name = "in_enMessageFormat", required = true)
    protected ESGSendMessageFormat inEnMessageFormat;
    @XmlElement(name = "in_enSendMethodType", required = true)
    protected ESGSendSendMethodType inEnSendMethodType;
    @XmlElement(name = "DeliveryResultRequest")
    protected int deliveryResultRequest;

    /**
     * Gets the value of the applicationID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApplicationID() {
        return applicationID;
    }

    /**
     * Sets the value of the applicationID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApplicationID(String value) {
        this.applicationID = value;
    }

    /**
     * Gets the value of the inDestinationAddresses property.
     * 
     * @return
     *     possible object is
     *     {@link ESGSendDestinationAddresses }
     *     
     */
    public ESGSendDestinationAddresses getInDestinationAddresses() {
        return inDestinationAddresses;
    }

    /**
     * Sets the value of the inDestinationAddresses property.
     * 
     * @param value
     *     allowed object is
     *     {@link ESGSendDestinationAddresses }
     *     
     */
    public void setInDestinationAddresses(ESGSendDestinationAddresses value) {
        this.inDestinationAddresses = value;
    }

    /**
     * Gets the value of the inSExtendCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInSExtendCode() {
        return inSExtendCode;
    }

    /**
     * Sets the value of the inSExtendCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInSExtendCode(String value) {
        this.inSExtendCode = value;
    }

    /**
     * Gets the value of the inSMessage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInSMessage() {
        return inSMessage;
    }

    /**
     * Sets the value of the inSMessage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInSMessage(String value) {
        this.inSMessage = value;
    }

    /**
     * Gets the value of the inEnMessageFormat property.
     * 
     * @return
     *     possible object is
     *     {@link ESGSendMessageFormat }
     *     
     */
    public ESGSendMessageFormat getInEnMessageFormat() {
        return inEnMessageFormat;
    }

    /**
     * Sets the value of the inEnMessageFormat property.
     * 
     * @param value
     *     allowed object is
     *     {@link ESGSendMessageFormat }
     *     
     */
    public void setInEnMessageFormat(ESGSendMessageFormat value) {
        this.inEnMessageFormat = value;
    }

    /**
     * Gets the value of the inEnSendMethodType property.
     * 
     * @return
     *     possible object is
     *     {@link ESGSendSendMethodType }
     *     
     */
    public ESGSendSendMethodType getInEnSendMethodType() {
        return inEnSendMethodType;
    }

    /**
     * Sets the value of the inEnSendMethodType property.
     * 
     * @param value
     *     allowed object is
     *     {@link ESGSendSendMethodType }
     *     
     */
    public void setInEnSendMethodType(ESGSendSendMethodType value) {
        this.inEnSendMethodType = value;
    }

    /**
     * Gets the value of the deliveryResultRequest property.
     * 
     */
    public int getDeliveryResultRequest() {
        return deliveryResultRequest;
    }

    /**
     * Sets the value of the deliveryResultRequest property.
     * 
     */
    public void setDeliveryResultRequest(int value) {
        this.deliveryResultRequest = value;
    }

}
