package com.huawei.esdk.sms.openapi.smpp.message;

import com.huawei.esdk.platform.common.utils.BytesUtils;

public class SMPPSubmitRespMessage extends SMPPPDU
{
    private String messageId;
    
    public void pack()
    {
        if (null != messageId)
        {
            setCommandLength(SMPP_HEADER_LENGTH + messageId.length() + 1);
        }
        byte[] result = new byte[getCommandLength()];
        super.pack(result);
        int pos = SMPP_HEADER_LENGTH;
        System.arraycopy(BytesUtils.getBytes(messageId), 0, result, pos, BytesUtils.getBytes(messageId).length);
        
        setByteData(result);
    }
    
    public void unpack()
    {
        
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
