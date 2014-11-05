package com.huawei.esdk.sms.openapi.smpp.message;

public class SMPPDeliverRespMessage extends SMPPPDU
{
    private String messageId;
    
    public void pack()
    {
        int length = SMPP_HEADER_LENGTH + 1;
        setCommandLength(SMPP_HEADER_LENGTH + 1);
        byte[] buf = new byte[length];
        super.pack(buf);
        buf[SMPP_HEADER_LENGTH] = 0;
        
        setByteData(buf);
    }
    
    public void unpack()
    {
        //The v3.4 specification says 'This field is unused and is set to NULL.',
        //so empty implementation here.
    }

    public String getMessageId()
    {
        return messageId;
    }

    public void setMessageId(String messageId)
    {
        this.messageId = messageId;
    }
}
