package com.huawei.esdk.sms.north.http.commu;

import com.huawei.esdk.platform.common.bean.aa.AccountInfo;

public class SMSGetDeliveryStatusCustProvider extends SMSBaseProtocolAdatperCustProvider
{    
    @Override
    public String getContent4Sending(Object reqMessage)
    {
        return fillSendMessage("", "", "", 0, "");
    }
    
    @Override
    protected String getSOAPActionPropName()
    {
        return "sms.http.gateway.get.delivery.status.soapaction";
    }
    
    @Override
    protected String getReqTemplateFileName()
    {
        return "sms_http_get_status_request.xml";
    }
    
    @Override
    protected String getResTemplateFileName()
    {
        return "sms_http_get_status_response.xml";
    }

    @Override
    public AccountInfo getProtocolAuthInfo()
    {
        return null;
    }
}
