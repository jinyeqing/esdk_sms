package com.huawei.esdk.sms.openapi.smpp.message;

import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;

import com.huawei.esdk.platform.common.utils.BytesUtils;

public class SMPPSubmitMessage extends SMPPPDU
{
    private static final Logger LOGGER = Logger.getLogger(SMPPSubmitMessage.class);
    
    private String serviceType;
    
    private int sourceAddrTon;
    
    private int sourceAddrNpi;
    
    private String sourceAddr;
    
    private int destAddrTon;
    
    private int destAddrNpi;
    
    private String destinationAddr;
    
    private int esmClass;
    
    private int protocolId;
    
    private int priorityFlag;
    
    private String scheduleDeliveryTime;
    
    private String validityPeriod;
    
    private int registeredDelivery;
    
    private int replaceIfPresentFlag;
    
    private int dataCoding;
    
    private int smDefaultMsgId;
    
    private int smLength;
    
    private String shortMessage;
    
    private byte[] shortMessageByte;
    
    public void pack()
    {
        //Now it empty implementation
    }
    
    public void unpack()
    {
        int pos = SMPP_HEADER_LENGTH;
        byte[] buf = getByteData();
        serviceType = getNullEndString(buf, pos, "");
        pos += BytesUtils.getBytes(serviceType).length + 1;
        sourceAddrTon = buf[pos++];
        sourceAddrNpi = buf[pos++];
        sourceAddr = getNullEndString(buf, pos, "");
        pos += BytesUtils.getBytes(sourceAddr).length + 1;
        destAddrTon = buf[pos++];
        destAddrNpi = buf[pos++];
        destinationAddr = getNullEndString(buf, pos, "");
        pos += BytesUtils.getBytes(destinationAddr).length + 1;
        esmClass = buf[pos++];
        protocolId = buf[pos++];
        priorityFlag = buf[pos++];
        scheduleDeliveryTime = getNullEndString(buf, pos, "");
        pos += BytesUtils.getBytes(scheduleDeliveryTime).length + 1;
        validityPeriod = getNullEndString(buf, pos, "");
        pos += BytesUtils.getBytes(validityPeriod).length + 1;
        registeredDelivery = buf[pos++];
        replaceIfPresentFlag = buf[pos++];
        dataCoding = buf[pos++];
        smDefaultMsgId = buf[pos++];
        smLength = buf[pos++];
        if (smLength < 0)
        {
            smLength = 256 + smLength;
        }
        
        byte[] shortMessageByte = new byte[smLength];
        System.arraycopy(buf, pos, shortMessageByte, 0, smLength);
        try
        {
            this.shortMessageByte = shortMessageByte;
            shortMessage = new String(shortMessageByte, getCharset(dataCoding));
        }
        catch (UnsupportedEncodingException e)
        {
            LOGGER.warn("The message data coding is " + dataCoding + " which is not a supported charset");
            try
            {
                shortMessage = new String(shortMessageByte, "UTF-8");
            }
            catch (UnsupportedEncodingException e1)
            {
                shortMessage = "";
            }
        }
    }
    
    public String getServiceType()
    {
        return serviceType;
    }
    
    public void setServiceType(String serviceType)
    {
        this.serviceType = serviceType;
    }
    
    public int getSourceAddrTon()
    {
        return sourceAddrTon;
    }
    
    public void setSourceAddrTon(int sourceAddrTon)
    {
        this.sourceAddrTon = sourceAddrTon;
    }
    
    public int getSourceAddrNpi()
    {
        return sourceAddrNpi;
    }
    
    public void setSourceAddrNpi(int sourceAddrNpi)
    {
        this.sourceAddrNpi = sourceAddrNpi;
    }
    
    public String getSourceAddr()
    {
        return sourceAddr;
    }
    
    public void setSourceAddr(String sourceAddr)
    {
        this.sourceAddr = sourceAddr;
    }
    
    public int getDestAddrTon()
    {
        return destAddrTon;
    }
    
    public void setDestAddrTon(int destAddrTon)
    {
        this.destAddrTon = destAddrTon;
    }
    
    public int getDestAddrNpi()
    {
        return destAddrNpi;
    }
    
    public void setDestAddrNpi(int destAddrNpi)
    {
        this.destAddrNpi = destAddrNpi;
    }
    
    public String getDestinationAddr()
    {
        return destinationAddr;
    }
    
    public void setDestinationAddr(String destinationAddr)
    {
        this.destinationAddr = destinationAddr;
    }
    
    public int getEsmClass()
    {
        return esmClass;
    }
    
    public void setEsmClass(int esmClass)
    {
        this.esmClass = esmClass;
    }
    
    public int getProtocolId()
    {
        return protocolId;
    }
    
    public void setProtocolId(int protocolId)
    {
        this.protocolId = protocolId;
    }
    
    public int getPriorityFlag()
    {
        return priorityFlag;
    }
    
    public void setPriorityFlag(int priorityFlag)
    {
        this.priorityFlag = priorityFlag;
    }
    
    public String getScheduleDeliveryTime()
    {
        return scheduleDeliveryTime;
    }
    
    public void setScheduleDeliveryTime(String scheduleDeliveryTime)
    {
        this.scheduleDeliveryTime = scheduleDeliveryTime;
    }
    
    public String getValidityPeriod()
    {
        return validityPeriod;
    }
    
    public void setValidityPeriod(String validityPeriod)
    {
        this.validityPeriod = validityPeriod;
    }
    
    public int getRegisteredDelivery()
    {
        return registeredDelivery;
    }
    
    public void setRegisteredDelivery(int registeredDelivery)
    {
        this.registeredDelivery = registeredDelivery;
    }
    
    public int getReplaceIfPresentFlag()
    {
        return replaceIfPresentFlag;
    }
    
    public void setReplaceIfPresentFlag(int replaceIfPresentFlag)
    {
        this.replaceIfPresentFlag = replaceIfPresentFlag;
    }
    
    public int getDataCoding()
    {
        return dataCoding;
    }
    
    public void setDataCoding(int dataCoding)
    {
        this.dataCoding = dataCoding;
    }
    
    public int getSmDefaultMsgId()
    {
        return smDefaultMsgId;
    }
    
    public void setSmDefaultMsgId(int smDefaultMsgId)
    {
        this.smDefaultMsgId = smDefaultMsgId;
    }
    
    public int getSmLength()
    {
        return smLength;
    }
    
    public void setSmLength(int smLength)
    {
        this.smLength = smLength;
    }
    
    public String getShortMessage()
    {
        return shortMessage;
    }
    
    public void setShortMessage(String shortMessage)
    {
        this.shortMessage = shortMessage;
    }

    public byte[] getShortMessageByte()
    {
        return shortMessageByte;
    }

    public void setShortMessageByte(byte[] shortMessageByte)
    {
        this.shortMessageByte = shortMessageByte;
    }
}
