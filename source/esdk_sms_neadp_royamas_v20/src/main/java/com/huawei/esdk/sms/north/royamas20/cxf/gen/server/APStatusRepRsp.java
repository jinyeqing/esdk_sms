
package com.huawei.esdk.sms.north.royamas20.cxf.gen.server;

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
 *         &lt;element name="NextCommand" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="NextInterval" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="ApSvcAuthType" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ApSvcPerfCmdType" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
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
    "nextCommand",
    "nextInterval",
    "apSvcAuthType",
    "apSvcPerfCmdType"
})
@XmlRootElement(name = "APStatusRepRsp")
public class APStatusRepRsp {

    @XmlElement(name = "NextCommand", required = true, nillable = true)
    protected String nextCommand;
    @XmlElement(name = "NextInterval")
    protected int nextInterval;
    @XmlElement(name = "ApSvcAuthType", nillable = true)
    protected List<String> apSvcAuthType;
    @XmlElement(name = "ApSvcPerfCmdType", nillable = true)
    protected List<String> apSvcPerfCmdType;

    /**
     * Gets the value of the nextCommand property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNextCommand() {
        return nextCommand;
    }

    /**
     * Sets the value of the nextCommand property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNextCommand(String value) {
        this.nextCommand = value;
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

    /**
     * Gets the value of the apSvcAuthType property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the apSvcAuthType property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getApSvcAuthType().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getApSvcAuthType() {
        if (apSvcAuthType == null) {
            apSvcAuthType = new ArrayList<String>();
        }
        return this.apSvcAuthType;
    }

    /**
     * Gets the value of the apSvcPerfCmdType property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the apSvcPerfCmdType property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getApSvcPerfCmdType().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getApSvcPerfCmdType() {
        if (apSvcPerfCmdType == null) {
            apSvcPerfCmdType = new ArrayList<String>();
        }
        return this.apSvcPerfCmdType;
    }

}
