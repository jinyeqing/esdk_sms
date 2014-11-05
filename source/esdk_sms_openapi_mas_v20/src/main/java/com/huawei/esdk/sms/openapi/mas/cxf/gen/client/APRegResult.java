
package com.huawei.esdk.sms.openapi.mas.cxf.gen.client;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for APRegResult.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="APRegResult">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="success"/>
 *     &lt;enumeration value="illegalAP"/>
 *     &lt;enumeration value="CMAbilityNotSup"/>
 *     &lt;enumeration value="repeatedReg"/>
 *     &lt;enumeration value="svcAddrMismatch"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "APRegResult")
@XmlEnum
public enum APRegResult {

    @XmlEnumValue("success")
    SUCCESS("success"),
    @XmlEnumValue("illegalAP")
    ILLEGAL_AP("illegalAP"),
    @XmlEnumValue("CMAbilityNotSup")
    CM_ABILITY_NOT_SUP("CMAbilityNotSup"),
    @XmlEnumValue("repeatedReg")
    REPEATED_REG("repeatedReg"),
    @XmlEnumValue("svcAddrMismatch")
    SVC_ADDR_MISMATCH("svcAddrMismatch");
    private final String value;

    APRegResult(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static APRegResult fromValue(String v) {
        for (APRegResult c: APRegResult.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
