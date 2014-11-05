
package com.huawei.esdk.sms.north.royamas20.cxf.gen.server;

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
 *         &lt;element name="alarmId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="orgseverity" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="orgtype" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="probablecause" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="eventTime" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ackTime" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="clearTime" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="activestatus" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="alarmtitle" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="alarmText" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "alarmId",
    "orgseverity",
    "orgtype",
    "probablecause",
    "eventTime",
    "ackTime",
    "clearTime",
    "activestatus",
    "alarmtitle",
    "alarmText"
})
@XmlRootElement(name = "AlarmReq")
public class AlarmReq {

    @XmlElement(required = true)
    protected String alarmId;
    protected int orgseverity;
    protected int orgtype;
    @XmlElement(required = true)
    protected String probablecause;
    @XmlElement(required = true)
    protected String eventTime;
    @XmlElement(required = true)
    protected String ackTime;
    @XmlElement(required = true)
    protected String clearTime;
    protected int activestatus;
    @XmlElement(required = true)
    protected String alarmtitle;
    @XmlElement(required = true)
    protected String alarmText;

    /**
     * Gets the value of the alarmId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAlarmId() {
        return alarmId;
    }

    /**
     * Sets the value of the alarmId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAlarmId(String value) {
        this.alarmId = value;
    }

    /**
     * Gets the value of the orgseverity property.
     * 
     */
    public int getOrgseverity() {
        return orgseverity;
    }

    /**
     * Sets the value of the orgseverity property.
     * 
     */
    public void setOrgseverity(int value) {
        this.orgseverity = value;
    }

    /**
     * Gets the value of the orgtype property.
     * 
     */
    public int getOrgtype() {
        return orgtype;
    }

    /**
     * Sets the value of the orgtype property.
     * 
     */
    public void setOrgtype(int value) {
        this.orgtype = value;
    }

    /**
     * Gets the value of the probablecause property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProbablecause() {
        return probablecause;
    }

    /**
     * Sets the value of the probablecause property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProbablecause(String value) {
        this.probablecause = value;
    }

    /**
     * Gets the value of the eventTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEventTime() {
        return eventTime;
    }

    /**
     * Sets the value of the eventTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEventTime(String value) {
        this.eventTime = value;
    }

    /**
     * Gets the value of the ackTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAckTime() {
        return ackTime;
    }

    /**
     * Sets the value of the ackTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAckTime(String value) {
        this.ackTime = value;
    }

    /**
     * Gets the value of the clearTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClearTime() {
        return clearTime;
    }

    /**
     * Sets the value of the clearTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClearTime(String value) {
        this.clearTime = value;
    }

    /**
     * Gets the value of the activestatus property.
     * 
     */
    public int getActivestatus() {
        return activestatus;
    }

    /**
     * Sets the value of the activestatus property.
     * 
     */
    public void setActivestatus(int value) {
        this.activestatus = value;
    }

    /**
     * Gets the value of the alarmtitle property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAlarmtitle() {
        return alarmtitle;
    }

    /**
     * Sets the value of the alarmtitle property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAlarmtitle(String value) {
        this.alarmtitle = value;
    }

    /**
     * Gets the value of the alarmText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAlarmText() {
        return alarmText;
    }

    /**
     * Sets the value of the alarmText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAlarmText(String value) {
        this.alarmText = value;
    }

}
