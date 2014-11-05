
package com.huawei.esdk.sms.openapi.mas.cxf.gen.server;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for APLogoutResult.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="APLogoutResult">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="success"/>
 *     &lt;enumeration value="illegalAPid"/>
 *     &lt;enumeration value="repeatedLogout"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "APLogoutResult")
@XmlEnum
public enum APLogoutResult {

    @XmlEnumValue("success")
    SUCCESS("success"),
    @XmlEnumValue("illegalAPid")
    ILLEGAL_A_PID("illegalAPid"),
    @XmlEnumValue("repeatedLogout")
    REPEATED_LOGOUT("repeatedLogout");
    private final String value;

    APLogoutResult(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static APLogoutResult fromValue(String v) {
        for (APLogoutResult c: APLogoutResult.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
