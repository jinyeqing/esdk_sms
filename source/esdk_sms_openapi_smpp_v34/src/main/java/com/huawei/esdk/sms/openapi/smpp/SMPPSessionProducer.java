package com.huawei.esdk.sms.openapi.smpp;

import com.huawei.esdk.tcp.base.ISession;
import com.huawei.esdk.tcp.base.SessionProducer;
import com.huawei.esdk.tcp.base.TCPServerEngine;

public class SMPPSessionProducer implements SessionProducer
{
    @Override
    public ISession buildSession(TCPServerEngine tcpServerEngine)
    {
        return new SMPPSession(tcpServerEngine);
    }
}
