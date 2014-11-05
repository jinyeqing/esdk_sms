
package com.huawei.esdk.sms.north.royamas20.cxf.gen.server;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MessageNotificationType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MessageNotificationType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CMAbility" type="{http://www.csapi.org/schema/common/v2_0}CMAbility"/>
 *         &lt;element name="WSURI" type="{http://www.w3.org/2001/XMLSchema}anyURI" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MessageNotificationType", namespace = "http://www.csapi.org/schema/common/v2_0", propOrder = {
    "cmAbility",
    "wsuri"
})
public class MessageNotificationType {

    @XmlElement(name = "CMAbility", required = true)
    protected CMAbility cmAbility;
    @XmlElement(name = "WSURI")
    @XmlSchemaType(name = "anyURI")
    protected List<String> wsuri;

    /**
     * Gets the value of the cmAbility property.
     * 
     * @return
     *     possible object is
     *     {@link CMAbility }
     *     
     */
    public CMAbility getCMAbility() {
        return cmAbility;
    }

    /**
     * Sets the value of the cmAbility property.
     * 
     * @param value
     *     allowed object is
     *     {@link CMAbility }
     *     
     */
    public void setCMAbility(CMAbility value) {
        this.cmAbility = value;
    }

    /**
     * Gets the value of the wsuri property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the wsuri property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getWSURI().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getWSURI() {
        if (wsuri == null) {
            wsuri = new ArrayList<String>();
        }
        return this.wsuri;
    }

}
