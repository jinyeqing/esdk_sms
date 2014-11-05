package com.huawei.esdk.sms.device.mas20;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.huawei.esdk.platform.commu.itf.ICXFSOAPCustProvider;
import com.huawei.esdk.sms.interceptor.SMSLoggingOutInterceptor;

public class SMSCXFSOAPCustProvider implements ICXFSOAPCustProvider
{

    @Override
    public Map<String, String> getSoapHeaders()
    {
        return new HashMap<String, String>();
    }

    @Override
    public List<Object> getInInterceptors()
    {
        return new ArrayList<Object>();
    }

    @Override
    public List<Object> getOutInterceptors()
    {
        List<Object> result = new ArrayList<Object>();
        result.add(new SMSLoggingOutInterceptor());
        return result;
    }

    @Override
    public Map<String, String> getSerivceURIMapping()
    {
        return new HashMap<String, String>();
    }

    @Override
    public String reBuildNewUrl(String url, String interfaceName)
    {
        return url;
    }
    
}
