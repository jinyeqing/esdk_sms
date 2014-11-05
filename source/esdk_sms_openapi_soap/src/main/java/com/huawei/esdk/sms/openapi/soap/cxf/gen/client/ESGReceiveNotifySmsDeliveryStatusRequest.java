
package com.huawei.esdk.sms.openapi.soap.cxf.gen.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ESGReceive.NotifySmsDeliveryStatusRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ESGReceive.NotifySmsDeliveryStatusRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="requestIdentifier" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="deliveryInformationList" type="{SMStoESG.wsdl}ESGReceive.DeliveryInformationList"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ESGReceive.NotifySmsDeliveryStatusRequest", propOrder = {
    "requestIdentifier",
    "deliveryInformationList"
})
public class ESGReceiveNotifySmsDeliveryStatusRequest {

    @XmlElement(required = true)
    protected String requestIdentifier;
    @XmlElement(required = true)
    protected ESGReceiveDeliveryInformationList deliveryInformationList;

    /**
     * Gets the value of the requestIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequestIdentifier() {
        return requestIdentifier;
    }

    /**
     * Sets the value of the requestIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequestIdentifier(String value) {
        this.requestIdentifier = value;
    }

    /**
     * Gets the value of the deliveryInformationList property.
     * 
     * @return
     *     possible object is
     *     {@link ESGReceiveDeliveryInformationList }
     *     
     */
    public ESGReceiveDeliveryInformationList getDeliveryInformationList() {
        return deliveryInformationList;
    }

    /**
     * Sets the value of the deliveryInformationList property.
     * 
     * @param value
     *     allowed object is
     *     {@link ESGReceiveDeliveryInformationList }
     *     
     */
    public void setDeliveryInformationList(ESGReceiveDeliveryInformationList value) {
        this.deliveryInformationList = value;
    }

}
