package com.huawei.esdk.sms.north.http.commu;

import com.huawei.esdk.platform.common.bean.aa.AccountInfo;

public class SMSGetSMSCustProvider extends SMSBaseProtocolAdatperCustProvider
{
    @Override
    public String getContent4Sending(Object reqMessage)
    {
        return fillSendMessage("", "", "", 0, "");
    }
    
    @Override
    protected String getSOAPActionPropName()
    {
        return "sms.http.gateway.get.sms.soapaction";
    }
    
    @Override
    protected String getReqTemplateFileName()
    {
        return "sms_http_get_sms_request.xml";
    }
    
    @Override
    protected String getResTemplateFileName()
    {
        return "sms_http_get_sms_response.xml";
    }

    @Override
    public AccountInfo getProtocolAuthInfo()
    {
        return null;
    }
}
