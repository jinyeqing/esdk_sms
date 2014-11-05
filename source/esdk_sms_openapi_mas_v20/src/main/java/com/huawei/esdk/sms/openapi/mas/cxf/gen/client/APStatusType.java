
package com.huawei.esdk.sms.openapi.mas.cxf.gen.client;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for APStatusType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="APStatusType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Normal"/>
 *     &lt;enumeration value="OutofActiveTime"/>
 *     &lt;enumeration value="NeedRegistration"/>
 *     &lt;enumeration value="OutofService"/>
 *     &lt;enumeration value="Paused"/>
 *     &lt;enumeration value="Closed"/>
 *     &lt;enumeration value="WaitingforConfirm"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "APStatusType")
@XmlEnum
public enum APStatusType {

    @XmlEnumValue("Normal")
    NORMAL("Normal"),
    @XmlEnumValue("OutofActiveTime")
    OUTOF_ACTIVE_TIME("OutofActiveTime"),
    @XmlEnumValue("NeedRegistration")
    NEED_REGISTRATION("NeedRegistration"),
    @XmlEnumValue("OutofService")
    OUTOF_SERVICE("OutofService"),
    @XmlEnumValue("Paused")
    PAUSED("Paused"),
    @XmlEnumValue("Closed")
    CLOSED("Closed"),
    @XmlEnumValue("WaitingforConfirm")
    WAITINGFOR_CONFIRM("WaitingforConfirm");
    private final String value;

    APStatusType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static APStatusType fromValue(String v) {
        for (APStatusType c: APStatusType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
