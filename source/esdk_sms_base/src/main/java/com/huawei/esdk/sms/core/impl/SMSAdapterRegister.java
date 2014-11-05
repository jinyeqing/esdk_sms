package com.huawei.esdk.sms.core.impl;

import com.huawei.esdk.sms.core.ISMSAdapter;
import com.huawei.esdk.sms.core.ISMSAdapterRegister;

public class SMSAdapterRegister implements ISMSAdapterRegister
{
    private Dispatcher dispatcher;
    
    @Override
    public void registerMsgCallback(ISMSAdapter smsAdapter)
    {
        if (null != dispatcher)
        {
            dispatcher.getSmsAdapters().add(smsAdapter);
        }
    }
    
    public Dispatcher getDispatcher()
    {
        return dispatcher;
    }
    
    public void setDispatcher(Dispatcher dispatcher)
    {
        this.dispatcher = dispatcher;
    }
}
