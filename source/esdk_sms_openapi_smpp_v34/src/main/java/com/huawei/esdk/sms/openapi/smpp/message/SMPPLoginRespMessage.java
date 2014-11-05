package com.huawei.esdk.sms.openapi.smpp.message;

import com.huawei.esdk.platform.common.utils.BytesUtils;

public class SMPPLoginRespMessage extends SMPPPDU
{
    private String systemId;
    
    public SMPPLoginRespMessage()
    {
        
    }
    
    public SMPPLoginRespMessage(byte buf[])
        throws IllegalArgumentException
    {
        byte[] tempBuf = new byte[buf.length];
        setByteData(tempBuf);
        
        if (buf.length < 17 || buf.length > 32)
        {
            throw new IllegalArgumentException("SMC_MESSAGE_ERROR");
        }
        else
        {
            System.arraycopy(buf, 0, getByteData(), 0, buf.length);
        }
    }
    
    public void pack()
    {
        setCommandLength(SMPP_HEADER_LENGTH + BytesUtils.getBytes(systemId).length + 1);
        byte[] result = new byte[getCommandLength()];
        super.pack(result);
        int pos = SMPP_HEADER_LENGTH;
        System.arraycopy(BytesUtils.getBytes(systemId), 0, result, pos, BytesUtils.getBytes(systemId).length);
        
        setByteData(result);
    }
    
    public String getSystemId()
    {
        return systemId;
    }
    
    public void setSystemId(String systemId)
    {
        this.systemId = systemId;
    }
}
