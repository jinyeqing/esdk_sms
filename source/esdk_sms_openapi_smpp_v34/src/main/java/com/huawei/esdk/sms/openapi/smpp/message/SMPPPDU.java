package com.huawei.esdk.sms.openapi.smpp.message;

import java.io.UnsupportedEncodingException;

import com.huawei.esdk.platform.common.utils.TypeConvertUtils;
import com.huawei.esdk.tcp.base.pdu.PDU;

public class SMPPPDU extends PDU
{
    protected static final int SMPP_HEADER_LENGTH = 16;
    
    private int commandLength;
    
    private int commandId;
    
    private int commandStatus;
    
    private int sequenceNumber;
    
    private byte[] bodyContent;
    
    public int getCommandLength()
    {
        return commandLength;
    }
    
    public void setCommandLength(int commandLength)
    {
        this.commandLength = commandLength;
    }
    
    public int getCommandId()
    {
        return commandId;
    }
    
    public void setCommandId(int commandId)
    {
        this.commandId = commandId;
    }
    
    public int getCommandStatus()
    {
        return commandStatus;
    }
    
    public void setCommandStatus(int commandStatus)
    {
        this.commandStatus = commandStatus;
    }
    
    public int getSequenceNumber()
    {
        return sequenceNumber;
    }
    
    public void setSequenceNumber(int sequenceNumber)
    {
        this.sequenceNumber = sequenceNumber;
    }
    
    public byte[] getBodyContent()
    {
        return bodyContent;
    }
    
    public void setBodyContent(byte[] bodyContent)
    {
        this.bodyContent = bodyContent;
    }
    
    protected String getCharset(int dataCoding)
    {
        String charset;
        if(dataCoding == 8)
        {
            charset = "utf-16be";
        }
        else if(dataCoding == 15)
        {
            charset = "gbk";
        }
        else if(dataCoding == 0)
        {
            charset = "ascii";
        }
        else
        {
            charset = "UFT-8";
        }
        
        return charset;
    }
    
    public static String getNullEndString(byte buf[], int sPos, String encoding)
    {
        byte tempBuf[];
        int endPos = sPos;
        int index = sPos;
        do
        {
            if (index >= buf.length)
                break;
            if (buf[index] == 0 || index == buf.length - 1)
            {
                endPos = index;
                break;
            }
            index++;
        } while (true);
        
        if (endPos == sPos)
        {
            return "";
        }
        
        tempBuf = new byte[endPos - sPos];
        System.arraycopy(buf, sPos, tempBuf, 0, endPos - sPos);
        try
        {
            if (encoding == null || encoding.length() == 0)
            {
                encoding = "UTF-8";
            }
            return new String(tempBuf, 0, tempBuf.length, encoding);
        }
        catch (Exception ex)
        {
            try
            {
                return new String(tempBuf, 0, tempBuf.length, "UTF-8");
            }
            catch (UnsupportedEncodingException e)
            {
                return "";
            }
        }
    }
    
    public static String getFixedLengthString(byte buf[], int sPos, int endPos, String encoding)
    {
        byte tempBuf[];
        if (endPos < sPos || endPos > buf.length)
        {
            return "";
        }
        tempBuf = new byte[endPos - sPos];
        System.arraycopy(buf, sPos, tempBuf, 0, endPos - sPos);
        
        try
        {
            if (encoding == null || encoding.length() == 0)
            {
                return new String(tempBuf, 0, tempBuf.length, "UTF-8");
            }
            else
            {
                return new String(tempBuf, 0, tempBuf.length, encoding);
            }
        }
        catch (Exception ex)
        {
            try
            {
                return new String(tempBuf, 0, tempBuf.length, "UTF-8");
            }
            catch (UnsupportedEncodingException e)
            {
                return "";
            }
        }
    }
    
    public void pack()
    {
    }
    
    public void pack(byte[] buf)
    {
        TypeConvertUtils.int2byte(commandLength, buf, 0);
        TypeConvertUtils.int2byte(commandId, buf, 4);
        TypeConvertUtils.int2byte(commandStatus, buf, 8);
        TypeConvertUtils.int2byte(sequenceNumber, buf, 12);
    }
    
    public void unpack()
    {
        
    }
    
    @Override
    public String toString()
    {
        return "SMPPPDU [commandLength=" + commandLength + ", commandId=" + commandId + ", commandStatus="
            + commandStatus + ", sequenceNumber=" + sequenceNumber + "]";
    }
}
