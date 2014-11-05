package com.huawei.esdk.sms.core.impl;

import com.huawei.esdk.sms.core.IMsgCallback;
import com.huawei.esdk.sms.core.IMsgCallbackRegister;

public class MsgCallbackRegister implements IMsgCallbackRegister
{
    private Dispatcher dispatcher;
    
    @Override
    public void registerMsgCallback(IMsgCallback msgCallback)
    {
        if (null != dispatcher)
        {
            dispatcher.getMsgCallbacks().add(msgCallback);
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
