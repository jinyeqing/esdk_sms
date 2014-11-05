
package com.huawei.esdk.sms.openapi.mas.cxf.gen.server;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TimeMetricsValues.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="TimeMetricsValues">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Millisecond"/>
 *     &lt;enumeration value="Second"/>
 *     &lt;enumeration value="Minute"/>
 *     &lt;enumeration value="Hour"/>
 *     &lt;enumeration value="Day"/>
 *     &lt;enumeration value="Week"/>
 *     &lt;enumeration value="Month"/>
 *     &lt;enumeration value="Year"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "TimeMetricsValues", namespace = "http://www.csapi.org/schema/common/v2_0")
@XmlEnum
public enum TimeMetricsValues {

    @XmlEnumValue("Millisecond")
    MILLISECOND("Millisecond"),
    @XmlEnumValue("Second")
    SECOND("Second"),
    @XmlEnumValue("Minute")
    MINUTE("Minute"),
    @XmlEnumValue("Hour")
    HOUR("Hour"),
    @XmlEnumValue("Day")
    DAY("Day"),
    @XmlEnumValue("Week")
    WEEK("Week"),
    @XmlEnumValue("Month")
    MONTH("Month"),
    @XmlEnumValue("Year")
    YEAR("Year");
    private final String value;

    TimeMetricsValues(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TimeMetricsValues fromValue(String v) {
        for (TimeMetricsValues c: TimeMetricsValues.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
