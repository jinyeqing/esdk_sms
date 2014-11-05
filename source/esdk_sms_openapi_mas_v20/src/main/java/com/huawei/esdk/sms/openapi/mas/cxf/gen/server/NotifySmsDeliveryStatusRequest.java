
package com.huawei.esdk.sms.openapi.mas.cxf.gen.server;

import java.util.ArrayList;
import java.util.List;
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
 *         &lt;element name="RequestIdentifier" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DeliveryInformation" type="{http://www.csapi.org/schema/sms}DeliveryInformation" maxOccurs="unbounded" minOccurs="0"/>
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
    "requestIdentifier",
    "deliveryInformation"
})
@XmlRootElement(name = "notifySmsDeliveryStatusRequest", namespace = "http://www.csapi.org/schema/sms")
public class NotifySmsDeliveryStatusRequest {

    @XmlElement(name = "RequestIdentifier", required = true, nillable = true)
    protected String requestIdentifier;
    @XmlElement(name = "DeliveryInformation", nillable = true)
    protected List<DeliveryInformation> deliveryInformation;

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
     * Gets the value of the deliveryInformation property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the deliveryInformation property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDeliveryInformation().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DeliveryInformation }
     * 
     * 
     */
    public List<DeliveryInformation> getDeliveryInformation() {
        if (deliveryInformation == null) {
            deliveryInformation = new ArrayList<DeliveryInformation>();
        }
        return this.deliveryInformation;
    }

}
