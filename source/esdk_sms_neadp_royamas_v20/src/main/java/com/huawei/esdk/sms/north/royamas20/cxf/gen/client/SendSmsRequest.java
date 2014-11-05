
package com.huawei.esdk.sms.north.royamas20.cxf.gen.client;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
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
 *         &lt;element name="ApplicationID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DestinationAddresses" type="{http://www.w3.org/2001/XMLSchema}anyURI" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ExtendCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Message" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="MessageFormat" type="{http://www.csapi.org/schema/sms}MessageFormat"/>
 *         &lt;element name="SendMethod" type="{http://www.csapi.org/schema/sms}SendMethodType"/>
 *         &lt;element name="DeliveryResultRequest" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
    "applicationID",
    "destinationAddresses",
    "extendCode",
    "message",
    "messageFormat",
    "sendMethod",
    "deliveryResultRequest"
})
@XmlRootElement(name = "sendSmsRequest", namespace = "http://www.csapi.org/schema/sms")
public class SendSmsRequest {

    @XmlElement(name = "ApplicationID", required = true, nillable = true)
    protected String applicationID;
    @XmlElement(name = "DestinationAddresses", nillable = true)
    @XmlSchemaType(name = "anyURI")
    protected List<String> destinationAddresses;
    @XmlElement(name = "ExtendCode", required = true, nillable = true)
    protected String extendCode;
    @XmlElement(name = "Message", required = true, nillable = true)
    protected String message;
    @XmlElement(name = "MessageFormat", required = true, nillable = true)
    protected MessageFormat messageFormat;
    @XmlElement(name = "SendMethod", required = true, nillable = true)
    protected SendMethodType sendMethod;
    @XmlElement(name = "DeliveryResultRequest")
    protected boolean deliveryResultRequest;

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
     * Gets the value of the destinationAddresses property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the destinationAddresses property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDestinationAddresses().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getDestinationAddresses() {
        if (destinationAddresses == null) {
            destinationAddresses = new ArrayList<String>();
        }
        return this.destinationAddresses;
    }

    /**
     * Gets the value of the extendCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExtendCode() {
        return extendCode;
    }

    /**
     * Sets the value of the extendCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExtendCode(String value) {
        this.extendCode = value;
    }

    /**
     * Gets the value of the message property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the value of the message property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessage(String value) {
        this.message = value;
    }

    /**
     * Gets the value of the messageFormat property.
     * 
     * @return
     *     possible object is
     *     {@link MessageFormat }
     *     
     */
    public MessageFormat getMessageFormat() {
        return messageFormat;
    }

    /**
     * Sets the value of the messageFormat property.
     * 
     * @param value
     *     allowed object is
     *     {@link MessageFormat }
     *     
     */
    public void setMessageFormat(MessageFormat value) {
        this.messageFormat = value;
    }

    /**
     * Gets the value of the sendMethod property.
     * 
     * @return
     *     possible object is
     *     {@link SendMethodType }
     *     
     */
    public SendMethodType getSendMethod() {
        return sendMethod;
    }

    /**
     * Sets the value of the sendMethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link SendMethodType }
     *     
     */
    public void setSendMethod(SendMethodType value) {
        this.sendMethod = value;
    }

    /**
     * Gets the value of the deliveryResultRequest property.
     * 
     */
    public boolean isDeliveryResultRequest() {
        return deliveryResultRequest;
    }

    /**
     * Sets the value of the deliveryResultRequest property.
     * 
     */
    public void setDeliveryResultRequest(boolean value) {
        this.deliveryResultRequest = value;
    }

}
