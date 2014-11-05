
package com.huawei.esdk.sms.openapi.soap.cxf.gen.server;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ESGSend.SendMethodType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ESGSend.SendMethodType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Normal"/>
 *     &lt;enumeration value="Instant"/>
 *     &lt;enumeration value="Long"/>
 *     &lt;enumeration value="Structured"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ESGSend.SendMethodType")
@XmlEnum
public enum ESGSendSendMethodType {

    @XmlEnumValue("Normal")
    NORMAL("Normal"),
    @XmlEnumValue("Instant")
    INSTANT("Instant"),
    @XmlEnumValue("Long")
    LONG("Long"),
    @XmlEnumValue("Structured")
    STRUCTURED("Structured");
    private final String value;

    ESGSendSendMethodType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ESGSendSendMethodType fromValue(String v) {
        for (ESGSendSendMethodType c: ESGSendSendMethodType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
