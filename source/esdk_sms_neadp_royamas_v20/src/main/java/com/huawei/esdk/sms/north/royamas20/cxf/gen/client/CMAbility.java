
package com.huawei.esdk.sms.north.royamas20.cxf.gen.client;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CMAbility.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CMAbility">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="SMSAbility"/>
 *     &lt;enumeration value="MMSAbility"/>
 *     &lt;enumeration value="WAPAbility"/>
 *     &lt;enumeration value="USSDAbility"/>
 *     &lt;enumeration value="LBSAbility"/>
 *     &lt;enumeration value="GPRSAbility"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "CMAbility", namespace = "http://www.csapi.org/schema/common/v2_0")
@XmlEnum
public enum CMAbility {

    @XmlEnumValue("SMSAbility")
    SMS_ABILITY("SMSAbility"),
    @XmlEnumValue("MMSAbility")
    MMS_ABILITY("MMSAbility"),
    @XmlEnumValue("WAPAbility")
    WAP_ABILITY("WAPAbility"),
    @XmlEnumValue("USSDAbility")
    USSD_ABILITY("USSDAbility"),
    @XmlEnumValue("LBSAbility")
    LBS_ABILITY("LBSAbility"),
    @XmlEnumValue("GPRSAbility")
    GPRS_ABILITY("GPRSAbility");
    private final String value;

    CMAbility(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CMAbility fromValue(String v) {
        for (CMAbility c: CMAbility.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
