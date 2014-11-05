
package com.huawei.esdk.sms.north.royamas20.cxf.gen.server;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MessageFormat.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="MessageFormat">
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
@XmlType(name = "MessageFormat", namespace = "http://www.csapi.org/schema/sms")
@XmlEnum
public enum MessageFormat {

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

    MessageFormat(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static MessageFormat fromValue(String v) {
        for (MessageFormat c: MessageFormat.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
