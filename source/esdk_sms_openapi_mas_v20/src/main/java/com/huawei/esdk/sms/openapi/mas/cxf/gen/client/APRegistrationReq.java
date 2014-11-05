
package com.huawei.esdk.sms.openapi.mas.cxf.gen.client;

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
 *         &lt;element name="Apid" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="APPid" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="HostIP" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="MessageNotification" type="{http://www.csapi.org/schema/common/v2_0}MessageNotificationType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="APWSURI" type="{http://www.w3.org/2001/XMLSchema}anyURI"/>
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
    "apid",
    "apPid",
    "hostIP",
    "messageNotification",
    "apwsuri"
})
@XmlRootElement(name = "APRegistrationReq")
public class APRegistrationReq {

    @XmlElement(name = "Apid", required = true, nillable = true)
    protected String apid;
    @XmlElement(name = "APPid")
    protected int apPid;
    @XmlElement(name = "HostIP", required = true, nillable = true)
    protected String hostIP;
    @XmlElement(name = "MessageNotification", nillable = true)
    protected List<MessageNotificationType> messageNotification;
    @XmlElement(name = "APWSURI", required = true)
    @XmlSchemaType(name = "anyURI")
    protected String apwsuri;

    /**
     * Gets the value of the apid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApid() {
        return apid;
    }

    /**
     * Sets the value of the apid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApid(String value) {
        this.apid = value;
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

    /**
     * Gets the value of the hostIP property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHostIP() {
        return hostIP;
    }

    /**
     * Sets the value of the hostIP property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHostIP(String value) {
        this.hostIP = value;
    }

    /**
     * Gets the value of the messageNotification property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the messageNotification property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMessageNotification().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MessageNotificationType }
     * 
     * 
     */
    public List<MessageNotificationType> getMessageNotification() {
        if (messageNotification == null) {
            messageNotification = new ArrayList<MessageNotificationType>();
        }
        return this.messageNotification;
    }

    /**
     * Gets the value of the apwsuri property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAPWSURI() {
        return apwsuri;
    }

    /**
     * Sets the value of the apwsuri property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAPWSURI(String value) {
        this.apwsuri = value;
    }

}
