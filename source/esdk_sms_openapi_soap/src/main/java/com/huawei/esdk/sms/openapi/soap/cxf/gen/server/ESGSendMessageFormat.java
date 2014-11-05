
package com.huawei.esdk.sms.openapi.soap.cxf.gen.server;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ESGSend.MessageFormat.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ESGSend.MessageFormat">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ASCII"/>
 *     &lt;enumeration value="UCS2"/>
 *     &lt;enumeration value="GB18030"/>
 *     &lt;enumeration value="GB2312"/>
 *     &lt;enumeration value="Binary"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ESGSend.MessageFormat")
@XmlEnum
public enum ESGSendMessageFormat {

    ASCII("ASCII"),
    @XmlEnumValue("UCS2")
    UCS_2("UCS2"),
    @XmlEnumValue("GB18030")
    GB_18030("GB18030"),
    @XmlEnumValue("GB2312")
    GB_2312("GB2312"),
    @XmlEnumValue("Binary")
    BINARY("Binary");
    private final String value;

    ESGSendMessageFormat(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ESGSendMessageFormat fromValue(String v) {
        for (ESGSendMessageFormat c: ESGSendMessageFormat.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
