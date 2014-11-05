package com.huawei.esdk.sms.north.royamas20.cxf.gen.server;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * This class was generated by Apache CXF 2.6.10
 * 2013-11-26T19:11:41.876+08:00
 * Generated source version: 2.6.10
 * 
 */
@WebService(targetNamespace = "http://www.csapi.org/service", name = "cmcc_mas_wbs")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface CmccMasWbs {

    @WebMethod(action = "http://www.csapi.org/service/notifySmsReception")
    public void notifySmsReception(
        @WebParam(partName = "notifySmsReceptionRequest", name = "notifySmsReceptionRequest", targetNamespace = "http://www.csapi.org/schema/sms")
        NotifySmsReceptionRequest notifySmsReceptionRequest
    ) throws ServiceException_Exception, PolicyException_Exception;

    @WebMethod(action = "http://www.csapi.org/service/notifySmsDeliveryStatus")
    public void notifySmsDeliveryStatus(
        @WebParam(partName = "notifySmsDeliveryStatusRequest", name = "notifySmsDeliveryStatusRequest", targetNamespace = "http://www.csapi.org/schema/sms")
        NotifySmsDeliveryStatusRequest notifySmsDeliveryStatusRequest
    ) throws ServiceException_Exception, PolicyException_Exception;
}