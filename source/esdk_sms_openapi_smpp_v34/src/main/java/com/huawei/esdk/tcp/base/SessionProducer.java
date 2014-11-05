package com.huawei.esdk.tcp.base;

public interface SessionProducer
{
    ISession buildSession(TCPServerEngine tcpServerEngine);
}
