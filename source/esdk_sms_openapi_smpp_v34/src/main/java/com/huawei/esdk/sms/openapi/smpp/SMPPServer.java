package com.huawei.esdk.sms.openapi.smpp;

import com.huawei.esdk.tcp.base.TCPServerEngine;

public class SMPPServer
{
    private TCPServerEngine tcpServerEngine;
    
    public void init()
    {
        
    }
    
    public TCPServerEngine getTcpServerEngine()
    {
        return tcpServerEngine;
    }

    public void setTcpServerEngine(TCPServerEngine tcpServerEngine)
    {
        this.tcpServerEngine = tcpServerEngine;
    }
}
