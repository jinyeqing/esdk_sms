package com.huawei.esdk.sms.openapi.smpp.message;

public class SMPPEnquireLinkRespMessage extends SMPPPDU
{
    public void pack()
    {
        setCommandLength(SMPP_HEADER_LENGTH);
        byte[] header = new byte[getCommandLength()];
        super.pack(header);
        setByteData(header);
    }
    
    public void unpack()
    {
       
    }
}
