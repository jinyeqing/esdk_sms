
package com.huawei.esdk.sms.north.royamas20.cxf.gen.client;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 2.6.10
 * 2013-11-26T19:08:35.328+08:00
 * Generated source version: 2.6.10
 */

@WebFault(name = "ServiceException", targetNamespace = "http://www.csapi.org/schema/common/v2_0")
public class ServiceException_Exception extends Exception {
    
    private com.huawei.esdk.sms.north.royamas20.cxf.gen.client.ServiceException serviceException;

    public ServiceException_Exception() {
        super();
    }
    
    public ServiceException_Exception(String message) {
        super(message);
    }
    
    public ServiceException_Exception(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException_Exception(String message, com.huawei.esdk.sms.north.royamas20.cxf.gen.client.ServiceException serviceException) {
        super(message);
        this.serviceException = serviceException;
    }

    public ServiceException_Exception(String message, com.huawei.esdk.sms.north.royamas20.cxf.gen.client.ServiceException serviceException, Throwable cause) {
        super(message, cause);
        this.serviceException = serviceException;
    }

    public com.huawei.esdk.sms.north.royamas20.cxf.gen.client.ServiceException getFaultInfo() {
        return this.serviceException;
    }
}
