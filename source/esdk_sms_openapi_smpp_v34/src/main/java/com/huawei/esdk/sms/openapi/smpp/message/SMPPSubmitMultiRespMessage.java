package com.huawei.esdk.sms.openapi.smpp.message;

import com.huawei.esdk.platform.common.utils.BytesUtils;

public class SMPPSubmitMultiRespMessage extends SMPPPDU
{
    private String messageId;
    
    private int noUnsuccess;
    
    private DestAddresses unsuccessSme[];
    
    public void pack()
    {
        int unsucLength = 0;
        if (null != unsuccessSme)
        {
            for (DestAddresses item : unsuccessSme)
            {
                unsucLength += item.getByte4ResPack().length;
            }
        }
        
        byte tempf[] = BytesUtils.getBytes(messageId);
        int len = SMPP_HEADER_LENGTH + tempf.length + 1 
            + 1//no_unsuccess;
            + unsucLength;//unsuccessSme
        setCommandLength(len);
        byte[] buf = new byte[len];
        super.pack(buf);
        
        int pos = SMPP_HEADER_LENGTH;
        //messageId
        System.arraycopy(tempf, 0, buf, pos, tempf.length);
        pos += tempf.length + 1;
        
        //no_unsuccess
        buf[pos++] = (byte)noUnsuccess;
        
        //unsuccessSme
        if (null != unsuccessSme)
        {
            for (DestAddresses item : unsuccessSme)
            {
                tempf = item.getByte4ResPack();
                System.arraycopy(tempf, 0, buf, pos, tempf.length);
                pos += tempf.length;
            }
        }
        
        setByteData(buf);
    }
    
    public void unpack()
    {
        //Now it's empty implementation
    }
    
    public String getMessageId()
    {
        return messageId;
    }
    
    public void setMessageId(String messageId)
    {
        this.messageId = messageId;
    }
    
    public int getNoUnsuccess()
    {
        return noUnsuccess;
    }
    
    public void setNoUnsuccess(int noUnsuccess)
    {
        this.noUnsuccess = noUnsuccess;
    }
    
    public DestAddresses[] getUnsuccessSme()
    {
        return unsuccessSme;
    }
    
    public void setUnsuccessSme(DestAddresses[] unsuccessSme)
    {
        this.unsuccessSme = unsuccessSme;
    }
}
