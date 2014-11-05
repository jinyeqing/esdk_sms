
package com.huawei.esdk.sms.openapi.mas.cxf.gen.server;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TimeMetric complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TimeMetric">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Metric" type="{http://www.csapi.org/schema/common/v2_0}TimeMetricsValues"/>
 *         &lt;element name="Units" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TimeMetric", namespace = "http://www.csapi.org/schema/common/v2_0", propOrder = {
    "metric",
    "units"
})
public class TimeMetric {

    @XmlElement(name = "Metric", required = true, nillable = true)
    protected TimeMetricsValues metric;
    @XmlElement(name = "Units")
    protected int units;

    /**
     * Gets the value of the metric property.
     * 
     * @return
     *     possible object is
     *     {@link TimeMetricsValues }
     *     
     */
    public TimeMetricsValues getMetric() {
        return metric;
    }

    /**
     * Sets the value of the metric property.
     * 
     * @param value
     *     allowed object is
     *     {@link TimeMetricsValues }
     *     
     */
    public void setMetric(TimeMetricsValues value) {
        this.metric = value;
    }

    /**
     * Gets the value of the units property.
     * 
     */
    public int getUnits() {
        return units;
    }

    /**
     * Sets the value of the units property.
     * 
     */
    public void setUnits(int value) {
        this.units = value;
    }

}
