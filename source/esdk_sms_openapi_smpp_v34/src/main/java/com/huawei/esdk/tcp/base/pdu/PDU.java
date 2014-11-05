package com.huawei.esdk.tcp.base.pdu;

public abstract class PDU
{
    private byte[] byteData;
    
    public byte[] getByteData()
    {
        return byteData;
    }

    public void setByteData(byte[] byteData)
    {
        this.byteData = byteData;
    }

    public boolean isRequest()
    {
        return false;
    }
    
    public boolean isResponse()
    {
        return false;
    }
}
